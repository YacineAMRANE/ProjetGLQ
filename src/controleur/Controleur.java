package controleur;

import java.util.ArrayList;
import cabine.ICabine;
import iug.IIUG;
import commande.IListeTrieeCirculaire;
import commande.ListeTrieeCirculaireDeDemandes;
import outils.Demande;
import outils.Sens;

/**
 * La classe controleur fait le lien entre l'iug et la cabine.
 * elle gere les mouvements de la cabine selon les demandes des utilisateurs.
 * @author yacine AMRANE, alexis BRUNET, franck BRUN, yann DENICOLO
 *
 */
public class Controleur implements IControleur {
	/*
	 * stock des demandes
	 */
	private IListeTrieeCirculaire<Demande> stock;
	/*
	 * cabine controlee
	 */
	private ICabine cabine;
	/*
	 * iug donnant les demandes
	 */
	private IIUG iug;
	/*
	 * position courante de la cabine
	 */
	private int position;
	/*
	 * sens courant de deplacement de la cabine
	 */
	private Sens sens;
	/*
	 * etat du controleur
	 */
	private Etat etatControleur;
	/*
	 * liste des demandes dans la cabine
	 */
	ArrayList<Demande> demandesIndefini = new ArrayList<Demande>();
	/*
	 * nombre d etage ou l'ascenseur peut se deplacer
	 */
	private int nbEtage=0;


	/**
	 * Constructeur de la classe contoleur
	 * @param liste des demandes
	 * @param nbEtages possibles
	 */
	public Controleur(ListeTrieeCirculaireDeDemandes liste, int nbEtages) {
		this.stock = liste;
		this.position = 0;
		etatControleur = Etat.ATTENTE;
		this.nbEtage = nbEtages;
	}

	/**
	 * Retourne la liste des demandes indéfinies
	 * @return ArrayList<Demande>
	 */
	public ArrayList<Demande> getIndefini() {
		return demandesIndefini;
	}

	/**
	 * Retourne l'etat du controleur
	 * @return EtatController
	 */
	public Etat getEtatControleur() {
		return etatControleur;
	}

	/**
	 * @param demande a inserer
	 * @param isPalier pour savoir si c'est une extremite
	 */
	private void insereDemande(Demande demande, boolean isPalier){
		stock.inserer(demande);
	}
	
	/**
	 * Supprime une demande de la liste des demande.
	 * Si le demande était en attende, calcul le temps écoulé
	 * @param d
	 */
	private void supprimerDemande(Demande d){
		stock.supprimer(d);
	}


	/**
	 * enum pour representer l'etat du controleur
	 */
	public enum Etat {
		DESCENTE, MONTEE, ARRETETAGE, ARRETIMMINENT, ARRETIMMEDIAT, ATTENTE;

		/**
		 * Retourne si l'état ou se trouve le controleur est un etat de mouvement de la cabine
		 * @return Boolean mouvement de la cabine
		 */
		public boolean isEnMouvement() {
			return this == Etat.DESCENTE || this == Etat.MONTEE
					|| this == Etat.ARRETIMMINENT;
		}
	}

	/**
	 * Retourne la liste des demandes
	 * @return ListeTrieeCirculaireDeDemandes
	 */
	public IListeTrieeCirculaire<Demande> getListe() {
		return stock;
	}

	/**
	 * Permet de donner un IUG au Controlleur
	 * @param iug donnant les demandes des utilisateurs
	 */
	public void setIUG(IIUG iug) {
		this.iug = iug;
	}

	/**
	 * Permet de donner une Cabine au Controleur
	 * @param cabine effectuant les deplacements
	 */
	public void setCabine(ICabine cabine) {
		this.cabine = cabine;
	}

	/**
	 * Retourne la demande sous sa forme initial, si elle était indéfini retourne la demande indéfini
	 * @param Demande
	 * @return Demande
	 */
	private Demande demandeDeDepart(Demande d) {
		if(demandesIndefini.contains(new Demande(d.etage(), Sens.INDEFINI))) {
			return new Demande(d.etage(), Sens.INDEFINI);
		}
		else return d;
	}

