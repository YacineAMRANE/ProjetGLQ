package cabine;

public class DoublureDeCabine implements ICabine{

	/** 
	 * Permet de modifier le sens de deplacement de la cabine vers la MONTEE.
	 */
	@Override
	public void monter() {
		System.out.println("monter");
		
	}

	/** 
	 * Permet de modifier le sens de deplacement de la cabine vers la DESCENTE.
	 */
	@Override
	public void descendre() {
		System.out.println("descendre");
		
	}

	/** 
	 * Indique que le prochain niveau est la destination.
	 */
	@Override
	public void arreterProchainNiveau() {
		System.out.println("arreter prochain etage");
		
	}

	/** 
	 * Indique un arret.
	 */
	@Override
	public void arreter() {
		System.out.println("arret");
		
	}

}
