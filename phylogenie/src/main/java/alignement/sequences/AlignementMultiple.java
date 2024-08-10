package alignement.sequences;

import java.util.ArrayList;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class AlignementMultiple {

	// Méthode qui demande à l'utilisateur de saisir des numéros d'accession et les range dans une ArrayList
	 
	 public static ArrayList<String> traiterSaisieNumAccessions(JTextField champTexte) {
			ArrayList<String> numAccessions = new ArrayList<String>();
			if (champTexte != null) {
				String saisie = champTexte.getText().trim();
		        if (!saisie.isEmpty()) {
		            numAccessions = AlignementMultiple.ListeNumAccessions(saisie);
		        } else {
		            JOptionPane.showMessageDialog(null, "Aucun numéro d'accession saisi.", 
		                "Erreur", JOptionPane.ERROR_MESSAGE);
		        }
			}
			return numAccessions;
		}
	 
	 public static ArrayList<String> ListeNumAccessions(String numAccessions) {
		 String[] num = numAccessions.trim().split("\\s+");
		 ArrayList<String> listeNumAccession = new ArrayList<String>();
		 if (numAccessions != null && !numAccessions.trim().isEmpty()) {
	            // Ajouter les numéros d'accession dans l'ArrayList
			 for (int i = 0; i < num.length; i++) {
				 listeNumAccession.add(num[i]);
	         }
		 }
		 return listeNumAccession;
	 }
	            
	// Méthode qui demande à l'utilisateur de saisir le chemin du fichier fasta et crée un objet new File
	public static File traiterCheminFasta (JTextField cheminFasta) {
		 String chemin = cheminFasta.getText();
		 if (chemin != null && !chemin.trim().isEmpty()) {	
			 File fichierFasta = new File(chemin.trim());
			 return fichierFasta;
		} else {
			 JOptionPane.showMessageDialog(null, "Aucun chemin de fichier FASTA saisi.", 
		     "Erreur", JOptionPane.ERROR_MESSAGE);
		 }
		 return null;
	}
}