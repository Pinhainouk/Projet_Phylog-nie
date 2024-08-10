package alignement.sequences;
import java.net.URL; 
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.io.File;
import java.io.IOException;
import org.biojava.nbio.alignment.*; 
import org.biojava.nbio.core.alignment.template.Profile;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound; 
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;
import org.biojava.nbio.core.util.ConcurrencyTools;
import org.biojava.nbio.core.sequence.*;

public class AlignementMultipleAdn extends AlignementMultiple{
	
	// Méthode pour effectuer un alignement multiple d'ADN à partir d'un fichier fasta
	public static void multipleAlignementAdn(File fichierFasta) throws Exception {  
		try {
			List<DNASequence> sequences = new ArrayList<DNASequence>();  
			LinkedHashMap<String, DNASequence> sequencesMap = FastaReaderHelper.readFastaDNASequence(fichierFasta);
			for (DNASequence sequence : sequencesMap.values()) {
				sequences.add(sequence);
			}
			// Afficher les séquences
			for (DNASequence sequence : sequences) {
	        System.out.println(sequence.getOriginalHeader());
	        System.out.println(sequence.getSequenceAsString());
			}
			Profile<DNASequence, NucleotideCompound> sequencesAlignees = Alignments.getMultipleSequenceAlignment(sequences); 
			System.out.printf("Clustalw:%n%s%n", sequencesAlignees);  
			ConcurrencyTools.shutdown(); 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// Méthode pour effectuer un alignement multiple d'ADN à partir des numéros d'accession NCBI
	public static void multipleAlignementAdn(ArrayList<String> numerosAccessions) throws Exception {  
		List<DNASequence> sequences = new ArrayList<DNASequence>();  
	    for (String num : numerosAccessions) {  
	        DNASequence sequence = getSequenceADNGenbank(num);
	            if (sequence != null) { 
	            	sequences.add(sequence);
	            } else {
	                System.err.printf("Erreur -> Séquence non trouvée pour l'identifiant : %s%n", num);
	            }     
	    }  
	    if (sequences.isEmpty()) {
	    	System.err.println("Erreur -> Aucune séquence valide trouvée.");
	        	return;
	    }
	    Profile<DNASequence, NucleotideCompound> sequencesAlignees = Alignments.getMultipleSequenceAlignment(sequences);  
	    System.out.printf("Clustalw:%n%s%n", sequencesAlignees);  
	    ConcurrencyTools.shutdown();  
	}
	// Méthode pour récupérer la séquence d'un ADN dans Genbank par son identifiant
	public static DNASequence getSequenceADNGenbank(String genbankId) throws IOException, CompoundNotFoundException {
	    // Adapter l'URL pour obtenir le fichier FASTA à partir de GenBank
	    URL genbankFasta = new URL(String.format("https://www.ncbi.nlm.nih.gov/sviewer/viewer.fcgi?id=%s&db=nuccore&report=fasta", genbankId));
	    // Lire et traiter le fichier FASTA
	    DNASequence sequence = FastaReaderHelper.readFastaDNASequence(genbankFasta.openStream()).values().iterator().next(); 
	    if (sequence != null) {
	        System.out.printf("id : %s %s%n%s%n", genbankId, sequence, sequence.getOriginalHeader());
	    } else {
	        System.err.printf("Erreur -> Aucune séquence trouvée pour l'identifiant : %s%n", genbankId);
	    }
	    return sequence;
	}
}