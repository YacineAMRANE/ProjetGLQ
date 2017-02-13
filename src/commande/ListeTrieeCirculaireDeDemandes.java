package commande;

import java.util.ArrayList;

import outils.Demande;
import outils.Sens;

public class ListeTrieeCirculaireDeDemandes implements IListeTrieeCirculaire{

	private ArrayList<Demande> listeTrieeCirculaireDeDemandes;
	
	public ListeTrieeCirculaireDeDemandes(int length)
	{
		listeTrieeCirculaireDeDemandes = new ArrayList<Demande>(length);
	}
	
	@Override
	public int taille() {
		return listeTrieeCirculaireDeDemandes.size();
	}

	@Override
	public boolean estVide() {
		return listeTrieeCirculaireDeDemandes.isEmpty();
	}

	@Override
	public void vider() {
		listeTrieeCirculaireDeDemandes.clear();
	}

	@Override
	public boolean contient(Object e) {
		return listeTrieeCirculaireDeDemandes.contains(e);
	}

	@Override
	public void inserer(Object e) {
		listeTrieeCirculaireDeDemandes.add((Demande) e);
	}

	@Override
	public void supprimer(Object e) {
		listeTrieeCirculaireDeDemandes.remove(e);
		
	}

	@Override
	public Object suivantDe(Object courant) {
		Demande demandeSuivanteTriee = new Demande();
		for (Demande demande : this.listeTrieeCirculaireDeDemandes) {
			if(demande.sens().equals(((Demande) courant).sens()) && 
					((Demande) courant).etage() < demandeSuivanteTriee.etage() &&
					demande.etage() > demandeSuivanteTriee.etage())
			{
				demande = demandeSuivanteTriee;
			}
		}
		return demandeSuivanteTriee;
	}
	
	@Override
	public String toString() {
		//permet de construire la String to return
		String s ="";
		// la liste triee apres jointure entre la montée et la descente
		ArrayList<Demande> listeTriee = new ArrayList<Demande>();
		// permet de stocker toutes les montées
		ArrayList<Demande> listeMontee = new ArrayList<Demande>();
		// permet de stocker toutes les descentes
		ArrayList<Demande> listeDescente = new ArrayList<Demande>();
		
		
		// on itere sur la donnée membre listeTrieeCirculaireDeDemandes où on a stockée toutes les demandes
		for (Demande demande : listeTrieeCirculaireDeDemandes) 
		{
			// gestion de la montée
			if(demande.enMontee())
			{
				System.out.println(demande.enMontee());
				// si la liste montée et vide ou si le dernier element de la iste montée est plus petit que l'element couranr de la boucle
				if(listeMontee.isEmpty() || listeMontee.get(listeMontee.size()).etage() < demande.etage())
				{
					// on ajoute
					listeMontee.add(demande);
				}
				// si le dernier element de la iste montée est plus grand que l'element couranr de la boucle
				else
				{
					Demande demandeToCheck = demande;
					for(int i = 0 ; i < listeMontee.size(); i++)
					{
						
						if(listeMontee.get(i).etage() < demandeToCheck.etage())
						{
							// on recupere la position du dernier element
							Demande demandeToMove = listeMontee.get(i);
							// on switch les positions entre l'element à insérer et le dernier element plus grand
							listeMontee.add(i, demandeToCheck);
							demandeToCheck = demandeToMove;
						}
						else
						{
							listeMontee.add(demandeToCheck);
						}
					}
					
				}
			}
			else if(demande.sens() == Sens.DESCENTE)
			{
				if(listeDescente.isEmpty() || listeDescente.get(listeDescente.size()).etage() > demande.etage())
				{
					listeDescente.add(demande);
				}
				else
				{
					Demande demandeToCheck = demande;
					for(int i = 0 ; i < listeDescente.size(); i++)
					{
						
						if(listeDescente.get(i).etage() < demandeToCheck.etage())
						{
							// on recupere la position du dernier element
							Demande demandeToMove = listeDescente.get(i);
							// on switch les positions entre l'element à insérer et le dernier element plus grand
							listeDescente.add(i, demandeToCheck);
							demandeToCheck = demandeToMove;
						}
						else
						{
							listeDescente.add(demandeToCheck);
						}
					}
				}
			}
		}
		//jointure
		listeMontee.addAll(listeDescente);
		listeTriee = listeMontee;
		// composition de la string to return easy
		s+="[ ";
		for (int i = 0; i < listeTriee.size();i++) {
			if(i == listeTriee.size())
			{
				s+=listeTriee.get(i).toString();
			}
			else
			{
				s+=listeTriee.get(i).toString() +", ";
			}
		}
		s = s.substring(0, s.length()-1);
		s+=" ]";
		return s;
	}

}
