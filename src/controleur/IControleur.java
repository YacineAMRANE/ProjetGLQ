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
	 * Incrémente position de 1 si sens vaut MONTEE, décrémente de 1 si sens vaut DESCENTE.
	 */
	void MAJPosition();

	/**
	 * Met sens à INDEFINI si la cabine est arrêtée, MONTEE si la cabine monte et DESCENTE si elle descend stocker( demande) stocke la demande.
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
	 * Renvoie la demande du stock qui vérifie certaines conditions par rapport à la position et au sens ou au sensPrecedent.
	 */
	Demande interrogerStock();

	/**
	 * enlève la demande du stock
	 * @param demande Demande à enlever du stock.
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
