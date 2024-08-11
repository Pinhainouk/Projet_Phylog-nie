package reconstruction.arbre;

public class AlgoNj extends MethodeConstructionArbre{
	
	public AlgoNj (MatriceDeDistance m) {
		super(m);
	}
	
	// Méthode qui exécute l'algo NJ, fusionne les clusters jusqu'à ce qu'il ne reste qu'un seul cluster (racine de l'arbre)
    public Noeud executerNJ() {
        while (n > 2) {
        	double[] divergences = calculerDivergence();
            MatriceDeDistance matriceCorrigee = calculerMatriceCorrigee(divergences);
            System.out.println("Affichage de la matrice corrigée : ");
            matriceCorrigee.afficherMatrice();
            int[] clustersAFusionner = trouverClustersAFusionner(matriceCorrigee);
            int cluster1 = clustersAFusionner[0];
            int cluster2 = clustersAFusionner[1];
            double distance = matrice.getDistance(cluster1, cluster2);  
            double Siu = calculerDistanceAuNoeud(distance, divergences[cluster1], divergences[cluster2]);
            double Sju = distance - Siu;
            System.out.println("Distance Siu : ");
            System.out.println(Siu);
            System.out.println("Distance Sju : ");
            System.out.println(Sju);
            fusionnerClusters(cluster1, cluster2, Siu, Sju);
            majMatrice(cluster1, cluster2);
            System.out.println("Affichage de la matrice mise à jour: ");
            matrice.afficherMatrice();
        }
        // Fusion finale des deux derniers clusters
        fusionnerClustersRestants();
        // Retourner le dernier cluster comme racine
        return clusters.get(0);
    }
      
    // Méthode pour calculer la divergence r(i) pour chaque séquence
    private double[] calculerDivergence() {
    	System.out.println("Calcul des sommes des distances pour chaque séquence:");
        double[] divergences = new double[n];
        for (int i = 0; i < n; i++) {
            divergences[i] = 0.0;
            for (int j = 0; j < n; j++) {
                if (i != j) {
                	divergences[i] += matrice.getDistance(i, j);
                }
            }
            System.out.println("Somme des distances pour la séquence " + matrice.getNomSequence(i) + ": " + divergences[i]);
        }
        return divergences;
    }
    
    // Méthode pour calculer la nouvelle matrice corrigee en utilisant les divergences de chacun des taxons
    private MatriceDeDistance calculerMatriceCorrigee(double [] divergences) {
        double[][] matriceCorrigee = new double[n][n];
        // Calcul des valeurs de la matrice corrigée
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    matriceCorrigee[i][j] = matrice.getDistance(i, j) - (divergences[i] + divergences[j]) / (n - 2); 
                    matriceCorrigee[j][i] = matriceCorrigee[i][j];
                }
            }    
        }
        // Créer et retourner un nouvel objet MatriceDeDistance avec la matrice corrigée
        MatriceDeDistance matriceDistanceCorrigee = new MatriceDeDistance(matriceCorrigee, matrice.getNomsSequences());
        return matriceDistanceCorrigee;            
    }
    
    // Méthode pour calculer la distance d'une séquence au nœud u
    private double calculerDistanceAuNoeud(double dij, double ri, double rj) {
        return dij / 2 + (ri - rj) / (2 * (n - 2));
    }
    
    
    private void fusionnerClusters(int cluster1, int cluster2, double Siu, double Sju) {
        Noeud clusterGauche = clusters.get(cluster1);
        Noeud clusterDroit = clusters.get(cluster2);
        Noeud nouveauCluster = new Noeud(clusterGauche.nom + clusterDroit.nom);
        // Ajouter les enfants au nouveau cluster
        nouveauCluster.ajouterEnfant(clusterGauche);
        nouveauCluster.ajouterEnfant(clusterDroit);
        // Mettre à jour la distance
        clusterGauche.setDistance(Siu);
        clusterDroit.setDistance(Sju);
        // Remplacer le cluster fusionné et retirer le cluster fusionné
        clusters.set(cluster1, nouveauCluster);
        clusters.remove(cluster2);
        // Décrémenter le nombre de clusters restants
        n--; 
        System.out.println("Fusion des clusters " + clusterGauche.nom + " et " + clusterDroit.nom + " pour former " + nouveauCluster.nom);
    }
    
    // Méthode pour mettre à jour la matrice de distances après la fusion de clusters
    private void majMatrice(int cluster1, int cluster2) {
        for (int i = 0; i < n; i++) {
            if (i != cluster1 && i!= cluster2) {
                double nouvelleDistance = (matrice.getDistance(i, cluster1) + matrice.getDistance(i, cluster2) - matrice.getDistance(cluster1, cluster2)) / 2;
                matrice.setDistance(i, cluster1, nouvelleDistance);
                matrice.setDistance(cluster1, i, nouvelleDistance);
            }
        }
        //matrice.diminuerTailleMatrice(cluster2); ne fonctionne pas pour NJ = à revoir
        matrice.removeCluster(cluster2);
        matrice.setNomsSequences(cluster1, "U");
    }
    
 // Méthode pour fusionner les deux derniers clusters restants
    private void fusionnerClustersRestants() {
        Noeud dernierCluster1 = clusters.get(0);
        Noeud dernierCluster2 = clusters.get(1);
        Noeud clusterFinal = new Noeud(dernierCluster1.nom + dernierCluster2.nom);
        clusterFinal.ajouterEnfant(dernierCluster1);
        clusterFinal.ajouterEnfant(dernierCluster2);
        double distance = matrice.getDistance(0, 1) / 2;
        dernierCluster1.setDistance(distance);
        dernierCluster2.setDistance(distance);
        clusters.clear();
        clusters.add(clusterFinal);
        System.out.println("Fusion finale pour former " + clusterFinal.nom);
    }

    // Méthode pour générer le format Newick à partir de l'arbre NJ construit
    public String genererNewick() {
        Noeud racineArbre = executerNJ(); // On obtient la racine de l'arbre NJ
        return racineArbre.toString() + ";";
    } 
}
