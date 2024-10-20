package reconstruction.arbre;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;

/**
 * La classe FichierImageArbre est utilisée pour générer l'image SVG représentant l'arbre phylogénétique 
 * à partir des algorithmes UPGMA ou NJ (Neighbor Joining).
 * Elle exécute un script R pour créer l'image et ouvre automatiquement le fichier SVG généré.
 */
public class FichierImageArbre {

	/**
     * Constructeur pour FichierImageArbre.
     * 
     * @param f Une instance de la classe FichierFormatNewick qui contient le format Newick à utiliser.
     */
	public FichierImageArbre(FichierFormatNewick f) {
    }

	/**
     * Méthode pour générer un fichier SVG représentant l'arbre UPGMA en appelant un script R.
     * 
     * @param cheminFichierNewick Le chemin du fichier Newick généré précédemment.
     * @param cheminFichierSvg Le chemin où le fichier SVG sera sauvegardé.
     */
    public void genererImageArbre(String cheminFichierNewick, String cheminFichierSvg) { 
        try {
            String cheminScriptR = "/home/elodie/Documents/Projet_tuteure_phylogenie/Script_root.R"; // Le chemin absolu du script R          
            String nomFichierNewick = Paths.get(cheminFichierNewick).getFileName().toString().replaceFirst("[.][^.]+$", ""); // Extrait le nom du fichier sans le chemin et l'extension
            String[] commande = new String[] {"Rscript", cheminScriptR, cheminFichierNewick, cheminFichierSvg, nomFichierNewick}; // Commande pour exécuter le script R
            System.out.println("Chemin du fichier Newick : " + cheminFichierNewick); // Debogage
            System.out.println("Chemin du fichier SVG : " + cheminFichierSvg); // Debogage         
            Process process = Runtime.getRuntime().exec(commande); // Exécution de la commande       
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream())); // Lire la sortie du script R
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                System.out.println(ligne);
            }         
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream())); //Lire la sortie d'erreur du script R
            String ligneErreur;
            while ((ligneErreur = errorReader.readLine()) != null) {
                System.err.println(ligneErreur);
            }          
            int exitCode = process.waitFor(); // Vérifier si le processus s'est terminé correctement
            if (exitCode == 0) {
                System.out.println("Fichier SVG généré : " + cheminFichierSvg);                
                File fichierSvg = new File(cheminFichierSvg); 
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(fichierSvg); // Ouvrir le fichier SVG automatiquement
                    System.out.println("Fichier SVG ouvert : " + cheminFichierSvg); // Debogage
                } else {
                    System.err.println("L'ouverture automatique n'est pas supportée sur ce système.");
                }
            } else {
               System.err.println("Erreur lors de l'exécution du script R.");
            }

        } catch (IOException | InterruptedException e) {
           e.printStackTrace();
        }
    }
}