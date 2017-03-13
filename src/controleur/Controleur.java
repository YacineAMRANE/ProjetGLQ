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
		this.sens = sens.INDEFINI;
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

		if (demande.sens() == Sens.INDEFINI && demande.etage() != 0 ) {
			if (this.sens == Sens.MONTEE && this.position<demande.etage()){
				demande.changeSens(Sens.MONTEE);
			}
			if (this.sens == Sens.MONTEE && this.position>demande.etage()){
				demande.changeSens(Sens.DESCENTE);
			}
			if (this.sens == Sens.DESCENTE && this.position>demande.etage()){
				demande.changeSens(Sens.DESCENTE);
			}
			if (this.sens == Sens.DESCENTE && this.position<demande.etage()){
				demande.changeSens(Sens.MONTEE);
			}
		}
		stock.inserer(demande);
	}

	@Override
	public void eteindreTousBoutons() {
		for(int i = 0; i < stock.taille(); i++) {
			this.iug.eteindreBouton(new Demande(stock.get(i).etage(), stock.get(i).sens()));
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
		if(this.mode == mode.ARRETURGENCE)
			this.mode = mode.ATTENTE;
		
		Demande demandeStock = new Demande();

		if (!stock.estVide()) {
			demandeStock = ((Demande)stock.suivantDe(new Demande(this.position, this.sens)));
		}else{
			if (this.mode == Mode.ARRETETAGE)
				this.mode = Mode.ATTENTE;	
		}
		if(this.sens == sens.INDEFINI)
			this.sens = this.sensPrecedent;
		else
			this.sensPrecedent = this.sens;
		Mode ancienMode = this.mode;

		if (ancienMode == Mode.ATTENTE) {
			if (dem.etage()>this.position) {
				cabine.monter();
				this.mode = Mode.MONTEE;
			}
			if (dem.etage()<this.position) {
				cabine.descendre();
				this.mode = Mode.DESCENTE;
			}
			MAJSens();
			if (Math.abs(dem.etage() - this.position) == 1) {
				this.mode = Mode.ARRETIMMINENT;
			}
			stocker(dem);
			iug.allumerBouton(dem);
		}

		if (ancienMode == Mode.MONTEE || ancienMode == Mode.DESCENTE ){
			stocker(dem);
			iug.allumerBouton(dem);
		}

		if (ancienMode == Mode.ARRETIMMINENT && (dem.etage() != position+1 || dem.sens() != sens )){
			stocker(dem);
			iug.allumerBouton(dem);
		}

		//14
		if (ancienMode == Mode.ARRETETAGE &&  (dem.etage() != position || dem.sens()!=sensPrecedent)){
			stocker(dem);
			iug.allumerBouton(dem);
		}


		if (ancienMode == Mode.ARRETETAGE ) {

			if (dem.sens() == Sens.INDEFINI){
				if (demandeStock == null) {
					if (dem.etage() > this.position){
						cabine.monter();
						MAJSens();
						this.mode = Mode.MONTEE;
					}else if (dem.etage() < this.position){
						cabine.descendre();
						MAJSens();
						this.mode = Mode.DESCENTE;
					}
				} else {
					if (sensPrecedent == Sens.MONTEE && demandeStock.sens() == sens.MONTEE ){
						if (dem.etage() > demandeStock.etage()){
							stocker(dem);
							dem = demandeStock;
						}else {
							dem.changeSens(Sens.MONTEE);
						}
						if (dem.etage() > this.position && dem.sens() == Sens.INDEFINI || dem.sens() == this.sensPrecedent){
							cabine.monter();
							MAJSens();
							this.mode = Mode.MONTEE;
						}
					}
					if (sensPrecedent == Sens.DESCENTE && demandeStock.sens() == sens.DESCENTE ){
						if (dem.etage() < demandeStock.etage()){
							stocker(dem);
							dem = demandeStock;
						}else {
							dem.changeSens(Sens.DESCENTE);
						}
						if (dem.etage() < this.position && dem.sens() == Sens.INDEFINI || dem.sens() == this.sensPrecedent){
							cabine.descendre();
							MAJSens();
							this.mode = Mode.DESCENTE;
						}
					}
				}
			}

			else { 

				if ( this.sens==Sens.DESCENTE && demandeStock!=null && dem.sens() == demandeStock.sens()) {
					if (demandeStock.etage()>dem.etage())
					{
						stocker(dem);
						dem = demandeStock;
					}
					if (dem.etage() < this.position && dem.sens() == Sens.INDEFINI || dem.sens() == this.sensPrecedent){
						cabine.descendre();
						MAJSens();
						this.mode = Mode.DESCENTE;
					}
				}
				if ( this.sens==Sens.MONTEE && demandeStock!=null && dem.sens() == demandeStock.sens()) {
					if (demandeStock.etage()<dem.etage())
					{
						stocker(dem);
						dem = demandeStock;
					}
					if (dem.etage() > this.position && dem.sens() == Sens.INDEFINI || dem.sens() == this.sensPrecedent){
						cabine.monter();
						MAJSens();
						this.mode = Mode.MONTEE;
					}
				}
			}
		}
	}


	@Override
	public void signalerChangementDEtage() {

		Mode nouveauMode = this.mode;

		if (this.mode == Mode.DESCENTE || this.mode == Mode.MONTEE) {

			ArrayList<Demande> listeTriee = stock.getListeTriee();

			for (Demande demande : listeTriee) {
				if ((demande.etage() == this.position+1) || (demande.etage() == this.position-1) && (demande.sens()==this.sens || demande.sens()==Sens.INDEFINI)){
					cabine.arreterProchainNiveau();
				}
				break;
			}
			nouveauMode = Mode.ARRETIMMINENT;
		}

		if (this.mode == Mode.MONTEE || this.mode == Mode.DESCENTE ){ 
			MAJPosition();
		}

		if (this.mode == Mode.ARRETIMMINENT){
			MAJPosition();	
			MAJSens();
			if(this.stock.contient(new Demande(this.position, this.sens)))
				enleverDuStock(new Demande(this.position, this.sens));
			cabine.arreterProchainNiveau();
			nouveauMode = Mode.ARRETETAGE;
		}

		this.mode = nouveauMode;
	}


	@Override
	public void arretDUrgence() {
		if (this.mode != Mode.ARRETURGENCE) {
			if(this.mode == Mode.DESCENTE || this.mode == Mode.MONTEE){
				eteindreTousBoutons();
				viderStock();
				arreter();
				MAJSens();
			}
			if(this.mode == Mode.ARRETETAGE || this.mode == Mode.ATTENTE || this.mode == Mode.ARRETIMMINENT) {
				eteindreTousBoutons();
				viderStock();
			}
		}
		this.mode = mode.ARRETURGENCE;
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