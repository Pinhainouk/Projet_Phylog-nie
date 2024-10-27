package alignement;
import java.net.HttpURLConnection;
import java.net.URL; 
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.JOptionPane;

import java.io.File;
import java.io.IOException;
import org.biojava.nbio.alignment.*; 
import org.biojava.nbio.core.alignment.template.Profile;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound; 
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;
import org.biojava.nbio.core.util.ConcurrencyTools;
import org.biojava.nbio.core.sequence.*;

/**
 * La classe fille AlignementMultipleAdn hérite de la classe AlignementMultiple et fournit des méthodes pour :
 * effectuer des alignements multiples de séquences ADN en utilisant des fichiers FASTA ou des numéros d'accession GenBank
 */
public class AlignementMultipleAdn extends AlignementMultiple{
	
	/**
	 * Effectue un alignement multiple de séquences ADN à partir d'un fichier FASTA.
	 *
	 * @param fichierFasta Le fichier FASTA contenant les séquences ADN à aligner.
	 * @return Un profil d'alignement des séquences ADN, ou null en cas d'erreur.
	 * @throws Exception Si une erreur survient lors de la lecture du fichier FASTA ou de l'alignement.
	 */
	public static Profile<DNASequence, NucleotideCompound> multipleAlignementAdn(File fichierFasta) throws Exception { 
		Profile<DNASequence, NucleotideCompound> sequencesAlignees = null;
		try {
			List<DNASequence> sequences = new ArrayList<DNASequence>();  
			LinkedHashMap<String, DNASequence> sequencesMap = FastaReaderHelper.readFastaDNASequence(fichierFasta);
			for (DNASequence sequence : sequencesMap.values()) {
				sequences.add(sequence);
			}	
			for (DNASequence sequence : sequences) { // Affiche les séquences
				System.out.println(sequence.getOriginalHeader());
				System.out.println(sequence.getSequenceAsString());
			}
			sequencesAlignees = Alignments.getMultipleSequenceAlignment(sequences); 
			System.out.printf("Clustalw:%n%s%n", sequencesAlignees);  
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("Erreur lors de la lecture du fichier FASTA", e);
		} finally { // S'exécute même si une exception est levée
			ConcurrencyTools.shutdown(); // Assure que les ressources sont libérées après l'exécution
		}
		return sequencesAlignees;
	}
	
	/**
	 * Effectue un alignement multiple de séquences ADN à partir de numéros d'accession NCBI.
	 *
	 * @param numerosAccessions Une liste de numéros d'accession NCBI.
	 * @return Un profil d'alignement des séquences ADN, ou null si aucune séquence valide n'est trouvée.
	 * @throws Exception Si une erreur survient lors de la récupération des séquences ou de l'alignement.
	 */
	public static Profile<DNASequence, NucleotideCompound> multipleAlignementAdn(ArrayList<String> numerosAccessions) throws Exception {  
		List<DNASequence> sequences = new ArrayList<DNASequence>();  
	    for (String num : numerosAccessions) {  
	    	try {
	    		DNASequence sequence = getSequenceADNGenbank(num);
	            if (sequence != null) { 
	            	sequences.add(sequence);
	            } else {
	            	System.err.printf("Erreur -> Séquence non trouvée pour l'identifiant : %s%n", num);
	            }	     
	    	} catch (Exception e) {
				 System.err.println("Erreur lors de la récupération de la séquence pour l'identifiant : " + num);
	             System.err.println("Détails : " + e.getMessage());
			}
	    }
	    if (sequences.isEmpty()) {
	    	System.err.println("Aucune séquence valide trouvée pour les identifiants fournis.");
	        return null; // Retourne null pour indiquer l'échec
	    }
	    Profile<DNASequence, NucleotideCompound> sequencesAlignees = Alignments.getMultipleSequenceAlignment(sequences);  
	    System.out.printf("Clustalw:%n%s%n", sequencesAlignees);  
	    ConcurrencyTools.shutdown(); 
	    return sequencesAlignees;
	   }
	
