package programme.utilisateur;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
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
	private static boolean isDna = false;
	private Profile<ProteinSequence, AminoAcidCompound> alignementProteine;
	private Profile<DNASequence, NucleotideCompound> alignementAdn;
	private MatriceDeDistance matrice;
	private JRadioButton upgmaRadioBouton; 
	private JRadioButton njRadioBouton;    
	private AlgoUpgma upgma;
	private AlgoNj nj;
	private FichierFormatNewick fichierNewick;
	private FichierImageArbre fichierSvg;
	
	 public InterfaceUtilisateur() {
	 }
	
	// Créer la fenêtre principale	
	public JFrame creerFenetrePrincipale(String nomFenetrePrincipale, String titreLogiciel) { 
		JFrame fenetrePrincipale = new JFrame(nomFenetrePrincipale);
	    fenetrePrincipale.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    fenetrePrincipale.setSize(1920, 1080); // taille écran 14 pouces       
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
	    fenetreSecondaire.setSize(1920, 1080); 
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
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 60))); // Ajout d'espacement vertical entre les labels
        panelPrincipal.add(label);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 60))); 
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
        JButton boutonAide = new JButton("Aide ?");
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
                       + "1. Cliquez sur le type de séquences avec lequel vous souhaitez créer un arbre phylogénétique.\n\n"
                       + "2. Cliquez sur la façon dont vous allez charger les séquences. Soit par numéros d'accessions, soit à partir d'un fichier fasta.\n\n"
                       + "3. Saisissez les numéros d'accessions des séquences séparés par un espace dans le champs de texte ou coller -ctrl+v-. \n"
                       + "ou saissisez le chemin absolu du fichier fasta contenant les séquences ou coller avec -ctrl+v-.\n\n"
                       + "4. Cliquez sur -Aligner- pour procéder à l'alignement multiple des séquences saisies.\n\n"
                       + "5. Sélectionnez l'algorithme souhaité pour la reconstruction de l'arbre : \n"
                       + "UPGMA (Unweighted pair group method with arithmetic mean).\n"
                       + "ou NJ (Neighbor-Joining).\n\n"
                       + "6. Cliquez sur -Générer l'arbre- : "
                       + "Cette action ouvrira une boite de dialogue vous invitant à choisir le chemin où \n"
                       + "enregistrer le fichier au format de Newick et enregistrera automatiquement l'arbre "
                       + "au format image SVG au même emplacement.";
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
	    Dimension tailleBouton = new Dimension(200, 100);
	    boutonRetour.setPreferredSize(tailleBouton);
	    panelBoutonRetour.add(boutonRetour);
	    boutonRetour.addActionListener(e -> {
	        parentFenetre.setVisible(true);
	        ((JFrame) SwingUtilities.getWindowAncestor(boutonRetour)).dispose();
	    });
	    return panelBoutonRetour;
	}
	
	// Créer le panel du bouton retour			
	private static JPanel creerPanelBoutonArbre(String nomBoutonArbre, ActionListener actionBoutonArbre) { 
		JPanel panelBoutonArbre = new JPanel();// Créer le panel du bouton retour
		panelBoutonArbre.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 50));
	    Font policeBouton = new Font("TimesRoman", Font.BOLD, 30);
	    JButton boutonArbre = new JButton(nomBoutonArbre);        
	    boutonArbre.setFont(policeBouton);  
	    boutonArbre.setForeground(Color.BLACK); // Changer la couleur du texte 
	    boutonArbre.setBackground(new java.awt.Color(28,144,153)); // Changer la couleur de fond du bouton 
	    Dimension tailleBouton = new Dimension(600, 150);
	    boutonArbre.setPreferredSize(tailleBouton);
	    panelBoutonArbre.add(boutonArbre);
	    boutonArbre.addActionListener(actionBoutonArbre);
	    return panelBoutonArbre;
	}
		
		
	// Créer le panel pour la fenêtre création de l'arbre avec les choix
	private JPanel creerPanelChampsAvecLabel(String nomSaisie1, JTextField saisie, ActionListener actionBoutonValider, 
		String nomSaisie2, String nomBoutonRadio1, String nomBoutonRadio2,
		String nomBouton1, ActionListener actionBouton1) { 		
		JPanel panelChamps = new JPanel(); // Créer le panel global 
		panelChamps.setLayout(new BoxLayout(panelChamps, BoxLayout.Y_AXIS));    
		panelChamps.add(creerPanelSaisieAvecBouton(nomSaisie1, saisie, actionBoutonValider)); // Ajouter le champ de saisie avec bouton "Aligner"
		panelChamps.add(Box.createRigidArea(new Dimension(0, 30))); 	    
		panelChamps.add(creerPanelBoutonsRadio(nomSaisie2, nomBoutonRadio1, nomBoutonRadio2)); // Ajouter le panel avec boutons radio pour l'algorithme
		panelChamps.add(Box.createRigidArea(new Dimension(0, 30)));
		panelChamps.add(creerPanelBoutonArbre(nomBouton1, actionBouton1));
		panelChamps.add(Box.createRigidArea(new Dimension(0, 30))); 
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
		panelSaisie.add(Box.createRigidArea(new Dimension(0, 30)));
		panelSaisie.add(label1);
		panelSaisie.add(Box.createRigidArea(new Dimension(0, 30)));
		panelSaisie.add(panelEntree);
		return panelSaisie;
	}
	
	// Créer le panel boutons radio avec le label 2
	private JPanel creerPanelBoutonsRadio(String nomSaisie2, String nomBoutonRadio1, 
		String nomBoutonRadio2) { 
		JPanel panelRadio = new JPanel(); // Créer le panel boutons radio
		panelRadio.setLayout(new BoxLayout(panelRadio, BoxLayout.Y_AXIS));	
		JLabel label2 = new JLabel(nomSaisie2); // Créer le label 2
		label2.setFont(new Font("TimesRoman", Font.BOLD, 26));
		label2.setAlignmentX(Component.CENTER_ALIGNMENT);  
		// Créer une icône personnalisée pour les ronds des boutons radios
	    Icon customIcon = new Icon() {
	    private int size = 28; // Taille du rond (peut être ajustée)
	    public void paintIcon(Component c, Graphics g, int x, int y) {
            // Dessiner le rond du bouton radio
            g.setColor(Color.BLACK);
            g.drawOval(x, y, size, size);
            
            // Si sélectionné, remplir le rond
            if (((JRadioButton) c).isSelected()) {
                g.fillOval(x + 4, y + 4, size - 8, size - 8);
            }
        }      
        @Override
        public int getIconWidth() {
            return size;
        }       
        @Override
        public int getIconHeight() {
            return size;
        }
    };
		upgmaRadioBouton = new JRadioButton(nomBoutonRadio1); // Créer les boutons radios
		njRadioBouton = new JRadioButton(nomBoutonRadio2);
		upgmaRadioBouton.setIcon(customIcon);
		njRadioBouton.setIcon(customIcon);
		upgmaRadioBouton.setFont(new Font("TimesRoman", Font.BOLD, 24));
		upgmaRadioBouton.setPreferredSize(new Dimension(180, 50));
		njRadioBouton.setFont(new Font("TimesRoman", Font.BOLD, 24));
		njRadioBouton.setPreferredSize(new Dimension(180, 50));		
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
	
	// Méthode qui ouvre une fenêtre secondaire avec 2 choix de chargement des séquences si on clique sur Protéines
	private void choixProteines(JFrame parentFenetre) { 
		isDna = false;
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
		isDna = true;
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
	
	// Méthode qui ouvre la fenêtre de saisie des numéros d'accessions	
	private void choixNumAccessions(JFrame parentFenetre) { 
        JFrame fenetreNumAccessions = new JFrame("Numéros d'accessions"); // Créer une nouvelle fenêtre
        fenetreNumAccessions.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fenetreNumAccessions.setSize(1920, 1080); 
        JPanel panelPrincipal = creerPanelPrincipal("Logiciel de reconstruction d'arbres phylogénétiques");  // Créer le panel principal avec titre du logiciel et l'ajouter à la fenêtre
        panelPrincipal.setBackground(new java.awt.Color(28,144,153));
        fenetreNumAccessions.add(panelPrincipal, BorderLayout.NORTH); 
        JTextField numAccessions = new JTextField(70); // Créer le champs de saisie des numéros d'accessions 
        numAccessions.setFont(new Font("TimesRoman", Font.BOLD, 18));
        ActionListener actionNumAccessions = creerActionNumAccessions(numAccessions); // Créer l'action du bouton aligner ajouter par le panelChamps ensuite            
        ActionListener actionGenererArbre = genererArbre(); // Créer l'action de générer l'arbre      
        JPanel panelChamps = creerPanelChampsAvecLabel("Saisie des numéros d'accessions (séparés par un espace) : ", numAccessions, actionNumAccessions, 
        		"Choix de l'algorithme de reconstruction de l'arbre : ", "UPGMA", "NJ", "Générer l'arbre (Newick et SVG)", actionGenererArbre); // Créer le panel des choix et l'ajouter à la fenêtre
        fenetreNumAccessions.add(panelChamps, BorderLayout.CENTER);
        fenetreNumAccessions.add(panelChamps);
        JPanel panelBoutonRetour = creerPanelBoutonRetour(fenetreNumAccessions);
        fenetreNumAccessions.add(panelBoutonRetour, BorderLayout.SOUTH);
        fenetreNumAccessions.setVisible(true);
        parentFenetre.setVisible(false);
    }
	
	// Méthode qui ouvre la fenêtre de saisie du fasta
	private void choixFasta(JFrame parentFenetre) { 
		JFrame fenetreFasta = new JFrame ("Fichier fasta"); // Créer une nouvelle fenêtre
		fenetreFasta.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		fenetreFasta.setSize(1920, 1080);
		JPanel panelPrincipal = creerPanelPrincipal("Logiciel de reconstruction d'arbres phylogénétiques"); // Créer le panel principal avec titre du logiciel et l'ajouter à la fenêtre
	    panelPrincipal.setBackground(new java.awt.Color(28,144,153));
		fenetreFasta.add(panelPrincipal, BorderLayout.NORTH);	    
		JTextField fasta = new JTextField(70); // Créer le champs de saisie du chemin fasta	    
		fasta.setFont(new Font("TimesRoman", Font.BOLD, 18));
		ActionListener actionFasta = creerActionFasta(fasta); // Créer l'action du bouton aligner ajouter par le panelChamps ensuite	    	    
		ActionListener actionGenererArbre = genererArbre(); // Créer l'action de générer l'arbre         
	    JPanel panelChamps = creerPanelChampsAvecLabel("Saisie du chemin du fichier fasta (chemin absolu) : ", fasta, actionFasta, 
		        		"Choix de l'algorithme de reconstruction de l'arbre : ", "UPGMA", "NJ", "Générer l'arbre (Newick et SVG)", actionGenererArbre); // Créer le panel des choix sous le panel principal et l'ajouter à la fenêtre
		fenetreFasta.add(panelChamps, BorderLayout.CENTER);
		fenetreFasta.add(panelChamps);
		JPanel panelBoutonRetour = creerPanelBoutonRetour(fenetreFasta);
		fenetreFasta.add(panelBoutonRetour, BorderLayout.SOUTH);
		fenetreFasta.setVisible(true);
		parentFenetre.setVisible(false);
	}
			
	// Méhode qui crée l'action de traiter les numéros d'accessions saisis (les range dans une arrayList, vérifie leur validité et les aligne)
	private ActionListener creerActionNumAccessions(JTextField numAccessions) {
	    return e1 -> {
	    	ArrayList<String> numerosAccessions = AlignementMultiple.traiterSaisieNumAccessions(numAccessions);
	        ArrayList<String> numAccessionsValides = AlignementMultiple.validerNumerosAccessions(numerosAccessions);
	        if (!numAccessionsValides.isEmpty()) {
	        	if (isProtein) {	        		
	        		this.alignementProteine = AlignementMultipleProteine.effectuerAlignementNumProteine(numAccessionsValides); // Effectuer l'alignement multiple des protéines	        		
	        	}
	        	else if (isDna) {	        		
	        		this.alignementAdn = AlignementMultipleAdn.effectuerAlignementNumAdn(numAccessionsValides); // Effectuer l'alignement multiple des nucléotides	        		
	        	}
	        } else {
	        	JOptionPane.showMessageDialog(null, "Aucun numéro d'accession valide trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
	        }
	    };
	}
	
	// Méhode qui crée l'action de traiter la saisie du chemin du fichier fasta (le range dans un File, vérifie sa validité et aligne les séquences)	
	private ActionListener creerActionFasta(JTextField fasta) {
		return e1 -> {
	        File fichierFasta = AlignementMultiple.traiterCheminFasta(fasta); 
	        if (isProtein) {
	        	this.alignementProteine = AlignementMultipleProteine.effectuerAlignementFastaProteine(fichierFasta);	
	        	System.out.println("Alignement protéique réalisé.");
        	}
	        else if (isDna) {
        		this.alignementAdn = AlignementMultipleAdn.effectuerAlignementFastaAdn(fichierFasta);   
        		System.out.println("Alignement ADN réalisé.");
	        }
	        else {
	        JOptionPane.showMessageDialog(null, "Aucun fasta valide trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
	        }
		};
	}
	
	// Méthode qui crée l'arbre
	private ActionListener genererArbre() { 
		return e1 ->{
			try {				
				if (upgmaRadioBouton.isSelected()) { // Vérification du bouton sélectionné
					executerAlgoUpgma();
				} else if(njRadioBouton.isSelected()) {
					executerAlgoNj();
				} else {
					JOptionPane.showMessageDialog(null, "Aucun algorithme sélectionné.", "Erreur", JOptionPane.ERROR_MESSAGE);
				} 
			} catch (IllegalStateException ex) {
	            // Capture et gestion spécifique des exceptions d'alignement non réalisé
	            JOptionPane.showMessageDialog(null, ex.getMessage(), "Erreur d'alignement", JOptionPane.ERROR_MESSAGE);
			} catch(Exception e) {
			e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Erreur lors de la génération de l'arbre.", "Erreur", JOptionPane.ERROR_MESSAGE);		
			}
		};	
	}
	
	// Méthode qui vérifie que l'alignement est valide, crée la matrice, exécute l'algo UPGMA, crée le format de Newick et l'image SVG de l'arbre
	private void executerAlgoUpgma() {    
	    try {
	    	verifierAlignement();
	    	if (isDna) {
	            System.out.println("Alignement ADN présent : " + (alignementAdn != null));
	        }// Vérification si l'alignement est null avant d'exécuter les calculs
	    	if (isProtein) {
	    		matrice = new MatriceDeDistanceProteine();
	    		((MatriceDeDistanceProteine) matrice).ajouterDistancesObserveesProteines(alignementProteine);
	    		((MatriceDeDistanceProteine) matrice).ajouterNomsSequences(alignementProteine);
	    	} else if (isDna) {
	    		matrice = new MatriceDeDistanceAdn();
	    		//((MatriceDeDistanceAdn) matrice).ajouterDistancesEvolutivesAdn(alignementAdn);
	    		((MatriceDeDistanceAdn) matrice).ajouterDistancesObserveesAdn(alignementAdn);
	    		((MatriceDeDistanceAdn) matrice).ajouterNomsSequences(alignementAdn);
	    	} else {
	    		JOptionPane.showMessageDialog(null, "Aucun alignement trouvé", "Erreur", JOptionPane.ERROR_MESSAGE);
	    		return;
	    	}
	    	matrice.afficherMatrice();        
	    	upgma = new AlgoUpgma(matrice); // Création de l'arbre UPGMA	       	        
	    	fichierNewick = new FichierFormatNewick(); // Générer le format Newick
	    	String newick = fichierNewick.genererNewick(upgma);
	    	System.out.println("Format Newick UPGMA généré : " + newick);	        
	    	String cheminNewick = demanderCheminFichier("Enregistrer l'arbre UPGMA au format de Newick");
	    	fichierNewick.setFormatNewick(newick);
	    	fichierNewick.genererFichierNewick(cheminNewick);        	      	        
	   		String cheminSvg = cheminNewick.replace(".nwk",".svg");  // Chemin du fichier SVG (automatiquement enregistré au même chemin que l'utilisateur a choisi pour Newick)       
	   		// Générer le fichier SVG
	   		fichierSvg = new FichierImageArbre(fichierNewick);
	   		fichierSvg.genererImageArbreUpgma(cheminNewick, cheminSvg);        
    		JOptionPane.showMessageDialog(null, "Arbre généré avec succès !\n" +
	   				"Arbre enregistré dans : " + cheminSvg + "\n");
	    } catch (IllegalStateException ex) {
	    	JOptionPane.showMessageDialog(null, ex.getMessage(), "Erreur d'alignement", JOptionPane.ERROR_MESSAGE);
	   	} catch (IOException ioEx) {
	   		JOptionPane.showMessageDialog(null, "Sauvegarde annulée.", "Info", JOptionPane.WARNING_MESSAGE);
	   	} catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Erreur lors de la génération de l'arbre UPGMA.", "Erreur", JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	// Méthode qui vérifie que l'alignement est valide, crée la matrice, exécute l'algo NJ, crée le format de Newick et l'image SVG de l'arbre
	private void executerAlgoNj() {
		try {
			verifierAlignement();
	        if (isProtein) {
	            matrice = new MatriceDeDistanceProteine();
	            ((MatriceDeDistanceProteine) matrice).ajouterDistancesObserveesProteines(alignementProteine);
	            ((MatriceDeDistanceProteine) matrice).ajouterNomsSequences(alignementProteine);
	        } else if (isDna) {
	            matrice = new MatriceDeDistanceAdn();
	          //((MatriceDeDistanceAdn) matrice).ajouterDistancesEvolutivesAdn(alignementAdn);
	            ((MatriceDeDistanceAdn) matrice).ajouterDistancesObserveesAdn(alignementAdn);
	            ((MatriceDeDistanceAdn) matrice).ajouterNomsSequences(alignementAdn);
	        } else {
	            JOptionPane.showMessageDialog(null, "Aucun alignement trouvé", "Erreur", JOptionPane.ERROR_MESSAGE);
	            return;
	        }
	        matrice.afficherMatrice();        
	        nj = new AlgoNj(matrice); // Création de l'arbre NJ	                
	        fichierNewick = new FichierFormatNewick(); // Générer le format Newick
	        String newick = fichierNewick.genererNewick(nj);
	        System.out.println("Format Newick NJ généré : " + newick);		        
	        String cheminNewick = demanderCheminFichier("Enregistrer l'arbre NJ au format de Newick");	                
	        fichierNewick.setFormatNewick(newick); 
	        fichierNewick.genererFichierNewick(cheminNewick);        	       
	        String cheminSvg = cheminNewick.replace(".nwk",".svg");        
	        fichierSvg = new FichierImageArbre(fichierNewick); // Générer le fichier SVG
	        fichierSvg.genererImageArbreNj(cheminNewick, cheminSvg);	        
	        JOptionPane.showMessageDialog(null, "Arbre généré avec succès !\n" +
	        "Format de Newick et image SVG enregistrés dans : " + cheminNewick + "\n");
		} catch (IllegalStateException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Erreur d'alignement", JOptionPane.ERROR_MESSAGE);
		} catch (IOException ioEx) {
			JOptionPane.showMessageDialog(null, "Sauvegarde annulée.", "Info", JOptionPane.WARNING_MESSAGE);
	    } catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Erreur lors de la génération de l'arbre NJ.", "Erreur", JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	// Méthode qui vérifie que l'alignement est réalisé
	private void verifierAlignement() {
	    if (isProtein && alignementProteine == null) {
	        throw new IllegalStateException("Erreur : alignement protéique non réalisé.");
	    } else if (isDna && alignementAdn == null) {
	        throw new IllegalStateException("Erreur : alignement ADN non réalisé.");
	    }
	}
	
	// Méthode qui renvoie le chemin choisit par l'utilisateur dans la boite de dialogue
	private String demanderCheminFichier(String titre) throws IOException {
	    JFileChooser fileChooser = new JFileChooser();
	    fileChooser.setDialogTitle(titre);
	    int userSelection = fileChooser.showSaveDialog(null);
	    if (userSelection == JFileChooser.APPROVE_OPTION) {
	        String chemin = fileChooser.getSelectedFile().getAbsolutePath();        
	        if (!chemin.endsWith(".nwk")) { // Si l'utilisateur n'a pas spécifié l'extension, l'ajouter automatiquement
	            chemin += ".nwk";
	        }
	        return chemin;
	    } else {
	        throw new IOException("Sauvegarde annulée par l'utilisateur.");
	    }
	}
}