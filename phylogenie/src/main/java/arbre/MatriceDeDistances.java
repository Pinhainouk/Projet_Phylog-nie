package arbre;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

/**
 * La classe mère MatriceDeDistances représente une matrice de distance par un tableau à 2 dimensions et une liste de noms de séquences. 
 * Elle fournit des méthodes pour manipuler la matrice de distance
 */
public class MatriceDeDistances {
	protected ArrayList<ArrayList<Double>> matrice;
	protected ArrayList<String> nomsSequences; 
	
	/**
     * Constructeur par défaut qui initialise une matrice de distance vide.
     */
	public MatriceDeDistances() {
		this.matrice = null;
		this.nomsSequences = null ;
	}
	
	/**
     * Constructeur qui initialise la matrice de distance avec une matrice donnée
     * et une liste de noms de séquences.
     *
     * @param matrice       La matrice de distances.
     * @param nomsSequences  La liste des noms des séquences correspondantes.
     */
	public MatriceDeDistances(ArrayList<ArrayList<Double>> matrice, ArrayList<String> nomsSequences) {
	    this.matrice = matrice;
	    this.nomsSequences = nomsSequences;
	}
	
	/**
     * Récupère la distance entre deux séquences spécifiées par leurs indices.
     *
     * @param i L'indice de la première séquence.
     * @param j L'indice de la deuxième séquence.
     * @return La distance entre les deux séquences.
     */
	public double getDistance(int i, int j) {
		return matrice.get(i).get(j);
	}
	
	/**
     * Met à jour la distance entre deux séquences.
     *
     * @param i L'indice de la première séquence.
     * @param j L'indice de la deuxième séquence.
     * @param nouvelleDistance La nouvelle distance à définir.
     */
	public void setDistance (int i, int j, double nouvelleDistance) {
		matrice.get(i).set(j, nouvelleDistance);
	}
	
	/**
     * Récupère la liste des noms de séquences.
     *
     * @return La liste des noms de séquences.
     */
	public ArrayList<String> getNomsSequences() {
		return nomsSequences;
	}
	
	/**
     * Récupère le nom d'une séquence à un index donné.
     *
     * @param i L'indice de la séquence.
     * @return Le nom de la séquence.
     */
	public String getNomSequence(int i) {
		return nomsSequences.get(i);
	}
	
	/**
     * Met à jour le nom d'une séquence à un index donné.
     *
     * @param i L'indice de la séquence.
     * @param nouveauNom Le nouveau nom de la séquence.
     */
	public void setNomsSequences(int i, String nouveauNom) {
		nomsSequences.set(i, nouveauNom);
	}
	
	/**
     * Récupère la taille de la matrice.
     *
     * @return La taille de la matrice (nombre de lignes/colonnes).
     */
	public int getTailleMatrice() {
		return matrice.size();
	}

	 /**
     * Trouve et retourne la distance minimale dans la matrice de distances.
     *
     * @return La distance minimale.
     */
	public double trouverDistanceMin() {
		double distanceMin = Double.MAX_VALUE;
		for (int i = 0; i < matrice.size(); i++) {
	        for (int j = i + 1; j < matrice.get(i).size(); j++) {
	            if (matrice.get(i).get(j) != 0 && matrice.get(i).get(j) < distanceMin) {
	            	distanceMin = matrice.get(i).get(j);
	            }
	        }
		}
	    return distanceMin;
	}
	
	/**
     * Affiche la matrice de distances de manière formatée dans la console.
     */
	public void afficherMatrice() {
		int maxLength = 0;
        for (int i = 0; i < matrice.size(); i++) {
            for (int j = 0; j < matrice.get(i).size(); j++) {
            	// Stringformat pour formater la distance avec une précision de 3 chiffres après la virgule %.3f
            	maxLength = Math.max(maxLength, String.format(Locale.US, "%.3f", matrice.get(i).get(j)).length());
            }
        }
        // Affichage des noms de séquences en haut
        System.out.print("         "); // espace pour aligner avec les noms de colonnes
       	for (int i = 0; i < matrice.size(); i++) {
	       System.out.printf("%" + maxLength + "s ", nomsSequences.get(i));
       	}
       	System.out.println();       	
	   	for (int i = 0; i < matrice.size(); i++) { // Affichage de la matrice dans la console avec les noms de lignes
	   		if (i < nomsSequences.size()) {
	   			System.out.printf("%-10s", nomsSequences.get(i)); // Nom de la séquence
	   			for (int j = 0; j < matrice.get(i).size(); j++) {
	   				System.out.printf(Locale.US, "%" + maxLength + ".3f" + "|", matrice.get(i).get(j));
	   			}
	   			System.out.println();
	   		}
	   	}
	}
	
