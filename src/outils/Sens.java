package outils;

/**
 * Enumeration Sens.
 * @author yacine
 */
public enum Sens {

	MONTEE,
	DESCENTE,
	INDEFINI;

	
	
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
