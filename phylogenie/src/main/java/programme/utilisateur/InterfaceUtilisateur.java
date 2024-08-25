package programme.utilisateur;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import org.biojava.nbio.core.alignment.template.Profile;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import alignement.sequences.AlignementMultiple;
import alignement.sequences.AlignementMultipleAdn;
import alignement.sequences.AlignementMultipleProteine;
import reconstruction.arbre.MatriceDeDistance;
import reconstruction.arbre.MatriceDeDistanceAdn;
import reconstruction.arbre.MatriceDeDistanceProteine;
import reconstruction.arbre.AlgoUpgma;
import reconstruction.arbre.FichierFormatNewick;
import reconstruction.arbre.FichierImageArbre;
import reconstruction.arbre.AlgoNj;

public class InterfaceUtilisateur {
	private static boolean isProtein = false;
	private Profile<ProteinSequence, AminoAcidCompound> alignementProteine;
	private Profile<DNASequence, NucleotideCompound> alignementAdn;
	private MatriceDeDistance matrice;
	private AlgoUpgma upgma;
	private AlgoNj nj;
	private FichierFormatNewick newick;
	private FichierImageArbre svg;
	
	 public InterfaceUtilisateur() {
	    this.alignementProteine = null;  
	    this.alignementAdn = null; 
	    this.upgma = null; 
	    this.nj = null;
	    this.newick = null;
	    this.svg = null;
	 }
	
	// Créer la fenêtre principale	
	public JFrame creerFenetrePrincipale(String nomFenetrePrincipale, String titreLogiciel) { 
		JFrame fenetrePrincipale = new JFrame(nomFenetrePrincipale);
	    fenetrePrincipale.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    fenetrePrincipale.setSize(1200, 1200);    
	    JPanel panelPrincipal = creerPanelPrincipal(titreLogiciel); // Ajouter le panel principal
	    panelPrincipal.setBackground(new java.awt.Color(28,144,153));
	    JPanel panelChoix = creerPanelChoix("Choix du type de séquences à aligner : ", // Ajouter le panel de choix
		        "Protéines", e-> choixProteines(fenetrePrincipale), 
		        "Nucléotides", e-> choixNucleotides(fenetrePrincipale));
		fenetrePrincipale.setLayout(new BorderLayout()); // Ajouter le panel principal à la fenêtre
	    fenetrePrincipale.add(panelPrincipal, BorderLayout.NORTH); 
	    fenetrePrincipale.add(panelChoix, BorderLayout.CENTER);
	    JPanel panelBoutonAide = creerPanelBoutonAide(); // Créer le bouton aide
	    fenetrePrincipale.add(panelBoutonAide, BorderLayout.SOUTH);
	    fenetrePrincipale.setVisible(true);
	    return fenetrePrincipale;
	}
	
	 // Créer la fenêtre secondaire avec 2 boutons et le bouton retour 
	private static JFrame creerFenetreSecondaire(String nomFenetreSecondaire2, String titreLogiciel, String sousTitreChoix, 
			String bouton1, ActionListener actionBouton1,
			String bouton2, ActionListener actionBouton2,  
			JFrame fenetrePrincipale, String boutonRetour, ActionListener actionBoutonRetour) { 
		JFrame fenetreSecondaire = new JFrame(nomFenetreSecondaire2);
		fenetreSecondaire.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		fenetreSecondaire.setSize(1200, 1200);
		JPanel panelPrincipal = creerPanelPrincipal(titreLogiciel); // Créer le panel principal
	    panelPrincipal.setBackground(new java.awt.Color(28,144,153));
		JPanel panelChoix = creerPanelChoix(sousTitreChoix, bouton1, actionBouton1, bouton2, actionBouton2);
		fenetreSecondaire.add(panelPrincipal, BorderLayout.NORTH);
		fenetreSecondaire.add(panelChoix, BorderLayout.CENTER);
		fenetreSecondaire.setVisible(true);
		JPanel panelBoutonRetour = creerPanelBoutonRetour(fenetrePrincipale);
		fenetreSecondaire.add(panelBoutonRetour, BorderLayout.SOUTH);
        fenetreSecondaire.setVisible(true);
        fenetrePrincipale.setVisible(false);
		return fenetreSecondaire;	
	}
	
