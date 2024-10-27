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
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound; 
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;
import org.biojava.nbio.core.util.ConcurrencyTools;
import org.biojava.nbio.core.sequence.ProteinSequence;

/**
 * La classe fille AlignementMultipleProteine hérite de la classe AlignementMultiple et fournit des méthodes pour :
 * effectuer des alignements multiples de séquences protéiques en utilisant des fichiers FASTA ou des numéros d'accession UniProt
 */
public class AlignementMultipleProteine extends AlignementMultiple {

	/**
     * Effectue un alignement multiple de séquences protéiques à partir d'un fichier FASTA.
     *
     * @param fichierFasta Le fichier FASTA contenant les séquences protéiques à aligner.
     * @return Un profil d'alignement des séquences protéiques.
     * @throws Exception Si une erreur survient lors de la lecture du fichier FASTA ou de l'alignement.
     */
	public static Profile<ProteinSequence, AminoAcidCompound> multipleAlignementProteine(File fichierFasta) throws Exception {
		Profile<ProteinSequence, AminoAcidCompound> sequencesAlignees = null;
		try {
			List<ProteinSequence> sequences = new ArrayList<ProteinSequence>(); 
			LinkedHashMap<String, ProteinSequence> sequencesMap = FastaReaderHelper.readFastaProteinSequence(fichierFasta);
			for (ProteinSequence sequence : sequencesMap.values()) {
				sequences.add(sequence);
			}		
		    for (ProteinSequence sequence : sequences) { // Affiche les séquences
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
     * Effectue un alignement multiple de séquences protéiques à partir d'une liste de numéros d'accession UniProt.
     *
     * @param numerosAccessions Liste des numéros d'accession UniProt.
     * @return Un profil d'alignement des séquences protéiques ou null si aucune séquence valide n'est trouvée.
     * @throws Exception Si une erreur survient lors de la récupération ou de l'alignement des séquences.
     */
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
	        return null; // Retourne null pour indiquer l'échec
		}
		Profile<ProteinSequence, AminoAcidCompound> sequencesAlignees = Alignments.getMultipleSequenceAlignment(sequences);  
		System.out.printf("Clustalw:%n%s%n", sequencesAlignees);  
		ConcurrencyTools.shutdown(); 
		return sequencesAlignees;
	}
	
	/**
     * Récupère une séquence protéique à partir de son identifiant UniProt.
     *
     * @param uniprotId L'identifiant UniProt de la protéine.
     * @return La séquence protéique associée à l'identifiant UniProt.
     * @throws Exception Si une erreur survient lors de la récupération de la séquence depuis UniProt.
     */
	public static ProteinSequence getSequenceProteineId(String uniprotId) throws Exception {  
		URL uniprotFasta = new URL(String.format("https://rest.uniprot.org/uniprotkb/%s.fasta", uniprotId));  
		HttpURLConnection connection = null;
		ProteinSequence sequence = null;	
		try {	
			connection = (HttpURLConnection) uniprotFasta.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();   
            if (responseCode == HttpURLConnection.HTTP_OK) { // Si le code de réponse est HTTP 200, l'identifiant est valide
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
    		if (connection != null) {  // Assure de fermer la connexion
    			connection.disconnect();
    		}
    	}
	}
	
	/**
     * Vérifie si un numéro d'accession UniProt est valide en effectuant une requête HTTP vers UniProt.
     *
     * @param uniprotId Le numéro d'accession UniProt à vérifier.
     * @return true si l'identifiant est valide, false sinon.
     */
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
	
	/**
     * Effectue l'alignement multiple de séquences protéiques à partir de numéros d'accession UniProt validés enveloppé par une interface utilisateur conviviale.
     *
     * @param numAccessionsValides Une liste de numéros d'accession UniProt valides.
     * @return Un profil d'alignement des séquences protéiques, ou {@code null} si aucune séquence valide n'est trouvée.
     */
	public static Profile<ProteinSequence, AminoAcidCompound> effectuerAlignementNumProteine(ArrayList<String> numAccessionsValides) {
	    Profile<ProteinSequence, AminoAcidCompound> alignement = null;
	    try {
	        alignement = AlignementMultipleProteine.multipleAlignementProteine(numAccessionsValides); 
	        if (alignement == null) {
	        	JOptionPane.showMessageDialog(null, "Aucun numéro d'accession valide trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
	    	    System.out.println("Erreur -> " + "Aucun numéro d'accession valide trouvé.");
	        } else {
	        	JOptionPane.showMessageDialog(null, "Alignement multiple terminé avec succès."); // Si l'alignement est réussi, affiche un message de succès
	        	System.out.println("Alignement multiple terminé.");
	        }
	    }catch (Exception ex) {
	        	gestionException(ex, "Erreur -> lors de l'alignement multiple des séquences protéiques.");
	        	ex.printStackTrace(); // Imprimer la trace de la pile pour le débogage
	        }
	        return alignement; 
	}
		
	/**
     * Effectue l'alignement multiple de séquences protéiques à partir d'un fichier FASTA enveloppé par une interface utilisateur conviviale.
     *
     * @param fichierFasta Le fichier FASTA contenant les séquences protéiques.
     * @return Un profil d'alignement des séquences protéiques, ou {@code null} en cas d'erreur.
     */
	public static Profile<ProteinSequence, AminoAcidCompound> effectuerAlignementFastaProteine(File fichierFasta) {
		Profile<ProteinSequence, AminoAcidCompound> alignement = null;
		    try {
		    	if (fichierFasta != null) {
		    		alignement = AlignementMultipleProteine.multipleAlignementProteine(fichierFasta);
		    		JOptionPane.showMessageDialog(null, "Alignement multiple terminé avec succès.");
		    	    System.out.println("Alignement multiple terminé."); 
		    	}
		    } catch (Exception ex) {
		    	gestionException(ex, "Erreur -> lors de l'alignement multiple des séquences protéiques.");
		    	System.out.println(ex);
		    }
		    return alignement;
	}	
}
