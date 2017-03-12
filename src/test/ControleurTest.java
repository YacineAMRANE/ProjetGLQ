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
		BufferedReader b = new BufferedReader(new InputStreamReader(fis));
		while(fis.read(buff) != -1 && b.readLine() != "END") {
			String s = new String(buff);
			sb2.append(s);
		}
		b.close();
		fis2.close();
		fis.close();
		assertEquals(sb2.toString(), sb.toString());
	}

/**
 * Les appels de l'ascenseur dans le même sens que celui de la cabine en cours de déplacement
 */
@Test 
public void testCase2() throws IOException
{
	File ficTestCase2 = new File("TestCase2.txt");
	PrintStream ps = new PrintStream (ficTestCase2);
	System.setOut(ps);
	
	StringBuffer sb = new StringBuffer();
	StringBuffer sb2 = new StringBuffer();
	
	//Manipulation de la cabine
	//---------------------------------------(cabine en 2)
	controleur.setPosition(2);
	// Un utilisateur interne appuie sur le bouton “étage 4”
	//appel 4C ------------------------------(utilisateur 1)
	controleur.demander(new Demande(4, Sens.INDEFINI));
	//allumer bouton 4C
	//monter
	//controleur.MAJSens();
	//signal de franchissement de palier ----(cabine en 3)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//A ce moment là un utilisateur externe se situant au 5ème demande l’ascenseur
	//appel 5M ------------------------------(utilisateur 2)
	//allumer bouton 5M
	controleur.demander(new Demande(5, Sens.MONTEE));
	//L’ascenseur se situe en 4, on éteint le bouton 4C
	//signal franchissement palier -----------(cabine en 4)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//éteindre bouton 4C
	//L’ascenseur continue de monter jusqu’à l’étage 5, demandé par l’utilisateur 2
	//monter
	//signal franchissement palier -----------(cabine en 5)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//L’utilisateur 2 monte dans l’ascenseur
	//monter
	//signal franchissement palier ------------(cabine en 6)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//signal franchissement palier ------------(cabine en 7)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//signal franchissement palier ------------(cabine en 8) 
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	
	byte[] buff = new byte[16];
	FileInputStream fis2 = new FileInputStream(ficTestCase2);
	while(fis2.read(buff) != -1) {
		String s = new String(buff);
		sb.append(s);
	}	
	
	File fic = new File("TestCase2Correct.txt");
	FileInputStream fis = new FileInputStream(fic);
	BufferedReader b = new BufferedReader(new InputStreamReader(fis));
	while(fis.read(buff) != -1 && b.readLine() != "END") {
		String s = new String(buff);
		sb2.append(s);
	}
	b.close();
	fis2.close();
	fis.close();
	assertEquals(sb2.toString(), sb.toString());
	}
/**
 * Les appels de l'ascenseur dans le sens inverse à celui de la cabine en cours de déplacement
 */
@Test 
public void testCase3() throws IOException
{
	File ficTestCase3 = new File("TestCase3.txt");
	PrintStream ps = new PrintStream (ficTestCase3);
	System.setOut(ps);
	
	StringBuffer sb = new StringBuffer();
	StringBuffer sb2 = new StringBuffer();

	//Manipulation de la cabine
	//----------------------------------------(cabine en 2) 
	controleur.setPosition(2);
	//appel 1M -------------------------------(utilisateur 1) 
	controleur.demander(new Demande(1, Sens.MONTEE));
	//allumer bouton 1M 
	//arrêter prochain étage 
	//descendre
	//signal de franchissement de palier ------(cabine en 1) 
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//éteindre bouton 1M 
	//appel 4C --------------------------------(utilisateur 1)
	controleur.demander(new Demande(4, Sens.INDEFINI));
	//allumer bouton 4C 
	//monter
	//controleur.MAJSens();
	//appel 3D---------------------------------(utilisateur 2) 
	controleur.demander(new Demande(3, Sens.DESCENTE));
	//allumer bouton 3D
	//signal de franchissement de palier ------(cabine en 2) 
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//signal de franchissement de palier ------(cabine en 3) 
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//arrêter prochain étage 
	//signal de franchissement de palier ------(cabine en 4)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//éteindre bouton 4C
	//descendre
	//controleur.MAJSens();
	//arrêter prochain étage 
	//signal de franchissement de palier ------(cabine en 3)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//éteindre bouton 3D
	//appel 0C --------------------------------(utilisateur 2)
	controleur.demander(new Demande(0, Sens.INDEFINI));
	//descendre
	//signal de franchissement de palier ------(cabine en 2)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	
	byte[] buff = new byte[16];
	FileInputStream fis2 = new FileInputStream(ficTestCase3);
	while(fis2.read(buff) != -1) {
		String s = new String(buff);
		sb.append(s);
	}	
	
	File fic = new File("TestCase3Correct.txt");
	FileInputStream fis = new FileInputStream(fic);
	BufferedReader b = new BufferedReader(new InputStreamReader(fis));
	while(fis.read(buff) != -1 && b.readLine() != "END") {
		String s = new String(buff);
		sb2.append(s);
	}
	b.close();
	fis2.close();
	fis.close();
	assertEquals(sb2.toString(), sb.toString());
	}
