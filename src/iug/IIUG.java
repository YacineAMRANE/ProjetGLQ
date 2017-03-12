package iug;

import outils.Demande;

/**
 * Classes et interfaces de la partie gerant l iug 
 * @author yann denicolo
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
	 * ajoute un message
	 * @param message message a ajouter.
	 */
	void ajouterMessage(String message);
}
