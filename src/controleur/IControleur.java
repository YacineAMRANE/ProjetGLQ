package controleur;

import outils.Demande;

/**
 * L'interface IControleur décrit un controleur qui fait le lien entre l'iug et la cabine.
 * @author yacine AMRANE, alexis BRUNET, franck BRUN, yann DENICOLO
 *
 */
public interface IControleur {
	/**
	 * Indique une nouvelle demande d'utilisateur.
	 * @param d demande de deplacement.
	 */
    public void demander( Demande d);
    
    /**
     * Indique un arret d'urgence demandé par un utilsateur stoppant les deplacement de l'ascenseur.
     */
    public void arretUrgence();
    
    /**
     * Indique un changement d'etage de la cabine.
     */
    public void signalerChangementDEtage();
}