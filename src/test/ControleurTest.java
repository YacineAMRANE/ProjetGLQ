package test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import outils.Demande;
import outils.Sens;
import cabine.ICabine;
import iug.IIUG;
import cabine.DoublureDeCabine;
import iug.DoublureDeIUG;
import controleur.Controleur;
import commande.ListeTrieeCirculaireDeDemandes;

public class ControleurTest {
	ListeTrieeCirculaireDeDemandes demandes;
	ICabine cabine;
	IIUG iug;
	Controleur controleur;

	@Before
	public void setUp()  {
		this.demandes = new ListeTrieeCirculaireDeDemandes(10);
		this.cabine = new DoublureDeCabine();
		this.iug  = new DoublureDeIUG();
		this.controleur = new Controleur(demandes, 10);
		this.controleur.setCabine(cabine);
		this.controleur.setIUG(iug);
	}

	@After
	public void tearDown() {
		this.demandes = null;
		this.cabine = null;
		this.iug = null;
		this.controleur = null;
		System.out.flush();
	}

	/**
	 * Les appels de l'ascenseur apres un arret prolonge
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
		controleur.setPosition(0);
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
		// arret urgence 
		controleur.arretUrgence();
		// reprise
		// appel 4C
		controleur.demander(new Demande(4, Sens.INDEFINI));
		// cabine en 3
		controleur.signalerChangementDEtage();
		System.out.println("END");
		
		byte[] buff = new byte[255];
		FileInputStream fis2 = new FileInputStream(ficTestCase1);
		BufferedReader b2 = new BufferedReader(new InputStreamReader(fis2));
		while(fis2.read(buff) != -1) {
			String s = new String(buff);
			sb.append(s);
		}	
		
		File fic = new File("src/test/TestCase1Correct.txt");
		FileInputStream fis = new FileInputStream(fic);
		BufferedReader b = new BufferedReader(new InputStreamReader(fis));
		while(fis.read(buff) != -1) {
			String s = new String(buff);
			sb2.append(s);
		}
		b2.close();
		b.close();
		fis2.close();
		fis.close();
		assertEquals(sb2.toString(), sb.toString());
	}

/**
 * Les appels de l'ascenseur dans le meme sens que celui de la cabine en cours de deplacement
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
	// Un utilisateur interne appuie sur le bouton etage 4
	//appel 5M ------------------------------(utilisateur 1)
	controleur.demander(new Demande(7, Sens.MONTEE));
	//allumer bouton 5M
	//monter
	//controleur.MAJSens();
	//signal de franchissement de palier ----(cabine en 3)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//A ce moment le un utilisateur externe se situant au 4eme demande l'ascenseur
	//appel 4M ------------------------------(utilisateur 2)
	//allumer bouton 4M
	controleur.demander(new Demande(5, Sens.MONTEE));
	//L'ascenseur se situe en 4, on eteint le bouton 4M
	//signal franchissement palier -----------(cabine en 4)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//eteindre bouton 4M
	//L'utilisateur 2 monte dans l'ascenseur
	//L'ascenseur continue de monter jusqu'a l'etage 5, demande par l'utilisateur 1
	//monter
	//signal franchissement palier -----------(cabine en 5)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	System.out.println("END");
	
	byte[] buff = new byte[255];
	FileInputStream fis2 = new FileInputStream(ficTestCase2);
	BufferedReader b2 = new BufferedReader(new InputStreamReader(fis2));
	while(fis2.read(buff) != -1) {
		String s = new String(buff);
		sb.append(s);
	}	
	
	File fic = new File("src/test/TestCase2Correct.txt");
	FileInputStream fis = new FileInputStream(fic);
	BufferedReader b = new BufferedReader(new InputStreamReader(fis));
	while(fis.read(buff) != -1) {
		String s = new String(buff);
		sb2.append(s);
	}
	b2.close();
	b.close();
	fis2.close();
	fis.close();
	assertEquals(sb2.toString(), sb.toString());
	}
/**
 * Les appels de l'ascenseur dans le sens inverse a celui de la cabine en cours de deplacement
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
	//arreter prochain etage 
	//descendre
	//signal de franchissement de palier ------(cabine en 1) 
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//eteindre bouton 1M 
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
	//arreter prochain etage 
	//signal de franchissement de palier ------(cabine en 4)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//eteindre bouton 4C
	//descendre
	//controleur.MAJSens();
	//arreter prochain etage 
	//signal de franchissement de palier ------(cabine en 3)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//eteindre bouton 3D
	System.out.println("END");
	
	byte[] buff = new byte[255];
	FileInputStream fis2 = new FileInputStream(ficTestCase3);
	BufferedReader b2 = new BufferedReader(new InputStreamReader(fis2));
	while(fis2.read(buff) != -1) {
		String s = new String(buff);
		sb.append(s);
	}	
	
	File fic = new File("src/test/TestCase3Correct.txt");
	FileInputStream fis = new FileInputStream(fic);
	BufferedReader b = new BufferedReader(new InputStreamReader(fis));
	while(fis.read(buff) != -1) {
		String s = new String(buff);
		sb2.append(s);
	}
	b2.close();
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
	//arret au prochain etage
	//signal de franchissement de palier ------(cabine en 6)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//eteindre bouton 6C
	//monter
	//controleur.MAJSens();
	//signal de franchissement de palier -------(cabine en 7)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//arret au prochain etage
	//signal de franchissement de palier -------(cabine en 8)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	// eteindre bouton 8M
	System.out.println("END");

	
	byte[] buff = new byte[255];
	FileInputStream fis2 = new FileInputStream(ficTestCase4);
	BufferedReader b2 = new BufferedReader(new InputStreamReader(fis2));
	while(fis2.read(buff) != -1) {
		String s = new String(buff);
		sb.append(s);
	}	
	
	File fic = new File("src/test/TestCase4Correct.txt");
	FileInputStream fis = new FileInputStream(fic);
	BufferedReader b = new BufferedReader(new InputStreamReader(fis));
	while(fis.read(buff) != -1) {
		String s = new String(buff);
		sb2.append(s);
	}
	b2.close();
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
	//arret au prochain etage
	//signal de franchissement de palier -----(cabine en 5)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//eteindre bouton 5C
	//descendre
	//controleur.MAJSens();
	//signal de franchissement de palier -----(cabine en 4)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//signal de franchissement de palier -----(cabine en 3)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//arret au prochain etage
	//signal de franchissement de palier -----(cabine en 2)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//eteindre bouton 2D
	//appel 0C -------------------------------(utilisateur 2)
	controleur.demander(new Demande(0, Sens.INDEFINI));
	//allumer bouton 0C
	//descendre
	//controleur.MAJSens();
	//signal de franchissement de palier -----(cabine en 1)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//arret au prochain etage
	//signal de franchissement de palier ------(cabine en 0)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//eteindre bouton 0C
	System.out.println("END");
	
	byte[] buff = new byte[255];
	FileInputStream fis2 = new FileInputStream(ficTestCase5);
	BufferedReader b2 = new BufferedReader(new InputStreamReader(fis2));
	while(fis2.read(buff) != -1) {
		String s = new String(buff);
		sb.append(s);
	}	
	
	File fic = new File("src/test/TestCase5Correct.txt");
	FileInputStream fis = new FileInputStream(fic);
	BufferedReader b = new BufferedReader(new InputStreamReader(fis));
	while(fis.read(buff) != -1) {
		String s = new String(buff);
		sb2.append(s);
	}
	b2.close();
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
	//arret au prochain etage
	//appel 4M
	controleur.demander(new Demande(3, Sens.MONTEE));
	//allumer bouton 3M -----------------------(utilisateur 2)
	//signal de franchissement ----------------(cabine en 6)
	controleur.signalerChangementDEtage();
	//eteindre bouton 6M
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
	//arret au prochain etage
	//signal de franchissement de palier -------(cabine en 2)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//eteindre bouton 2C
	//Deuxieme changement de sens
	//monter
	//controleur.MAJSens();
	//signal de franchissement de palier -------(cabine en 3)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//arret au prochain etage
	//signal de franchissement de palier --------(cabine en 4)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//eteindre bouton 4M
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
	//arret au prochain etage
	//signal de franchissement de palier ---------(cabine en 0)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//eteindre bouton 0C */
	System.out.println("END");
	
