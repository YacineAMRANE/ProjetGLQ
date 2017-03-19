package cabine;

import controleur.IControleur;

/**
 * Cette classe temporaire simule le comportement d'une cabine
 */
public class DoublureDeCabine implements ICabine{
	/**
	 * Indique la position de la cabine.
	 */
	protected int position ;

	/**
	 * La cabine est lie a un controleur.
	 */
	private IControleur controleur ; 

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
		
	}
	
	
	/** 
	 * Indique un changement de position de la cabine vers une descente.
	 */
	@Override
	public void descendre() {	
		System.out.println("descendre");
		position--;
	}
	/** 
	 * Indique un changement de position de la cabine vers une montee.
	 */
	@Override
	public void monter() {
		System.out.println("monter");
		position++ ;
	}

	/**
	 * Permet de donner un controleur a la cabine.
	 */
	@Override
	public void assignerControleur(IControleur controleur) {
		this.controleur = controleur;
	}

}
