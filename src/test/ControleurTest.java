package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cabine.DoublureDeCabine;
import cabine.ICabine;
import commande.IListeTrieeCirculaire;
import commande.ListeTrieeCirculaireDeDemandes;
import controleur.*;
import iug.DoublureDeIUG;
import outils.Demande;
import outils.Sens;

public class ControleurTest {
	private IControleur controleur;
	private ICabine cabine;
	private IIug iug;
	
	@Before
	public void setUp() {
		controleur = new Controleur(10);
		cabine = new DoublureDeCabine();
		iug = new DoublureDeIUG();
		controleur.setIUG(iug);
		controleur.setCabine(cabine);
	}

	@After
	public void tearDown() throws Exception {
		
	}

	/**
	 * Methode de test de {@link controleur.Controleur#Controleur()}.
	 */
	@Test
	public void testControleur() {
		assertTrue(controleur.getStockDeDemandes().estVide());
	}
}
