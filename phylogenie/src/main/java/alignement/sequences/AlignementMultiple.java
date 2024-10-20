package alignement.sequences;
import java.util.ArrayList;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * La classe mère AlignementMultiple fournit des méthodes utilitaires communes aux classes filles AlignementMultipleProteine et AlignementMultipleAdn pour :
 * traiter et vérifier la validité des numéros d'accession, 
 * traiter et vérifier la validité des chemins de fichiers FASTA,
 * afficher des messages d'erreur à l'utilisateur
 */
public class AlignementMultiple {

	/**
     * Récupère le texte d'un champ de texte, le nettoie, le divise en une liste de numéros
     * d'accession, et retourne cette liste.
     *
     * @param champTexte Le champ de texte contenant les numéros d'accession.
     * @return Une liste de numéros d'accession traités.
     */
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
	 
	/**
     * Divise une chaîne de caractères de numéros d'accession en une liste.
     * 
     * @param numAccessions La chaîne de caractère contenant les numéros d'accession à diviser.
     * @return Une liste de numéros d'accession.
     */
	public static ArrayList<String> ListeNumAccessions(String numAccessions) {
		 String[] num = numAccessions.trim().split("\\s+");
		 ArrayList<String> listeNumAccession = new ArrayList<String>();
		 if (numAccessions != null && !numAccessions.trim().isEmpty()) {      
			 for (int i = 0; i < num.length; i++) { 
				 listeNumAccession.add(num[i]);
	         }
		 }
		 return listeNumAccession;
	 }
	            
	/**
     * Traite le chemin du fichier FASTA saisi dans un champ de texte et crée un
     * objet File.
     *
     * @param cheminFasta Le champ de texte contenant le chemin du fichier FASTA.
     * @return Un objet File correspondant au fichier FASTA ou null si le fichier n'existe pas ou n'est pas valide.
     */
	public static File traiterCheminFasta (JTextField cheminFasta) {
		 String chemin = cheminFasta.getText().trim();
		 if (chemin.isEmpty()) {
			 JOptionPane.showMessageDialog(null, "Aucun chemin de fichier FASTA saisi.", 
                     "Erreur", JOptionPane.ERROR_MESSAGE);
			 return null;
		 }
		File fichierFasta = new File(chemin);
		if (fichierFasta != null && fichierFasta.exists() && fichierFasta.isFile()) { // Vérifie si le fichier est non-null, existe, et que c'est bien un fichier
			 return fichierFasta;
		} else {
			 JOptionPane.showMessageDialog(null,  "Le fichier spécifié n'existe pas ou n'est pas un fichier valide.", 
		     "Erreur", JOptionPane.ERROR_MESSAGE);
		 }
		 return null;
	}
	
	/**
     * Valide une liste de numéros d'accession, en appelant une méthode qui vérifie la validité de chaque numéro d'accession.
     *
     * @param numerosAccessions La liste des numéros d'accession à valider.
     * @return Une liste de numéros d'accession valides.
     */
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
		
	/**
     * Vérifie si un numéro d'accession est valide en consultant les bases de données (GenBank et UniProt).
     *
     * @param num Le numéro d'accession à valider.
     * @return true si le numéro est valide, false sinon.
     */
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
		
	/**
     * Affiche un message d'erreur à l'utilisateur avec les détails de l'exception.
     *
     * @param e L'exception à afficher.
     * @param message Un message complémentaire à afficher avec l'exception.
     */
	public static void gestionException(Exception e, String message) {     
		JOptionPane.showMessageDialog(null, message + "\n" + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
	}
}