	byte[] buff = new byte[255];
	FileInputStream fis2 = new FileInputStream(ficTestCase6);
	BufferedReader b2 = new BufferedReader(new InputStreamReader(fis2));
	while(fis2.read(buff) != -1) {
		String s = new String(buff);
		sb.append(s);
	}	
	
	File fic = new File("src/test/TestCase6Correct.txt");
	FileInputStream fis = new FileInputStream(fic);
	BufferedReader b = new BufferedReader(new InputStreamReader(fis));
	while(fis.read(buff) != -1) {
		String s = new String(buff);
		sb2.append(s);
	}
	b2.close();
	b.close();
	fis2.close();
	fis.close();
	assertEquals(sb2.toString(), sb.toString());
	}
/**
 * Appel intermediaire et arret intermediaire
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
	//arreter prochain etage 
	//signal de franchissement de palier ----(cabine en 4) 
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//eteindre bouton 4D
	//appel 2C ------------------------------(utilisateur 2) 
	controleur.demander(new Demande(2, Sens.INDEFINI));
	//allumer bouton 2C
	//descendre
	//controleur.MAJSens();
	//signal de franchissement de palier -----(cabine en 3) 
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//arreter prochain etage 
	//signal de franchissement de palier -----(cabine en 2)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//eteindre bouton 2C
	//descendre
	//arreter prochain etage 
	//signal de franchissement de palier ------(cabine en 1) 
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//eteindre bouton 1C 
	System.out.println("END");
	
	byte[] buff = new byte[255];
	FileInputStream fis2 = new FileInputStream(ficTestCase7);
	BufferedReader b2 = new BufferedReader(new InputStreamReader(fis2));
	while(fis2.read(buff) != -1) {
		String s = new String(buff);
		sb.append(s);
	}	
	
	File fic = new File("src/test/TestCase7Correct.txt");
	FileInputStream fis = new FileInputStream(fic);
	BufferedReader b = new BufferedReader(new InputStreamReader(fis));
	while(fis.read(buff) != -1) {
		String s = new String(buff);
		sb2.append(s);
	}
	b2.close();
	b.close();
	fis2.close();
	fis.close();
	assertEquals(sb2.toString(), sb.toString());
	}
/**
 * Deux appels a partir du meme palier
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
	//arreter prochain etage 
	//signal de franchissement de palier ------(cabine en 4) 
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//eteindre bouton 4M
	//eteindre bouton 4D
	//appel 6C --------------------------------(utilisateur 1)
	controleur.demander(new Demande(6, Sens.INDEFINI));
	//allumer bouton 6C 
	//appel 2C --------------------------------(utilisateur 2)
	controleur.demander(new Demande(2, Sens.INDEFINI));
	//allumer bouton 2C
	//monter */
	//controleur.MAJSens();
	controleur.signalerChangementDEtage();
	System.out.println("END");
	
	byte[] buff = new byte[255];
	FileInputStream fis2 = new FileInputStream(ficTestCase8);
	BufferedReader b2 = new BufferedReader(new InputStreamReader(fis2));
	while(fis2.read(buff) != -1) {
		String s = new String(buff);
		sb.append(s);
	}	
	
	File fic = new File("src/test/TestCase8Correct.txt");
	FileInputStream fis = new FileInputStream(fic);
	BufferedReader b = new BufferedReader(new InputStreamReader(fis));
	while(fis.read(buff) != -1) {
		String s = new String(buff);
		sb2.append(s);
	}
	b2.close();
	b.close();
	fis2.close();
	fis.close();
	assertEquals(sb2.toString(), sb.toString());
	}
