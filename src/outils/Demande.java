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
	 * Retourne l'etage de la demande.
	 * @return L'etage de destination.
	 */
	public Integer etage() {
		return this.etage;
	}

	/**
	 * Retourne true si le sens de la demande est indefini.
	 * @return True si le sens est indefini.
	 */
	public boolean estIndefini() {
		return (this.sens == Sens.INDEFINI);
	}

	/**
	 * Retourne true si le sens de la demande est montee.
	 * @return True si le sens est montee.
	 */
	public boolean enMontee() {
		return (this.sens == Sens.MONTEE);
	}

	/**
	 * Retourne true si le sens de la demande est descente.
	 * @return True si le sens es t descente.
	 */
	public boolean enDescente() {
		return (this.sens == Sens.DESCENTE);
	}

	/**
	 * Passage � l'etage suivant.
	 * @throws ExceptionCabineArretee Exception g�n�r�e dans le cas d'une demande indefini.
	 */
	public void passeEtageSuivant() throws ExceptionCabineArretee {
		if (this.sens == Sens.DESCENTE) etage--;
		if (this.sens == Sens.MONTEE) etage++;
		if (this.sens == Sens.INDEFINI) throw new ExceptionCabineArretee();
	}

	/**
	 * Changement de sens.
	 * @param nouveauSens Nouveau sens.
	 */
	public void changeSens(Sens nouveauSens) {
		this.sens = nouveauSens;
	}
	
	/**
	 * Retour le sens de la demande.
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
	 * @param object Objet re�u.
	 * @return True si l'objet re�u est une demande.
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
