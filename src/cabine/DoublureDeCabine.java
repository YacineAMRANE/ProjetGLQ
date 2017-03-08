package cabine;

public class DoublureDeCabine implements ICabine{

	@Override
	public void monter() {
		System.out.println("La cabine monte");
		
	}

	@Override
	public void descendre() {
		System.out.println("La cabine descends");
		
	}

	@Override
	public void arreterProchainNiveau() {
		System.out.println("La cabine s'arrete au prochain niveau");
		
	}

	@Override
	public void arreter() {
		System.out.println("La cabine s'arrete");
		
	}

}