/**
 * Deux appels pour le meme etage
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
	controleur.demander(new Demande(4, Sens.INDEFINI));
	//allumer bouton 3C
	//controleur.MAJSens();
	//appel 1M ------------------------------(utilisateur 2)
	controleur.demander(new Demande(2, Sens.MONTEE));
	//allumer bouton 1M
	//monter
	//arreter prochain etage
	//signal de franchissement de palier -----(cabine en 1) 
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//eteindre bouton 1M
	// leutilisateur 2 monte egalement en 3
	//monter
	//signal de franchissement de palier ------(cabine en 2) 
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//arreter prochain etage
	//signal de franchissement de palier ------(cabine en 3)
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//eteindre bouton 3C
	System.out.println("END");
	
	byte[] buff = new byte[255];
	FileInputStream fis2 = new FileInputStream(ficTestCase9);
	BufferedReader b2 = new BufferedReader(new InputStreamReader(fis2));
	while(fis2.read(buff) != -1) {
		String s = new String(buff);
		sb.append(s);
	}	
	
	File fic = new File("src/test/TestCase9Correct.txt");
	FileInputStream fis = new FileInputStream(fic);
	BufferedReader b = new BufferedReader(new InputStreamReader(fis));
	while(fis.read(buff) != -1) {
		String s = new String(buff);
		sb2.append(s);
	}
	b2.close();
	b.close();
	fis2.close();
	fis.close();
	assertEquals(sb2.toString(), sb.toString());
	}
/**
 * Un appel pour un etage en cours de service
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
	//arreter prochain etage
	//signal de franchissement de palier ----(cabine en 2) 
	controleur.signalerChangementDEtage();
	//controleur.MAJPosition();
	//eteindre bouton 2C
	//eteindre bouton 2M
	System.out.println("END");
	
	byte[] buff = new byte[255];
	FileInputStream fis2 = new FileInputStream(ficTestCase10);
	BufferedReader b2 = new BufferedReader(new InputStreamReader(fis2));
	while(fis2.read(buff) != -1) {
		String s = new String(buff);
		sb.append(s);
	}	
	
	File fic = new File("src/test/TestCase10Correct.txt");
	FileInputStream fis = new FileInputStream(fic);
	BufferedReader b = new BufferedReader(new InputStreamReader(fis));
	while(fis.read(buff) != -1) {
		String s = new String(buff);
		sb2.append(s);
	}
	b2.close();
	b.close();
	fis2.close();
	fis.close();
	assertEquals(sb2.toString(), sb.toString());
	}
}