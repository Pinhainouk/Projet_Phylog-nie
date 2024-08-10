package reconstruction.arbre;

import java.util.ArrayList;
import java.util.Arrays;

public class testMatrice {
	public static void main(String[] args) {
		double [][] distances =
				{{0, 3, 5, 7},
	            {3, 0, 4, 1},
	            {5, 4, 0, 2},
	            {7, 1, 2, 0}};
        ArrayList<String> sequences = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
		MatriceDeDistance m = new MatriceDeDistance(distances, sequences);
		 // Afficher la matrice initiale
		System.out.println("Afficher matrice initiale : ");
		m.afficherMatrice();
		// Créer une instance de Upgma et exécuter l'algorithme
		Upgma upgma = new Upgma(m);
		String formatNewick = upgma.genererNewick();
		System.out.println("Format Newick généré :");
        System.out.println(formatNewick);
	}
}