/**
 * Les appels de l'ascenseur qui sont satisfaits sans changement de sens de la cabine
 */
@Test 
public void testCase4() throws IOException
{
	File ficTestCase4 = new File("TestCase4.txt");
	PrintStream ps = new PrintStream (ficTestCase4);
	System.setOut(ps);
	
	StringBuffer sb = new StringBuffer();
	StringBuffer sb2 = new StringBuffer();

	//Manipulation de la cabine
	//----------------------------------------(cabine en 2)
	controleur.setPosition(2);
	//appel 6C -------------------------------(utilisateur 1)
	controleur.demander(new Demande(6, Sens.INDEFINI));
	//allumer bouton 6C
	//monter
	//controleur.MAJSens();
	//signal de franchissement ----------------(cabine en 3)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//appel 8M --------------------------------(utilisateur 2)
	controleur.demander(new Demande(8, Sens.MONTEE));
	//allumer bouton 8M
	//signal de franchissement de palier ------(cabine en 4)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//signal de franchissement de palier ------(cabine en 5)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//arrêt au prochain étage
	//signal de franchissement de palier ------(cabine en 6)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//éteindre bouton 6C
	//monter
	//controleur.MAJSens();
	//signal de franchissement de palier -------(cabine en 7)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//arrêt au prochain étage
	//signal de franchissement de palier -------(cabine en 8)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	// eteindre bouton 8M

	
	byte[] buff = new byte[16];
	FileInputStream fis2 = new FileInputStream(ficTestCase4);
	while(fis2.read(buff) != -1) {
		String s = new String(buff);
		sb.append(s);
	}	
	
	File fic = new File("TestCase4Correct.txt");
	FileInputStream fis = new FileInputStream(fic);
	BufferedReader b = new BufferedReader(new InputStreamReader(fis));
	while(fis.read(buff) != -1 && b.readLine() != "END") {
		String s = new String(buff);
		sb2.append(s);
	}
	b.close();
	fis2.close();
	fis.close();
	assertEquals(sb2.toString(), sb.toString());
	}
/**
 * Les appels de l'ascenseur qui sont satisfaits avec un changement de sens de la cabine
 */
