package arbre;
import java.util.ArrayList;
import java.util.Collections;

import org.biojava.nbio.core.alignment.template.AlignedSequence;
import org.biojava.nbio.core.alignment.template.Profile;
import org.biojava.nbio.core.sequence.*;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;

/**
 * La classe fille MatriceDeDistancesProteine hérite de la classe MatriceDeDistances et représente une matrice de distances pour les séquences protéiques.
 * Elle fournit des méthodes pour calculer les distances observées entre des séquences d'acides aminés alignées.
 */
public class MatriceDeDistancesProteine extends MatriceDeDistances{

	/**
     * Constructeur par défaut qui initialise une matrice vide de distances.
     */
	public MatriceDeDistancesProteine() {
		super();
	}
	
	/**
     * Remplit le tableau des distances observées à partir de l'alignement de séquences protéiques.
     *
     * @param sequencesAlignees Le profil des séquences alignées contenant les séquences protéiques.
     * @return La matrice des distances observées.
     */
	public ArrayList<ArrayList<Double>> ajouterDistancesObserveesProteines(Profile<ProteinSequence, AminoAcidCompound> sequencesAlignees) {
		int nombreSequences = sequencesAlignees.getAlignedSequences().size();
		matrice = new ArrayList<>(nombreSequences);
		System.out.println("Nombre de séquences alignées: " + nombreSequences);
		for (int i = 0; i < nombreSequences; i++) { // Initialisez la matrice avec des ArrayList
	        matrice.add(new ArrayList<>(Collections.nCopies(nombreSequences, 0.0))); // Remplissage avec 0.0
	    }	
		for (int i = 0; i < nombreSequences; i++) {
			for (int j = i + 1; j < nombreSequences; j++) { //i+2?			
				if (i >= 0 && i < nombreSequences && j >= 0 && j < nombreSequences) { // Vérifie les index avant d'accéder aux séquences
					double distance = calculerDistanceObserveeProteine(sequencesAlignees.getAlignedSequence(i+1), sequencesAlignees.getAlignedSequence(j+1)); // cf getAlignedSequence
					matrice.get(i).set(j, distance);
	                matrice.get(j).set(i, distance);
				} else {
					System.out.println("Index i ou j hors limites : i=" + i + ", j=" + j);
				}
			}
		}
		return matrice;	
	}
	
	/**
     * Récupère les identifiants des séquences depuis l'alignement et les range dans une ArrayList.
     *
     * @param sequencesAlignees Le profil des séquences alignées contenant les séquences protéiques.
     * @return Une liste des identifiants des séquences.
     */
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
	
	/**
     * Calcule la distance observée entre deux séquences protéiques alignées.
     *
     * @param sequence1 La première séquence alignée.
     * @param sequence2 La deuxième séquence alignée.
     * @return La distance observée entre les deux séquences.
     */
	public double calculerDistanceObserveeProteine(AlignedSequence<ProteinSequence, AminoAcidCompound> sequence1, AlignedSequence<ProteinSequence, AminoAcidCompound> sequence2) {
		double longueurSequencesAlignees = sequence1.getLength(); // Les séquences alignées font toutes la même taille
		double correspondances = 0.0; // Nombre de similitudes initialisé à 0	
		for (int i = 0; i < longueurSequencesAlignees; i++) {
			if (sequence1.getCompoundAt(i + 1).equals(sequence2.getCompoundAt(i + 1))) { // position - Biological index (1 to n)
				correspondances = correspondances + 1;
			}
		}
		double similarite = correspondances / longueurSequencesAlignees; // Calcule la proportion de similarité
		double distanceObservee = 1 - similarite; // Calcule la proportion de différences
	    return distanceObservee;
	}
}
	