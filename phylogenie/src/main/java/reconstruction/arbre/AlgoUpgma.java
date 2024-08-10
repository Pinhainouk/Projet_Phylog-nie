package reconstruction.arbre;

public class AlgoUpgma extends MethodeConstructionArbre{
	
	public AlgoUpgma(MatriceDeDistance m) {
	    super(m);
	}
	
		// Calcule la distance moyenne entre le nouveau cluster et les autres clusters
    private double[] calculerNouvellesDistances(int cluster1, int cluster2) {
        double[] nouvellesDistances = new double[n];
        int index = 0;
        for (int i = 0; i < n; i++) {
            if (i != cluster1 && i != cluster2) { // garantit que l'on n'inclut pas les distances des clusters fusionnés
                nouvellesDistances[index] = (matrice.getDistance(cluster1, i) + matrice.getDistance(cluster2, i)) / 2.0;
                index++;
            }
        }
        // Débogage
        System.out.println("Nouvelles distances après fusion des clusters " + cluster1 + " et " + cluster2 + ":");
        for (int i = 0; i < nouvellesDistances.length; i++) {
            System.out.print(nouvellesDistances[i] + " ");
        }
        System.out.println();       
        return nouvellesDistances;
    }
		   
    // Méthode qui exécute l'algo UPGMA, fusionne les clusters jusqu'à ce qu'il ne reste qu'un seul cluster (racine de l'arbre)
    public Noeud demarrerUpgma() {
        while (n > 1) {
            int[] clustersAFusionner = trouverClustersAFusionner(matrice);
            int cluster1 = clustersAFusionner[0];
            int cluster2 = clustersAFusionner[1];
            double distance = matrice.getDistance(cluster1, cluster2);
            fusionnerClusters(cluster1, cluster2, distance); // n diminue ici après la fusion
            double[] nouvellesDistances = calculerNouvellesDistances(cluster1, cluster2); 
            matrice.majMatrice(cluster1, cluster2, nouvellesDistances);
        }
        return clusters.get(0); // Le noeud restant est la racine de l'arbre
    }
    
    // Méthode pour générer le format Newick à partir de l'arbre UPGMA construit
    public String genererNewick() {
        Noeud racineArbre = demarrerUpgma(); // On obtient la racine de l'arbre UPGMA
        return racineArbre.toString() + ";";
    }
}