package reconstruction.arbre;

public class AlgoNj extends MethodeConstructionArbre{
	
	public AlgoNj (MatriceDeDistance m) {
		super(m);
	}
	
	// Méthode pour calculer la nouvelle matrice corrigee en utilisant les divergences de chacun des taxons
    private MatriceDeDistance calculerMatriceCorrigee(MatriceDeDistance m) {
    	int n = m.getTailleMatrice();
        double[][] matriceCorrigee = new double[n][n];
        double[] sommeDistances = new double[n];
     // Calcul des sommes des distances pour chaque séquence = divergence de chaque N taxons par rapport aux autres
        System.out.println("Calcul des sommes des distances pour chaque séquence:");
        for (int i = 0; i < n; i++) {
            sommeDistances[i] = 0.0;
            for (int k = 0; k < n; k++) {
                sommeDistances[i] += m.getDistance(i, k);
            }
            System.out.println("Somme des distances pour la séquence " + m.getNomSequence(i) + ": " + sommeDistances[i]);
        }
     // Calcul des valeurs de la matrice corrigée
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    //matriceCorrigee[i][j] = (n - 2) * matrice.getDistance(i, j) - sommeDistances[i] - sommeDistances[j];
                    matriceCorrigee[i][j] = m.getDistance(i, j) - (sommeDistances[i] + sommeDistances[j]) / (n - 2); //vérifier si idem ligne du dessus
                //} else {
                 //  matriceCorrigee[i][j] = Double.MAX_VALUE; // Pour éviter de fusionner un cluster avec lui-même
                //}
                }
            }
        }          
        // Créer et retourner un nouvel objet MatriceDeDistance avec la matrice corrigée
        MatriceDeDistance matriceDistanceCorrigee = new MatriceDeDistance(matriceCorrigee, matrice.getNomsSequences());
        return matriceDistanceCorrigee;
}
    
    // Méthode pour calculer les distances du nouveau cluster par rapport aux autres = A REVOIR !!!!!
    private double[] calculerNouvellesDistances(int cluster1, int cluster2) {
        double[] nouvellesDistances = new double[n];
        for (int k = 0; k < n; k++) {
            if (k != cluster1 && k != cluster2) {
                double distance = (matrice.getDistance(cluster1, k) + matrice.getDistance(cluster2, k)
                                   - matrice.getDistance(cluster1, cluster2)) / 2.0;
                nouvellesDistances[k] = distance;
            }
        }
        return nouvellesDistances;
    }
    
 // Méthode qui exécute l'algo NJ, fusionne les clusters jusqu'à ce qu'il ne reste qu'un seul cluster (racine de l'arbre)
    public Noeud demarrerNJ() {
        while (n > 2) {
            MatriceDeDistance matriceCorrigee = calculerMatriceCorrigee(matrice);
            System.out.println("Affichage de la matrice corrigée : ");
            matriceCorrigee.afficherMatrice();
            int[] clustersAFusionner = trouverClustersAFusionner(matriceCorrigee);
            int cluster1 = clustersAFusionner[0];
            int cluster2 = clustersAFusionner[1];
            double distance = matrice.getDistance(cluster1, cluster2);
            // Fusionner les clusters et créer un nouveau noeud
            fusionnerClusters(cluster1, cluster2, distance);
            // Mettre à jour la matrice de distances
            double[] nouvellesDistances = calculerNouvellesDistances(cluster1, cluster2);
            matrice.majMatrice(cluster1, cluster2, nouvellesDistances);    
        }
        Noeud fusionCluster = fusionnerClusters(0, 1, matrice.getDistance(0, 1) / 2.0);
        // Fusion finale pour les deux derniers clusters restants
        return fusionCluster;
    }

    // Méthode pour générer le format Newick à partir de l'arbre NJ construit
    public String genererNewick() {
        Noeud racineArbre = demarrerNJ(); // On obtient la racine de l'arbre NJ
        return racineArbre.toString() + ";";
    }
    
}
