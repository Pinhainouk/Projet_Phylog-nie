package reconstruction.arbre;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;

public class FichierImageArbre {
	//private FichierFormatNewick fichierNewick;

	public FichierImageArbre(FichierFormatNewick f) {
        //this.fichierNewick = f;
    }

	// Méthode pour générer le fichier Newick et l'image SVG
    public void genererImageArbreUpgma(String cheminFichierNewick, String cheminFichierSvg) {
        try {
            // Construire le chemin complet du script R
            String cheminScriptR = "/home/elodie/Documents/Projet_tuteure_phylogenie/Script_root.R"; // Le chemin réel du script R
            // Extraire le nom du fichier sans le chemin et l'extension
            String nomFichierNewick = Paths.get(cheminFichierNewick).getFileName().toString().replaceFirst("[.][^.]+$", "");
            // Commande pour exécuter le script R
            String[] commande = new String[] {
                "Rscript", cheminScriptR, cheminFichierNewick, cheminFichierSvg, nomFichierNewick
            };
            
            System.out.println("Chemin du fichier Newick : " + cheminFichierNewick);
            System.out.println("Chemin du fichier SVG : " + cheminFichierSvg);

            // Exécution de la commande
            Process process = Runtime.getRuntime().exec(commande);

            // Lire la sortie du script R
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                System.out.println(ligne);
            }
            
             //Lire la sortie d'erreur du script R
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String ligneErreur;
            while ((ligneErreur = errorReader.readLine()) != null) {
                System.err.println(ligneErreur);
            }

            // Vérifier si le processus s'est terminé correctement
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Fichier SVG généré : " + cheminFichierSvg);
            } else {
               System.err.println("Erreur lors de l'exécution du script R.");
            }

        } catch (IOException | InterruptedException e) {
           e.printStackTrace();
        }
    }
    
 // Méthode pour générer le fichier Newick et l'image SVG
    public void genererImageArbreNj(String cheminFichierNewick, String cheminFichierSvg) {
        try {
            // Construire le chemin complet du script R
            String cheminScriptR = "/home/elodie/Documents/Projet_tuteure_phylogenie/Script_unroot.R"; // Le chemin réel du script R
            String nomFichierNewick = Paths.get(cheminFichierNewick).getFileName().toString().replaceFirst("[.][^.]+$", "");
            // Commande pour exécuter le script R
            String[] commande = new String[] {
                "Rscript", cheminScriptR, cheminFichierNewick, cheminFichierSvg, nomFichierNewick
            };
            
            System.out.println("Chemin du fichier Newick : " + cheminFichierNewick);
            System.out.println("Chemin du fichier SVG : " + cheminFichierSvg);

            // Exécution de la commande
            Process process = Runtime.getRuntime().exec(commande);

            // Lire la sortie du script R
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                System.out.println(ligne);
            }
            
             //Lire la sortie d'erreur du script R
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String ligneErreur;
            while ((ligneErreur = errorReader.readLine()) != null) {
                System.err.println(ligneErreur);
            }

            // Vérifier si le processus s'est terminé correctement
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Fichier SVG généré : " + cheminFichierSvg);
            } else {
               System.err.println("Erreur lors de l'exécution du script R.");
            }

        } catch (IOException | InterruptedException e) {
           e.printStackTrace();
        }
    }
}