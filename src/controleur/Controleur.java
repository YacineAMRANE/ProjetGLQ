package Controleur;

import outils.Demande;

public class Controleur implements IControleur {

	@Override
	public void MAJPosition() {
		// TODO Auto-generated method stub
		iug.setPosition(this.position);
		swith(this.sens){
			case MONTEE:
				cabine.monter();
				break;
			case DESCENTE:
				cabine.descendre();
				break;
			case INDEFINI:
				cabine.arreter();
				break;
		}

	}

	@Override
	public void MAJSens() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stocker(Demande demande) {
		// TODO Auto-generated method stub

	}

	@Override
	public void eteindreTousBoutons() {
		// TODO Auto-generated method stub

	}

	@Override
	public void viderStock() {
		// TODO Auto-generated method stub

	}

	@Override
	public void interrogerStock() {
		// TODO Auto-generated method stub

	}

	@Override
	public void enleverDuStock(Demande demande) {
		// TODO Auto-generated method stub

	}

	@Override
	public void demander(Demande demande) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void arretDUrgence() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void signalerChangementDEtage() {
		// TODO Auto-generated method stub
		
	}
}
