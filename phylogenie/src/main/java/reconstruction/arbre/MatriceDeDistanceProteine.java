package reconstruction.arbre;

import java.util.ArrayList;
import org.biojava.nbio.core.alignment.template.AlignedSequence;
import org.biojava.nbio.core.alignment.template.Profile;
import org.biojava.nbio.core.sequence.*;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;

public class MatriceDeDistanceProteine extends MatriceDeDistance{

	public MatriceDeDistanceProteine(double[][] distances, ArrayList<String> noms) {
		super(distances, noms);
	}
	
	// Méthode qui crée une matrice de distance pour des séquences protéiques
	public double[][] matriceDeDistanceObserveeProteine(Profile<ProteinSequence, AminoAcidCompound> sequencesAlignees) {
		int nombreSequences = sequencesAlignees.getAlignedSequences().size();
		matrice = new double[nombreSequences][nombreSequences];
		nomsSequences.clear();
		for (AlignedSequence<ProteinSequence, AminoAcidCompound> seqAlignee : sequencesAlignees.getAlignedSequences()) {
           nomsSequences.add(seqAlignee.getAccession().getID());
        }
		for (int i = 0; i < nombreSequences; i++) {
			for (int j = i + 1; j < nombreSequences; j++) {
				double distance = calculerDistanceObserveeProteine(
						sequencesAlignees.getAlignedSequence(i), 
						sequencesAlignees.getAlignedSequence(j));
				matrice[i][j] = distance;
				matrice[j][i] = distance;
			}
		}
	    return matrice;
	}
	
	// Méthode qui calcule les distances observées entre toutes les paires de séquences de l'alignement
	public double calculerDistanceObserveeProteine(AlignedSequence<ProteinSequence, AminoAcidCompound> sequence1, AlignedSequence<ProteinSequence, AminoAcidCompound> sequence2) {
		double longueurSequencesAlignees = sequence1.getLength(); // Les séquences sont alignées donc elles font toutes la même taille
		double correspondances = 0.0; // nombre de similitudes initialisé à 0
		double similarite = correspondances / longueurSequencesAlignees; // calcul de la proportion de similarité
		double distanceObservee = 1 - similarite; // calcul de la proportion de différences
		for (int i = 0; i < longueurSequencesAlignees; i++) {
			if (sequence1.getCompoundAt(i + 1).equals(sequence2.getCompoundAt(i + 1))) { //    position - Biological index (1 to n)
				correspondances = correspondances + 1;
			}
		}
	    return distanceObservee;
	}
	
	// Méthode qui calcule les distances évolutives selon le modèle -> à voir
}
	

	