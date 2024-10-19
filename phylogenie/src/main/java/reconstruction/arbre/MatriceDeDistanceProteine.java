package reconstruction.arbre;

import java.util.ArrayList;
import org.biojava.nbio.core.alignment.template.AlignedSequence;
import org.biojava.nbio.core.alignment.template.Profile;
import org.biojava.nbio.core.sequence.*;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;

public class MatriceDeDistanceProteine extends MatriceDeDistance{

	public MatriceDeDistanceProteine() {
		super();
	}
	
	// Méthode qui remplit le tableau des distances à partir de l'alignement protéique
	public double[][] ajouterDistancesObserveesProteines(Profile<ProteinSequence, AminoAcidCompound> sequencesAlignees) {
		int nombreSequences = sequencesAlignees.getAlignedSequences().size();
		matrice = new double[nombreSequences][nombreSequences];
		System.out.println("Nombre de séquences alignées: " + nombreSequences);
		for (int i = 0; i < nombreSequences; i++) {
			for (int j = i + 1; j < nombreSequences; j++) { //i+2?
				// Vérification des index avant d'accéder aux séquences
				if (i >= 0 && i < nombreSequences && j >= 0 && j < nombreSequences) {
					double distance = calculerDistanceObserveeProteine(sequencesAlignees.getAlignedSequence(i+1), sequencesAlignees.getAlignedSequence(j+1)); // cf getAlignedSequence
						matrice[i][j] = distance;
						matrice[j][i] = distance;
				} else {
					System.out.println("Index i ou j hors limites : i=" + i + ", j=" + j);
				}
			}
		}
		return matrice;	
	}
	
	// Récupère les numéros d'accessions des séquences et les range dans une ArrayList
	public ArrayList<String> ajouterNomsSequences(Profile<ProteinSequence, AminoAcidCompound> sequencesAlignees) {
		nomsSequences = new ArrayList<String>(); 
		for (AlignedSequence<ProteinSequence, AminoAcidCompound> seqAlignee : sequencesAlignees.getAlignedSequences()) {
			 if (seqAlignee.getAccession() != null) {
				 nomsSequences.add(seqAlignee.getAccession().getID());
			 } else {
		            throw new IllegalStateException("Erreur : Séquence sans numéro d'accession.");
		     }
	    }
		return nomsSequences;
	}
	
	// Méthode qui calcule les distances observées entre toutes les paires de séquences de l'alignement
	public double calculerDistanceObserveeProteine(AlignedSequence<ProteinSequence, AminoAcidCompound> sequence1, AlignedSequence<ProteinSequence, AminoAcidCompound> sequence2) {
		double longueurSequencesAlignees = sequence1.getLength(); // Les séquences sont alignées donc elles font toutes la même taille
		double correspondances = 0.0; // nombre de similitudes initialisé à 0	
		for (int i = 0; i < longueurSequencesAlignees; i++) {
			if (sequence1.getCompoundAt(i + 1).equals(sequence2.getCompoundAt(i + 1))) { // position - Biological index (1 to n)
				correspondances = correspondances + 1;
			}
		}
		double similarite = correspondances / longueurSequencesAlignees; // calcul de la proportion de similarité
		double distanceObservee = 1 - similarite; // calcul de la proportion de différences
	    return distanceObservee;
	}
	
	// Méthode qui calcule les distances évolutives selon le modèle -> à voir
}
	