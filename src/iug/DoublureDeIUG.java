package iug;

import java.util.ArrayList;

import outils.Demande;
import outils.Sens;
import iug.IIUG;
import controleur.IControleur;
import commande.ListeTrieeCirculaireDeDemandes;

public class DoublureDeIUG implements IIUG {
	/** 
	 * Indique qu'un utilisateur souhaite se deplacer.
	 * @param d demande.
	 */
	public void allumerBouton(Demande d){
		System.out.println("allumer bouton "+ d.toString());
	}
	
	/** 
	 * Indique que le palier est atteint
	 * @param d demande.
	 */
	public void eteindreBouton(Demande d){
		System.out.println("eteindre bouton "+ d.toString());
	}
	
	/** 
	 * Indique un changement de position de la cabine.
	 * @param position position de la cabine.
	 */
	@Override
	public void changerPosition(int position){
		System.out.println("franchissement de palier: cabine en "+ position); 
	}

	/** 
	 * ajoute un message
	 * @param message message a ajouter.
	 */
	@Override
	public void ajouterMessage(String message) {
		System.out.println("ajout du message: "+message);
		
	}
}
