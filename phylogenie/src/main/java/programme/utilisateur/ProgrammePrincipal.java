package programme.utilisateur;
import java.io.File;
import java.util.Scanner;

import alignement.sequences.AlignementMultipleAdn;
import alignement.sequences.AlignementMultipleProteine;

public class ProgrammePrincipal {

	public static void main(String[] args) {
		InterfaceUtilisateur.creerFenetrePrincipale("Phylogénie", "Logiciel de reconstruction d'arbres phylogénétiques");
		//String[] numerosAccessionsProteines = new String[] {"A0A3G2Y4Z7", "A0A2S1J4M6", "A0A6M4F9Y7", "A0A0U3VAT9", "A0A2R2XFF2", "Q6Y8R6", "Q9WLD7", "Q76638", "A0A159W4K4", "Q9Q072"}; //OK
		//String[] numerosAccessionsADN = new String[] {"MH705157.1", "MH078546.1", "U52953.1", "KU168271.1", "AJ006022.1", "L20571.1", "AF082339.1","L07625.1", "KU892415.1", "X52154.1"}; //A transformer en ArrayList?
		try {  
			//AlignementMultipleProteine.multipleAlignementProteine(numerosAccessionsProteines);  //OK
			//AlignementMultipleAdn.multipleAlignementADN(numerosAccessionsADN); //Problème de mémoire
			//File fichierAdnEnv = new File ("/home/elodie/Documents/Projet_phylogenie/Sequences/sequences_adn_env_entieres.fasta"); //n'aligne que 9 séquences et c'est long
			//AlignementMultipleAdn.multipleAlignementADN(fichierAdnEnv); //n'aligne que 9 séquences et c'est long
			//File fichierEnv = new File ("/home/elodie/Documents/Projet_phylogenie/Sequences/env_entieres.fasta"); //OK
			//AlignementMultipleProteine.multipleAlignementProteine(fichierEnv); //OK
			//File fichierGp41 = new File("/home/elodie/Documents/Projet_phylogenie/Sequences/gp41.fasta"); //OK
			//AlignementMultipleProteine.multipleAlignementProteine(fichierGp41); //OK
			//File fichierGp120 = new File("/home/elodie/Documents/Projet_phylogenie/Sequences/gp120.fasta"); //OK
			//AlignementMultipleProteine.multipleAlignementProteine(fichierGp120); //OK
			//File fichierTat = new File("/home/elodie/Documents/Projet_phylogenie/Sequences/tat.fasta"); //OK
			//AlignementMultipleProteine.multipleAlignementProteine(fichierTat); //OK
			//File fichierEnv1_100 = new File("/home/elodie/Documents/Projet_phylogenie/Sequences/sequences_env_1-100.fasta"); //OK
			//AlignementMultipleProteine.multipleAlignementProteine(fichierEnv1_100); //OK
			//File fichierEnv101_200 = new File("/home/elodie/Documents/Projet_phylogenie/Sequences/sequences_env_101-200.fasta"); //OK
			//AlignementMultipleProteine.multipleAlignementProteine(fichierEnv101_200); //OK
			//File fichierEnv201_300 = new File("/home/elodie/Documents/Projet_phylogenie/Sequences/sequences_env_201-300.fasta"); //OK
			//AlignementMultipleProteine.multipleAlignementProteine(fichierEnv201_300); //OK
			//File fichierEnv301_400 = new File("/home/elodie/Documents/Projet_phylogenie/Sequences/sequences_env_301-400.fasta"); //OK
			//AlignementMultipleProteine.multipleAlignementProteine(fichierEnv301_400); //OK
			//File fichierEnvAdn300_600 = new File("/home/elodie/Documents/Projet_phylogenie/Sequences/sequences_env_300-600.fasta"); //OK
			//AlignementMultipleAdn.multipleAlignementADN(fichierEnvAdn300_600); //OK
			//File fichierEnvAdn600_900 = new File("/home/elodie/Documents/Projet_phylogenie/Sequences/sequences_env_600-900.fasta"); //OK
			//AlignementMultipleAdn.multipleAlignementADN(fichierEnvAdn600_900); //OK
		} catch (Exception e){  
			e.printStackTrace();  
		} 
	}
}
