package controleur;

import cabine.ICabine;
import commande.ListeTrieeCirculaireDeDemandes;
import iug.IIUG;
import outils.Demande;
import outils.Sens;

/**
 * Interface IControleur.
 * @author yacine
 */
public interface IControleur {
	
	/**
	 * Faire une demande au controleur.
	 */
	void demander(Demande demande);
	
	/**
	 * Arret d'urgence.
	 */
	void arretDUrgence();
	
	/**
	 * Signaler un changement d'etage.
	 */
	void signalerChangementDEtage();
	
	/**
	 * Incr�mente position de 1 si sens vaut MONTEE, d�cr�mente de 1 si sens vaut DESCENTE.
	 */
	void MAJPosition();

	/**
	 * Met sens � INDEFINI si la cabine est arr�t�e, MONTEE si la cabine monte et DESCENTE si elle descend stocker( demande) stocke la demande.
	 */
	void MAJSens();

	/**
	 * Stocke la demande.
	 */
	void stocker (Demande demande);

	/**
	 * Eteint tous les boutons.
	 */
	void eteindreTousBoutons();

	/**
	 * Vide le stock des demandes.
	 */
	void viderStock();

	/**
	 * Renvoie la demande du stock qui v�rifie certaines conditions par rapport � la position et au sens ou au sensPrecedent.
	 */
	Demande interrogerStock();

	/**
	 * enl�ve la demande du stock
	 * @param demande Demande � enlever du stock.
	 */
	void enleverDuStock(Demande demande);

	ListeTrieeCirculaireDeDemandes getStockDeDemandes();

	void setIUG(IIUG iug);

	void setCabine(ICabine cabine);

	Sens getSens();

	int getPosition();

	Demande getDemandeCourante();

	void setPosition(int i);

}
