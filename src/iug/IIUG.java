package iug;

import controleur.IControleur;
import outils.Demande;

/**
 * Classes et interfaces de la partie gerant l iug 
 * @author yacine AMRANE, alexis BRUNET, franck BRUN, yann DENICOLO
 *
 */
public interface IIUG {
	/** 
	 * Indique qu'un utilisateur souhaite se deplacer.
	 * @param d demande.
	 */
	void allumerBouton(Demande d);
	
	/** 
	 * Indique que le palier est atteint
	 * @param d demande.
	 */
	void eteindreBouton(Demande d);
	
	/** 
	 * Indique un changement de position de la cabine.
	 * @param position position de la cabine.
	 */
	void changerPosition(int position);
	
	/** 
	 * Ajoute un message.
	 * @param message message a ajouter.
	 */
	void ajouterMessage(String message);
	
	/** 
	 * Permet d'eteindre tous les boutons des demandes.
	 */
	void eteindreTousBoutons();

	/**
	 * Permet de donner un controleur a l'IUG
	 * @param controleur controleur associe.
	 */
	public void assignerControleur(IControleur controleur);
}
