package controleur;

import outils.Demande;
import java.util.ArrayList;
import cabine.DoublureDeCabine;
import cabine.ICabine;
import commande.ListeTrieeCirculaireDeDemandes;
import iug.DoublureDeIUG;
import iug.IIUG;
import outils.Sens;

/**
 * Classe controleur.
 * @author yacine
 */
public class Controleur implements IControleur {

	private int nombreEtages;
	private int position;
	private Sens sens;
	private Sens sensPrecedent;
	private ListeTrieeCirculaireDeDemandes stock;
	private DoublureDeCabine cabine;
	private DoublureDeIUG iug;
	private Mode mode;

	public Controleur(int nombreEtages) {
		super();
		this.nombreEtages = nombreEtages;
		this.stock = new ListeTrieeCirculaireDeDemandes(nombreEtages);
		this.mode = Mode.ATTENTE;
		this.position = 0;
	}

	@Override
	public void MAJPosition() {
		if (this.sens == Sens.MONTEE) {
			this.position++;
		} else if (this.sens == Sens.DESCENTE) {
			this.position--;
		}
	}

	@Override
	public void MAJSens() {
		if (this.mode == Mode.ARRETETAGE || this.mode == Mode.ATTENTE || this.mode == Mode.ARRETURGENCE) {
			this.sens = Sens.INDEFINI;
		}
		if (this.mode == Mode.DESCENTE) this.sens = Sens.DESCENTE; 
		if (this.mode == Mode.MONTEE) this.sens = Sens.MONTEE;
	}


	@Override
	public void stocker(Demande demande) {
		stock.inserer(demande);
	}

	@Override
	public void eteindreTousBoutons() {
		for(int etage = 0; etage < this.nombreEtages; etage++) {
			for(Sens sens : Sens.values()) {
				this.iug.eteindreBouton(new Demande(etage, sens));
			}
		}
	}

	@Override
	public void viderStock() {
		this.stock.vider();
	}

	@Override
	public Demande interrogerStock() {
		return (Demande) (this.stock.suivantDe(new Demande(this.getPosition(), this.getSens())));
	}

	@Override
	public void enleverDuStock(Demande demande) {
		this.stock.supprimer(demande);
	}

	@Override
	public void demander(Demande dem) {

		Mode nouveauMode = this.mode;

		if (this.mode == Mode.ATTENTE) {
			if (dem.etage()>this.position) {
				cabine.monter();
				nouveauMode = Mode.MONTEE;
			}
			if (dem.etage()<this.position) {
				cabine.descendre();
				nouveauMode = Mode.DESCENTE;
			}
			MAJSens();
			if (Math.abs(dem.etage() - this.position) == 1) {
				cabine.arreterProchainNiveau();
				nouveauMode = Mode.ARRETIMMINENT;
			}
			stocker(dem);
			iug.allumerBouton(dem);
		}

		if (this.mode == Mode.MONTEE || this.mode == Mode.DESCENTE ){
			stocker(dem);
			iug.allumerBouton(dem);
		}

		if (this.mode == Mode.ARRETIMMINENT && (dem.etage() != position+1 || dem.sens() != sens )){
			stocker(dem);
			iug.allumerBouton(dem);
		}

		if (this.mode == Mode.ARRETETAGE &&  (dem.etage() != position || dem.sens()!=sensPrecedent)){
			stocker(dem);
			iug.allumerBouton(dem);
		}

		this.mode = nouveauMode; 

	}


	@Override
	public void signalerChangementDEtage() {

		Mode nouveauMode = this.mode;

		if (this.mode == Mode.DESCENTE || this.mode == Mode.MONTEE) {

			ArrayList<Demande> listeTriee = stock.getListeTriee();

			for (Demande demande : listeTriee) {
				if ((demande.etage() == this.position+1) || (demande.etage() == this.position-1) && (demande.sens()==this.sens || demande.sens()==Sens.INDEFINI)){
					cabine.arreterProchainNiveau();
					MAJPosition();
				}
				else
					MAJPosition();
			}

			nouveauMode = Mode.ARRETIMMINENT;

		}

		if (this.mode == Mode.MONTEE || this.mode == Mode.DESCENTE ){ 
			MAJPosition();
		}
		
		if (this.mode == Mode.ARRETIMMINENT){
			enleverDuStock(new Demande(this.position, this.sens));
			MAJPosition();
			MAJSens();
		}
		
		this.mode = nouveauMode;
	}


	@Override
	public void arretDUrgence() {
		if (this.mode != Mode.ARRETURGENCE) {
			if(this.mode == Mode.DESCENTE || this.mode == Mode.DESCENTE){
				viderStock();
				eteindreTousBoutons();
				arreter();
				MAJSens();
			}
			if(this.mode == Mode.ARRETETAGE || this.mode == Mode.ATTENTE) {
				viderStock();
				eteindreTousBoutons();
			}
		}
		else 
		{
			this.mode = Mode.ATTENTE;
		}
	}

	private void arreter() {
		this.cabine.arreter();
	}

	@Override
	public void setIUG(IIUG iug) {
		this.iug = (DoublureDeIUG) iug;
	}

	@Override
	public void setCabine(ICabine cabine) {
		this.cabine = (DoublureDeCabine) cabine;
	}

	@Override
	public ListeTrieeCirculaireDeDemandes getStockDeDemandes() {
		return this.stock;
	}

	@Override
	public Sens getSens() {
		return this.sens;
	}

	@Override
	public int getPosition() {
		return this.position;
	}

	@Override
	public Demande getDemandeCourante() {
		return new Demande(this.position, this.sens);
	}

	@Override
	public void setPosition(int i) {
		this.position = i;
	}

}
