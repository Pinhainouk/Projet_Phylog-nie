package reconstruction.arbre;

import java.util.ArrayList;

public class MatriceDeDistance {
	protected double[][] matrice;
	protected ArrayList<String> nomsSequences; // voir pour changer en String[][]
	
	public MatriceDeDistance(double[][] distances, ArrayList<String> noms) {
		this.matrice = distances;
		this.nomsSequences = noms;
	}
	
	public double getDistance(int i, int j) {
		return matrice[i][j];
	}
	
	public void setDistance (int i, int j, double nouvelleDistance) {
		matrice[i][j] = nouvelleDistance;
	}
	
	public ArrayList<String> getNomsSequences() {
		return nomsSequences;
	}
	
	public String getNomSequence(int i) {
		return nomsSequences.get(i);
	}
	
	public void setNomsSequences(int i, String nouveauNom) {
		nomsSequences.set(i, nouveauNom);
	}
	
	public int getTailleMatrice() {
		return matrice.length;
	}

	// Méthode pour trouver la distance minimale dans la matrice
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
	
	// Méthode pour afficher les valeurs de la matrice
	public void afficherMatrice() {
		int maxLength = 0;
        for (int i = 0; i < matrice.length; i++) {
            for (int j = 0; j < matrice[i].length; j++) {
            	// Stringformat pour formater la distance avec une précision d'un chiffre après la virgule %.1f
            	maxLength = Math.max(maxLength, String.format("%.1f", matrice[i][j]).length());
	       }
	   }
     // Affichage des noms de séquences en haut
        System.out.print("   "); // espace pour aligner avec les noms de colonnes
        for (int i = 0; i < matrice.length; i++) {
	        System.out.printf("%" + maxLength + "s ", nomsSequences.get(i));
	    }
	    System.out.println();    
	    // Affichage de la matrice avec les noms de lignes
	    for (int i = 0; i < matrice.length; i++) {
	        System.out.printf("%-10s", nomsSequences.get(i)); // nom de la séquence
	        for (int j = 0; j < matrice[i].length; j++) {
	            System.out.printf("%" + maxLength + ".1f" + "|", matrice[i][j]);
	        }
	        System.out.println();
	    }
	}              
	
	// Méthode qui diminue la taille de la matrice
	public void diminuerTailleMatrice(int index) {
        int taille = matrice.length;
        double[][] nouvelleMatrice = new double[taille - 1][taille - 1];
        ArrayList<String> nouveauxNomsSequences = new ArrayList<>(nomsSequences);
        // Copie des valeurs de l'ancienne matrice vers la nouvelle matrice
        int k = 0;
        for (int i = 0; i < taille; i++) {
            if (i == index) continue; //si vrai saute à i+1
            int l = 0;
            for (int j = 0; j < taille; j++) {
                if (j == index) continue ; //saute
                nouvelleMatrice[k][l] = matrice[i][j];
                l++;
            }
            k++;
        }
        // Mise à jour de la matrice et des noms de séquences
        this.matrice = nouvelleMatrice;
        this.nomsSequences = nouveauxNomsSequences;
    }
	
	// Méthode pour enlever un cluster à un index donné en paramètre = diminuerTailleMatrice
		public void removeCluster(int index) {
	        for (int i = index; i < matrice.length - 1; i++) {
	            for (int j = 0; j < matrice.length; j++) {
	            	matrice[i][j] = matrice[i + 1][j];
	            }
	        }
	        for (int i = 0; i < matrice.length; i++) {
	            for (int j = index; j < matrice.length - 1; j++) {
	            	matrice[i][j] = matrice[i][j + 1];
	            }
	        }
	        double[][] newDistances = new double[matrice.length - 1][matrice.length - 1];
	        for (int i = 0; i < newDistances.length; i++) {
	            for (int j = 0; j < newDistances.length; j++) {
	                newDistances[i][j] = matrice[i][j];
	            }
	        }
	        matrice = newDistances;
	        nomsSequences.remove(index);
	    }
	
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