@Test 
public void testCase5() throws IOException
{
	File ficTestCase5 = new File("TestCase5.txt");
	PrintStream ps = new PrintStream (ficTestCase5);
	System.setOut(ps);
	
	StringBuffer sb = new StringBuffer();
	StringBuffer sb2 = new StringBuffer();

	//Manipulation de la cabine
	//---------------------------------------(cabine en 0)
	controleur.setPosition(0);
	//appel 5C ------------------------------(utilisateur 1)
	controleur.demander(new Demande(5, Sens.INDEFINI));
	//allumer bouton 5C
	//monter
	//controleur.MAJSens();
	//signal de franchissement de palier ----(cabine en 1)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//signal de franchissement de palier ----(cabine en 2)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//signal de franchissement de palier ----(cabine en 3)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//appel 2D ------------------------------(utilisateur 2)
	controleur.demander(new Demande(2, Sens.DESCENTE));
	//allumer bouton 2D
	//monter
	//signal de franchissement de palier -----(cabine en 4)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//arrêt au prochain étage
	//signal de franchissement de palier -----(cabine en 5)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//éteindre bouton 5C
	//descendre
	//controleur.MAJSens();
	//signal de franchissement de palier -----(cabine en 4)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//signal de franchissement de palier -----(cabine en 3)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//arrêt au prochain étage
	//signal de franchissement de palier -----(cabine en 2)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//éteindre bouton 2D
	//appel 0C -------------------------------(utilisateur 2)
	controleur.demander(new Demande(0, Sens.INDEFINI));
	//allumer bouton 0C
	//descendre
	//controleur.MAJSens();
	//signal de franchissement de palier -----(cabine en 1)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//arrêt au prochain étage
	//signal de franchissement de palier ------(cabine en 0)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//éteindre bouton 0C

	
	byte[] buff = new byte[16];
	FileInputStream fis2 = new FileInputStream(ficTestCase5);
	while(fis2.read(buff) != -1) {
		String s = new String(buff);
		sb.append(s);
	}	
	
	File fic = new File("TestCase5Correct.txt");
	FileInputStream fis = new FileInputStream(fic);
	BufferedReader b = new BufferedReader(new InputStreamReader(fis));
	while(fis.read(buff) != -1 && b.readLine() != "END") {
		String s = new String(buff);
		sb2.append(s);
	}
	b.close();
	fis2.close();
	fis.close();
	assertEquals(sb2.toString(), sb.toString());
	}
/**
 * Les appels de l'ascenseur qui sont satisfaits avec deux changements de sens de la cabine
 */
@Test 
public void testCase6() throws IOException
{
	File ficTestCase6 = new File("TestCase6.txt");
	PrintStream ps = new PrintStream (ficTestCase6);
	System.setOut(ps);
	
	StringBuffer sb = new StringBuffer();
	StringBuffer sb2 = new StringBuffer();

	//Manipulation de la cabine
	
	//---------------------------------------(cabine en 3)
	controleur.setPosition(3);
	//appel 6M ------------------------------(utilisateur 1)
	controleur.demander(new Demande(6, Sens.MONTEE));
	//allumer bouton 6M
	//monter
	//controleur.MAJSens();
	//signal de franchissement de palier -----(cabine en 4)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//signal de franchissement de palier -----(cabine en 5)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//arrêt au prochain étage
	//appel 4M
	controleur.demander(new Demande(4, Sens.MONTEE));
	//allumer bouton 4M -----------------------(utilisateur 2)
	//signal de franchissement ----------------(cabine en 6)
	controleur.signalerChangementDEtage();
	//éteindre bouton 6M
	//appel 2C --------------------------------(utilisateur 1)
	controleur.demander(new Demande(2, Sens.INDEFINI));
	//allumer bouton 2C
	//Premier changement de sens
	//descendre
	//controleur.MAJSens();
	//signal de franchissement de palier -------(cabine en 5)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//signal de franchissement de palier -------(cabine en 4)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//signal de franchissement de palier -------(cabine en 3)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//arrêt au prochain étage
	//signal de franchissement de palier -------(cabine en 2)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//éteindre bouton 2C
	//Deuxième changement de sens
	//monter
	//controleur.MAJSens();
	//signal de franchissement de palier -------(cabine en 3)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//arrêt au prochain étage
	//signal de franchissement de palier --------(cabine en 4)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//éteindre bouton 4M
	//appel 0C ----------------------------------(utilisateur 2)
	controleur.demander(new Demande(0, Sens.INDEFINI));
	//allumer bouton 0C
	//descendre
	//controleur.MAJSens();
	//signal de franchissement de palier --------(cabine en 3)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//signal de franchissement de palier --------(cabine en 2)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//signal de franchissement de palier --------(cabine en 1)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//arrêt au prochain étage
	//signal de franchissement de palier ---------(cabine en 0)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//éteindre bouton 0C */
	
	byte[] buff = new byte[16];
	FileInputStream fis2 = new FileInputStream(ficTestCase6);
	while(fis2.read(buff) != -1) {
		String s = new String(buff);
		sb.append(s);
	}	
	
	File fic = new File("TestCase6Correct.txt");
	FileInputStream fis = new FileInputStream(fic);
	BufferedReader b = new BufferedReader(new InputStreamReader(fis));
	while(fis.read(buff) != -1 && b.readLine() != "END") {
		String s = new String(buff);
		sb2.append(s);
	}
	b.close();
	fis2.close();
	fis.close();
	assertEquals(sb2.toString(), sb.toString());
	}
