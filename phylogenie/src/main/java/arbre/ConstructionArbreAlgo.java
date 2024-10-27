package arbre;
import java.util.ArrayList;

/**
 * La Classe mère ConstructionArbreAlgo fournit les méthodes nécessaires pour construire un arbre phylogénétique
 * en fusionnant progressivement des clusters de séquences/taxons/OTU en fonction de leur distance pour les deux méthodes UPGMA et NJ. 
 */

public class ConstructionArbreAlgo {
	protected MatriceDeDistances matrice;
	protected ArrayList<Noeud> clusters;
	protected int n; // Nombre actuel de séquences/OTU
	
	/**
     * Constructeur de la classe MethodeConstructionArbre.
     *
     * @param m La matrice de distances à utiliser pour la construction de l'arbre.
     */
	public ConstructionArbreAlgo(MatriceDeDistances m) {
		this.matrice = m;
	    initialiserClusters();
	    this.n = matrice.getTailleMatrice();
	}
	
	/**
     * Méthode qui initialise les clusters à partir des séquences contenues dans la matrice de distances.
     * Chaque séquence est représentée par un nœud dans la structure de l'arbre.
     */
	private void initialiserClusters() {
		this.clusters = new ArrayList<>();
		for (int i = 0; i < matrice.getNomsSequences().size(); i++) {
			String nom = matrice.getNomsSequences().get(i);
			clusters.add(new Noeud(nom));		
		}
		afficherClusters();
	}
	
	/**
     * Méthode qui affiche la liste des clusters
     * Chaque séquence est représentée par un nœud dans la structure de l'arbre.
     */
	private void afficherClusters() {
	    System.out.println("Liste des clusters :");
	    for (Noeud cluster : clusters) {
	        System.out.println(cluster.nom); // Supposons que `nom` est un attribut de la classe Noeud
	    }
	}
	
	/**
     * Méthode pour trouver les indices des clusters/séquences/OTU à fusionner ayant la distance minimale.
     *
     * @param matrice La matrice de distances utilisée pour trouver les clusters à fusionner.
     * @return Une liste contenant les indices des clusters à fusionner. 
     */
	public ArrayList<Integer> trouverClustersAFusionner(MatriceDeDistances matrice) {
		double distanceMin = matrice.trouverDistanceMin();
		ArrayList<Integer> clustersAFusionner = new ArrayList<>(2); 
	    int tailleMatrice = matrice.getTailleMatrice();
	    for (int i = 0; i < tailleMatrice; i++) {
	        for (int j = i + 1; j < tailleMatrice; j++) {
	    	    double valeurMatrice = matrice.getDistance(i, j);
	            if (valeurMatrice == distanceMin) {
	            	clustersAFusionner.add(i);
	                clustersAFusionner.add(j);
	                return clustersAFusionner; // Retourne dès que la paire de clusters est trouvée
	            }
	        }
	    }     
	    return clustersAFusionner; 
	}
	
	/**
     * Fusionne deux clusters en un nouveau cluster pour la méthode UPGMA (Unweighted Pair Group Method with Arithmetic Mean),
     * crée un nouveau noeud pour représenter le cluster fusionné et met à jour la liste des clusters.
     *
     * @param cluster1 L'indice du premier cluster à fusionner dans la liste des clusters.
     * @param cluster2 L'indice du deuxième cluster à fusionner dans la liste des clusters.
     * @param distance La distance entre les deux clusters à fusionner, utilisée pour calculer la distance au nouveau nœud.
     * @return Le nouveau noeud représentant le cluster fusionné.
     */
	public Noeud fusionnerClusters(int cluster1, int cluster2, double distance) {
        Noeud clusterGauche = clusters.get(cluster1);
        Noeud clusterDroit = clusters.get(cluster2);       
        Noeud nouveauCluster = new Noeud(clusterGauche.nom + "/" + clusterDroit.nom);
        double distanceGauche = distance / 2.0 - clusterGauche.getDistance(); 
        System.out.println("distanceGauche = " + distanceGauche);
        double distanceDroit = distance / 2.0 - clusterDroit.getDistance();
        System.out.println("distanceDroit = " + distanceDroit);
        nouveauCluster.ajouterEnfant(clusterGauche, distanceGauche);
        nouveauCluster.ajouterEnfant(clusterDroit, distanceDroit);
        nouveauCluster.setDistance(distance/2);   
        clusters.add(nouveauCluster); 
        clusters.remove(cluster2); 
        clusters.remove(cluster1);
        n--; // Réduit le nombre de taxons     
        System.out.println("Fusion des clusters " + clusterGauche.nom + " et " + clusterDroit.nom + " à une distance de " + distance/2); // Débogage
        System.out.println("Nouveau cluster: " + nouveauCluster.nom);
        afficherClusters(); // débogage    
        return nouveauCluster;
    }

	/**
     * Fusionne deux clusters en un nouveau cluster pour la méthode NJ (Neighbor Joining),
     * crée un nouveau nœud pour représenter le cluster fusionné, et met à jour les distances
     * au nœud de fusion.
     * 
     * @param cluster1 L'indice du premier cluster à fusionner dans la liste des clusters.
     * @param cluster2 L'indice du deuxième cluster à fusionner dans la liste des clusters.
     * @param Siu La distance entre le premier cluster (cluster1) et le nouveau noeud de fusion.
     * @param Sju La distance entre le deuxième cluster (cluster2) et le nouveau noeud de fusion.
     * @return Le nouveau noeud représentant le cluster fusionné.
     */      
	public Noeud fusionnerClusters(int cluster1, int cluster2, double Siu, double Sju) {
        Noeud clusterGauche = clusters.get(cluster1);
        Noeud clusterDroit = clusters.get(cluster2);
        Noeud nouveauCluster = new Noeud(clusterGauche.nom + clusterDroit.nom);       
        nouveauCluster.ajouterEnfant(clusterGauche, Siu); // Ajoute les enfants au nouveau cluster
        nouveauCluster.ajouterEnfant(clusterDroit, Sju); 
        clusters.add(nouveauCluster);
        clusters.remove(cluster2);
        clusters.remove(cluster1);           
        n--; // Réduit le nombre de taxons
        System.out.println("Fusion des clusters " + clusterGauche.nom + " et " + clusterDroit.nom + " pour former " + nouveauCluster.nom); // Débogage
        System.out.println("Nouveau cluster: " + nouveauCluster.nom);
        afficherClusters(); // débogage
        return nouveauCluster;
    }
}