package reconstruction.arbre;
import java.util.ArrayList;

import org.biojava.nbio.core.alignment.template.AlignedSequence;
import org.biojava.nbio.core.alignment.template.Profile;
import org.biojava.nbio.core.sequence.*;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;

/**
 * La classe fille MatriceDeDistancesAdn hérite de la classe MatriceDeDistances et représente une matrice de distances pour les séquences ADN.
 * Elle fournit des méthodes pour calculer les distances observées et évolutives entre des séquences d'ADN alignées.
 */
public class MatriceDeDistancesAdn extends MatriceDeDistances{
	
	/**
     * Constructeur par défaut qui initialise une matrice vide de distances.
     */
	public MatriceDeDistancesAdn() {
		super();
	}
	
	/**
     * Remplit le tableau des distances observées à partir de l'alignement de séquences ADN.
     *
     * @param sequencesAlignees Le profil des séquences alignées contenant les séquences ADN.
     * @return La matrice des distances observées.
     */
	public double[][] ajouterDistancesObserveesAdn(Profile<DNASequence, NucleotideCompound> sequencesAlignees) {
		int nombreSequences = sequencesAlignees.getAlignedSequences().size();
		matrice = new double[nombreSequences][nombreSequences];
		System.out.println("Nombre de séquences alignées: " + nombreSequences);
		for (int i = 0; i < nombreSequences; i++) {
			for (int j = i + 1; j < nombreSequences; j++) {				
				if (i >= 0 && i < nombreSequences && j >= 0 && j < nombreSequences) { // Vérifie les index avant d'accéder aux séquences
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
	
	/**
     * Calcule la distance observée entre deux séquences alignées.
     *
     * @param sequence1 La première séquence alignée.
     * @param sequence2 La deuxième séquence alignée.
     * @return La distance observée entre les deux séquences.
     */
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
	
	/**
     * Remplit le tableau des distances évolutives selon le modèle de Kimura (K2P).
     *
     * @param sequencesAlignees Le profil des séquences alignées contenant les séquences ADN.
     * @return La matrice des distances évolutives.
     */
	public double[][] ajouterDistancesEvolutivesAdn(Profile<DNASequence, NucleotideCompound> sequencesAlignees) {
		int nombreSequences = sequencesAlignees.getAlignedSequences().size();
		matrice = new double[nombreSequences][nombreSequences];
		System.out.println("Nombre de séquences alignées: " + nombreSequences);
		for (int i = 0; i < nombreSequences; i++) {
			for (int j = i + 1; j < nombreSequences; j++) {				
				if (i >= 0 && i < nombreSequences && j >= 0 && j < nombreSequences) { // Vérifie les index avant d'accéder aux séquences
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
		
	/**
     * Calcule la distance évolutive de Kimura entre deux séquences alignées.
     *
     * @param sequence1 La première séquence alignée.
     * @param sequence2 La deuxième séquence alignée.
     * @return La distance évolutive de Kimura entre les deux séquences.
     * @throws IllegalArgumentException si P ou Q est en dehors de l'intervalle [0, 1].
     * @throws ArithmeticException si les calculs pour le logarithme sont négatifs ou nuls.
     */
	public static double calculerDistanceKimura(AlignedSequence<DNASequence, NucleotideCompound> sequence1, AlignedSequence<DNASequence, NucleotideCompound> sequence2) {
		double P = calculerProportionTransitionsP(sequence1, sequence2);
		double Q = calculerProportionTransversionsQ(sequence1,sequence2);
        if (P < 0 || P > 1 || Q < 0 || Q > 1) {
            throw new IllegalArgumentException("Les valeurs des proportions P et Q doivent être entre 0 et 1.");
        }
        double calcul1 = 1 - 2 * P - Q;
        double calcul2 = 1 - 2 * Q;
        if (calcul1 <= 0 || calcul2 <= 0) {	
        	throw new ArithmeticException("Les calculs à l'intérieur du logarithme doivent être positifs.");
	    }
	    double lnCalcul1 = Math.log(calcul1);
	    double lnCalcul2 = Math.log(calcul2);        
	    double distanceKimura = -0.5 * lnCalcul1 + 0.25 * lnCalcul2;
	    return distanceKimura;
	} 
		
	/**
     * Calcule la proportion de transitions entre deux séquences alignées.
     *
     * @param sequence1 La première séquence alignée.
     * @param sequence2 La deuxième séquence alignée.
     * @return La proportion de transitions entre les deux séquences.
     */
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
        return 0; // Evite la division par zéro
    }
    double P = nbTransitions / nbComparaisons;
	return P;
	}
			
	/**
     * Calcule la proportion de transversions entre deux séquences alignées.
     *
     * @param sequence1 La première séquence alignée.
     * @param sequence2 La deuxième séquence alignée.
     * @return La proportion de transversions entre les deux séquences.
     */
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
	
	/**
     * Vérifie si une paire de bases constitue une transition.
     *
     * @param a La première base.
     * @param b La deuxième base.
     * @return true si a et b sont une transition, false sinon.
     */
	public static boolean estTransition(char a, char b) {
		return (a == 'A' && b == 'G') || (a == 'G' && b == 'A') ||
		       (a == 'C' && b == 'T') || (a == 'T' && b == 'C');
	}

	/**
     * Vérifie si une paire de bases constitue une transversion.
     *
     * @param a La première base.
     * @param b La deuxième base.
     * @return true si a et b sont une transversion, false sinon.
     */
	public static boolean estTransversion(char a, char b) {
		return (a == 'A' && b == 'C') || (a == 'A' && b == 'T') ||
		       (a == 'G' && b == 'C') || (a == 'G' && b == 'T') ||
		       (a == 'C' && b == 'A') || (a == 'C' && b == 'G') ||
		       (a == 'T' && b == 'A') || (a == 'T' && b == 'G');
	}
		
	/**
     * Récupère les identifiants des séquences depuis l'alignement et les range dans une ArrayList.
     *
     * @param sequencesAlignees Le profil des séquences alignées contenant les séquences ADN.
     * @return Une liste des identifiants des séquences.
     */
	public ArrayList<String> ajouterNomsSequences(Profile<DNASequence, NucleotideCompound> sequencesAlignees) {
		nomsSequences = new ArrayList<String>(); 
		for (AlignedSequence<DNASequence, NucleotideCompound> seqAlignee : sequencesAlignees.getAlignedSequences()) {
		 	nomsSequences.add(seqAlignee.getAccession().getID());
		}
		return nomsSequences;
	}	
}