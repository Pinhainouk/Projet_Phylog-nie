package reconstruction.arbre;

import java.util.ArrayList;

import org.biojava.nbio.core.alignment.template.AlignedSequence;
import org.biojava.nbio.core.alignment.template.Profile;
import org.biojava.nbio.core.sequence.*;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;

public class MatriceDeDistanceAdn extends MatriceDeDistance{
	
	public MatriceDeDistanceAdn() {
		super();
	}
	
	// Méthode qui remplit le tableau des distances observées à partir de l'alignement ADN
	public double[][] ajouterDistancesObserveesAdn(Profile<DNASequence, NucleotideCompound> sequencesAlignees) {
		int nombreSequences = sequencesAlignees.getAlignedSequences().size();
		matrice = new double[nombreSequences][nombreSequences];
		System.out.println("Nombre de séquences alignées: " + nombreSequences);
		for (int i = 0; i < nombreSequences; i++) {
			for (int j = i + 1; j < nombreSequences; j++) {				
				if (i >= 0 && i < nombreSequences && j >= 0 && j < nombreSequences) { // Vérification des index avant d'accéder aux séquences
					double distance = calculerDistanceObserveeAdn(sequencesAlignees.getAlignedSequence(i+1), sequencesAlignees.getAlignedSequence(j+1));
					matrice[i][j] = distance;
					matrice[j][i] = distance;
				} else {
					System.out.println("Index i ou j hors limites : i=" + i + ", j=" + j);
				}
			}
		}
		return matrice;
	}
	
	// Méthode qui calcule la distances observée par paire de séquences
	public static double calculerDistanceObserveeAdn(AlignedSequence<DNASequence, NucleotideCompound> sequence1, AlignedSequence<DNASequence, NucleotideCompound> sequence2) {
		double longueurSequencesAlignees = sequence1.getLength(); 
		double correspondances = 0.0; 
		for (int i = 0; i < longueurSequencesAlignees; i++) {
			if (sequence1.getCompoundAt(i + 1).equals(sequence2.getCompoundAt(i + 1))) { // position - Biological index (1 to n) : getCompoundAt commence à 1
				correspondances = correspondances + 1;
			}
		}
			double similarite = correspondances / longueurSequencesAlignees; 
			double distanceObservee = 1 - similarite; 			    
			return distanceObservee;
		}
	
	// Méthode qui remplit le tableau des distances évolutives selon Kimura (K2P) à partir de l'alignement ADN
	public double[][] ajouterDistancesEvolutivesAdn(Profile<DNASequence, NucleotideCompound> sequencesAlignees) {
		int nombreSequences = sequencesAlignees.getAlignedSequences().size();
		matrice = new double[nombreSequences][nombreSequences];
		System.out.println("Nombre de séquences alignées: " + nombreSequences);
		for (int i = 0; i < nombreSequences; i++) {
			for (int j = i + 1; j < nombreSequences; j++) {				
				if (i >= 0 && i < nombreSequences && j >= 0 && j < nombreSequences) { // Vérification des index avant d'accéder aux séquences
					double distance = calculerDistanceKimura(sequencesAlignees.getAlignedSequence(i+1), sequencesAlignees.getAlignedSequence(j+1));
					matrice[i][j] = distance;
					matrice[j][i] = distance;
				} else {
					System.out.println("Index i ou j hors limites : i=" + i + ", j=" + j);
				}
			}
		}
		return matrice;
	}
		
	// Méthode qui calcule la distance de Kimura par paire de séquences
	public static double calculerDistanceKimura(AlignedSequence<DNASequence, NucleotideCompound> sequence1, AlignedSequence<DNASequence, NucleotideCompound> sequence2) {
		double P = calculerProportionTransitionsP(sequence1, sequence2);
		double Q = calculerProportionTransversionsQ(sequence1,sequence2);
		// Vérifie que les valeurs de P et Q sont dans un domaine valide
        if (P < 0 || P > 1 || Q < 0 || Q > 1) {
            throw new IllegalArgumentException("Les valeurs des proportions P et Q doivent être entre 0 et 1.");
        }
        double calcul1 = 1 - 2 * P - Q;
        double calcul2 = 1 - 2 * Q;
        // Vérifier que les calculs ne sont pas négatifs ou nuls avant d'appliquer le logarithme népérien
        if (calcul1 <= 0 || calcul2 <= 0) {	
        	throw new ArithmeticException("Les calculs à l'intérieur du logarithme doivent être positifs.");
	    }
	    double lnCalcul1 = Math.log(calcul1);
	    double lnCalcul2 = Math.log(calcul2);        
	    double distanceKimura = -0.5 * lnCalcul1 + 0.25 * lnCalcul2;
	    return distanceKimura;
	} 
		