	/**
	 * Indique une nouvelle demande d'utilisateur.
	 * @param d demande de deplacement.
	 */
	public void demander(Demande d) {
		boolean isPalier = true; 
		System.out.println("appel " + d.toString());
		if (d.estIndefini()) {
			isPalier = false; 
			if(!demandesIndefini.contains(d)) demandesIndefini.add(d);
			if (d.etage() >= this.position) {
				if (d.etage() == this.nbEtage-1)
					d = new Demande(d.etage(), Sens.DESCENTE);
				else 
					d = new Demande(d.etage(),Sens.MONTEE);
			}
			else if (d.etage() <= this.position){
				if (d.etage() == 0)
					d = new Demande(d.etage(), Sens.MONTEE);
				else 
					d = new Demande(d.etage(),Sens.DESCENTE);
			}
		}
		switch (this.etatControleur) {
		case ATTENTE:
			this.insereDemande(d, isPalier);
			if (d.etage() == this.position) {
				// ToDo (ex : ouverture des portes)
			}
			else if (Math.abs(d.etage() - this.position) == 1) {
				iug.allumerBouton(demandeDeDepart(d));
				if (d.etage() - this.position > 0) {
					this.sens = Sens.MONTEE;
				} else {
					this.sens = Sens.DESCENTE;
				}
				majEtat(Etat.ARRETIMMINENT);
			}
			else if (d.etage() > this.position) {
				iug.allumerBouton(demandeDeDepart(d));
				majEtat(Etat.MONTEE);
			}
			else if (d.etage() < this.position) {
				iug.allumerBouton(demandeDeDepart(d));
				majEtat(Etat.DESCENTE);
			}			
			break;
		case MONTEE:
		case DESCENTE:
			// Même traitement pour l'etat Montee ou Descente en cas de demande
			this.insereDemande(d, isPalier);
			iug.allumerBouton(demandeDeDepart(d));
			break;
		case ARRETIMMINENT:
			// on stocke la demande s'il y a plus d'un etage d'écart entre la
			// position actuelle de la cabine, et l'etage de la demande,
			// ou si le sens de la cabine l'eloigne de l'etage de la demande.
			int etageProche = Math.abs(d.etage() - this.position);
			if (etageProche > 1 || (this.sens != d.sens())) {
				this.insereDemande(d, isPalier);
				iug.allumerBouton(demandeDeDepart(d));
			}
			break;
		case ARRETETAGE:
			if (d.etage() != this.position || !d.sens().toString().equals(this.etatControleur.toString())) {
				this.insereDemande(d, isPalier);
				iug.eteindreBouton(demandeDeDepart(d));
				demandesIndefini.remove(demandeDeDepart(d));
			}
			break;
		default:
			break;
		}
	}

	/**
     * Indique un changement d'etage de la cabine.
     */
	public synchronized void signalerChangementDEtage() {
		Demande demandeEnCours;
		if (etatControleur.isEnMouvement()) {
			if (Sens.DESCENTE == sens) {
				position--;
				iug.changerPosition(this.position);
			} else if (Sens.MONTEE == sens) {
				position++;
				iug.changerPosition(this.position);
			}
		}
		System.out.println("cabine en " + position);
		switch (etatControleur) {
		case MONTEE:
		case DESCENTE:
			if (arretProchain()) {
				majEtat(Etat.ARRETIMMINENT);
			} else {
				majPosition();
			}
			break;
		case ARRETIMMINENT:
			// recherche de la demande en cours
			if(this.position == this.nbEtage-1){
				demandeEnCours = stock.suivantDe(new Demande(this.position -1, Sens.DESCENTE));	
			}
			else if(this.position == 0) {
				demandeEnCours = stock.suivantDe(new Demande(this.position , Sens.MONTEE));
			}
			else 		
				demandeEnCours = stock.suivantDe(new Demande(this.position , this.sens));
			iug.eteindreBouton(demandeDeDepart(demandeEnCours));
			this.supprimerDemande(demandeEnCours);
			demandesIndefini.remove(demandeDeDepart(demandeEnCours));
			arreter();
			break;
		default:
			break;
		}

	}

