package reconstruction.arbre;

import java.util.ArrayList;

public class MethodeConstructionArbre {
	protected MatriceDeDistance matrice;
	protected ArrayList<Noeud> clusters;
	protected int n; // nombre actuel de clusters/séquences
	
	public MethodeConstructionArbre(MatriceDeDistance m) {
		this.matrice = m;
	    initialiserClusters();
	    this.n = matrice.getTailleMatrice();
	}
	
	// Méthode qui initialise les clusters à partir des séquences
	private void initialiserClusters() {
		this.clusters = new ArrayList<>();
		for (int i = 0; i < matrice.getNomsSequences().size(); i++) {
			String nom = matrice.getNomsSequences().get(i);
			clusters.add(new Noeud(nom));		
		}
	}
	
	// Méthode pour trouver les indices des clusters/séquences/taxons à fusionner avec la distance minimale
	public int[] trouverClustersAFusionner(MatriceDeDistance matrice) {
		double distanceMin = matrice.trouverDistanceMin();
	    int[] clustersAFusionner = new int[2]; 
	    int tailleMatrice = matrice.getTailleMatrice();
	    for (int i = 0; i < tailleMatrice; i++) {
	        for (int j = i + 1; j < tailleMatrice; j++) {
	    	    double valeurMatrice = matrice.getDistance(i, j);
	            if (valeurMatrice == distanceMin) {
	            	clustersAFusionner[0] = i;
	                clustersAFusionner[1] = j;
	                return clustersAFusionner; // retourne dès que la paire de clusters est trouvée
	            }
	        }
	    }     
	    return clustersAFusionner; // retourne un tableau avec [0,0] si aucune paire de clusters n'est trouvée
	}
}