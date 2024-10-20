package reconstruction.arbre;
import java.util.ArrayList;

/**
 * La classe AlgoUpgma implémente l'algorithme UPGMA (Unweighted Pair Group Method with Arithmetic Mean)
 * pour reconstruire un arbre phylogénétique à partir d'une matrice de distances.
 * Elle hérite de la classe MethodeConstructionArbre et gère la fusion des clusters
 * jusqu'à obtenir un unique cluster représentant la racine de l'arbre.
 */
public class AlgoUpgma extends MethodeConstructionArbre{ 
	
	 /**
     * Constructeur de la classe AlgoUpgma.
     *
     * @param m La matrice de distances à partir de laquelle l'arbre sera construit.
     */
	public AlgoUpgma(MatriceDeDistances m) {
	    super(m);
	}
		
	/**
     * Exécute l'algorithme UPGMA
     * trouve les clusters à fusionner (ceux dont la distance est minimale)
     * fusionne les clusters 
     * calcule les nouvelles distances pour le cluster et les autres séquences et met à jour la matrice
     * Répète le processus
     * jusqu'à ce qu'il ne reste qu'un seul cluster (racine de l'arbre).
     *
     * @return Le noeud représentant la racine de l'arbre phylogénétique construit.
     */
    public Noeud executerUpgma() {
        while (n > 1) {
            int[] clustersAFusionner = trouverClustersAFusionner(matrice);
            int cluster1 = clustersAFusionner[0];
            int cluster2 = clustersAFusionner[1];
            double distance = matrice.getDistance(cluster1, cluster2);
            calculerDistanceAuNoeud(distance);
            fusionnerClusters(cluster1, cluster2, distance); // n diminue ici après la fusion
            double[] nouvellesDistances = calculerNouvellesDistances(cluster1, cluster2); 
            majMatrice(cluster1, cluster2, nouvellesDistances);
        }
        return clusters.get(0); // Le noeud restant est la racine de l'arbre
    }
    
    /**
     * Calcule la taille de la branche pour un noeud.
     *
     * @param dij La distance entre les deux clusters à fusionner.
     * @return La distance au noeud, qui est la moitié de la distance entre les clusters.
     */
    private double calculerDistanceAuNoeud(double dij) {
        return dij / 2;
    }
	
    /**
     * Fusionne deux clusters, crée un nouveau noeud pour le cluster et met à jour la liste des clusters.
     *
     * @param cluster1 L'indice du premier cluster à fusionner.
     * @param cluster2 L'indice du deuxième cluster à fusionner.
     * @param distance La distance entre les deux clusters à fusionner.
     * @return Le nouveau noeud représentant le cluster fusionné.
     */
	public Noeud fusionnerClusters(int cluster1, int cluster2, double distance) {
        Noeud clusterGauche = clusters.get(cluster1);
        Noeud clusterDroit = clusters.get(cluster2);
        Noeud nouveauCluster = new Noeud(clusterGauche.nom + "/" + clusterDroit.nom);
        double distanceGauche = distance / 2.0 - clusterGauche.getDistance(); // Calcule les distances des clusters fusionnés
        System.out.println("clusterGauche = " + clusterGauche.getDistance());
        System.out.println("distance = " + distance);
        System.out.println("distanceGauche = " + distanceGauche);
        double distanceDroit = distance / 2.0 - clusterDroit.getDistance();
        System.out.println("distanceDroit = " + distanceDroit);
        nouveauCluster.ajouterEnfant(clusterGauche, distanceGauche);
        nouveauCluster.ajouterEnfant(clusterDroit, distanceDroit);
        nouveauCluster.setDistance(distance / 2.0); // Diviser par 2 pour la taille de la branche correcte      
        clusters.add(nouveauCluster); // Mettre à jour la liste des clusters
        clusters.remove(cluster2); // Suppression dans l'ordre inverse pour éviter le décalage
        clusters.remove(cluster1);
        ArrayList<String> nouveauxNomsSequences = new ArrayList<>(matrice.getNomsSequences());
        nouveauxNomsSequences.add(nouveauCluster.nom);
        nouveauxNomsSequences.remove(cluster2);
        nouveauxNomsSequences.remove(cluster1);
        for (int i = 0; i < nouveauxNomsSequences.size(); i++) {
   	        matrice.setNomsSequences(i, nouveauxNomsSequences.get(i));
        }        
        n--; // Réduit le nombre de clusters      
        System.out.println("Fusion des clusters " + clusterGauche.nom + " et " + clusterDroit.nom + " à une distance de " + distance/2); // Débogage
        System.out.println("Nouveau cluster: " + nouveauCluster.nom);
        return nouveauCluster;
    }
	
	/**
     * Calcule la distance moyenne entre le nouveau cluster et les autres clusters.
     *
     * @param cluster1 L'indice du premier cluster fusionné.
     * @param cluster2 L'indice du deuxième cluster fusionné.
     * @return Un tableau contenant les nouvelles distances du nouveau cluster aux autres clusters.
     */
    private double[] calculerNouvellesDistances(int cluster1, int cluster2) {
        double[] nouvellesDistances = new double[matrice.getTailleMatrice()];
        int index = 0;
        for (int i = 0; i < matrice.getTailleMatrice(); i++) {
            if (i != cluster1 && i != cluster2) { // Garantit que l'on n'inclut pas les distances des clusters fusionnés
                nouvellesDistances[index] = (matrice.getDistance(cluster1, i) + matrice.getDistance(cluster2, i)) / 2.0;
                index++;
           }
        }      
        System.out.println("Nouvelles distances après fusion des clusters " + cluster1 + " et " + cluster2 + ":");  // Débogage
        //System.out.println("Nouvelles distances après fusion des clusters " + matrice.getNomSequence(cluster1) + " et " + matrice.getNomSequence(cluster2) + ":"); à revoir
        for (int i = 0; i < nouvellesDistances.length; i++) {
            System.out.print(nouvellesDistances[i] + " ");
        }
        System.out.println();       
        return nouvellesDistances;
   }
	   
    /**
     * Met à jour la matrice de distances en supprimant les lignes et colonnes des clusters fusionnés
     * et en ajoutant une nouvelle ligne et colonne pour le nouveau cluster.
     *
     * @param cluster1 L'indice du premier cluster fusionné.
     * @param cluster2 L'indice du deuxième cluster fusionné.
     * @param nouvellesDistances Les distances mises à jour du nouveau cluster.
     */
 	public void majMatrice(int cluster1, int cluster2, double[] nouvellesDistances) {
 		matrice.diminuerTailleMatrice(cluster2);
 		matrice.diminuerTailleMatrice(cluster1);
 		matrice.ajouterLigneColonne(nouvellesDistances);		
 		System.out.println("Matrice après mise à jour:"); // Débogage
 		matrice.afficherMatrice();
 	}
}