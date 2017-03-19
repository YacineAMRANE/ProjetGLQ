package cabine;

import controleur.IControleur;

/**
 * Classes et interfaces de la partie gerant la cabine 
 * @author yacine AMRANE, alexis BRUNET, franck BRUN, yann DENICOLO
 *
 */
public interface ICabine 
{
	/** 
	 * Indique un changement de position de la cabine vers une montee.
	 */
	void monter();
	
	/** 
	 * Indique un changement de position de la cabine vers une descente.
	 */
	void descendre();
	
	/** 
	 * Indique que le prochain niveau est la destination.
	 */
	void arreterProchainNiveau();
	
	/** 
	 * Indique un arret.
	 */
	void arreter();
	
	/**
	 * Permet de donner un controleur a la cabine
	 * @param controleur controleur associe.
	 */
	public void assignerControleur(IControleur controleur);
}
