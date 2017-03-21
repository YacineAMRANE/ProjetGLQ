package commande;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import operative.ICabine;
import operative.IIUG;
import commande.IControleur;
import commande.IListeTrieeCirculaire;
import outils.Demande;
import outils.Sens;

/**
 * La classe controleur fait le lien entre l'iug et la cabine.
 * elle gere les mouvements de la cabine selon les demandes des utilisateurs.
 * @author yacine AMRANE, alexis BRUNET, franck BRUN, yann DENICOLO
 *
 */
public class Controleur implements IControleur {
	/**
	 * stock des demandes
	 */
	private IListeTrieeCirculaire<Demande> stock;
	/**
	 * cabine controlee
	 */
	private ICabine cabine;
	/**
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
	/**
	 * etat du controleur
	 */
	private Etat etatControleur;
	/**
	 * liste des demandes dans la cabine
	 */
	ArrayList<Demande> demandesIndefini = new ArrayList<Demande>();
	/**
	 * nombre d etage ou l'ascenseur peut se deplacer
	 */
	private int nbEtage=0;
	/**
	 * Cette map stocke pour chaque demande, la date d'ajout.
	 */
	protected Map<Demande, Date> attentes = new HashMap<Demande, Date>();
	/**
	 * attente maximum avant l'arrivee de la cabine
	 */
	private long attenteMaximale = 0 ;
	/**
	 * attente minimum avant l'arrivee de la cabine
	 */
	private long attenteMinimale = Long.MAX_VALUE ;
	/**
	 * cumul d'attente des personnes
	 */
	private long attenteTotale = 0 ;
	/**
	 * calcul de l'attente moyenne des personnes
	 */
	private long attenteMoyenne = 0;
	/**
	 * nombre de demandes
	 */
	private int nbDemande = 0;

	/**
	 * Constructeur de la classe contoleur
	 * @param liste des demandes
	 * @param nbEtages possibles
	 */
	public Controleur(ListeTrieeCirculaireDeDemandes stock, int nbEtages) {
		this.stock = stock;
		this.position = 0;
		this.etatControleur = Etat.ATTENTE;
		this.nbEtage = nbEtages;
	}
	
	/**
	 * Constructeur de la classe contoleur
	 * @param stock des demandes
	 * @param nbEtages possibles
	 * @param iug des commandes
	 * @param cabine d'ascenseur
	 * 
	 */
	public Controleur( int  nbEtages, IIUG iug, ICabine cabine, IListeTrieeCirculaire<Demande> stock)
	{
		this.nbEtage = nbEtages;
		this.iug = iug;
		this.cabine = cabine;
		this.stock = stock;
		this.position = 0;
		this.etatControleur = Etat.ATTENTE;
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
	 * @param isPalier pour savoir si c'est une extremite (0 ou nbEtage-1)
	 */
	private void insereDemande(Demande demande, boolean isPalier){
		stock.inserer(demande);
		attentes.put(demandeDeDepart(demande),new Date());
		nbDemande = nbDemande+1;
	}
	
	/**
	 * Supprime une demande de la liste des demande.
	 * Si la demande etait en attente, on calcule le temps ecoule
	 * @param d
	 */
	private void supprimerDemande(Demande d){
		stock.supprimer(d);
		
		d = demandeDeDepart(d);
		boolean isKey = false;
		Demande maDemande = null;
		Date maDate = null;
		for (Map.Entry<Demande, Date> entry : attentes.entrySet()) {
			if(entry.getKey().equals(d))
			{
				isKey = true;
				maDate = entry.getValue();
				maDemande= entry.getKey();
				break;
			}
		}
		// On calcule les statistiques liees a l'attente
		if(isKey && maDate != null && maDemande != null)
		{
			Date dateDebut = maDate;
			Date dateFin = new Date();
			long tempsAttente = dateFin.getTime() - dateDebut.getTime();
			
			//On supprime la demande de la liste d'attente
			attentes.remove(maDemande);
			
			// On calcule l'attente totale
			attenteTotale = attenteTotale + tempsAttente;
			
			// On calcule l'attente maximale
			if(tempsAttente > attenteMaximale)
			{
				attenteMaximale = tempsAttente;
			}
			
			// On calcule l'attente minimale
			if(tempsAttente < attenteMinimale){
				attenteMinimale = tempsAttente;
			}
			
			// On calcule de l'attente moyenne
			attenteMoyenne = attenteTotale / nbDemande;
			
			// On affiche dans l'iug
			iug.ajouterMessage("Les temps d'attentes pour realiser la demande "+ d.toString()
					+ "\nL'attente minimale est de "+attenteMinimale + " ms"
					+ "\nL'attente maximale est de "+ attenteMaximale + " ms"
					+ "\nL'attente moyenne est de "+ attenteMoyenne + " ms");
		}
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
			return this == Etat.DESCENTE || this == Etat.MONTEE || this == Etat.ARRETIMMINENT;
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
			this.insereDemande(d, isPalier);
			iug.allumerBouton(demandeDeDepart(d));
			break;
		case ARRETIMMINENT:
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
		ArrayList<Demande> demandes = ((ListeTrieeCirculaireDeDemandes) this.stock).getListeTriee();
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

	@Override
	public void exit() {
		// TODO Auto-generated method stub
	}
}