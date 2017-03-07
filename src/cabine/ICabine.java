package cabine;

/**
 * Classes et interfaces de la partie gerant la cabine 
 * @author alexis brunet
 *
 */
public interface ICabine 
{
	/** 
	 * Permet d'indiquer s'il y a un changement d'étage.
	 */
	void signalerChangementDEtage();
	
	/** 
	 * Permet de modifier le sens de deplacement de la cabine vers la MONTEE.
	 */
	void monter();
	
	/** 
	 * Permet de modifier le sens de deplacement de la cabine vers la DESCENTE.
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
	
	
	
}
