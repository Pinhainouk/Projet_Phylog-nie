package arbre;
import java.util.ArrayList;
import java.util.Collections;

/**
 * La classe AlgoNj implémente l'algorithme Neighbor-Joining (NJ) pour la construction d'arbres phylogénétiques.
 * Cette classe hérite de la classe ConstructionArbreAlgo et fournit des méthodes spécifiques 
 * pour appliquer l'algorithme NJ en fusionnant des clusters de séquences sur la base de leurs divergences
 * et en mettant à jour la matrice de distances de manière itérative.
 */
public class AlgoNj extends ConstructionArbreAlgo{
	
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
        	ArrayList<Double> divergences = calculerDivergence();
        	MatriceDeDistances matriceCorrigee = calculerMatriceCorrigee(divergences);
            System.out.println("Affichage de la matrice corrigée : "); // affichage
            matriceCorrigee.afficherMatrice(); // affichage
            ArrayList<Integer> clustersAFusionner = trouverClustersAFusionner(matriceCorrigee);
            int cluster1 = clustersAFusionner.get(0);
            int cluster2 = clustersAFusionner.get(1);
            double distance = matrice.getDistance(cluster1, cluster2);  
            double Siu = calculerDistanceAuNoeud(distance, divergences.get(cluster1), divergences.get(cluster2));
            double Sju = distance - Siu;
            System.out.println("Distance Siu : ");
            System.out.println(Siu);
            System.out.println("Distance Sju : ");
            System.out.println(Sju);
            Noeud nouveauCluster = fusionnerClusters(cluster1, cluster2, Siu, Sju);
            String nouveauNomCluster = nouveauCluster.nom;
            ArrayList<Double> nouvellesDistances = calculerNouvellesDistances(cluster1, cluster2);
            matrice.majMatrice(cluster1, cluster2, nouvellesDistances, nouveauNomCluster);
        }
        fusionnerClustersRestants();  
        return clusters.get(0); // Retourne le dernier cluster fusionné
    }
      
    /**
     * Calcule les divergences r(i) pour chaque séquence en sommant les distances avec les autres séquences.
     * 
     * @return Une liste contenant la divergence de chaque séquence.
     */
    private ArrayList<Double> calculerDivergence() {
    	System.out.println("Calcul des sommes des distances pour chaque séquence:");
    	ArrayList<Double> divergences = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            double somme = 0.0;
            for (int j = 0; j < n; j++) {
                if (i != j) {
                	somme += matrice.getDistance(i, j);
                }
            }
            divergences.add(somme);
            System.out.println("Somme des distances pour la séquence " + matrice.getNomSequence(i) + ": " + somme);
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
    private MatriceDeDistances calculerMatriceCorrigee(ArrayList<Double> divergences) {
    	ArrayList<ArrayList<Double>> matriceCorrigee = new ArrayList<>();
    	for (int i = 0; i < n; i++) {
            matriceCorrigee.add(new ArrayList<>(Collections.nCopies(n, 0.0))); // Initialisation
        }
        for (int i = 0; i < n; i++) { // Calcul des valeurs de la matrice corrigée
            for (int j = 0; j < n; j++) {
                if (i != j) {
                	double valeurCorrigee = matrice.getDistance(i, j) - (divergences.get(i) + divergences.get(j)) / (n - 2);
                    matriceCorrigee.get(i).set(j, valeurCorrigee); 
                    matriceCorrigee.get(j).set(i, valeurCorrigee); 
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
        return dij / 2 + ((ri - rj) / (2 * (n - 2)));
    } 
    
    /**
     * Calcule les nouvelles distances entre le nouveau cluster et les autres clusters.
     *
     * @param cluster1 L'indice du premier cluster fusionné.
     * @param cluster2 L'indice du deuxième cluster fusionné.
     * @return Une liste contenant les nouvelles distances du nouveau cluster aux autres clusters.
     */
    private ArrayList<Double> calculerNouvellesDistances(int cluster1, int cluster2) {
        ArrayList<Double> nouvellesDistances = new ArrayList<>();
        for (int i = 0; i < matrice.getTailleMatrice(); i++) {
            if (i != cluster1 && i != cluster2) { // Garantit que l'on n'inclut pas les distances des clusters fusionnés
            	 double distance = (matrice.getDistance(i, cluster1) + matrice.getDistance(i, cluster2) - matrice.getDistance(cluster1, cluster2)) / 2;
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
    
    /**
     * Fusionne les deux derniers clusters restants pour former le cluster final.
     * Cette méthode est utilisée une fois que tous les clusters ont été progressivement fusionnés
     * pour obtenir un arbre de regroupement complet, avec un nœud racine unique représentant 
     * le dernier cluster fusionné.
     * 
     * Les distances entre les deux derniers clusters sont calculées, et chaque nœud enfant est ajouté
     * au nouveau cluster avec sa distance respective au nœud racine.
     */
    private void fusionnerClustersRestants() {
        Noeud dernierCluster1 = clusters.get(0);
        Noeud dernierCluster2 = clusters.get(1);
        double distance = matrice.getDistance(0, 1) / 2;
        Noeud clusterFinal = new Noeud(dernierCluster1.nom + dernierCluster2.nom);
        clusterFinal.ajouterEnfant(dernierCluster1, distance);
        clusterFinal.ajouterEnfant(dernierCluster2, distance);
        dernierCluster1.setDistance(distance);
        dernierCluster2.setDistance(distance);
        clusters.clear();
        clusters.add(clusterFinal);
        System.out.println("Fusion finale pour former " + clusterFinal.nom);
    }
}
