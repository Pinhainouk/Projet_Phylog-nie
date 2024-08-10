package alignement.sequences;

import java.net.URL; 
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
	public static void multipleAlignementProteine(File fichierFasta) throws Exception {  
		try {
			List<ProteinSequence> sequences = new ArrayList<ProteinSequence>();  
			LinkedHashMap<String, ProteinSequence> sequencesMap = FastaReaderHelper.readFastaProteinSequence(fichierFasta);
			for (ProteinSequence sequence : sequencesMap.values()) {
				sequences.add(sequence);
			}
			// Afficher les séquences
		    for (ProteinSequence sequence : sequences) {
		        System.out.println(sequence.getOriginalHeader());
		        System.out.println(sequence.getSequenceAsString());
		    }
		    Profile<ProteinSequence, AminoAcidCompound> sequencesAlignees = Alignments.getMultipleSequenceAlignment(sequences);
		    System.out.printf("Clustalw:%n%s%n", sequencesAlignees); 
			ConcurrencyTools.shutdown(); 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// Méthode pour effectuer un alignement multiple de protéines à partir des numéros d'accession Uniprot
	public static void multipleAlignementProteine(ArrayList<String> numerosAccessions) throws Exception {  
		List<ProteinSequence> sequences = new ArrayList<ProteinSequence>();  
			for (String num : numerosAccessions) {  
				ProteinSequence sequence = getSequenceProteineId(num);
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
			Profile<ProteinSequence, AminoAcidCompound> sequencesAlignees = Alignments.getMultipleSequenceAlignment(sequences);  
			System.out.printf("Clustalw:%n%s%n", sequencesAlignees);  
			ConcurrencyTools.shutdown();  
		}
	// Méthode pour récupérer la séquence dans UniProt d'une protéine par son identifiant
	public static ProteinSequence getSequenceProteineId(String uniprotId) throws Exception {  
	URL uniprotFasta = new URL(String.format("https://rest.uniprot.org/uniprotkb/%s.fasta", uniprotId));  
		ProteinSequence sequence = FastaReaderHelper.readFastaProteinSequence(uniprotFasta.openStream()).get(uniprotId); 
		if (sequence != null) {
			System.out.printf("id : %s %s%n%s%n", uniprotId, sequence, sequence.getOriginalHeader());  
		} else {
			System.err.printf("Erreur -> Aucune séquence trouvée pour l'identifiant : %s%n", uniprotId);
		}
		return sequence;  
	}
}
