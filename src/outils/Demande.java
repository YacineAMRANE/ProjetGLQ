package outils;

/**
 * Classe Demande.
 * @author yacine
 */
public class Demande  {

	/*
	 * Sens de la demande.
	 */
	private Sens sens;
	
	/**
	 * Etage de destination.
	 */
	private int etage;

	/**
	 * Constructeur par default.
	 */
	public Demande() {
		this.etage = 0;
		this.sens = Sens.INDEFINI;
	}
	
	/**
	 * Constructeur.
	 * @param destination Destination.
	 * @param sens Direction.
	 */
	public Demande(int destination, Sens sens) {
		this.etage = destination;
		this.sens = sens;
	}

	/**
	 * 
	 * @return L'etage de destination.
	 */
	public int etage() {
		return this.etage;
	}

	/**
	 * 
	 * @return True si le sens est indefini.
	 */
	public boolean estIndefini() {
		return (this.sens == Sens.INDEFINI);
	}

	/**
	 * 
	 * @return True si le sens est montee.
	 */
	public boolean enMontee() {
		return (this.sens == Sens.MONTEE);
	}

	/**
	 * 
	 * @return True si le sens es t descente.
	 */
	public boolean enDescente() {
		return (this.sens == Sens.DESCENTE);
	}

	/**
	 * Passage à l'etage suivant.
	 */
	public void passeEtageSuivant() {
		if (this.sens == Sens.DESCENTE) etage--;
		if (this.sens == Sens.MONTEE) etage++;
	}

	/**
	 * Changement de sens.
	 * @param nouveauSens nouveau sens.
	 */
	public void changeSens(Sens nouveauSens) {
		this.sens = nouveauSens;
	}
	
	/**
	 * 
	 * @return Le sens.
	 */
	public Sens sens() {
		return this.sens;
	}
	
	/**
	 * Redefinition de toString.
	 */
	@Override
	public String toString(){
		return this.etage+this.sens.toString();
	}
	
	/**
	 * Redefinition de equals.
	 * @param demande
	 * @return
	 */
	@Override
	public boolean equals(Object object){
		if (object != null && object instanceof Demande) {
		Demande demande = (Demande)object;
		return (demande.etage == this.etage && demande.sens == this.sens);
		}
		else return false;
	}

}
