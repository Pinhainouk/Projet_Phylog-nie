package alignement.sequences;
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

public class AlignementMultipleAdn extends AlignementMultiple{
	
	// Méthode pour effectuer un alignement multiple d'ADN à partir d'un fichier fasta
	public static Profile<DNASequence, NucleotideCompound> multipleAlignementAdn(File fichierFasta) throws Exception { 
		Profile<DNASequence, NucleotideCompound> sequencesAlignees = null;
		try {
			List<DNASequence> sequences = new ArrayList<DNASequence>();  
			LinkedHashMap<String, DNASequence> sequencesMap = FastaReaderHelper.readFastaDNASequence(fichierFasta);
			for (DNASequence sequence : sequencesMap.values()) {
				sequences.add(sequence);
			}	
			for (DNASequence sequence : sequences) { // Afficher les séquences
				System.out.println(sequence.getOriginalHeader());
				System.out.println(sequence.getSequenceAsString());
			}
			sequencesAlignees = Alignments.getMultipleSequenceAlignment(sequences); 
			System.out.printf("Clustalw:%n%s%n", sequencesAlignees);  
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("Erreur lors de la lecture du fichier FASTA", e);
		} finally { //toujours appelé même si une exception est levée
			ConcurrencyTools.shutdown(); // s'assure que toutes les ressources sont libérées après que toutes les tâches parallélisées par biojava en arrière plans soient terminées 
		}
		return sequencesAlignees;
	}
	
	// Méthode pour effectuer un alignement multiple d'ADN à partir des numéros d'accession NCBI
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
	        return null; // Retourner null pour indiquer l'échec
	    }
	    Profile<DNASequence, NucleotideCompound> sequencesAlignees = Alignments.getMultipleSequenceAlignment(sequences);  
	    System.out.printf("Clustalw:%n%s%n", sequencesAlignees);  
	    ConcurrencyTools.shutdown(); 
	    return sequencesAlignees;
	   }
	
	// Méthode pour récupérer la séquence d'un ADN dans Genbank par son identifiant
	public static DNASequence getSequenceADNGenbank(String genbankId) throws Exception {
	    // Adapter l'URL pour obtenir le fichier FASTA à partir de GenBank
	    URL genbankFasta = new URL(String.format("https://www.ncbi.nlm.nih.gov/sviewer/viewer.fcgi?id=%s&db=nuccore&report=fasta", genbankId));
	    HttpURLConnection connection = null;
	    DNASequence sequence = null;
	    try {
	    	connection = (HttpURLConnection) genbankFasta.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            
            // Lire et traiter le fichier FASTA
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
        	// Assurez-vous de fermer la connexion
        	if (connection != null) {
                connection.disconnect();
            }
    	}
	}
	
	public static boolean estValideGenbankId(String genbankId) {
	    try {
	    	URL genbankFasta = new URL(String.format("https://www.ncbi.nlm.nih.gov/sviewer/viewer.fcgi?id=%s&db=nuccore&report=fasta", genbankId));
	        HttpURLConnection connection = (HttpURLConnection) genbankFasta.openConnection();
	        connection.setRequestMethod("GET");
	        int responseCode = connection.getResponseCode();
	        
	        // Si le code de réponse est HTTP 200, l'identifiant est valide
	        if (responseCode == HttpURLConnection.HTTP_OK) {
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
	
	public static Profile<DNASequence, NucleotideCompound> effectuerAlignementNumAdn(ArrayList<String> numAccessionsValides) { // redondant
	    Profile<DNASequence, NucleotideCompound> alignement = null;
	    try {
	        alignement = AlignementMultipleAdn.multipleAlignementAdn(numAccessionsValides);
	        if (alignement == null) {
	        	JOptionPane.showMessageDialog(null, "Aucun numéro d'accession valide trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
	    	    System.out.println("Erreur -> " + "Aucun numéro d'accession valide trouvé.");
	        } else {
	        // Si l'alignement est réussi, affichez un message de succès
	        JOptionPane.showMessageDialog(null, "Alignement multiple terminé avec succès.");
	        System.out.println("Alignement multiple terminé. Ouverture de la nouvelle fenêtre...");
	        }
	    } catch (Exception ex) {
	        gestionException(ex, "Erreur -> lors de l'alignement multiple des séquences d'ADN.");
	        ex.printStackTrace(); // Imprimer la trace de la pile pour le débogage
	    }
	    return alignement; // Retourner le résultat d'alignement (ou null en cas d'échec)
	}
		
	// Méthode qui effectue l'alignement multiple des ADN à partir du fichier fasta
	public static Profile<DNASequence, NucleotideCompound> effectuerAlignementFastaAdn(File fichierFasta) { //redondant
		Profile<DNASequence, NucleotideCompound> alignement = null;
			try {
				if (fichierFasta != null) {
			    	alignement = AlignementMultipleAdn.multipleAlignementAdn(fichierFasta);
			    	JOptionPane.showMessageDialog(null, "Alignement multiple terminé avec succès.");
			    	System.out.println("Alignement multiple terminé. Ouverture de la nouvelle fenêtre...");
			    }   
			 } catch (Exception ex) {
				 gestionException(ex, "Erreur -> lors de l'alignement multiple des séquences d'ADN.");
				 	System.out.println(ex);
			 }
			 return alignement;
		}
	}