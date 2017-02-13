package commande;

import java.util.ArrayList;

import outils.Demande;
import outils.Sens;

public class ListeTrieeCirculaireDeDemandes implements IListeTrieeCirculaire{

	private ArrayList<Demande> listeTrieeCirculaireDeDemandes;
	private int length;
	
	public ListeTrieeCirculaireDeDemandes(int length)
	{
		listeTrieeCirculaireDeDemandes = new ArrayList<Demande>(length);
		this.length = length;
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
	public void inserer(Object e) throws IllegalArgumentException{
		if(!(e instanceof Demande)) 
		{
			throw new IllegalArgumentException();
		}
		if(listeTrieeCirculaireDeDemandes.size() == this.length)
		{
			throw new IllegalArgumentException();
		}
		if(((Demande) e).estIndefini())
		{
			throw new IllegalArgumentException();
		}
		if(((Demande) e).enDescente() && ((Demande) e).etage() == 0)
		{
			throw new IllegalArgumentException();
		}
		if(((Demande) e).etage() < 0)
		{
			throw new IllegalArgumentException();
		}
		if(((Demande) e).etage() > this.length-1)
		{
			throw new IllegalArgumentException();
		}
		if(((Demande) e).enMontee() && ((Demande) e).etage() == this.length-1)
		{
			throw new IllegalArgumentException();
		}
		if(!this.contient(e))
		{
			listeTrieeCirculaireDeDemandes.add((Demande) e);
		}
	}

	@Override
	public void supprimer(Object o) {
		if(!this.contient(o))
		{
			throw new IllegalArgumentException();
		}
		else
		{
			listeTrieeCirculaireDeDemandes.remove((Demande)o);
		}
	}

	@Override
	public Object suivantDe(Object courant) {
		if(this.estVide())
		{
			return null;
		}
		if(!this.contient(courant))
		{
			this.inserer(courant);
		}
		// TODO Ajouter si pas existant ????
		Demande demandeSuivanteTriee = null;
		for (Demande demande : this.listeTrieeCirculaireDeDemandes)
		{
			if(demandeSuivanteTriee == null )demandeSuivanteTriee = demande;
			if(demande.sens().equals(((Demande) courant).sens()))
			{
				if(demande.enMontee() && ((Demande) courant).etage() > demande.etage() && 
						demande.etage() < demandeSuivanteTriee.etage())
				{
					demandeSuivanteTriee =  demande;
				}
				if(demande.enDescente() && ((Demande) courant).etage() < demande.etage() && 
						demande.etage() > demandeSuivanteTriee.etage())
				{
					demandeSuivanteTriee =  demande;
				}
			}
		}
		return demandeSuivanteTriee;
	}
	
	@Override
	public String toString() {
		//permet de construire la String to return
		String s ="";
		// la liste triee apres jointure entre la montée et la descente
		ArrayList<Demande> listeTriee = new ArrayList<Demande>(listeTrieeCirculaireDeDemandes.size());
		// permet de stocker toutes les montées
		ArrayList<Demande> listeMontee = new ArrayList<Demande>(listeTrieeCirculaireDeDemandes.size());
		// permet de stocker toutes les descentes
		ArrayList<Demande> listeDescente = new ArrayList<Demande>(listeTrieeCirculaireDeDemandes.size());
		
		
		// on itere sur la donnée membre listeTrieeCirculaireDeDemandes où on a stockée toutes les demandes
		for (Demande demande : listeTrieeCirculaireDeDemandes) 
		{
			// gestion de la montée
			if(demande.enMontee())
			{
				// si la liste montée et vide ou si le dernier element de la iste montée est plus petit que l'element couranr de la boucle
				if(listeMontee.isEmpty() || listeMontee.get(listeMontee.size()-1).etage() < demande.etage())
				{
					// on ajoute
					listeMontee.add(demande);
				}
				// si le dernier element de la iste montée est plus grand que l'element couranr de la boucle
				else
				{
					for(int i = 0 ; i < listeMontee.size(); i++)
					{
						if(listeMontee.get(i).etage() > demande.etage())
						{
							listeMontee.add(i, demande);
							break;
						}
					}
				}
			}
			else if(demande.sens() == Sens.DESCENTE)
			{
				if(listeDescente.isEmpty() || listeDescente.get(listeDescente.size()-1).etage() > demande.etage())
				{
					listeDescente.add(demande);
				}
				else
				{
					for(int i = 0 ; i < listeDescente.size(); i++)
					{
						
						if(listeDescente.get(i).etage() < demande.etage())
						{
							// on switch les positions entre l'element à insérer et le dernier element plus grand
							listeDescente.add(i, demande);
							break;
						}
					}
				}
			}
		}
		//jointure
		listeMontee.addAll(listeDescente);
		listeTriee = listeMontee;
		// composition de la string to return easy
		s+="[";
		for (int i = 0; i < listeTriee.size();i++) {
			if(i == listeTriee.size())
			{
				s+=listeTriee.get(i).toString();
			}
			else
			{
				s+=listeTriee.get(i).toString() +",";
			}
		}
		if(s.endsWith(","))
		{
			s = s.substring(0, s.length()-1);
		}
		s+="]";
		return s;
	}
}
