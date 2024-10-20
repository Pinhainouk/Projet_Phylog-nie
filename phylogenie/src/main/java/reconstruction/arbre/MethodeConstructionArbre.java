package reconstruction.arbre;
import java.util.ArrayList;

/**
 * La Classe mère MethodeConstructionArbre représente une méthode de construction d'un arbre phylogénétique à partir d'une matrice de distances.
 * Elle gère l'initialisation des clusters à partir des séquences et la recherche des clusters à fusionner.
 */
public class MethodeConstructionArbre {
	protected MatriceDeDistances matrice;
	protected ArrayList<Noeud> clusters;
	protected int n; // Nombre actuel de séquences/OTU
	
	/**
     * Constructeur de la classe MethodeConstructionArbre.
     *
     * @param m La matrice de distances à utiliser pour la construction de l'arbre.
     */
	public MethodeConstructionArbre(MatriceDeDistances m) {
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
	}
	
	/**
     * Méthode pour trouver les indices des clusters/séquences/otu à fusionner ayant la distance minimale.
     *
     * @param matrice La matrice de distances utilisée pour trouver les clusters à fusionner.
     * @return Un tableau d'entiers de taille 2 contenant les indices des clusters à fusionner. 
     *         Retourne [0, 0] si aucune paire de clusters n'est trouvée.
     */
	public int[] trouverClustersAFusionner(MatriceDeDistances matrice) {
		double distanceMin = matrice.trouverDistanceMin();
	    int[] clustersAFusionner = new int[2]; 
	    int tailleMatrice = matrice.getTailleMatrice();
	    for (int i = 0; i < tailleMatrice; i++) {
	        for (int j = i + 1; j < tailleMatrice; j++) {
	    	    double valeurMatrice = matrice.getDistance(i, j);
	            if (valeurMatrice == distanceMin) {
	            	clustersAFusionner[0] = i;
	                clustersAFusionner[1] = j;
	                return clustersAFusionner; // Retourne dès que la paire de clusters est trouvée
	            }
	        }
	    }     
	    return clustersAFusionner; 
	}
}