	// Méthode qui calcule la proportion de transitions
	public static double calculerProportionTransitionsP(AlignedSequence<DNASequence, NucleotideCompound> sequence1, AlignedSequence<DNASequence, NucleotideCompound> sequence2) {	
		double longueurSequencesAlignees = sequence1.getLength();
		double nbTransitions = 0.0;
		double nbComparaisons = 0.0;
		for (int i = 0; i < longueurSequencesAlignees; i++) {
			if (sequence1.getCompoundAt(i+1).getBase().equals("-") || sequence2.getCompoundAt(i+1).getBase().equals("-")) {
            continue; // Ignore les positions avec des gaps
        }   
        char base1 = sequence1.getCompoundAt(i+1).getBase().charAt(0);
        char base2 = sequence2.getCompoundAt(i+1).getBase().charAt(0);
        if (estTransition(base1, base2)) {
            nbTransitions++;
        }
        nbComparaisons++;
    } 
    if (nbComparaisons == 0) {
        return 0; // Eviter division par zéro
    }
    double P = nbTransitions / nbComparaisons;
	return P;
	}
			
	// Méthode qui calcule la proportion de transversions
	public static double calculerProportionTransversionsQ(AlignedSequence<DNASequence, NucleotideCompound> sequence1, AlignedSequence<DNASequence, NucleotideCompound> sequence2) {
	    double longueurSequencesAlignees = sequence1.getLength();
	    double nbTransversions = 0.0;
	    double nbComparaisons = 0.0;
	   
	    for (int i = 0; i < longueurSequencesAlignees; i++) {
	        if (sequence1.getCompoundAt(i+1).getBase().equals("-") || sequence2.getCompoundAt(i+1).getBase().equals("-")) {
	            continue; // Ignore les positions avec des gaps : à voir si je dois les compter il s'agit de distance !
	        }
	    char base1 = sequence1.getCompoundAt(i+1).getBase().charAt(0);
	    char base2 = sequence2.getCompoundAt(i+1).getBase().charAt(0);
	    if (estTransversion(base1, base2)) {
	            nbTransversions++;
	    }
	    nbComparaisons++;
	}    
	if (nbComparaisons == 0) {
	    return 0; // Eviter division par zéro
	}
	double Q = nbTransversions / nbComparaisons;
	return Q;
	}
	
	// Méthode pour vérifier si une paire de bases constitue une transition
	public static boolean estTransition(char a, char b) {
		return (a == 'A' && b == 'G') || (a == 'G' && b == 'A') ||
		       (a == 'C' && b == 'T') || (a == 'T' && b == 'C');
	}

	// Méthode pour vérifier si une paire de bases constitue une transversion
	public static boolean estTransversion(char a, char b) {
		return (a == 'A' && b == 'C') || (a == 'A' && b == 'T') ||
		       (a == 'G' && b == 'C') || (a == 'G' && b == 'T') ||
		       (a == 'C' && b == 'A') || (a == 'C' && b == 'G') ||
		       (a == 'T' && b == 'A') || (a == 'T' && b == 'G');
	}
		
	// Récupère les numéros d'accessions des séquences et les range dans une ArrayList
	public ArrayList<String> ajouterNomsSequences(Profile<DNASequence, NucleotideCompound> sequencesAlignees) {
		nomsSequences = new ArrayList<String>(); 
		for (AlignedSequence<DNASequence, NucleotideCompound> seqAlignee : sequencesAlignees.getAlignedSequences()) {
		 	nomsSequences.add(seqAlignee.getAccession().getID());
		}
		return nomsSequences;
	}	
}