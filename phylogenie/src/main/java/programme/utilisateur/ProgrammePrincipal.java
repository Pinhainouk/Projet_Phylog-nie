package programme.utilisateur;

/**
 * La classe ProgrammePrincipal sert de point d'entrée pour le logiciel de reconstruction d'arbres phylogénétiques. 
 * 
 * Ce logiciel est conçu pour aider les utilisateurs à reconstruire des arbres phylogénétiques,
 * qui représentent les relations évolutives entre différentes espèces.
 */
public class ProgrammePrincipal {
	
	/**
	 * La méthode principale main initialise l'interface utilisateur, 
	 * créé la fenêtre principale grâce à la méthode de la classe InterfaceUtilisateur. 
	 * */
	public static void main(String[] args) throws Exception {
		InterfaceUtilisateur interfaceUtilisateur = new InterfaceUtilisateur();  
		interfaceUtilisateur.creerFenetrePrincipale("Phylogénie", "Logiciel de reconstruction d'arbres phylogénétiques");
	}
}