	// Créer le panel principal avec le titre du logiciel      
	private static JPanel creerPanelPrincipal(String titre) { 
        JPanel panelPrincipal = new JPanel(); // Panel principal avec BoxLayout pour disposer les composants verticalement
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        Font policeTitre = new Font("TimesRoman", Font.BOLD, 36);
        JLabel label = new JLabel(titre, JLabel.CENTER);
        label.setFont(policeTitre);
        label.setForeground(Color.BLACK);    
        label.setAlignmentX(Component.CENTER_ALIGNMENT);  
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 100))); // Ajout d'espacement vertical entre les labels
        panelPrincipal.add(label);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 100))); 
        return panelPrincipal;
    }
	
	// Créer le panel avec les boutons et le titre de l'action à réaliser dans la fenêtre
	private static JPanel creerPanelChoix(String titreChoix, String nomBouton1, ActionListener actionBouton1, 
		String nomBouton2, ActionListener actionBouton2) { 
		JPanel panelChoix = new JPanel();
		panelChoix.setLayout(new BoxLayout(panelChoix, BoxLayout.Y_AXIS));
		Font policeSousTitre = new Font("TimesRoman", Font.BOLD, 26);
		JLabel sousTitre = new JLabel(titreChoix, JLabel.CENTER);  
		sousTitre.setFont(policeSousTitre);
		sousTitre.setForeground(Color.BLACK); 
		sousTitre.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelChoix.add(Box.createRigidArea(new Dimension(0, 50))); // hauteur d'espace
		panelChoix.add(sousTitre);
		panelChoix.add(Box.createRigidArea(new Dimension(0, 20))); // hauteur d'espace
		panelChoix.add(creerPanel2Boutons(nomBouton1, actionBouton1, nomBouton2, actionBouton2)); // Ajout des boutons au panel choix
		return panelChoix;
	}
	
	// Créer le panel du bouton aide
	private static JPanel creerPanelBoutonAide() { 
        JPanel panelBoutonAide = new JPanel();
        panelBoutonAide.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 50));
        JButton boutonAide = new JButton("Aide");
        boutonAide.setFont(new Font("TimesRoman", Font.ITALIC, 24));
        boutonAide.setForeground(Color.BLACK);
        boutonAide.setBackground(new java.awt.Color(189, 189, 189));
        boutonAide.setPreferredSize(new Dimension(200, 100));
        boutonAide.addActionListener(e -> afficherMessageAide());
        panelBoutonAide.add(boutonAide);
        return panelBoutonAide;
    }
	
	// Méthode qui affiche le message d'aide dans une fenêtre d'information
	private static void afficherMessageAide() { 
        String message = "Pour utiliser ce logiciel de reconstruction d'arbre phylogénétique :\n\n"
                       + "Choisissez le type de séquences avec lequel vous souhaitez créer un arbre phylogénétique.\n\n"
                       + "Choisissez la façon dont vous allez charger les séquences. Par numéros d'accessions ou à partir d'un fichier fasta : \n\n"
                       + "- Saisissez les numéros d'accessions des séquences séparés par un espace. \n"
                       + "ou \n"
                       + "- Saissisez le chemin absolu du fichier fasta contenant toutes les séquences.\n\n"
                       + "Cliquez sur -Aligner- pour procéder à l'alignement multiple des séquences saisies.\n\n"
                       + "Sélectionnez l'algorithme souhaité pour la reconstruction de l'arbre : \n\n"
                       + "- UPGMA (Unweighted pair group method with arithmetic mean).\n"
                       + "ou \n"
                       + "- NJ (Neighbor-Joining).\n\n"
                       + "Choisissez le format du fichier de sortie de l'arbre : le format de Newick (.nwk) ou l'affichage de l'arbre en image (.svg)";
        JOptionPane.showMessageDialog(null, message, "Aide", JOptionPane.INFORMATION_MESSAGE);
    }
	
	// Créer le panel des deux boutons avec action
	private static JPanel creerPanel2Boutons(String nomBouton1, ActionListener actionBouton1, 
		String nomBouton2, ActionListener actionBouton2) { 
	    JPanel panelBoutons = new JPanel(); // Créer le panel des boutons 
	    Font policeBouton = new Font("TimesRoman", Font.BOLD, 24);
	    JButton bouton1 = new JButton(nomBouton1);
	    JButton bouton2 = new JButton(nomBouton2);	        
	    bouton1.setFont(policeBouton);
	    bouton2.setFont(policeBouton);	              
	    bouton1.setForeground(Color.BLACK); // Changer la couleur du texte
	    bouton1.setBackground(new java.awt.Color(153,216,201)); // Changer la couleur de fond des boutons
	    bouton2.setForeground(Color.BLACK);
	    bouton2.setBackground(new java.awt.Color(153,216,201));		      
	    Dimension tailleBouton = new Dimension(400, 100);
	    bouton1.setPreferredSize(tailleBouton);
	    bouton2.setPreferredSize(tailleBouton);	
	    panelBoutons.add(Box.createRigidArea(new Dimension(50, 50))); 
	    panelBoutons.add(bouton1);
	    panelBoutons.add(Box.createRigidArea(new Dimension(50, 50))); 
	    panelBoutons.add(bouton2);	    
	    bouton1.addActionListener(actionBouton1); // Ajouter les actions des boutons
	    bouton2.addActionListener(actionBouton2);
	    return panelBoutons;
	}
	
	// Créer le panel du bouton retour			
	private static JPanel creerPanelBoutonRetour(JFrame parentFenetre) { 
		JPanel panelBoutonRetour = new JPanel();// Créer le panel du bouton retour
		panelBoutonRetour.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 50));
	    Font policeBouton = new Font("TimesRoman", Font.ITALIC, 24);
	    JButton boutonRetour = new JButton("Retour");        
	    boutonRetour.setFont(policeBouton);  
	    boutonRetour.setForeground(Color.BLACK); // Changer la couleur du texte 
	    boutonRetour.setBackground(new java.awt.Color(189, 189, 189)); // Changer la couleur de fond du bouton 
	    Dimension tailleBouton = new Dimension(400, 100);
	    boutonRetour.setPreferredSize(tailleBouton);
	    panelBoutonRetour.add(boutonRetour);
	    boutonRetour.addActionListener(e -> {
	        parentFenetre.setVisible(true);
	        ((JFrame) SwingUtilities.getWindowAncestor(boutonRetour)).dispose();
	    });
	    return panelBoutonRetour;
	}
	
	// Créer le panel pour la fenêtre création de l'arbre avec les choix
	private static JPanel creerPanelChampsAvecLabel(String nomSaisie1, JTextField saisie, ActionListener actionBoutonValider, 
		String nomSaisie2, String nomBoutonRadio1, ActionListener actionBoutonRadio1, 
		String nomBoutonRadio2, ActionListener actionBoutonRadio2,
		String nomSaisie3, String nomBouton1, ActionListener actionBouton1,
		String nomBouton2, ActionListener actionBouton2) { 		
		JPanel panelChamps = new JPanel(); // Créer le panel global 
	    panelChamps.setLayout(new BoxLayout(panelChamps, BoxLayout.Y_AXIS));    
	    panelChamps.add(creerPanelSaisieAvecBouton(nomSaisie1, saisie, actionBoutonValider)); // Ajouter le champ de saisie avec bouton "Aligner"
	    panelChamps.add(Box.createRigidArea(new Dimension(0, 80))); 	    
	    panelChamps.add(creerPanelBoutonsRadio(nomSaisie2, nomBoutonRadio1, actionBoutonRadio1, nomBoutonRadio2, actionBoutonRadio2)); // Ajouter le panel avec boutons radio pour l'algorithme
	    panelChamps.add(Box.createRigidArea(new Dimension(0, 80)));     
	    panelChamps.add(creerPanelAvecDeuxBoutons(nomSaisie3, nomBouton1, actionBouton1, nomBouton2, actionBouton2)); // Ajouter le panel avec deux boutons pour le format du fichier de sortie
	    panelChamps.add(Box.createRigidArea(new Dimension(0, 50))); 
	    return panelChamps;
	}
	
	// Créer le panel du champs de saisie avec le label		
	private static JPanel creerPanelSaisieAvecBouton(String nomSaisie1, JTextField saisie, ActionListener actionBoutonValider) { 
		JPanel panelSaisie = new JPanel(); // Créer le panel du champs de saisie
		panelSaisie.setLayout(new BoxLayout(panelSaisie, BoxLayout.Y_AXIS));		
		JLabel label1 = new JLabel(nomSaisie1); // Créer le label 1
		label1.setFont(new Font("TimesRoman", Font.BOLD, 26));
		label1.setAlignmentX(Component.CENTER_ALIGNMENT);
		JButton boutonValider = new JButton("Aligner"); // Créer le bouton Aligner et le personnaliser	
		boutonValider.setFont(new Font("TimesRoman", Font.BOLD, 24));
		boutonValider.setBackground(new java.awt.Color(153,216,201));
		boutonValider.setPreferredSize(new Dimension(200,100));
		boutonValider.addActionListener(actionBoutonValider);
		JPanel panelEntree = new JPanel(); // Créer le panel du champs d'entrée des données
		panelEntree.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		saisie.setBackground(Color.white);
		saisie.setPreferredSize(new Dimension(100, 50));
		panelEntree.add(saisie);
		panelEntree.add(boutonValider);
		panelSaisie.add(Box.createRigidArea(new Dimension(0, 50)));
		panelSaisie.add(label1);
		panelSaisie.add(Box.createRigidArea(new Dimension(0, 20)));
		panelSaisie.add(panelEntree);
		return panelSaisie;
	}
	
	// Créer le panel boutons radio avec le label 2
	private static JPanel creerPanelBoutonsRadio(String nomSaisie2, String nomBoutonRadio1, ActionListener actionBoutonRadio1, 
		String nomBoutonRadio2, ActionListener actionBoutonRadio2) { 
		JPanel panelRadio = new JPanel(); // Créer le panel boutons radio
		panelRadio.setLayout(new BoxLayout(panelRadio, BoxLayout.Y_AXIS));	
		JLabel label2 = new JLabel(nomSaisie2); // Créer le label 2
		label2.setFont(new Font("TimesRoman", Font.BOLD, 26));
		label2.setAlignmentX(Component.CENTER_ALIGNMENT);   
		JRadioButton upgmaRadioBouton = new JRadioButton(nomBoutonRadio1); // Créer les boutons radios
		JRadioButton njRadioBouton = new JRadioButton(nomBoutonRadio2);
		upgmaRadioBouton.setFont(new Font("TimesRoman", Font.BOLD, 24));
		upgmaRadioBouton.setPreferredSize(new Dimension(180, 50));
		njRadioBouton.setFont(new Font("TimesRoman", Font.BOLD, 24));
		njRadioBouton.setPreferredSize(new Dimension(180, 50));
		upgmaRadioBouton.addActionListener(actionBoutonRadio1); // Ajouter les actions aux boutons radios
		njRadioBouton.addActionListener(actionBoutonRadio2);		
		ButtonGroup algoGroup = new ButtonGroup(); // Créer le groupe de bouton pour avoir une exclusion mutuelle dans le groupe et pour n'avoir qu'un unique bouton de coché
		algoGroup.add(upgmaRadioBouton);
    	algoGroup.add(njRadioBouton);
    	JPanel panelRadioBoutons = new JPanel(); // Créer le panel avec les boutons radio pour le choix de l'algorithme
    	panelRadioBoutons.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
    	panelRadioBoutons.add(upgmaRadioBouton);
    	panelRadioBoutons.add(njRadioBouton);
    	panelRadio.add(label2);
    	panelRadio.add(panelRadioBoutons);
    	return panelRadio;
    	}
	
	// Créer le panel avec 2 boutons et un label
	private static JPanel creerPanelAvecDeuxBoutons(String nomSaisie3, String nomBouton1, ActionListener actionBouton1, 
        String nomBouton2, ActionListener actionBouton2) { // Créer le panel 2 boutons avec le label 3		 
		JPanel panelBoutons = new JPanel(); // Créer le panel boutons
		panelBoutons.setLayout(new BoxLayout(panelBoutons, BoxLayout.Y_AXIS));		 
		JLabel label3 = new JLabel(nomSaisie3); // Créer le label 3	   
		label3.setFont(new Font("TimesRoman", Font.BOLD, 26));
		label3.setAlignmentX(Component.CENTER_ALIGNMENT);	
		JPanel panelBoutonsFichiers = creerPanel2Boutons(nomBouton1, actionBouton1, nomBouton2, actionBouton2); // Créer le panel boutons choix du type de fichiers de sortie
		panelBoutons.add(label3);
		panelBoutons.add(Box.createRigidArea(new Dimension(0, 20)));
		panelBoutons.add(panelBoutonsFichiers);
		return panelBoutons;
	}
	
	// Méthode qui ouvre une fenêtre secondaire avec 2 choix de chargement des séquences si on clique sur Protéines
	private void choixProteines(JFrame parentFenetre) { 
		isProtein = true;
		JFrame fenetreProteine = creerFenetreSecondaire("Séquences protéiques", 
				"Logiciel de reconstruction d'arbres phylogénétiques", 
				"Chargement des séquences à aligner : ", 
				"N° accession UniProt",
				e -> choixNumAccessions(parentFenetre), 
				"Fichier fasta",
				e -> choixFasta(parentFenetre), parentFenetre, null, null);
		fenetreProteine.setVisible(true);
        parentFenetre.setVisible(false);
	}
	
	// Méthode qui ouvre une fenêtre secondaire avec 2 choix de chargement des séquences si on clique sur Nucléotides
	private void choixNucleotides(JFrame parentFenetre) { 
		isProtein = false;
		JFrame fenetreADN = creerFenetreSecondaire("Séquences nucléotidiques", 
				"Logiciel de reconstruction d'arbres phylogénétiques", 
				"Chargement des séquences à aligner : ", 
				"N° accession Genbank", 
				e -> choixNumAccessions(parentFenetre), 
				"Fichier fasta", 
				e -> choixFasta(parentFenetre), 
				parentFenetre, null, null);
		fenetreADN.setVisible(true);
		parentFenetre.setVisible(false);
	}
	
	// Méthode qui ouvre la fenêtre de choix numéros accessions	
	private void choixNumAccessions(JFrame parentFenetre) { 
        JFrame fenetreNumAccessions = new JFrame("Numéros d'accessions"); // Créer une nouvelle fenêtre
        fenetreNumAccessions.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fenetreNumAccessions.setSize(1200, 1200); 
        JPanel panelPrincipal = creerPanelPrincipal("Logiciel de reconstruction d'arbres phylogénétiques");  // Créer le panel principal avec titre du logiciel et l'ajouter à la fenêtre
        panelPrincipal.setBackground(new java.awt.Color(28,144,153));
        fenetreNumAccessions.add(panelPrincipal, BorderLayout.NORTH); 
        JTextField numAccessions = new JTextField(80); // Créer le champs de saisie des numéros d'accessions     
        ActionListener actionNumAccessions = creerActionNumAccessions(numAccessions); // Créer l'action du bouton aligner ajouter par le panelChamps ensuite       
        ActionListener actionUpgma = null; // Action lorsque l'on sélectionne le bouton radio UPGMA       
        ActionListener actionNj = null; // Action lorsque l'on sélectionne le bouton radio NJ       
        ActionListener actionNewick = null; // Action lorsque l'on clique sur le bouton Newick       
        ActionListener actionSvg = null; // Action lorsque l'on clique sur le bouton SVG      
        JPanel panelChamps = creerPanelChampsAvecLabel("1. Saisie des numéros d'accessions (séparés par un espace) : ", numAccessions, actionNumAccessions, 
        		"2. Choix de l'algorithme : ", "UPGMA", actionUpgma, "NJ", actionNj,
        		"3. Choix de la représentation de l'arbre phylogénétique : ", "Format de Newick", actionNewick, "Format image SVG", actionSvg); // Créer le panel des choix et l'ajouter à la fenêtre
        fenetreNumAccessions.add(panelChamps, BorderLayout.CENTER);
        fenetreNumAccessions.add(panelChamps);
        JPanel panelBoutonRetour = creerPanelBoutonRetour(fenetreNumAccessions);
        fenetreNumAccessions.add(panelBoutonRetour, BorderLayout.SOUTH);
        fenetreNumAccessions.setVisible(true);
        parentFenetre.setVisible(false);
    }
	
	// Méthode qui éxécute l'alignement multiple à partir du fichier fasta en fonction s'il s'agit de protéines ou nucléotides
	private void choixFasta(JFrame parentFenetre) { 
		JFrame fenetreFasta = new JFrame ("Fichier fasta"); // Créer une nouvelle fenêtre
		fenetreFasta.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		fenetreFasta.setSize(1200, 1200);
		JPanel panelPrincipal = creerPanelPrincipal("Logiciel de reconstruction d'arbres phylogénétiques"); // Créer le panel principal avec titre du logiciel et l'ajouter à la fenêtre
	    panelPrincipal.setBackground(new java.awt.Color(28,144,153));
		fenetreFasta.add(panelPrincipal, BorderLayout.NORTH);	    
		JTextField fasta = new JTextField(80); // Créer le champs de saisie du chemin fasta	    
		ActionListener actionFasta = creerActionFasta(fasta); // Créer l'action du bouton aligner ajouter par le panelChamps ensuite	    
	    ActionListener actionUpgma = creerActionUpgma(); // Action lorsque l'on sélectionne le bouton radio UPGMA	    
		ActionListener actionNj = creerActionNj(); // Action lorsque l'on sélectionne le bouton radio NJ	    
		ActionListener actionNewick = creerNewick(); // Action lorsque l'on clique sur le bouton Newick	    
		ActionListener actionSvg = creerSVG();; // Action lorsque l'on clique sur le bouton SVG   
	    JPanel panelChamps = creerPanelChampsAvecLabel("1. Saisie du chemin du fichier fasta (chemin absolu) : ", fasta, actionFasta, 
		        		"2. Choix de l'algorithme : ", "UPGMA", actionUpgma, "NJ", actionNj,
		        		"3. Choix de la représentation de l'arbre phylogénétique : ", "Format de Newick", actionNewick, "Format image SVG", actionSvg); // Créer le panel des choix sous le panel principal et l'ajouter à la fenêtre
		fenetreFasta.add(panelChamps, BorderLayout.CENTER);
		fenetreFasta.add(panelChamps);
		JPanel panelBoutonRetour = creerPanelBoutonRetour(fenetreFasta);
		fenetreFasta.add(panelBoutonRetour, BorderLayout.SOUTH);
		fenetreFasta.setVisible(true);
		parentFenetre.setVisible(false);
	}
			
	// Méhode qui crée l'action de traiter les numéros d'accessions saisis (les range dans une arrayList et vérifie leur validité)
	private ActionListener creerActionNumAccessions(JTextField numAccessions) { 
	    return e1 -> {
	    	ArrayList<String> numerosAccessions = AlignementMultiple.traiterSaisieNumAccessions(numAccessions);
	        ArrayList<String> numAccessionsValides = AlignementMultiple.validerNumerosAccessions(numerosAccessions);
	        if (!numAccessionsValides.isEmpty()) {
	        	if (isProtein) {
	        		this.alignementProteine = AlignementMultipleProteine.effectuerAlignementNumProteine(numAccessionsValides);
	        		((MatriceDeDistanceProteine) matrice).matriceDeDistanceObserveeProteine(alignementProteine);
		        } else {
		        	this.alignementAdn = AlignementMultipleAdn.effectuerAlignementNumAdn(numAccessionsValides);
		        	((MatriceDeDistanceAdn) matrice).MatriceDeDistanceObserveeAdn(alignementAdn);
		        }
		    } else {
		    	JOptionPane.showMessageDialog(null, "Aucun numéro d'accession valide trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
			    System.out.println("Erreur -> " + "Aucun numéro d'accession valide trouvé.");
	        }
	    };
	}
	
	// Méhode qui crée l'action de traiter la saisie du chemin du fichier fasta (le range dans un File et vérifie sa validité)	
	private ActionListener creerActionFasta(JTextField fasta) { 
		return e1 -> {
	        File fichierFasta = AlignementMultiple.traiterCheminFasta(fasta); 
	        if (isProtein) {
	        	this.alignementProteine = AlignementMultipleProteine.effectuerAlignementFastaProteine(fichierFasta);
	        	System.out.println("Taille de l'alignement protéique: " + alignementProteine.getAlignedSequences()); // debogage
	        	//if (matrice instanceof MatriceDeDistanceProteine) {
	        		//MatriceDeDistanceProteine matriceProteine = (MatriceDeDistanceProteine) matrice;
	        		((MatriceDeDistanceProteine) matrice).matriceDeDistanceObserveeProteine(alignementProteine);
	        		System.out.println("Matrice de distances après remplissage:");
	        		matrice.afficherMatrice();// debogage
	        		//}
	        	} else if (!isProtein) {
	        		this.alignementAdn = AlignementMultipleAdn.effectuerAlignementFastaAdn(fichierFasta);
	        		MatriceDeDistanceAdn matriceAdn = (MatriceDeDistanceAdn) matrice;
	        		matriceAdn.MatriceDeDistanceObserveeAdn(alignementAdn);
	        JOptionPane.showMessageDialog(null, "Type de matrice incompatible.", "Erreur", JOptionPane.ERROR_MESSAGE);
	        }
	    };
	}
	
	// Méthode qui crée l'action d'exécuter UPGMA à partir de l'alignement
	private ActionListener creerActionUpgma() { 	
	    return e1 -> {
	        try {
	            if (isProtein) {
	                if (alignementProteine == null) { // Vérifier si l'alignement des protéines est disponible
	                    JOptionPane.showMessageDialog(null, "Alignement des protéines non disponible.", "Erreur", JOptionPane.ERROR_MESSAGE);
	                    return;
	                }        
	                if (matrice instanceof MatriceDeDistanceProteine) { // Vérifier si la matrice est de type MatriceDeDistanceProteine
	                    ((MatriceDeDistanceProteine) matrice).matriceDeDistanceObserveeProteine(alignementProteine);
	                    matrice.afficherMatrice();
	                } else {
	                    JOptionPane.showMessageDialog(null, "Matrice incompatible pour les protéines.", "Erreur", JOptionPane.ERROR_MESSAGE);
	                    return;
	                }
	            } else {
	                if (alignementAdn == null) { // Vérifier si l'alignement ADN est disponible
	                    JOptionPane.showMessageDialog(null, "Alignement ADN non disponible.", "Erreur", JOptionPane.ERROR_MESSAGE);
	                    return;
	                } 
	                if (matrice instanceof MatriceDeDistanceAdn) { // Vérifier si la matrice est de type MatriceDeDistanceAdn
	                    
	                } else {
	                    JOptionPane.showMessageDialog(null, "Matrice incompatible pour les ADN.", "Erreur", JOptionPane.ERROR_MESSAGE);
	                    return;
	                }
	            }
	            upgma.executerUpgma();
	        } catch (Exception ex) {
	            JOptionPane.showMessageDialog(null, "Erreur lors de l'exécution de UPGMA : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
	            ex.printStackTrace(); // Pour le débogage
	        }
	    };
	}

	
	// Méthode qui crée l'action d'exécuter NJ à partir de l'alignement
	private ActionListener creerActionNj() { 
		return e1 ->{};
	}
	
	// Méthode qui crée l'action d'exécuter la création du fichier avec format de Newick 
	private ActionListener creerNewick() { 
		return e1 ->{};
	}
	
	// Méthode qui créé l'action d'exécuter la création de l'image SVG de l'arbre reconstruit
	private ActionListener creerSVG() { //
		return e1 ->{};
	}
}