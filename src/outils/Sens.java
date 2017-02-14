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
}
