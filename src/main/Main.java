package main;

import outils.Demande;
import operative.Cabine;
import operative.IUG;
import operative.ICabine;
import operative.IIUG;
import commande.IControleur;
import commande.Controleur;
import commande.IListeTrieeCirculaire;
import commande.ListeTrieeCirculaireDeDemandes;

public class Main {
    public static void main(String[] args) throws Exception {
    	int nbEtages = 7; // nombre d'étages
        int hauteurEtage = 3; // hauteur d'un étage en nombre de pas
        IIUG iug = new IUG( nbEtages);
        ICabine cabine = new Cabine( 
        		500, // délai d'un pas en ms
        		hauteurEtage, // hauteur d'un étage en nombre de pas
        		4); // délai de l'arrêt cabine en nombre de pas
        IListeTrieeCirculaire<Demande> stock = new ListeTrieeCirculaireDeDemandes( nbEtages);
        IControleur controleur = new Controleur( nbEtages, iug, cabine, stock);
        cabine.assignerControleur( controleur);
        iug.assignerControleur( controleur);
        //Attention, dans votre code, apres chaque modification de la position de la cabine 
        //(dans la methode signalerChanegementDEtage de Controleur), il faut ajouter
        //un appel a iug.changerPosition(nouvellePositionDeLaCabine).
    }
}

