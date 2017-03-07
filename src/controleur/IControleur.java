package controleur;

import outils.Demande;

/**
 * 
 * @author yacine
 */
public interface IControleur {
	
	/**
	 * 
	 */
	void demander(Demande demande);
	
	/**
	 * 
	 */
	void arretDUrgence();
	
	/**
	 * 
	 */
	void signalerChangementDEtage();
	
	/**
	 * incr�mente position de 1 si sens vaut MONTEE, d�cr�mente de 1
	 * si sens vaut DESCENTE
	 */
	void MAJPosition();

	/**
	 * met sens � INDEFINI si la cabine est arr�t�e, MONTEE si la
	 * cabine monte et DESCENTE si elle descend
	 * stocker( demande) stocke la demande
	 * @return 
	 */
	void MAJSens();

	/**
	 * stocke la demande
	 */
	void stocker (Demande demande);

	/**
	 * �teint tous les boutons
	 */
	void eteindreTousBoutons();

	/**
	 * vide le stock des demandes
	 */
	void viderStock();

	/**
	 * renvoie la demande du stock qui v�rifie certaines conditions par
	 * rapport � la position et au sens ou au sensPrecedent
	 */
	void interrogerStock();

	/**
	 * enl�ve la demande du stock
	 * @param demande
	 */
	void enleverDuStock(Demande demande);

}