	/**
	 * Récupère la séquence ADN d'un fichier GenBank par son identifiant.
	 *
	 * @param genbankId L'identifiant GenBank de la séquence.
	 * @return La séquence ADN correspondante, ou null si elle n'est pas trouvée.
	 * @throws Exception Si une erreur survient lors de la récupération de la séquence.
	 */
	public static DNASequence getSequenceADNGenbank(String genbankId) throws Exception {
	    URL genbankFasta = new URL(String.format("https://www.ncbi.nlm.nih.gov/sviewer/viewer.fcgi?id=%s&db=nuccore&report=fasta", genbankId));
	    HttpURLConnection connection = null;
	    DNASequence sequence = null;
	    try {
	    	connection = (HttpURLConnection) genbankFasta.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { 
            	sequence = FastaReaderHelper.readFastaDNASequence(genbankFasta.openStream()).values().iterator().next(); 
            	if (sequence != null) {
            		System.out.printf("id : %s %s%n%s%n", genbankId, sequence, sequence.getOriginalHeader());
            		return sequence;
            	} else {
            		throw new Exception("Séquence non trouvée dans le fichier FASTA pour l'identifiant : " + genbankId);
            	}
            } else {
                throw new Exception(String.format("Erreur HTTP %d lors de la récupération de la séquence pour l'identifiant : %s", responseCode, genbankId));
            }
        } catch (IOException e) {
            throw new IOException("Erreur lors de la connexion à GenBank pour l'identifiant : " + genbankId, e);
        } finally {
        	if (connection != null) {
                connection.disconnect(); // Ferme la connexion
            }
    	}
	}
	
	/**
	 * Vérifie si un identifiant GenBank est valide.
	 *
	 * @param genbankId L'identifiant GenBank à vérifier.
	 * @return true si l'identifiant est valide, false sinon.
	 */
	public static boolean estValideGenbankId(String genbankId) {
	    try {
	    	URL genbankFasta = new URL(String.format("https://www.ncbi.nlm.nih.gov/sviewer/viewer.fcgi?id=%s&db=nuccore&report=fasta", genbankId));
	        HttpURLConnection connection = (HttpURLConnection) genbankFasta.openConnection();
	        connection.setRequestMethod("GET");
	        int responseCode = connection.getResponseCode();        
	        if (responseCode == HttpURLConnection.HTTP_OK) { // Si le code de réponse est HTTP 200, l'identifiant est valide
	            return true;
	        } else {
	            System.err.printf("Erreur HTTP %d -> L'identifiant Genbank : %s n'est pas valide.%n", responseCode, genbankId);
	            return false;
	        }
	    } catch (IOException e) {
	        System.err.println("Erreur lors de la vérification de l'identifiant Genbank : " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}
	
	/**
	 * Effectue l'alignement multiple des séquences ADN à partir d'une liste de numéros d'accession GenBank enveloppé par une interface utilisateur conviviale.
	 *
	 * @param numAccessionsValides Une liste de numéros d'accession valides.
	 * @return Le profil d'alignement des séquences ADN, ou null en cas d'échec.
	 */
	public static Profile<DNASequence, NucleotideCompound> effectuerAlignementNumAdn(ArrayList<String> numAccessionsValides) {
	    Profile<DNASequence, NucleotideCompound> alignement = null;
	    try {
	        alignement = AlignementMultipleAdn.multipleAlignementAdn(numAccessionsValides);
	        if (alignement == null) {
	        	JOptionPane.showMessageDialog(null, "Aucun numéro d'accession valide trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
	    	    System.out.println("Erreur -> " + "Aucun numéro d'accession valide trouvé.");
	        } else {
	        
	        JOptionPane.showMessageDialog(null, "Alignement multiple terminé avec succès."); 
	        System.out.println("Alignement multiple terminé.");
	        }
	    } catch (Exception ex) {
	        gestionException(ex, "Erreur -> lors de l'alignement multiple des séquences d'ADN.");
	        ex.printStackTrace();
	    }
	    return alignement; 
	}
		
	/**
	 * Effectue l'alignement multiple des séquences ADN à partir d'un fichier FASTA enveloppé par une interface utilisateur conviviale.
	 *
	 * @param fichierFasta Le fichier FASTA contenant les séquences ADN.
	 * @return Le profil d'alignement des séquences ADN, ou null en cas d'échec.
	 */
	public static Profile<DNASequence, NucleotideCompound> effectuerAlignementFastaAdn(File fichierFasta) {
		Profile<DNASequence, NucleotideCompound> alignement = null;
			try {
				if (fichierFasta != null) {
			    	alignement = AlignementMultipleAdn.multipleAlignementAdn(fichierFasta);
			    	JOptionPane.showMessageDialog(null, "Alignement multiple terminé avec succès.");
			    	System.out.println("Alignement multiple terminé.");
			    }   
			 } catch (Exception ex) {
				 gestionException(ex, "Erreur -> lors de l'alignement multiple des séquences d'ADN.");
				 	System.out.println(ex);
			 }
			 return alignement;
		}
	}