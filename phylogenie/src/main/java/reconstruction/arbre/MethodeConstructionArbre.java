package reconstruction.arbre;

import java.util.ArrayList;

public class MethodeConstructionArbre {
	protected MatriceDeDistance matrice;
	protected ArrayList<Noeud> clusters;
	protected int n; // nombre de clusters/séquences actuel
	
	public MethodeConstructionArbre(MatriceDeDistance m) {
		this.matrice = m;
	    initialiserClusters();
	    this.n = matrice.getTailleMatrice();
	}
	
	// Méthode qui initialise les clusters à partir des séquences
	private void initialiserClusters() {
		clusters = new ArrayList<>();
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
	
	// Méthode qui fusionne les clusters, crée un nouveau noeud pour le cluster met à jour la liste des clusters
		public Noeud fusionnerClusters(int cluster1, int cluster2, double distance) {
	        Noeud clusterGauche = clusters.get(cluster1);
	        Noeud clusterDroit = clusters.get(cluster2);
	        Noeud nouveauCluster = new Noeud(clusterGauche.nom + clusterDroit.nom);
	        
	        // crée noeud enfant et la distance = taille de la branche
	        nouveauCluster.ajouterEnfant(clusterGauche);
	        nouveauCluster.ajouterEnfant(clusterDroit);
	        nouveauCluster.setDistance(distance / 2.0); // Diviser par 2 pour la distance correcte
	        // Mettre à jour la liste des clusters
	        clusters.set(cluster1, nouveauCluster);
	        clusters.remove(cluster2);
            // Réduire le nombre de clusters
	        n--;
	        ArrayList<String> nouveauxNomsSequences = new ArrayList<>(matrice.getNomsSequences());
	        nouveauxNomsSequences.set(cluster2, nouveauCluster.nom);
	        nouveauxNomsSequences.remove(cluster1);
	        for (int i = 0; i < nouveauxNomsSequences.size(); i++) {
	   	        matrice.setNomsSequences(i, nouveauxNomsSequences.get(i));
	        }
	        // Débogage
	        System.out.println("Fusion des clusters " + clusterGauche.nom + " et " + clusterDroit.nom + " à une distance de " + distance);
	        System.out.println("Nouveau cluster: " + nouveauCluster.nom);
	        return nouveauCluster;
	    }
}

