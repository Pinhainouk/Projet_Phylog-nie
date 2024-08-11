package reconstruction.arbre;

import java.util.ArrayList;
import java.util.Arrays;

public class testMatrice {

	public static void main(String[] args) {
		//double [][] distances =
				//{{0, 3, 5, 7},
	            //{3, 0, 4, 1},
	            //{5, 4, 0, 2},
	            //{7, 1, 2, 0}};
        //ArrayList<String> sequences = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
		//MatriceDeDistance m1 = new MatriceDeDistance(distances, sequences);
		 // Afficher la matrice initiale
		//System.out.println("******************UPGMA********************");
		//System.out.println("Afficher matrice initiale : ");
		//m1.afficherMatrice();
		// Créer une instance de Upgma et exécuter l'algorithme
		//AlgoUpgma upgma = new AlgoUpgma(m1);
		//String formatNewickUpgma = upgma.genererNewick();
		//System.out.println("Format Newick UPGMA généré :");
        //System.out.println(formatNewickUpgma);
		double [][] d =
			{{0, 5, 4, 7, 6, 8},
            {5, 0, 7, 10, 9, 11},
            {4, 7, 0, 7, 6, 8},
            {7, 10, 7, 0, 5, 9},
            {6, 9, 6, 5, 0, 8},
            {8, 11, 8, 9, 8, 0}};
        ArrayList<String> s = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F"));
        MatriceDeDistance m = new MatriceDeDistance(d, s); 
        System.out.println("********************NJ*********************");
		System.out.println("Afficher matrice initiale : ");
		m.afficherMatrice();
        AlgoNj nj = new AlgoNj(m);
        String formatNewickNj = nj.genererNewick();
		System.out.println("Format Newick NJ généré :");
		System.out.println(formatNewickNj);
		//System.out.println("********************UPGMA*********************");
		//double [][] d3 =
			//{{0, 5, 4, 7, 6, 8},
            //{5, 0, 7, 10, 9, 11},
            //{4, 7, 0, 7, 6, 8},
            //{7, 10, 7, 0, 5, 9},
            //{6, 9, 6, 5, 0, 8},
            //{8, 11, 8, 9, 8, 0}};
        //ArrayList<String> s3 = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F"));
        //MatriceDeDistance m3 = new MatriceDeDistance(d3, s3); 
		//System.out.println("Afficher matrice initiale : ");
		//m3.afficherMatrice();
		// Créer une instance de Upgma et exécuter l'algorithme		
		//AlgoUpgma upgma2 = new AlgoUpgma(m3);
		//String formatNewickUpgma2 = upgma2.genererNewick();
		//System.out.println("Format Newick UPGMA généré :");
        //System.out.println(formatNewickUpgma2);
        System.out.println("*******************UPGMA******************");
        double [][] dismatrice = 
        	{{0,32,48,51,50,48,98,148},
        	{32,0,26,34,29,33,84,136},
        	{48,26,0,42,44,44,92,152},
        	{51,34,42,0,44,38,86,142},
        	{50,29,44,44,0,24,89,142},
        	{48,33,44,38,24,0,90,142},
        	{98,84,92,86,89,90,0,148},
        	{148,136,152,142,142,142,148,0}};
        ArrayList<String> seqmatrice = new ArrayList<>(Arrays.asList("Chien", "Ours", "Racoon", "Belette", "Phoque", "Otarie", "Chat", "Singe"));
        MatriceDeDistance exmatrice = new MatriceDeDistance(dismatrice, seqmatrice); 
		System.out.println("Afficher matrice initiale : ");
		exmatrice.afficherMatrice();
		AlgoUpgma upgmaex = new AlgoUpgma(exmatrice);
		String formatNewickex = upgmaex.genererNewick();
		System.out.println("Format Newick UPGMA généré :");
        System.out.println(formatNewickex);
	}
}
	