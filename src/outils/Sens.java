package outils;

/**
 * Enumeration Sens.
 * @author alexis,yacine,franck,yann
 */
public enum Sens {

	MONTEE,
	DESCENTE,
	INDEFINI;

	/**
	 * Redefinition de toString.
	 */
	@Override
	public String toString() {
		switch(this) {
		case MONTEE: return "^";
		case DESCENTE: return "v";
		case INDEFINI: return "-";
		default: throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Retourne le sens opposé du sens qui est passé en paramètre, reste indéfini si le sens est indéfini
	 * @param sens
	 * @return Sens
	 */
	public static Sens getOppose(Sens sens){
		if(sens == DESCENTE)
			return Sens.MONTEE;
		if(sens == MONTEE)
			return Sens.DESCENTE;
		return INDEFINI;		
	}
}
