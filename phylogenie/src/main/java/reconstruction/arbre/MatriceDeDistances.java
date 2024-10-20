package reconstruction.arbre;
import java.util.ArrayList;
import java.util.Locale;

/**
 * La classe mère MatriceDeDistances représente une matrice de distance par un tableau à 2 dimensions et une liste de noms de séquences. 
 * Elle fournit des méthodes pour manipuler la matrice de distance
 */
public class MatriceDeDistances {
	protected double[][] matrice;
	protected ArrayList<String> nomsSequences; 
	
	/**
     * Constructeur par défaut qui initialise une matrice de distance vide.
     */
	public MatriceDeDistances() {
		this.matrice = null;
		this.nomsSequences = new ArrayList<String>() ;
	}
	
	/**
     * Constructeur qui initialise la matrice de distance avec une matrice donnée
     * et une liste de noms de séquences.
     *
     * @param matrice       La matrice de distances.
     * @param nomsSequences  La liste des noms des séquences correspondantes.
     */
	public MatriceDeDistances(double[][] matrice, ArrayList<String> nomsSequences) {
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
		return matrice[i][j];
	}
	
	/**
     * Met à jour la distance entre deux séquences.
     *
     * @param i L'indice de la première séquence.
     * @param j L'indice de la deuxième séquence.
     * @param nouvelleDistance La nouvelle distance à définir.
     */
	public void setDistance (int i, int j, double nouvelleDistance) {
		matrice[i][j] = nouvelleDistance;
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
		return matrice.length;
	}

	 /**
     * Trouve et retourne la distance minimale dans la matrice de distances.
     *
     * @return La distance minimale.
     */
	public double trouverDistanceMin() {
		double distanceMin = Double.MAX_VALUE;
		for (int i = 0; i < matrice.length; i++) {
	        for (int j = i + 1; j < matrice[i].length; j++) {
	            if (matrice[i][j] != 0 && matrice[i][j] < distanceMin) {
	            	distanceMin = matrice[i][j];
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
        for (int i = 0; i < matrice.length; i++) {
            for (int j = 0; j < matrice[i].length; j++) {
            	// Stringformat pour formater la distance avec une précision de 2 chiffres après la virgule %.3f
            	maxLength = Math.max(maxLength, String.format(Locale.US, "%.3f", matrice[i][j]).length());
            }
        }
        // Affichage des noms de séquences en haut
        System.out.print("         "); // espace pour aligner avec les noms de colonnes
       	for (int i = 0; i < matrice.length; i++) {
	       System.out.printf("%" + maxLength + "s ", nomsSequences.get(i));
	   }
	   System.out.println();       	
	   	for (int i = 0; i < matrice.length; i++) { // Affichage de la matrice dans la console avec les noms de lignes
	       	System.out.printf("%-10s", nomsSequences.get(i)); // Nom de la séquence
	       	for (int j = 0; j < matrice[i].length; j++) {
	       		System.out.printf(Locale.US, "%" + maxLength + ".3f" + "|", matrice[i][j]);
	       	}
	       	System.out.println();
	   	}
	}              
	
	/**
     * Diminue la taille de la matrice de distance en supprimant la ligne et la colonne correspondant à l'index spécifié.
     *
     * @param index L'index de la séquence à supprimer.
     */
	public void diminuerTailleMatrice(int index) {
        int taille = matrice.length;
        double[][] nouvelleMatrice = new double[taille - 1][taille - 1];
        ArrayList<String> nouveauxNomsSequences = new ArrayList<>(nomsSequences); // Copie des valeurs de l'ancienne matrice vers la nouvelle matrice       
        int k = 0;
        for (int i = 0; i < taille; i++) {
            if (i == index) continue; // Si vrai saute à i+1
            int l = 0;
            for (int j = 0; j < taille; j++) {
               if (j == index) continue ; // Saute
                nouvelleMatrice[k][l] = matrice[i][j];
                l++;
            }
            k++;
        }      
        this.matrice = nouvelleMatrice; // Mise à jour de la matrice et des noms de séquences
        this.nomsSequences = nouveauxNomsSequences;
    }
	
	/**
     * Ajoute une nouvelle ligne et une nouvelle colonne à la matrice de distances avec les nouvelles distances fournies.
     *
     * @param nouvellesDistances Le tableau contenant les nouvelles distances à ajouter.
     */
	public void ajouterLigneColonne(double[] nouvellesDistances) {
        int tailleActuelle = getTailleMatrice();
        int nouvelleTaille = tailleActuelle + 1;
        double[][] nouvelleMatrice = new double[nouvelleTaille][nouvelleTaille];
        for (int i = 0; i < tailleActuelle; i++) {
            for (int j = 0; j < tailleActuelle; j++) {
                nouvelleMatrice[i][j] = matrice[i][j];
            }
        }
        for (int i = 0; i < tailleActuelle; i++) {
            nouvelleMatrice[i][nouvelleTaille - 1] = nouvellesDistances[i];
            nouvelleMatrice[nouvelleTaille - 1][i] = nouvellesDistances[i];
        }
        matrice = nouvelleMatrice;
    }
}
