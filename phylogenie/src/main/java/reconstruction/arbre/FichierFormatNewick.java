package reconstruction.arbre;
import java.io.FileWriter;
import java.io.IOException;

/**
 * La classe FichierFormatNewick est utilisée pour générer et stocker 
 * un arbre au format Newick. Elle permet également de sauvegarder cet arbre 
 * dans un fichier.
 */
public class FichierFormatNewick {
	private String formatNewick;  // Attribut qui contient la chaîne représentant l'arbre au format Newick
	
	/**
     * Constructeur par défaut. Initialise la chaîne formatNewick à une chaîne vide.
     */
	public FichierFormatNewick() {
		this.formatNewick = "";
    }
	
	/**
     * Retourne la chaîne représentant l'arbre au format Newick.
     * 
     * @return la chaîne formatNewick.
     */
	public String getFormatNewick() {
		return formatNewick;
	}
	
	/**
     * Modifie la chaîne représentant l'arbre au format Newick.
     * 
     * @param nouveauFormatNewick La nouvelle chaîne à utiliser pour le format Newick.
     */
	public void setFormatNewick(String nouveauFormatNewick) {
		formatNewick = nouveauFormatNewick;
	}
	
	/**
     * Génère le format Newick à partir de l'arbre obtenu via l'algorithme UPGMA.
     * 
     * @param u Une instance de l'algorithme AlgoUpgma.
     * @return la chaîne représentant l'arbre au format Newick.
     * @throws IllegalStateException si l'algorithme UPGMA n'est pas initialisé.
     */
    public String genererNewick(AlgoUpgma u) {
        Noeud arbre = null;
        if (u != null) {
            arbre = u.executerUpgma(); // On obtient la racine de l'arbre UPGMA
            formatNewick = arbre.toString() + ";"; 
        } else {
            throw new IllegalStateException("UPGMA n'est pas initialisé.");
        }
    	return formatNewick;
    }
    
    /**
     * Génère le format Newick à partir de l'arbre obtenu via l'algorithme NJ (Neighbor Joining).
     * 
     * @param n Une instance de l'algorithme AlgoNj.
     * @return la chaîne représentant l'arbre au format Newick.
     * @throws IllegalStateException si l'algorithme NJ n'est pas initialisé.
     */
    public String genererNewick(AlgoNj n) {
        Noeud arbre = null;
        if (n != null) {
            arbre = n.executerNJ(); 
            formatNewick = arbre.toString() + ";";
        } else {
            throw new IllegalStateException("NJ n'est pas initialisé.");
        }
        return formatNewick;
    }
    
    /**
     * Ecrit la chaine Newick dans un fichier donné.
     * 
     * @param cheminFichier Le chemin vers le fichier où l'arbre sera sauvegardé.
     * @throws IOException si une erreur survient lors de l'écriture dans le fichier.
     */
    public void genererFichierNewick(String cheminFichier) {
    	if (formatNewick == null || formatNewick.isEmpty()) {
            System.out.println("Le format Newick est vide, impossible d'écrire dans le fichier.");
            return;
    	}    
        try (FileWriter fichierNewick = new FileWriter(cheminFichier)) { // Écrire la chaîne Newick dans le fichier
        	fichierNewick.write(formatNewick);
            System.out.println("Arbre en format Newick écrit dans le fichier : " + cheminFichier);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}