package reconstruction.arbre;

import java.util.ArrayList;

import org.biojava.nbio.core.alignment.template.AlignedSequence;
import org.biojava.nbio.core.alignment.template.Profile;
import org.biojava.nbio.core.sequence.*;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;

public class MatriceDeDistanceAdn extends MatriceDeDistance{
	
	public MatriceDeDistanceAdn(double[][] distances, ArrayList<String> noms) {
		super(distances, noms);
	}
	
	// Méthode qui crée une matrice de distance pour des séquences ADN
	public double[][] MatriceDeDistanceObserveeADN(Profile<DNASequence, NucleotideCompound> sequencesAlignees) {
		int nombreSequences = sequencesAlignees.getAlignedSequences().size();
		matrice = new double[nombreSequences][nombreSequences];
		for (int i = 0; i < nombreSequences; i++) {
			for (int j = i + 1; j < nombreSequences; j++) {
				double distance = calculerDistanceObserveeADN(sequencesAlignees.getAlignedSequence(i), sequencesAlignees.getAlignedSequence(j));
				matrice[i][j] = distance;
				matrice[j][i] = distance;
			}
		}
		return matrice;
	}
	
	// Méthode qui calcule les distances observées entre toutes les paires de séquences de l'alignement
	public static double calculerDistanceObserveeADN(AlignedSequence<DNASequence, NucleotideCompound> sequence1, AlignedSequence<DNASequence, NucleotideCompound> sequence2) {
		double longueurSequencesAlignees = sequence1.getLength(); 
		double correspondances = 0.0; 
		double similarite = correspondances / longueurSequencesAlignees; 
		double distanceObservee = 1 - similarite; 
		for (int i = 0; i < longueurSequencesAlignees; i++) {
			if (sequence1.getCompoundAt(i + 1).equals(sequence2.getCompoundAt(i + 1))) { //    position - Biological index (1 to n)
				correspondances = correspondances + 1;
			}
		}
	    return distanceObservee;
	}
	
	// Méthode qui calcule les distances évolutives selon le modèle -> à voir
}
