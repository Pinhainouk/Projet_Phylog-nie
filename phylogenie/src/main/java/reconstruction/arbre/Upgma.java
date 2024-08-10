package reconstruction.arbre;
import java.util.ArrayList;

public class Upgma {
	private MatriceDeDistance matrice;
	private ArrayList<Noeud> clusters;
	private int n; // nombre de clusters/séquences actuel

	public Upgma(MatriceDeDistance m) {
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
	
	// Méthode qui fusionne les clusters, crée un nouveau noeud pour le cluster met à jour la liste des clusters
	private Noeud fusionnerClusters(int cluster1, int cluster2, double distance) {
        Noeud clusterGauche = clusters.get(cluster1);
        Noeud clusterDroit = clusters.get(cluster2);
        Noeud nouveauCluster = new Noeud(clusterGauche.nom + clusterDroit.nom);
        
        // pour pouvoir ajouter au format de Newick
        nouveauCluster.ajouterEnfant(clusterGauche);
        nouveauCluster.ajouterEnfant(clusterDroit);
        nouveauCluster.setDistance(distance / 2.0); // Diviser par 2 pour la distance correcte
        
        clusters.set(cluster1, nouveauCluster);
        clusters.remove(cluster2);
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
	
	// Calcule la distance moyenne entre le nouveau cluster et les autres clusters
    private double[] calculerNouvellesDistances(int cluster1, int cluster2) {
        double[] nouvellesDistances = new double[n];
        int index = 0;
        for (int i = 0; i < n; i++) {
            if (i != cluster1 && i != cluster2) { // garantit que l'on n'inclut pas les distances des clusters fusionnés
                nouvellesDistances[index] = (matrice.getDistance(cluster1, i) + matrice.getDistance(cluster2, i)) / 2.0;
                index++;
            }
        }
        // Débogage
        System.out.println("Nouvelles distances après fusion des clusters " + cluster1 + " et " + cluster2 + ":");
        for (int i = 0; i < nouvellesDistances.length; i++) {
            System.out.print(nouvellesDistances[i] + " ");
        }
        System.out.println();       
        return nouvellesDistances;
    }
	
    // Méthode qui met à jour la matrice en supprimant lignes et colonnes des clusters fusionnés et ajoute une nouvelle ligne et colonne pour le nouveau cluster
	private void majMatrice(int clusterA, int clusterB, double[] nouvellesDistances) {
		matrice.diminuerTailleMatrice(clusterB);
		matrice.diminuerTailleMatrice(clusterA);
		matrice.ajouterLigneColonne(nouvellesDistances);
		System.out.println("Matrice après mise à jour:");
	    matrice.afficherMatrice();
	}
	   
    // Méthode qui exécute l'algo UPGMA, fusionne les clusters jusqu'à ce qu'il ne reste qu'un seul cluster (racine de l'arbre)
    public Noeud demarreUpgma() {
        while (n > 1) {
            int[] clustersAFusionner = matrice.trouverClustersAFusionner();
            int cluster1 = clustersAFusionner[0];
            int cluster2 = clustersAFusionner[1];
            double distance = matrice.getDistance(cluster1, cluster2);
            fusionnerClusters(cluster1, cluster2, distance); // n diminue ici après la fusion
            double[] nouvellesDistances = calculerNouvellesDistances(cluster1, cluster2); 
            majMatrice(cluster1, cluster2, nouvellesDistances);
        }
        return clusters.get(0); // Le noeud restant est la racine de l'arbre
    }
    
    // Méthode pour générer le format Newick à partir de l'arbre UPGMA construit
    public String genererNewick() {
        Noeud racineArbre = demarreUpgma(); // On obtient la racine de l'arbre UPGMA
        return racineArbre.toString() + ";";
    }
}