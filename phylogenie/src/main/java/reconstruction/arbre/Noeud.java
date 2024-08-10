package reconstruction.arbre;

import java.util.ArrayList;

public class Noeud {
	public String nom;	
	private ArrayList<Noeud> enfants;
	private double distance;
	
	public Noeud (String n) {
		this.nom = n;
		this.enfants = new ArrayList<>();
		this.distance = 0.0;
	}
	
	// Noeud avec enfants
	//public Noeud (String n, Noeud g, Noeud d, double dist) {
		//this.nom = n;
		//this.gauche = g;
		//this.droit = d;
		//this.distance = dist;
	//}
	
	public void ajouterEnfant(Noeud enfant) {
		enfants.add(enfant);
	}
	
	public String getNom() {
		return nom;
	}
	
	public ArrayList<Noeud> getEnfants() {
		return enfants;
	}
	
	public Double getDistance() {
        return distance;
    }
	
	public void setDistance(double distance) {
        this.distance = distance;
    }
	
	public String toString() {
        StringBuilder sb = new StringBuilder();
        toNewick(sb);
        return sb.toString();
    }
	
	//Construction récursive : La méthode toString est conçue pour être récursive. 
	//Si le nœud est une feuille (pas d'enfants), elle retourne simplement le nom du nœud. 
	//Sinon, elle construit une chaîne qui représente les enfants du nœud entre parenthèses.
	// Si une distance est définie pour le nœud, elle est ajoutée après le nom du nœud avec le format :distance.
	private void toNewick(StringBuilder chaineNewick) {
	        if (enfants.isEmpty()) {
            // Si le nœud est une feuille, retourner le nom du nœud
        	chaineNewick.append(nom);
       } else {
            // Sinon, construire récursivement pour chaque enfant
        	chaineNewick.append("(");
            for (int i = 0; i < enfants.size(); i++) {
            	enfants.get(i).toNewick(chaineNewick); // méthode appelée sur chaque enfant du noeud pour construire la chaine
                if (i < enfants.size()-1) {
                	chaineNewick.append(",");
                }
            }
            chaineNewick.append(")");
        }
        if (distance != 0.0) {
        	chaineNewick.append(":").append(distance);
        }
    }

}
