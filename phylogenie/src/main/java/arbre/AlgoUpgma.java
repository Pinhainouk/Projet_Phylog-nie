package arbre;
import java.util.ArrayList;

/**
 * La classe AlgoUpgma implémente l'algorithme UPGMA (Unweighted Pair Group Method with Arithmetic Mean)
 * pour reconstruire un arbre phylogénétique à partir d'une matrice de distances.
 * Elle hérite de la classe ConstructionArbreAlgo et gère la fusion des clusters
 * jusqu'à obtenir un unique cluster représentant la racine de l'arbre.
 */
public class AlgoUpgma extends ConstructionArbreAlgo{ 
	
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
            ArrayList<Integer> clustersAFusionner = trouverClustersAFusionner(matrice);
            int cluster1 = clustersAFusionner.get(0);
            System.out.println("cluster A fusionner get(0) : " + clustersAFusionner.get(0));
            int cluster2 = clustersAFusionner.get(1);
            System.out.println("cluster A fusionner get(1) : " + clustersAFusionner.get(1));
            double distance = matrice.getDistance(cluster1, cluster2);
            Noeud nouveauCluster = fusionnerClusters(cluster1, cluster2, distance); // n diminue ici après la fusion
            ArrayList<Double> nouvellesDistances = calculerNouvellesDistances(cluster1, cluster2);
            String nouveauNomCluster = nouveauCluster.nom;
            matrice.majMatrice(cluster1, cluster2, nouvellesDistances, nouveauNomCluster);
        }
        return clusters.get(0); // Le noeud restant est la racine de l'arbre
    }
    	
	/**
     * Calcule la distance moyenne entre le nouveau cluster et les autres clusters.
     *
     * @param cluster1 L'indice du premier cluster fusionné.
     * @param cluster2 L'indice du deuxième cluster fusionné.
     * @return Une liste contenant les nouvelles distances du nouveau cluster aux autres clusters.
     */
    private ArrayList<Double> calculerNouvellesDistances(int cluster1, int cluster2) {
        ArrayList<Double> nouvellesDistances = new ArrayList<>();
        for (int i = 0; i < matrice.getTailleMatrice(); i++) {
            if (i != cluster1 && i != cluster2) { // Garantit que l'on n'inclut pas les distances des clusters fusionnés
            	 double distance = (matrice.getDistance(cluster1, i) + matrice.getDistance(cluster2, i)) / 2.0;
                 nouvellesDistances.add(distance); // Ajoute la nouvelle distance à la liste
           }
        }      
        System.out.print("Nouvelles distances après fusion des clusters : ");  // Débogage
        for (int i = 0; i < nouvellesDistances.size(); i++) {
            System.out.print(nouvellesDistances.get(i) + " | ");
        }
        System.out.println();       
        return nouvellesDistances;
    }
}