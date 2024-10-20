package reconstruction.arbre;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Cette classe représente un nœud d'un arbre, contenant un nom, une liste d'enfants et une distance.
 * Cette classe permet de construire des arbres et de représenter leur structure sous le format de Newick.
 */
public class Noeud {
	public String nom;	
	public ArrayList<Noeud> enfants;
	public double distance;
	
	/**
     * Constructeur d'un nœud avec un nom donné.
     *
     * @param n Le nom du nœud.
     */
	public Noeud (String n) {
		this.nom = n;
		this.enfants = new ArrayList<>();
		this.distance = 0.0;
	}
	
	/**
     * Vérifie si le nœud est une feuille, c'est-à-dire s'il n'a pas d'enfants.
     *
     * @return true si le nœud est une feuille, false sinon.
     */
	public boolean estFeuille() {
		return enfants.isEmpty();
	}
	
	/**
     * Ajoute un enfant à ce nœud.
     *
     * @param enfant Le nœud enfant à ajouter.
     */
	public void ajouterEnfant(Noeud enfant) {
		enfants.add(enfant);
	}
	
	 /**
     * Ajoute un enfant à ce nœud avec une distance spécifiée.
     *
     * @param enfant  Le nœud enfant à ajouter.
     * @param distance La distance à associer à l'enfant.
     */
	public void ajouterEnfant(Noeud enfant, double distance) {
        enfant.setDistance(distance); // Set distance à l'enfant
        enfants.add(enfant);
    }
	
	/**
     * Récupère le nom du nœud.
     *
     * @return Le nom du nœud.
     */
	public String getNom() {
		return nom;
	}
	
	 /**
     * Récupère la liste des enfants de ce nœud.
     *
     * @return Une liste d'enfants de type Noeud.
     */
	public ArrayList<Noeud> getEnfants() {
		return enfants;
	}
	
	/**
     * Récupère la distance associée à ce nœud.
     *
     * @return La distance du nœud.
     */
	public Double getDistance() {
        return distance;
    }
	
	/**
     * Définit la distance pour ce nœud.
     *
     * @param distance La distance à définir.
     */
	public void setDistance(double distance) {
        this.distance = distance;
    }
	
	/**
     * Renvoie une représentation sous forme de chaîne du nœud au format Newick.
     *
     * @return Une chaîne représentant le nœud et sa structure.
     */
	public String toString() {
        StringBuilder sb = new StringBuilder();
        toNewick(sb);
        return sb.toString();
    }
	
	/**
     * Construction récursive de la représentation Newick du nœud.
     * Si le nœud est une feuille (pas d'enfants), retourne simplement le nom du nœud.
     * Sinon, construit une chaîne qui représente les enfants du nœud entre parenthèses.
     * Si une distance est définie pour le nœud, elle est ajoutée après le nom du nœud au format : distance.
     *
     * @param chaineNewick Le StringBuilder utilisé pour construire la chaîne Newick.
     */
	private void toNewick(StringBuilder chaineNewick) {
	        if (enfants.isEmpty()) {  // Si le nœud est une feuille, retourner le nom du nœud          
        	chaineNewick.append(nom);
       } else {  
        	chaineNewick.append("("); // Sinon, construire récursivement pour chaque enfant
            for (int i = 0; i < enfants.size(); i++) {
            	enfants.get(i).toNewick(chaineNewick); // Méthode appelée sur chaque enfant du noeud pour construire la chaine
                if (i < enfants.size()-1) {
                	chaineNewick.append(",");
                }
            }
            chaineNewick.append(")");
        }    
        if (distance > 0.0) { // Ajouter la distance au noeud si elle est définie
        	chaineNewick.append(":").append(String.format(Locale.US, "%.3f", distance));
        }
    }
}
