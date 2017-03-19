package iug;

import outils.Demande;
import iug.IIUG;
import controleur.IControleur;

/**
 * Classe simulant le comportement d'un iug 
 * @author yacine AMRANE, alexis BRUNET, franck BRUN, yann DENICOLO
 *
 */
public class DoublureDeIUG implements IIUG {
	/**
	 * L'iug est lie a un controleur.
	 */
	private IControleur controleur;

	/** 
	 * Permet d'eteindre tous les boutons des demandes.
	 */
	public void eteindreTousBoutons() {
		// TODO Auto-generated method stub
	}

	/** 
	 * Indique qu'un utilisateur souhaite se deplacer.
	 * @param d demande.
	 */
	public void allumerBouton(Demande d) {
		System.out.println("allumer bouton "+d.toString());
	}

	/** 
	 * Indique que le palier est atteint.
	 * @param d demande.
	 */
	public void eteindreBouton(Demande d) {
		
		System.out.println("eteindre bouton "+d.toString());
	}
	/** 
	 * Indique un changement de position de la cabine.
	 * @param position position de la cabine.
	 */
	@Override
	public void changerPosition(int position) {
		// TODO Auto-generated method stub
	}

	/** 
	 * Ajoute un message.
	 * @param message message a ajouter.
	 */
	@Override
	public void ajouterMessage(String message) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Permet de donner un controleur a l'iug.
	 */
	@Override
	public void assignerControleur(IControleur controleur) {
		this.controleur = controleur;
	}
}
