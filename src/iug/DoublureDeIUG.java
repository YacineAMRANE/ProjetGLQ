package iug;

import java.util.ArrayList;

import outils.Demande;
import outils.Sens;
import iug.IIUG;
import Controleur.IControleur;
import commande.ListeTrieeCirculaireDeDemandes;

public class DoublureDeIUG implements IIUG {
	
	
	public void eteindreTousBoutons(){
		
	}
	
	public void allumerBouton(Demande d){
		System.out.println("Bouton allum� : "+d.toString());
	}
	
	public void eteindreBouton(Demande d){
		
	}
	
	@Override
	public void changerPosition(int position){
		// TODO Auto-generated method stub
	}

	@Override
	public void ajouterMessage(String message) {
		// TODO Auto-generated method stub
		
	}
}
