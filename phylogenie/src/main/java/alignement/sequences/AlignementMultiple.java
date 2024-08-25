package alignement.sequences;

import java.util.ArrayList;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class AlignementMultiple {

	// Récupère le texte d'un champ de texte, le nettoie, le divise en une liste de numéros d'accession, et retourne cette liste.
	public static ArrayList<String> traiterSaisieNumAccessions(JTextField champTexte) {
			ArrayList<String> numAccessions = new ArrayList<String>();
			if (champTexte != null) {
				String saisie = champTexte.getText().trim();
		        if (!saisie.isEmpty()) {
		            numAccessions = AlignementMultiple.ListeNumAccessions(saisie);		            
		        }
			}
			return numAccessions;
		}
	 
	// Divise une chaîne de numéros d'accessions en une liste.
	public static ArrayList<String> ListeNumAccessions(String numAccessions) {
		 String[] num = numAccessions.trim().split("\\s+");
		 ArrayList<String> listeNumAccession = new ArrayList<String>();
		 if (numAccessions != null && !numAccessions.trim().isEmpty()) {      
			 for (int i = 0; i < num.length; i++) { // Ajouter les numéros d'accession dans l'ArrayList
				 listeNumAccession.add(num[i]);
	         }
		 }
		 return listeNumAccession;
	 }
	            
	// Méthode qui demande à l'utilisateur de saisir le chemin du fichier fasta et crée un objet new File
	public static File traiterCheminFasta (JTextField cheminFasta) {
		 String chemin = cheminFasta.getText().trim();
		 if (chemin.isEmpty()) {
			 JOptionPane.showMessageDialog(null, "Aucun chemin de fichier FASTA saisi.", 
                     "Erreur", JOptionPane.ERROR_MESSAGE);
			 return null;
		 }
		File fichierFasta = new File(chemin);
		if (fichierFasta != null && fichierFasta.exists() && fichierFasta.isFile()) { // Vérifie si le fichier est non-null, existe, et c'est bien un fichier
			 return fichierFasta;
		} else {
			 JOptionPane.showMessageDialog(null,  "Le fichier spécifié n'existe pas ou n'est pas un fichier valide.", 
		     "Erreur", JOptionPane.ERROR_MESSAGE);
		 }
		 return null;
	}
	
	// Méthode qui crée une liste de numéros valides, appel d'une méthode qui vérifie que chaque numéro d'accession est valide
	public static ArrayList<String> validerNumerosAccessions(ArrayList<String> numerosAccessions) { 
	    ArrayList<String> numAccessionsValides = new ArrayList<>();
	    if (numerosAccessions != null && !numerosAccessions.isEmpty()) {
	        for (String num : numerosAccessions) {
	            if (validerNumeroAccession(num)) {
	                numAccessionsValides.add(num);
	            }
	        }
	    }
	    return numAccessionsValides;
	}
		
	// Méthode qui vérifie qu'un numéro d'accession est valide grâce aux méthodes qui vérifient dans les bases de données (Genbank et Uniprot) si l'identifiant est valide
	private static boolean validerNumeroAccession(String num) { 
		boolean isProtein = false;
		boolean numValide = false;
		if (isProtein) {
			numValide = AlignementMultipleProteine.estValideUniprotId(num);
		} else {
			numValide = AlignementMultipleAdn.estValideGenbankId(num);
		}
			return numValide;
	}
		
	// Méthode qui affiche un message d'erreur à l'utilisateur
	public static void gestionException(Exception e, String message) {     
		JOptionPane.showMessageDialog(null, message + "\n" + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
	}
}