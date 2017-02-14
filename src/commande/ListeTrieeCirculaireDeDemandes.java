<<<<<<< HEAD
package commande;

import java.util.ArrayList;

import outils.Demande;
import outils.Sens;

public class ListeTrieeCirculaireDeDemandes implements IListeTrieeCirculaire{

	/*
	 * liste triee contenant l'ensemble des demandes.
	 */
	private ArrayList<Demande> listeTrieeCirculaireDeDemandes;
	/*
	 * taille de la liste
	 */
	private int length;
	
	/**
	 * Constructeur.
	 * @param length taille.
	 */
	public ListeTrieeCirculaireDeDemandes(int length)
	{
		listeTrieeCirculaireDeDemandes = new ArrayList<Demande>(length);
		this.length = length;
	}
	
	/**
	 * 
	 * @return la taille de la liste.
	 */
	@Override
	public int taille() {
		return listeTrieeCirculaireDeDemandes.size();
	}

	/**
	 * 
	 * @return True si la liste est vide.
	 */
	@Override
	public boolean estVide() {
		return listeTrieeCirculaireDeDemandes.isEmpty();
	}

	/**
	 * 
	 *  Permet de vider la liste.
	 */
	@Override
	public void vider() {
		listeTrieeCirculaireDeDemandes.clear();
	}
	
	/**
	 * @param demande Demande à vérifier.
	 * @return True si la liste contient la demande indiquée.
	 */
	@Override
	public boolean contient(Object e) {
		return listeTrieeCirculaireDeDemandes.contains(e);
	}

