package reconstruction.arbre;

import java.io.FileWriter;
import java.io.IOException;

public class FichierFormatNewick {
	private String formatNewick;
	
	public FichierFormatNewick() {
		this.formatNewick = "";
    }
	
	public String getFormatNewick() {
		return formatNewick;
	}
	
	public void setFormatNewick(String nouveauFormatNewick) {
		formatNewick = nouveauFormatNewick;
	}
	
	// Méthode pour générer le format Newick à partir de l'arbre
    public String genererNewick(AlgoUpgma u) {
        Noeud racineArbre = null;
        if (u != null) {
            racineArbre = u.executerUpgma(); // On obtient la racine de l'arbre UPGMA
            formatNewick = racineArbre.toString() + ";"; 
        } else {
            throw new IllegalStateException("UPGMA n'est pas initialisé.");
        }
    	return formatNewick;
    }
    
    public String genererNewick(AlgoNj n) {
        Noeud racineArbre = null;
        if (n != null) {
            racineArbre = n.executerNJ(); // On obtient la racine de l'arbre NJ
            formatNewick = racineArbre.toString() + ";";
        } else {
            throw new IllegalStateException("NJ n'est pas initialisé.");
        }
        return formatNewick;
    }
    
    // Méthode pour générer et écrire le format Newick dans un fichier
    public void genererFichierNewick(String cheminFichier) {
    	if (formatNewick == null || formatNewick.isEmpty()) {
            System.out.println("Le format Newick est vide, impossible d'écrire dans le fichier.");
            return;
    	}
        // Écrire la chaîne Newick dans le fichier
        try (FileWriter fichierNewick = new FileWriter(cheminFichier)) {
        	fichierNewick.write(formatNewick);
            System.out.println("Arbre en format Newick écrit dans le fichier : " + cheminFichier);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}