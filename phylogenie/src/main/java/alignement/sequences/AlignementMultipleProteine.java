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
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound; 
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;
import org.biojava.nbio.core.util.ConcurrencyTools;
import org.biojava.nbio.core.sequence.ProteinSequence;

//Récupérer des séquences de protéines depuis UniProt et effectuer un alignement multiple sur celles-ci. 
//Pas besoin d'un constructeur, car les méthodes statiques peuvent être appelées sans créer une instance de la classe.
public class AlignementMultipleProteine extends AlignementMultiple {

	// Méthode pour effectuer un alignement multiple de protéines à partir d'un fichier fasta
	public static Profile<ProteinSequence, AminoAcidCompound> multipleAlignementProteine(File fichierFasta) throws Exception {
		Profile<ProteinSequence, AminoAcidCompound> sequencesAlignees = null;
		try {
			List<ProteinSequence> sequences = new ArrayList<ProteinSequence>(); 
			LinkedHashMap<String, ProteinSequence> sequencesMap = FastaReaderHelper.readFastaProteinSequence(fichierFasta);
			for (ProteinSequence sequence : sequencesMap.values()) {
				sequences.add(sequence);
			}		
		    for (ProteinSequence sequence : sequences) { // Afficher les séquences
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
	
	// Méthode pour effectuer un alignement multiple de protéines à partir des numéros d'accession Uniprot
	public static Profile<ProteinSequence, AminoAcidCompound> multipleAlignementProteine(ArrayList<String> numerosAccessions) throws Exception {  
		List<ProteinSequence> sequences = new ArrayList<ProteinSequence>();  
		for (String num : numerosAccessions) {  
			try {
				ProteinSequence sequence = getSequenceProteineId(num);
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
		Profile<ProteinSequence, AminoAcidCompound> sequencesAlignees = Alignments.getMultipleSequenceAlignment(sequences);  
		System.out.printf("Clustalw:%n%s%n", sequencesAlignees);  
		ConcurrencyTools.shutdown(); 
		return sequencesAlignees;
	}
	
	// Méthode pour récupérer la séquence dans UniProt d'une protéine par son identifiant
	public static ProteinSequence getSequenceProteineId(String uniprotId) throws Exception {  
		URL uniprotFasta = new URL(String.format("https://rest.uniprot.org/uniprotkb/%s.fasta", uniprotId));  
		HttpURLConnection connection = null;
		ProteinSequence sequence = null;	
		try {	
			connection = (HttpURLConnection) uniprotFasta.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();   
            if (responseCode == HttpURLConnection.HTTP_OK) { // Vérifier le code de réponse HTTP
            	sequence = FastaReaderHelper.readFastaProteinSequence(uniprotFasta.openStream()).get(uniprotId); 
            	if (sequence != null) {
            		System.out.printf("id : %s %s%n%s%n", uniprotId, sequence, sequence.getOriginalHeader());  
            		return sequence;
            	} else {
            		throw new Exception("Séquence non trouvée dans le fichier FASTA pour l'identifiant : " + uniprotId);
            	}
            } else {
            	throw new Exception(String.format("Erreur HTTP %d lors de la récupération de la séquence pour l'identifiant : %s", responseCode, uniprotId));
            }
    	} catch (IOException e) {
        throw new Exception("Erreur lors de la connexion à UniProt pour l'identifiant : " + uniprotId, e);
    	} finally {   
    		if (connection != null) {  //Assurez-vous de fermer la connexion
    			connection.disconnect();
    		}
    	}
	}
	
	// Méthode pour vérifier si un numéro d'accession UniProt est valide
	public static boolean estValideUniprotId(String uniprotId) {
	    try {
	        URL uniprotFasta = new URL(String.format("https://rest.uniprot.org/uniprotkb/%s.fasta", uniprotId));
	        HttpURLConnection connection = (HttpURLConnection) uniprotFasta.openConnection();
	        connection.setRequestMethod("GET");
	        int responseCode = connection.getResponseCode();               
	        if (responseCode == HttpURLConnection.HTTP_OK) {  // Si le code de réponse est HTTP 200, l'identifiant est valide
	            return true;
	        } else {
	            System.err.printf("Erreur HTTP %d -> L'identifiant UniProt : %s n'est pas valide.%n", responseCode, uniprotId);
	            return false;
	        }
	    } catch (IOException e) {
	        System.err.println("Erreur lors de la vérification de l'identifiant UniProt : " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public static Profile<ProteinSequence, AminoAcidCompound> effectuerAlignementNumProteine(ArrayList<String> numAccessionsValides) {
	    Profile<ProteinSequence, AminoAcidCompound> alignement = null;
	    try {
	        alignement = AlignementMultipleProteine.multipleAlignementProteine(numAccessionsValides); 
	        if (alignement == null) {
	        	JOptionPane.showMessageDialog(null, "Aucun numéro d'accession valide trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
	    	    System.out.println("Erreur -> " + "Aucun numéro d'accession valide trouvé.");
	        } else {
	        	JOptionPane.showMessageDialog(null, "Alignement multiple terminé avec succès."); // Si l'alignement est réussi, affichez un message de succès
	        	System.out.println("Alignement multiple terminé. Ouverture de la nouvelle fenêtre...");
	        }
	    }catch (Exception ex) {
	        	gestionException(ex, "Erreur -> lors de l'alignement multiple des séquences protéiques.");
	        	ex.printStackTrace(); // Imprimer la trace de la pile pour le débogage
	        }
	        return alignement; // Retourner le résultat d'alignement (ou null en cas d'échec)
	}
		
	// Méthode qui effectue l'alignement multiple des protéines à partir du fichier fasta
	public static Profile<ProteinSequence, AminoAcidCompound> effectuerAlignementFastaProteine(File fichierFasta) {
		Profile<ProteinSequence, AminoAcidCompound> alignement = null;
		    try {
		    	if (fichierFasta != null) {
		    		alignement = AlignementMultipleProteine.multipleAlignementProteine(fichierFasta);
		    		JOptionPane.showMessageDialog(null, "Alignement multiple terminé avec succès.");
		    	    System.out.println("Alignement multiple terminé. Ouverture de la nouvelle fenêtre..."); 
		    	}
		    } catch (Exception ex) {
		    	gestionException(ex, "Erreur -> lors de l'alignement multiple des séquences protéiques.");
		    	System.out.println(ex);
		    }
		    return alignement;
	}	
}