	/**
	 * Permet d'insérer la demande indiquée.
	 * @param demande Demande à insérer.
	 */
	@Override
	public void inserer(Object e) throws IllegalArgumentException{
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

	/**
	 * Supprime la demande indiquée.
	 * @param demande Demande à supprimer.
	 */
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

	/**
	 * @param demande Demande à vérifier.
	 * @return demande Demande suivante.
	 */
	@Override
	public Object suivantDe(Object courant) {
		if(this.estVide())
		{
			return null;
		}
		if(this.contient(courant))
		{
			return courant;
		}
		
		Demande demandeSuivanteTriee = null;
		Demande demandePlusPetitMontee = null;
		Demande demandePlusGrandMontee = null;
		Demande demandePlusPetitDescente = null;
		Demande demandePlusGrandDescente = null;
		int i = 0;
		for (Demande demande : this.listeTrieeCirculaireDeDemandes)
		{
			if(demandeSuivanteTriee == null )demandeSuivanteTriee = demande;
			if(demande.enMontee())
			{
				if(demandePlusPetitMontee == null )demandePlusPetitMontee = demande;
				if(demandePlusGrandMontee == null )demandePlusGrandMontee = demande;
				if(demande.etage() < demandePlusPetitMontee.etage())
				{
					demandePlusPetitMontee = demande;
				}
				else
				{
					demandePlusGrandMontee = demande;
				}
			}
			if(demande.enDescente())
			{
				if(demandePlusPetitDescente == null )demandePlusPetitDescente = demande;
				if(demandePlusGrandDescente == null )demandePlusGrandDescente = demande;
				if(demande.etage() < demandePlusPetitDescente.etage())
				{
					demandePlusPetitDescente = demande;
				}
				else
				{
					demandePlusGrandDescente = demande;
				}
			}
			
			if(demande.sens().equals(((Demande) courant).sens()))
			{
				if(demande.enMontee() && ((Demande) courant).etage() > demande.etage() && 
						demande.etage() > demandeSuivanteTriee.etage())
				{
					demandeSuivanteTriee =  demande;
				}
				else if(demande.enDescente() && ((Demande) courant).etage() > demande.etage() && 
						demande.etage() < demandeSuivanteTriee.etage())
				{
					demandeSuivanteTriee =  demande;
				}
			}
			if(listeTrieeCirculaireDeDemandes.size()-1 == i)
			{
				if(!demande.sens().equals(((Demande) courant).sens()))
				{
					if(((Demande) courant).enDescente())
					{
						if(demandePlusPetitDescente == null)
						{
							demandeSuivanteTriee = demandePlusPetitMontee;
						}
						else if(((Demande) courant).etage() < demandePlusPetitDescente.etage())
						{
							if(demandePlusPetitMontee != null)
							{
								demandeSuivanteTriee = demandePlusPetitMontee;
							}
							else
							{
								demandeSuivanteTriee = demandePlusGrandDescente;
							}
						}
					}
					
					if(((Demande) courant).enMontee())
					{
						if(demandePlusGrandMontee == null)
						{
							demandeSuivanteTriee = demandePlusGrandDescente;
						}
						else if(((Demande) courant).etage() > demandePlusGrandMontee.etage())
						{
							demandeSuivanteTriee = demandePlusPetitMontee;
						}
						else if(((Demande) courant).etage() < demandePlusPetitMontee.etage())
						{
							demandeSuivanteTriee = demandePlusPetitMontee;
						}
					}
				}
				if(demande.enMontee() && ((Demande) courant).etage() > demandePlusGrandMontee.etage())
				{
					demandeSuivanteTriee = demandePlusPetitMontee;
				}
				
				if(demande.enDescente())
				{
					if(demande.sens().equals(((Demande) courant).sens()) && ((Demande) courant).etage() < demandePlusPetitDescente.etage())
					{
						if(demandePlusPetitMontee != null)
						{
							demandeSuivanteTriee = demandePlusPetitMontee;
						}
						else
						{
							demandeSuivanteTriee = demandePlusGrandDescente;
						}
					}
				}
			}
			i++;
		}
		return demandeSuivanteTriee;
	}
	
	/**
	 * Redefinition de toString.
	 * @return la liste triee.
	 */
	@Override
	public String toString() {
		String s ="";
		ArrayList<Demande> listeMontee = new ArrayList<Demande>();
		ArrayList<Demande> listeDescente = new ArrayList<Demande>();
		
		for (Demande demande : listeTrieeCirculaireDeDemandes) 
		{
			if(demande.enMontee())
			{
				if(listeMontee.isEmpty() || listeMontee.get(listeMontee.size()-1).etage() < demande.etage())
				{
					listeMontee.add(demande);
				}
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
			else if(demande.enDescente())
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
		listeMontee.addAll(listeDescente);
		ArrayList<Demande> listeTriee = new ArrayList<Demande>(listeMontee);
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
=======
package commande;

import java.util.ArrayList;

import outils.Demande;
import outils.Sens;

/**
 * Classe IListeTrieeCirculaire.
 * @author alexis,yacine,franck,yann
 */
public class ListeTrieeCirculaireDeDemandes implements IListeTrieeCirculaire{

	/*
	 * liste triee contenant l'ensemble des demandes.
	 */
	private ArrayList<Demande> listeTrieeCirculaireDeDemandes;
	/*
	 * taille de la liste
	 */
	private int length;

	/**
	 * Constructeur.
	 * @param length taille.
	 */
	public ListeTrieeCirculaireDeDemandes(int length)
	{
		listeTrieeCirculaireDeDemandes = new ArrayList<Demande>(length);
		this.length = length;
	}

	/**
	 * 
	 * @return la taille de la liste.
	 */
	@Override
	public int taille() {
		return listeTrieeCirculaireDeDemandes.size();
	}

	/**
	 * 
	 * @return True si la liste est vide.
	 */
	@Override
	public boolean estVide() {
		return listeTrieeCirculaireDeDemandes.isEmpty();
	}

	/**
	 * 
	 *  Permet de vider la liste.
	 */
	@Override
	public void vider() {
		listeTrieeCirculaireDeDemandes.clear();
	}

	/**
	 * @param e Demande à vérifier.
	 * @return True si la liste contient la demande indiquée.
	 */
	@Override
	public boolean contient(Object e) {
		return listeTrieeCirculaireDeDemandes.contains(e);
	}

	/**
	 * Permet d'insérer la demande indiquée.
	 * @param e Demande à insérer.
	 */
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

	/**
	 * Supprime la demande indiquée.
	 * @param o Demande à supprimer.
	 */
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

	/**
	 * @param demande Demande à vérifier.
	 * @return courant Demande suivante.
	 */
	@Override
	public Object suivantDe(Object courant) {
		if(this.estVide())
		{
			return null;
		}
		if(this.contient(courant))
		{
			return courant;
		}

		Demande demandeSuivanteTriee = null;
		Demande demandePlusPetitMontee = null;
		Demande demandePlusGrandMontee = null;
		Demande demandePlusPetitDescente = null;
		Demande demandePlusGrandDescente = null;
		int i = 0;
		for (Demande demande : this.listeTrieeCirculaireDeDemandes)
		{
			if(demandeSuivanteTriee == null )demandeSuivanteTriee = demande;
			if(demande.enMontee())
			{
				if(demandePlusPetitMontee == null )demandePlusPetitMontee = demande;
				if(demandePlusGrandMontee == null )demandePlusGrandMontee = demande;
				if(demande.etage() < demandePlusPetitMontee.etage())
				{
					demandePlusPetitMontee = demande;
				}
				if(demande.etage() > demandePlusGrandMontee.etage())
				{
					demandePlusGrandMontee = demande;
				}
			}
			if(demande.enDescente())
			{
				if(demandePlusPetitDescente == null )demandePlusPetitDescente = demande;
				if(demandePlusGrandDescente == null )demandePlusGrandDescente = demande;
				if(demande.etage() < demandePlusPetitDescente.etage())
				{
					demandePlusPetitDescente = demande;
				}
				if(demande.etage() > demandePlusGrandDescente.etage())
				{
					demandePlusGrandDescente = demande;
				}
			}

			if(demande.sens().equals(((Demande) courant).sens()))
			{
				if(demande.enMontee() && ((Demande) courant).etage() > demande.etage() && 
						demande.etage() > demandeSuivanteTriee.etage())
				{
					demandeSuivanteTriee =  demande;
				}
				if(demande.enDescente() && ((Demande) courant).etage() > demande.etage() && 
						demande.etage() < demandeSuivanteTriee.etage())
				{
					demandeSuivanteTriee =  demande;
				}
			}
			if(listeTrieeCirculaireDeDemandes.size()-1 == i)
			{
				if(!demande.sens().equals(((Demande) courant).sens()))
				{
					if(((Demande) courant).enDescente())
					{
						if(demandePlusPetitDescente == null)
						{
							demandeSuivanteTriee = demandePlusPetitMontee;
						}
						else if(((Demande) courant).etage() < demandePlusPetitDescente.etage())
						{
							if(demandePlusPetitMontee != null)
							{
								demandeSuivanteTriee = demandePlusPetitMontee;
							}
							else
							{
								demandeSuivanteTriee = demandePlusGrandDescente;
							}
						}
					}

					if(((Demande) courant).enMontee())
					{
						if(demandePlusGrandMontee == null)
						{
							demandeSuivanteTriee = demandePlusGrandDescente;
						}
						else if(((Demande) courant).etage() > demandePlusGrandMontee.etage())
						{
							demandeSuivanteTriee = demandePlusPetitMontee;
						}
						else if(((Demande) courant).etage() < demandePlusPetitMontee.etage())
						{
							demandeSuivanteTriee = demandePlusPetitMontee;
						}
					}
				}
				if(demande.enMontee() && ((Demande) courant).etage() > demandePlusGrandMontee.etage())
				{
					demandeSuivanteTriee = demandePlusPetitMontee;
				}

				if(demande.enDescente())
				{
					if(demande.sens().equals(((Demande) courant).sens()) && ((Demande) courant).etage() < demandePlusPetitDescente.etage())
					{
						if(demandePlusPetitMontee != null)
						{
							demandeSuivanteTriee = demandePlusPetitMontee;
						}
						else
						{
							demandeSuivanteTriee = demandePlusGrandDescente;
						}
					}
				}
			}
			i++;
		}
		return demandeSuivanteTriee;
	}

	/**
	 * Redefinition de toString.
	 * @return la liste triee.
	 */
	@Override
	public String toString() {
		String s ="";
		ArrayList<Demande> listeMontee = new ArrayList<Demande>(listeTrieeCirculaireDeDemandes.size());
		ArrayList<Demande> listeDescente = new ArrayList<Demande>(listeTrieeCirculaireDeDemandes.size());

		for (Demande demande : listeTrieeCirculaireDeDemandes) 
		{
			if(demande.enMontee())
			{
				if(listeMontee.isEmpty() || listeMontee.get(listeMontee.size()-1).etage() < demande.etage())
				{
					listeMontee.add(demande);
				}
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
			else if(demande.enDescente())
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
		listeMontee.addAll(listeDescente);
		ArrayList<Demande> listeTriee = new ArrayList<Demande>(listeMontee);
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
>>>>>>> refs/remotes/origin/yacine