/**
 * Appel intermédiaire et arrêt intermédiaire
 */
@Test 
public void testCase7() throws IOException
{
	File ficTestCase7 = new File("TestCase7.txt");
	PrintStream ps = new PrintStream (ficTestCase7);
	System.setOut(ps);
	
	StringBuffer sb = new StringBuffer();
	StringBuffer sb2 = new StringBuffer();

	//Manipulation de la cabine
	
	//--------------------------------------(cabine en 6) 
	controleur.setPosition(6);
	//appel 1C -----------------------------(utilisateur 1) 
	controleur.demander(new Demande(1, Sens.INDEFINI));
	//allumer bouton 1C
	//appel 4D -----------------------------(utilisateur 2)
	controleur.demander(new Demande(4, Sens.DESCENTE));
	//allumer bouton 4D
	//descendre
	//controleur.MAJPosition();
	//signal de franchissement de palier ----(cabine en 5) 
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//arrêter prochain étage 
	//signal de franchissement de palier ----(cabine en 4) 
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//éteindre bouton 4D
	//appel 2C ------------------------------(utilisateur 2) 
	controleur.demander(new Demande(2, Sens.INDEFINI));
	//allumer bouton 2C
	//descendre
	//controleur.MAJSens();
	//signal de franchissement de palier -----(cabine en 3) 
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//arrêter prochain étage 
	//signal de franchissement de palier -----(cabine en 2)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//éteindre bouton 2C
	//descendre
	//arrêter prochain étage 
	//signal de franchissement de palier ------(cabine en 1) 
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//éteindre bouton 1C 
	
	byte[] buff = new byte[16];
	FileInputStream fis2 = new FileInputStream(ficTestCase7);
	while(fis2.read(buff) != -1) {
		String s = new String(buff);
		sb.append(s);
	}	
	
	File fic = new File("TestCase7Correct.txt");
	FileInputStream fis = new FileInputStream(fic);
	BufferedReader b = new BufferedReader(new InputStreamReader(fis));
	while(fis.read(buff) != -1 && b.readLine() != "END") {
		String s = new String(buff);
		sb2.append(s);
	}
	b.close();
	fis2.close();
	fis.close();
	assertEquals(sb2.toString(), sb.toString());
	}
/**
 * Deux appels à partir du même palier
 */
@Test 
public void testCase8() throws IOException
{
	File ficTestCase8 = new File("TestCase8.txt");
	PrintStream ps = new PrintStream (ficTestCase8);
	System.setOut(ps);
	
	StringBuffer sb = new StringBuffer();
	StringBuffer sb2 = new StringBuffer();

	//Manipulation de la cabine
	
	//---------------------------------------(cabine en 6) 
	controleur.setPosition(6);
	//appel 4M ------------------------------(utilisateur 1) 
	controleur.demander(new Demande(4, Sens.MONTEE));
	//allumer bouton 4M
	//appel 4D -------------------------------(utilisateur 2)
	controleur.demander(new Demande(4, Sens.DESCENTE));
	//allumer bouton 4D
	//descendre
	//signal de franchissement de palier ------(cabine en 5) 
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//arrêter prochain étage 
	//signal de franchissement de palier ------(cabine en 4) 
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//éteindre bouton 4M
	//éteindre bouton 4D
	//appel 6C --------------------------------(utilisateur 1)
	controleur.demander(new Demande(6, Sens.INDEFINI));
	//allumer bouton 6C 
	//appel 2C --------------------------------(utilisateur 2)
	controleur.demander(new Demande(2, Sens.INDEFINI));
	//allumer bouton 2C
	//descendre */
	//controleur.MAJSens();
	controleur.signalerChangementDEtage();
	
	byte[] buff = new byte[16];
	FileInputStream fis2 = new FileInputStream(ficTestCase8);
	while(fis2.read(buff) != -1) {
		String s = new String(buff);
		sb.append(s);
	}	
	
	File fic = new File("TestCase8Correct.txt");
	FileInputStream fis = new FileInputStream(fic);
	BufferedReader b = new BufferedReader(new InputStreamReader(fis));
	while(fis.read(buff) != -1 && b.readLine() != "END") {
		String s = new String(buff);
		sb2.append(s);
	}
	b.close();
	fis2.close();
	fis.close();
	assertEquals(sb2.toString(), sb.toString());
	}
