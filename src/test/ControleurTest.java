package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.*;

import org.junit.*;

import cabine.DoublureDeCabine;
import cabine.ICabine;
import commande.IListeTrieeCirculaire;
import commande.ListeTrieeCirculaireDeDemandes;
import controleur.*;
import iug.DoublureDeIUG;
import outils.Demande;
import outils.Sens;
import iug.IIUG;

public class ControleurTest {
	private IControleur controleur;
	private ICabine cabine;
	private IIUG iug;
	
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
		assertTrue(controleur.getSens().equals(Sens.INDEFINI));
		assertTrue(controleur.getPosition() == 0);
		assertTrue(controleur.getDemandeCourante() == null);
	}
	
	/**
	 * Les appels de l'ascenseur après un arrêt prolongé
	 */
	@Test 
	public void testCase1() throws IOException
	{
		File ficTestCase1 = new File("TestCase1.txt");
		PrintStream ps = new PrintStream (ficTestCase1);
		System.setOut(ps);
		
		StringBuffer sb = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		// cabine en 0
		// appel 1M
		controleur.demander(new Demande(1, Sens.MONTEE));
		// cabine en 1
		controleur.signalerChangementDEtage();
		//appel 4C
		controleur.demander(new Demande(4, Sens.INDEFINI));
		//appel 7D
		controleur.demander(new Demande(7, Sens.DESCENTE));
		// cabine en 2
		controleur.signalerChangementDEtage();
		// arrêt urgence 
		controleur.arretDUrgence();
		// eteindre tous les boutons
		controleur.eteindreTousBoutons();
		// reprise
		// appel 4C
		controleur.demander(new Demande(4, Sens.INDEFINI));
		// cabine en 3
		controleur.signalerChangementDEtage();
		
		byte[] buff = new byte[16];
		FileInputStream fis2 = new FileInputStream(ficTestCase1);
		while(fis2.read(buff) != -1) {
			String s = new String(buff);
			sb.append(s);
		}	
		
		File fic = new File("TestCase1Correct.txt");
		FileInputStream fis = new FileInputStream(fic);
		while(fis.read(buff) != -1) {
			String s = new String(buff);
			sb2.append(s);
		}
		fis2.close();
		fis.close();
		assertEquals(sb2.toString(), sb.toString());
	}
}