	/** 
	 * Met a jour l'état du controller en fonction de la demande suivante
	 */
	public void arreter() {
		Demande demandeSuivante;
		majEtat(Etat.ARRETETAGE);
		
		if(this.position == this.nbEtage-1){
			demandeSuivante = stock.suivantDe(new Demande(this.position -1, Sens.DESCENTE));	
		}
		else if(this.position == 0) {
			demandeSuivante = stock.suivantDe(new Demande(this.position , Sens.MONTEE));
		}
		else 			
			demandeSuivante = stock.suivantDe(new Demande(this.position , this.sens));
		
		if (demandeSuivante == null) {
			majEtat(Etat.ATTENTE);
		}
		else if (demandeSuivante.etage() > this.position) {
			if (arretProchain()) {
				majEtat(Etat.ARRETIMMINENT);
			} else {
				majEtat(Etat.MONTEE);
			}
		}
		else if (demandeSuivante.etage() < this.position) {
			if (arretProchain()) {
				majEtat(Etat.ARRETIMMINENT);
			} else {
				majEtat(Etat.DESCENTE);
			}
		}
		else if(demandeSuivante.etage() == this.position)
		{
			iug.eteindreBouton(demandeDeDepart(demandeSuivante));
			supprimerDemande(demandeSuivante);
			demandesIndefini.remove(demandeDeDepart(demandeSuivante));
			arreter();
		}
	}

	/**
     * Indique un arret d'urgence demandé par un utilsateur stoppant les deplacement de l'ascenseur.
     */
	public void arretUrgence() {
		System.out.println("arret d'urgence");
		ArrayList<Demande> demandes = this.stock.getListeTriee();
		for (Demande monBouton : demandes) {
			Demande boutonDeMaDemandeIndefinie = new Demande(monBouton.etage(), Sens.INDEFINI);
			if (this.demandesIndefini.contains(boutonDeMaDemandeIndefinie)) {
				iug.eteindreBouton(boutonDeMaDemandeIndefinie);
			} else {
				iug.eteindreBouton(monBouton);
			}
		}
		stock.vider();
		majEtat(Etat.ATTENTE);
	}

	/**
	 * Retourne la position de la cabine
	 * @return int position
	 */
	public int getPosition() {
		return this.position;
	}

	/**
	 * Indique si la cabine va devoir s'arreter au prochaine etage.
	 * @return boolean arret au prochain etage
	 */
	boolean arretProchain() {
		int positionRecherchee = this.position;
		Demande d;
		if(this.position == this.nbEtage-1){
			d = new Demande(this.position , Sens.DESCENTE);	
		}
		else if(this.position == 0) {
			d = new Demande(this.position , Sens.MONTEE);
		}
		else {
			d = new Demande(this.position,this.sens);
		}
		d = stock.suivantDe(d);

		if(this.position>d.etage()) this.sens = Sens.DESCENTE;
		else if(this.position<d.etage()) this.sens = Sens.MONTEE;

		if (Sens.MONTEE == this.sens) {
			positionRecherchee++;
		} else if (Sens.DESCENTE == this.sens) {
			positionRecherchee--;
		}
		Demande demandeRecherchee = new Demande(positionRecherchee, this.sens);
		if (stock.taille() == 1) {
			Demande demandeRechercheeOpposee = new Demande(positionRecherchee, Sens.getOppose(sens));
			return stock.contient(demandeRecherchee) || stock.contient(demandeRechercheeOpposee);
		} else {
			return stock.contient(demandeRecherchee); 
		}
	}

	/**
	 * Met a jour l'état du controleur par rapport au mouvement de la cabine
	 * @param Etat etat
	 */
	private void majEtat(Etat etat) {
		this.etatControleur = etat;
		if (etatControleur == Etat.MONTEE) {
			sens = Sens.MONTEE;
		}
		if (etatControleur == Etat.DESCENTE) {
			sens = Sens.DESCENTE;

		}
		if (etatControleur == Etat.ARRETIMMINENT) {
			cabine.arreterProchainNiveau();
		}
		if (etatControleur == Etat.MONTEE 
				|| etatControleur == Etat.DESCENTE
				|| etatControleur == Etat.ARRETIMMINENT) {
			majPosition();
		} else {
			cabine.arreter();
		}
	}

	/**
	 * Indique un changement de position de la cabine
	 * Signal a la cabine de monter, descendre ou s'arreter selon notre etat
	 */
	private void majPosition() {
		iug.changerPosition(this.position);
		switch (this.sens) {
		case MONTEE:
			cabine.monter();
			break;
		case DESCENTE:
			cabine.descendre();
			break;
		case INDEFINI:
			cabine.arreter();
			break;
		}
	}

	/**
	 * Permet de placer la cabine a la position souhaitee
	 * @param position de la cabine
	 */
	public void setPosition(int position) {
		System.out.println("cabine en " + position);
		this.position = position;
	}
}