/**
 * Deux appels pour le même étage
 */
@Test 
public void testCase9() throws IOException
{
	File ficTestCase9 = new File("TestCase9.txt");
	PrintStream ps = new PrintStream (ficTestCase9);
	System.setOut(ps);
	
	StringBuffer sb = new StringBuffer();
	StringBuffer sb2 = new StringBuffer();

	//Manipulation de la cabine
	
	// --------------------------------------(cabine en 0) 
	controleur.setPosition(0);
	//appel 3C ------------------------------(utilisateur 1) 
	controleur.demander(new Demande(3, Sens.INDEFINI));
	//allumer bouton 3C
	//controleur.MAJSens();
	//appel 1M ------------------------------(utilisateur 2)
	controleur.demander(new Demande(1, Sens.MONTEE));
	//allumer bouton 1M
	//monter
	//arrêter prochain étage
	//signal de franchissement de palier -----(cabine en 1) 
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//éteindre bouton 1M
	// l’utilisateur 2 monte également en 3
	//monter
	//signal de franchissement de palier ------(cabine en 2) 
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//arrêter prochain étage
	//signal de franchissement de palier ------(cabine en 3)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//éteindre bouton 3C
	
	byte[] buff = new byte[16];
	FileInputStream fis2 = new FileInputStream(ficTestCase9);
	while(fis2.read(buff) != -1) {
		String s = new String(buff);
		sb.append(s);
	}	
	
	File fic = new File("TestCase9Correct.txt");
	FileInputStream fis = new FileInputStream(fic);
	BufferedReader b = new BufferedReader(new InputStreamReader(fis));
	while(fis.read(buff) != -1 && b.readLine() != "END") {
		String s = new String(buff);
		sb2.append(s);
	}
	b.close();
	fis2.close();
	fis.close();
	assertEquals(sb2.toString(), sb.toString());
	}
/**
 * Un appel pour un étage en cours de service
 */
@Test 
public void testCase10() throws IOException
{
	File ficTestCase10 = new File("TestCase10.txt");
	PrintStream ps = new PrintStream (ficTestCase10);
	System.setOut(ps);
	
	StringBuffer sb = new StringBuffer();
	StringBuffer sb2 = new StringBuffer();

	//Manipulation de la cabine
	
	//------------------------------------(cabine en 0) 
	controleur.setPosition(0);
	//appel 2C ---------------------------(utilisateur 1) 
	controleur.demander(new Demande(2, Sens.INDEFINI));
	//allumer bouton 2C
	//Monter
	//controleur.MAJSens();
	//appel 2M ----------------------------(utilisateur 2)
	controleur.demander(new Demande(2, Sens.MONTEE));
	//allumer bouton 2M
	//monter
	//signal de franchissement de palier ---(cabine en 1) 
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//arrêter prochain étage
	//signal de franchissement de palier ----(cabine en 2) 
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//éteindre bouton 2C
	//éteindre bouton 2M
	
	byte[] buff = new byte[16];
	FileInputStream fis2 = new FileInputStream(ficTestCase10);
	while(fis2.read(buff) != -1) {
		String s = new String(buff);
		sb.append(s);
	}	
	
	File fic = new File("TestCase10Correct.txt");
	FileInputStream fis = new FileInputStream(fic);
	BufferedReader b = new BufferedReader(new InputStreamReader(fis));
	while(fis.read(buff) != -1 && b.readLine() != "END") {
		String s = new String(buff);
		sb2.append(s);
	}
	b.close();
	fis2.close();
	fis.close();
	assertEquals(sb2.toString(), sb.toString());
	}
}