	/**
	 * Supprime un cluster de la matrice de distances et de la liste des noms de séquences.
	 * Cette opération consiste à retirer la ligne et la colonne correspondantes dans la matrice,
	 * ainsi que le nom du cluster dans la liste des noms de séquences.
	 *
	 * @param index L'indice du cluster à supprimer dans la matrice de distances et dans la liste des noms.
	 */
	public void enleverCluster(int index) {
	    // Supprime la ligne `index` de la matrice
	    matrice.remove(index);    
	    // Supprime la colonne `index` de chaque ligne restante
	    for (int i = 0; i < matrice.size(); i++) {
	        matrice.get(i).remove(index);
	    }
	    // Supprime le nom de séquence correspondant dans `nomsSequences`
	    if (index < nomsSequences.size()) {
	        nomsSequences.remove(index);
	    }
	}
	
	/**
	 * Ajoute une nouvelle ligne et colonne à la matrice de distances pour intégrer un nouveau cluster.
	 * Les nouvelles distances entre le nouveau cluster et les clusters existants sont spécifiées 
	 * dans la liste `nouvellesDistances`.
	 *
	 * @param nouvellesDistances Une liste contenant les distances entre le nouveau cluster et les clusters existants.
	 *                           L'index de chaque élément de la liste correspond au cluster existant avec lequel la distance
	 *                           est mesurée.
	 */
	public void ajouterLigneColonne(ArrayList<Double> nouvellesDistances) {
	    int tailleActuelle = matrice.size(); // Récupère la taille actuelle de la matrice
	    int nouvelleTaille = tailleActuelle + 1; // Nouvelle taille après ajout d'une ligne et d'une colonne   
	    ArrayList<ArrayList<Double>> nouvelleMatrice = new ArrayList<>(nouvelleTaille); // Créer la nouvelle matrice avec ArrayList    
	    for (int i = 0; i < nouvelleTaille; i++) { // Initialiser la nouvelle matrice
	        nouvelleMatrice.add(new ArrayList<>(Collections.nCopies(nouvelleTaille, 0.0))); // Initialise à 0.0
	    }    
	    for (int i = 0; i < tailleActuelle; i++) { // Copier les distances existantes dans la nouvelle matrice
	        for (int j = 0; j < tailleActuelle; j++) {
	            nouvelleMatrice.get(i).set(j, matrice.get(i).get(j)); // Copie les anciennes valeurs
	        }
	    }   
	    for (int i = 0; i < tailleActuelle; i++) {  // Ajouter la nouvelle colonne et ligne de distances
	        nouvelleMatrice.get(i).set(nouvelleTaille - 1, nouvellesDistances.get(i)); // Ajoute nouvelle colonne
	        nouvelleMatrice.get(nouvelleTaille - 1).set(i, nouvellesDistances.get(i)); // Ajoute nouvelle ligne
	    }
	    nouvelleMatrice.get(nouvelleTaille - 1).set(nouvelleTaille - 1, 0.0); // Distance à soi-même est 0 
	    matrice = nouvelleMatrice;  // Mettre à jour la matrice existante avec la nouvelle matrice
	}
	
	/**
	 * Met à jour la matrice de distances après la fusion de deux clusters. 
	 * Supprime les clusters fusionnés, ajoute une nouvelle ligne et colonne pour représenter le nouveau cluster 
	 * avec les distances mises à jour, et ajoute le nom du nouveau cluster dans la liste des noms de séquences.
	 *
	 * @param cluster1 L'indice du premier cluster à fusionner dans la matrice.
	 * @param cluster2 L'indice du deuxième cluster à fusionner dans la matrice.
	 * @param nouvellesDistances Une liste contenant les distances du nouveau cluster aux autres clusters restants.
	 * @param nouveauNomCluster Le nom du nouveau cluster résultant de la fusion.
	 */
	public void majMatrice(int cluster1, int cluster2, ArrayList<Double> nouvellesDistances, String nouveauNomCluster) { 
		 if (cluster1 < cluster2) {
			 enleverCluster(cluster2);
			 enleverCluster(cluster1);
		 } else {
			enleverCluster(cluster1);
	        enleverCluster(cluster2);
	    }
		ajouterLigneColonne(nouvellesDistances);
		nomsSequences.add(nouveauNomCluster);
		System.out.println("Affichage de la matrice mise à jour: ");
		afficherMatrice();
		}
	}