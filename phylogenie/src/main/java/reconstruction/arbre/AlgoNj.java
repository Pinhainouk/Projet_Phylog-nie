package reconstruction.arbre;

/**
 * La classe AlgoNj implémente l'algorithme Neighbor-Joining
 * pour reconstruire un arbre phylogénétique à partir d'une matrice de distances.
 * Elle hérite de la classe MethodeConstructionArbre et corrige la matrice de distances à chaque étape 
 * en fonction des divergences entre les séquences, 
 * puis fusionnent les deux clusters qui minimisent cette distance corrigée.
 */
public class AlgoNj extends MethodeConstructionArbre{ // A REVOIR
	
	/**
     * Constructeur qui initialise l'algorithme avec une matrice de distances donnée.
     * 
     * @param m La matrice de distances entre les séquences.
     */
	public AlgoNj (MatriceDeDistances m) {
		super(m);
	}
	
	/**
     * Exécute l'algorithme Neighbor-Joining 
     * Calcul les divergences pour chaque séquence.
     * Corrige la matrice de distances.
     * Sélectionne les deux clusters les plus proches à fusionner.
     * Fusionne les clusters et met à jour la matrice
     * Répète le processus jusqu'à ce qu'il ne reste qu'un seul cluster.
     * @return Le dernier cluster.
     */
    public Noeud executerNJ() {
        while (n > 2) {
        	double[] divergences = calculerDivergence();
        	MatriceDeDistances matriceCorrigee = calculerMatriceCorrigee(divergences);
            System.out.println("Affichage de la matrice corrigée : "); // affichage
            matriceCorrigee.afficherMatrice(); // affichage
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
        fusionnerClustersRestants(); // Fusion finale des deux derniers clusters
        return clusters.get(0); // Retourne le dernier cluster fusionné
    }
      
    /**
     * Calcule les divergences (r(i)) pour chaque séquence en sommant les distances avec les autres séquences.
     * 
     * @return Un tableau contenant la divergence de chaque séquence.
     */
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
    
    /**
     * Calcule la matrice de distances corrigée en fonction des divergences de chaque séquence.
     * La matrice corrigée est utilisée pour déterminer les clusters à fusionner.
     * 
     * @param divergences Les divergences calculées pour chaque séquence.
     * @return Une nouvelle matrice de distances corrigée.
     */
    private MatriceDeDistances calculerMatriceCorrigee(double [] divergences) {
        double[][] matriceCorrigee = new double[n][n];    
        for (int i = 0; i < n; i++) { // Calcul des valeurs de la matrice corrigée
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    matriceCorrigee[i][j] = matrice.getDistance(i, j) - (divergences[i] + divergences[j]) / (n - 2); 
                    matriceCorrigee[j][i] = matriceCorrigee[i][j];
                }
            }    
        }       
        MatriceDeDistances matriceDistanceCorrigee = new MatriceDeDistances(matriceCorrigee, matrice.getNomsSequences()); // Crée et retourne un nouvel objet MatriceDeDistance avec la matrice corrigée
        return matriceDistanceCorrigee;            
    }
    
    /**
     * Calcule la distance d'une séquence au nœud u lors de la fusion de clusters.
     * 
     * @param dij La distance entre les clusters i et j.
     * @param ri La divergence de la séquence i.
     * @param rj La divergence de la séquence j.
     * @return La distance de la séquence i au nœud u.
     */
    private double calculerDistanceAuNoeud(double dij, double ri, double rj) {
        return dij / 2 + (ri - rj) / (2 * (n - 2));
    }
    
    /**
     * Fusionne deux clusters en un nouveau cluster et met à jour leurs distances au nœud de fusion.
     * 
     * @param cluster1 L'indice du premier cluster à fusionner.
     * @param cluster2 L'indice du deuxième cluster à fusionner.
     * @param Siu La distance du cluster1 au nouveau nœud.
     * @param Sju La distance du cluster2 au nouveau nœud.
     */
    private void fusionnerClusters(int cluster1, int cluster2, double Siu, double Sju) {
        Noeud clusterGauche = clusters.get(cluster1);
        Noeud clusterDroit = clusters.get(cluster2);
        Noeud nouveauCluster = new Noeud(clusterGauche.nom + clusterDroit.nom);       
        nouveauCluster.ajouterEnfant(clusterGauche); // Ajoute les enfants au nouveau cluster
        nouveauCluster.ajouterEnfant(clusterDroit); 
        clusterGauche.setDistance(Siu); // Met à jour la distance
        clusterDroit.setDistance(Sju);  
        clusters.set(cluster1, nouveauCluster); // Remplace le cluster1 fusionné
        clusters.remove(cluster2);  // Retire le cluster2 fusionné     
        n--; // Décrémenter le nombre de clusters restants
        System.out.println("Fusion des clusters " + clusterGauche.nom + " et " + clusterDroit.nom + " pour former " + nouveauCluster.nom);
    }
    
    /**
     * Met à jour la matrice de distances après la fusion de deux clusters.
     * 
     * @param cluster1 L'indice du premier cluster fusionné.
     * @param cluster2 L'indice du deuxième cluster fusionné.
     */
    private void majMatrice(int cluster1, int cluster2) {
        for (int i = 0; i < n; i++) {
            if (i != cluster1 && i!= cluster2) {
                double nouvelleDistance = (matrice.getDistance(i, cluster1) + matrice.getDistance(i, cluster2) - matrice.getDistance(cluster1, cluster2)) / 2;
                matrice.setDistance(i, cluster1, nouvelleDistance);
                matrice.setDistance(cluster1, i, nouvelleDistance);
            }
        }
        matrice.diminuerTailleMatrice(cluster2); 
        matrice.setNomsSequences(cluster1, "U");
    }
    
    /**
     * Fusionne les deux derniers clusters restants.
     */
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
}
