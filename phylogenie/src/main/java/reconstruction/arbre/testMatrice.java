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
		MatriceDeDistance m1 = new MatriceDeDistance(distances, sequences);
		 // Afficher la matrice initiale
		System.out.println("******************UPGMA********************");
		System.out.println("Afficher matrice initiale : ");
		m1.afficherMatrice();
		// Créer une instance de Upgma et exécuter l'algorithme
		AlgoUpgma upgma = new AlgoUpgma(m1);
		String formatNewickUpgma = upgma.genererNewick();
		System.out.println("Format Newick UPGMA généré :");
        System.out.println(formatNewickUpgma);
        double [][] d2 =
			{{0, 5, 4, 7, 6, 8},
            {5, 0, 7, 10, 9, 11},
            {4, 7, 0, 7, 6, 8},
            {7, 10, 7, 0, 5, 9},
            {6, 9, 6, 5, 0, 8},
            {8, 11, 8, 9, 8, 0}};
        ArrayList<String> s2 = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F"));
        MatriceDeDistance m2 = new MatriceDeDistance(d2, s2); 
        System.out.println("********************NJ*********************");
		System.out.println("Afficher matrice initiale : ");
		m2.afficherMatrice();
        AlgoNj nj = new AlgoNj(m2);
        String formatNewickNj = nj.genererNewick();
		System.out.println("Format Newick NJ généré :");
		System.out.println(formatNewickNj);
	}
}