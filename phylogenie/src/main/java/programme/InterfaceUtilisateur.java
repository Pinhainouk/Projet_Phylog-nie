package programme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.biojava.nbio.core.alignment.template.Profile;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;

import alignement.AlignementMultiple;
import alignement.AlignementMultipleAdn;
import alignement.AlignementMultipleProteine;
import arbre.AlgoNj;
import arbre.AlgoUpgma;
import arbre.FichierFormatNewick;
import arbre.FichierImageArbre;
import arbre.MatriceDeDistances;
import arbre.MatriceDeDistancesAdn;
import arbre.MatriceDeDistancesProteine;

import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;

/**
 * La classe InterfaceUtilisateur fournit une interface graphique utilisateur conviviale
 * pour un logiciel de reconstruction d'arbres phylogénétiques.
 * Elle permet aux utilisateurs de sélectionner le type de séquences (protéiques ou ADN)
 * d'aligner les séquences, de choisir l'algorithme (UPGMA ou NJ) 
 * pour reconstruire un arbre phylogénétique.
 * 
 */
public class InterfaceUtilisateur {
	private static boolean isProtein = false;
	private static boolean isDna = false;
	private Profile<ProteinSequence, AminoAcidCompound> alignementProteine;
	private Profile<DNASequence, NucleotideCompound> alignementAdn;
	private MatriceDeDistances matrice;
	private JRadioButton upgmaRadioBouton; 
	private JRadioButton njRadioBouton;    
	private AlgoUpgma upgma;
	private AlgoNj nj;
	private FichierFormatNewick fichierNewick;
	private FichierImageArbre fichierSvg;
	
	/**
     * Constructeur de la classe InterfaceUtilisateur.
     */
	 public InterfaceUtilisateur() {
	 }
	
	 /**
	     * Crée la fenêtre principale de l'interface utilisateur.
	     *
	     * @param nomFenetrePrincipale Le nom de la fenêtre principale.
	     * @param titreLogiciel Le titre du logiciel.
	     * @return La fenêtre principale créée.
	     */
	public JFrame creerFenetrePrincipale(String nomFenetrePrincipale, String titreLogiciel) { 
		JFrame fenetrePrincipale = new JFrame(nomFenetrePrincipale);
	    fenetrePrincipale.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    fenetrePrincipale.setSize(1920, 1080); // taille écran 14 pouces       
	    JPanel panelPrincipal = creerPanelPrincipal(titreLogiciel); // Ajouter le panel principal
	    panelPrincipal.setBackground(new java.awt.Color(28,144,153));
	    JPanel panelChoix = creerPanelChoix("Choisir le type de séquences à aligner : ", // Ajouter le panel de choix
		        "Protéines", e-> choixProteines(fenetrePrincipale), 
		        "ADN", e-> choixNucleotides(fenetrePrincipale));
		fenetrePrincipale.setLayout(new BorderLayout()); // Ajouter le panel principal à la fenêtre
	    fenetrePrincipale.add(panelPrincipal, BorderLayout.NORTH); 
	    fenetrePrincipale.add(panelChoix, BorderLayout.CENTER);
	    JPanel panelBoutonAide = creerPanelBoutonAide(); // Créer le bouton aide
	    fenetrePrincipale.add(panelBoutonAide, BorderLayout.SOUTH);
	    fenetrePrincipale.setVisible(true);
	    return fenetrePrincipale;
	}
	
	/**
     * Crée la fenêtre secondaire avec 2 boutons et le bouton retour.
     *
     * @param nomFenetreSecondaire2 Le nom de la fenêtre secondaire.
     * @param titreLogiciel Le titre du logiciel.
     * @param sousTitreChoix Le sous-titre pour les choix.
     * @param bouton1 Le nom du premier bouton.
     * @param actionBouton1 L'action à réaliser lors du clic sur le premier bouton.
     * @param bouton2 Le nom du deuxième bouton.
     * @param actionBouton2 L'action à réaliser lors du clic sur le deuxième bouton.
     * @param fenetrePrincipale La fenêtre principale.
     * @param boutonRetour Le nom du bouton de retour.
     * @param actionBoutonRetour L'action à réaliser lors du clic sur le bouton de retour.
     * @return La fenêtre secondaire créée.
     */
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
	
	/**
     * Crée le panel principal avec le titre du logiciel.
     *
     * @param titre Le titre à afficher dans le panel.
     * @return Le panel principal créé.
     */  
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
	
	/**
     * Crée le panel avec les boutons et le titre de l'action à réaliser dans la fenêtre.
     *
     * @param titreChoix Le titre des choix à afficher.
     * @param nomBouton1 Le nom du premier bouton.
     * @param actionBouton1 L'action à réaliser lors du clic sur le premier bouton.
     * @param nomBouton2 Le nom du deuxième bouton.
     * @param actionBouton2 L'action à réaliser lors du clic sur le deuxième bouton.
     * @return Le panel de choix créé.
     */
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
	
	/**
     * Crée le panel du bouton aide.
     *
     * @return Le panel du bouton aide créé.
     */
	private static JPanel creerPanelBoutonAide() { 
        JPanel panelBoutonAide = new JPanel();
        panelBoutonAide.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 50));
        JButton boutonAide = new JButton("Aide ?");
        boutonAide.setFont(new Font("TimesRoman", Font.ITALIC, 24));
        boutonAide.setForeground(Color.BLACK);
        boutonAide.setBackground(new java.awt.Color(28,144,153));
        boutonAide.setPreferredSize(new Dimension(200, 100));
        boutonAide.addActionListener(e -> afficherMessageAide());
        panelBoutonAide.add(boutonAide);
        return panelBoutonAide;
    }
	
	/**
     * Affiche un message d'aide dans une fenêtre d'information.
     */
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
                       + "au format image SVG au même emplacement. \n"
                       + "L'image de l'arbre s'ouvre automatique après l'enregistrement.";
        JOptionPane.showMessageDialog(null, message, "Aide", JOptionPane.INFORMATION_MESSAGE);
    }
	
	/**
     * Crée un panneau avec deux boutons.
     *
     * @param nomBouton1 le nom du premier bouton
     * @param actionBouton1 l'action à effectuer lorsque le premier bouton est cliqué
     * @param nomBouton2 le nom du second bouton
     * @param actionBouton2 l'action à effectuer lorsque le second bouton est cliqué
     * @return Le panneau de boutons créé.
     */
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
	
	 /**
     * Crée le panneau pour le bouton de retour.
     *
     * @param parentFenetre la fenêtre parent à laquelle ce panneau sera lié
     * @return Le panneau contenant le bouton de retour.
     */
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
	
	/**
     * Crée le panneau pour le bouton de l'arbre.
     *
     * @param nomBoutonArbre le nom du bouton de l'arbre
     * @param actionBoutonArbre l'action à effectuer lorsque le bouton est cliqué
     * @return Le panneau contenant le bouton d'arbre.
     */	
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
		
		
	 /**
     * Crée un panneau pour la fenêtre de création de l'arbre avec les choix de saisie pour faire l'alignement.
     *
     * @param nomSaisie1 le nom du premier champ de saisie
     * @param saisie le champ de texte pour la saisie
     * @param actionBoutonValider l'action à effectuer lorsque le bouton "Aligner" est cliqué
     * @param nomSaisie2 le nom du second champ de saisie
     * @param nomBoutonRadio1 le nom du premier bouton radio
     * @param nomBoutonRadio2 le nom du second bouton radio
     * @param nomBouton1 le nom du bouton pour générer l'arbre
     * @param actionBouton1 l'action à effectuer lorsque le bouton pour générer l'arbre est cliqué
     * @return Le panneau de champs avec labels créé.
     */
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
	
	/**
     * Crée un panneau pour le champ de saisie avec un label.
     *
     * @param nomSaisie1 le nom du champ de saisie
     * @param saisie le champ de texte pour la saisie
     * @param actionBoutonValider l'action à effectuer lorsque le bouton "Aligner" est cliqué
     * @return Le panneau de saisie avec label créé.
     */
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
	
	/**
     * Crée un panneau pour les boutons radio avec un label.
     *
     * @param nomSaisie2 le nom du champ de saisie associé aux boutons radio
     * @param nomBoutonRadio1 le nom du premier bouton radio
     * @param nomBoutonRadio2 le nom du second bouton radio
     * @return Le panneau contenant les boutons radio créé.
     */
	private JPanel creerPanelBoutonsRadio(String nomSaisie2, String nomBoutonRadio1, 
		String nomBoutonRadio2) { 
		JPanel panelRadio = new JPanel(); // Créer le panel boutons radio
		panelRadio.setLayout(new BoxLayout(panelRadio, BoxLayout.Y_AXIS));	
		JLabel label2 = new JLabel(nomSaisie2); // Créer le label 2
		label2.setFont(new Font("TimesRoman", Font.BOLD, 26));
		label2.setAlignmentX(Component.CENTER_ALIGNMENT);  
		
	    Icon customIcon = new Icon() { // Créer une icône personnalisée pour les ronds des boutons radios
	    private int size = 28; // Taille du rond (peut être ajustée)
	    public void paintIcon(Component c, Graphics g, int x, int y) { // Dessiner le rond du bouton radio           
            g.setColor(Color.BLACK);
            g.drawOval(x, y, size, size);         
            if (((JRadioButton) c).isSelected()) { // Si sélectionné, remplir le rond
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
	
	/**
     * Ouvre une fenêtre secondaire avec deux choix de chargement des séquences si on clique sur Protéines.
     *
     * @param parentFenetre la fenêtre parent à laquelle cette nouvelle fenêtre sera liée
     */
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
	
	/**
     * Ouvre une fenêtre secondaire avec deux choix de chargement des séquences si on clique sur Nucléotides.
     *
     * @param parentFenetre la fenêtre parent à laquelle cette nouvelle fenêtre sera liée
     */
	private void choixNucleotides(JFrame parentFenetre) {
		isProtein = false;
		isDna = true;
		JFrame fenetreADN = creerFenetreSecondaire("Séquences ADN", 
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
	
	/**
     * Ouvre la fenêtre de saisie des numéros d'accessions.
     *
     * @param parentFenetre la fenêtre parent à laquelle cette nouvelle fenêtre sera liée
     */
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
        JPanel panelChamps = creerPanelChampsAvecLabel("Saisir les numéros d'accessions (séparés par un espace) et cliquer sur -Aligner- : ", numAccessions, actionNumAccessions, 
        		"Choisir l'algorithme de reconstruction de l'arbre et cliquer sur -Générer l'arbre- : ", "UPGMA", "NJ", "Générer l'arbre (Newick et SVG)", actionGenererArbre); // Créer le panel des choix et l'ajouter à la fenêtre
        fenetreNumAccessions.add(panelChamps, BorderLayout.CENTER);
        fenetreNumAccessions.add(panelChamps);
        JPanel panelBoutonRetour = creerPanelBoutonRetour(fenetreNumAccessions);
        fenetreNumAccessions.add(panelBoutonRetour, BorderLayout.SOUTH);
        fenetreNumAccessions.setVisible(true);
        parentFenetre.setVisible(false);
    }
	
	/**
     * Ouvre la fenêtre de saisie du fichier fasta.
     *
     * @param parentFenetre la fenêtre parent à laquelle cette nouvelle fenêtre sera liée
     */
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
	    JPanel panelChamps = creerPanelChampsAvecLabel("Saisir le chemin du fichier fasta (chemin absolu) et cliquer sur -Aligner- : ", fasta, actionFasta, 
		        		"Choisir l'algorithme de reconstruction de l'arbre et cliquer sur -Générer l'arbre- : ", "UPGMA", "NJ", "Générer l'arbre (Newick et SVG)", actionGenererArbre); // Créer le panel des choix sous le panel principal et l'ajouter à la fenêtre
		fenetreFasta.add(panelChamps, BorderLayout.CENTER);
		fenetreFasta.add(panelChamps);
		JPanel panelBoutonRetour = creerPanelBoutonRetour(fenetreFasta);
		fenetreFasta.add(panelBoutonRetour, BorderLayout.SOUTH);
		fenetreFasta.setVisible(true);
		parentFenetre.setVisible(false);
	}
			
	/**
     * Crée l'action de traiter les numéros d'accessions saisis.
     *
     * @param numAccessions le champ de texte contenant les numéros d'accessions saisis
     * @return L'action à exécuter lorsque le bouton "Aligner" est cliqué.
     */
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
	
	/**
     * Crée l'action de traiter la saisie du chemin du fichier fasta.
     *
     * @param fasta le champ de texte contenant le chemin du fichier fasta saisi
     * @return L'action à exécuter lorsque le bouton "Aligner" est cliqué.
     */
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
	
	/**
     * Génère l'arbre en fonction des séquences alignées et de l'algorithme sélectionné.
     *
     * @return L'action à exécuter lorsque le bouton "Générer l'arbre" est cliqué.
     */
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
	            JOptionPane.showMessageDialog(null, ex.getMessage(), "Erreur d'alignement", JOptionPane.ERROR_MESSAGE);
			} catch(Exception e) {
			e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Erreur lors de la génération de l'arbre.", "Erreur", JOptionPane.ERROR_MESSAGE);		
			}
		};	
	}
	
	/**
     * Vérifie que l'alignement est valide et exécute l'algorithme UPGMA pour générer un arbre.
     *
     * @throws IllegalStateException si l'alignement n'est pas valide
     */
	private void executerAlgoUpgma() {    
	    try {
	    	verifierAlignement();
	    	if (isDna) {
	            System.out.println("Alignement ADN présent : " + (alignementAdn != null)); // Vérification si l'alignement est null avant d'exécuter les calculs
	        }
	    	if (isProtein) {
	    		matrice = new MatriceDeDistancesProteine();
	    		((MatriceDeDistancesProteine) matrice).ajouterDistancesObserveesProteines(alignementProteine);
	    		((MatriceDeDistancesProteine) matrice).ajouterNomsSequences(alignementProteine);
	    	} else if (isDna) {
	    		matrice = new MatriceDeDistancesAdn();
	    		((MatriceDeDistancesAdn) matrice).ajouterDistancesEvolutivesAdn(alignementAdn); // distances évolutives ADN
	    		//((MatriceDeDistancesAdn) matrice).ajouterDistancesObserveesAdn(alignementAdn); // distances observées ADN
	    		((MatriceDeDistancesAdn) matrice).ajouterNomsSequences(alignementAdn);
	    	} else {
	    		JOptionPane.showMessageDialog(null, "Aucun alignement trouvé", "Erreur", JOptionPane.ERROR_MESSAGE);
	    		return;
	    	}
	    	matrice.afficherMatrice();        
	    	upgma = new AlgoUpgma(matrice); // Création de l'arbre UPGMA	       	        
	    	fichierNewick = new FichierFormatNewick(); // Générer le format Newick
	    	String newick = fichierNewick.genererNewick(upgma);
	    	System.out.println("Format Newick UPGMA généré : " + newick);	        
	    	String cheminNewick = demanderCheminFichier("Enregistrer l'arbre UPGMA au format de Newick et SVG");
	    	fichierNewick.setFormatNewick(newick);
	    	fichierNewick.genererFichierNewick(cheminNewick);        	      	        
	   		String cheminSvg = cheminNewick.replace(".nwk",".svg");  // Chemin du fichier SVG (automatiquement enregistré au même chemin que l'utilisateur a choisi pour Newick)       
	   		// Générer le fichier SVG
	   		fichierSvg = new FichierImageArbre(fichierNewick);
	   		fichierSvg.genererImageArbreUpgma(cheminNewick, cheminSvg);        
    		JOptionPane.showMessageDialog(null, "Arbre généré avec succès !\n" +
    				"Format de Newick et format SVG de l'arbre enregistrés dans " + cheminNewick + "\n");
	    } catch (IllegalStateException ex) {
	    	JOptionPane.showMessageDialog(null, ex.getMessage(), "Erreur d'alignement", JOptionPane.ERROR_MESSAGE);
	   	} catch (IOException ioEx) {
	   		JOptionPane.showMessageDialog(null, "Sauvegarde annulée.", "Info", JOptionPane.WARNING_MESSAGE);
	   	} catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Erreur lors de la génération de l'arbre UPGMA.", "Erreur", JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	/**
     * Vérifie que l'alignement est valide et exécute l'algorithme NJ pour générer un arbre.
     *
     * @throws IllegalStateException si l'alignement n'est pas valide
     */
	private void executerAlgoNj() {
		try {
			verifierAlignement();
	        if (isProtein) {
	            matrice = new MatriceDeDistancesProteine();
	            ((MatriceDeDistancesProteine) matrice).ajouterDistancesObserveesProteines(alignementProteine);
	            ((MatriceDeDistancesProteine) matrice).ajouterNomsSequences(alignementProteine);
	        } else if (isDna) {
	            matrice = new MatriceDeDistancesAdn();
	            ((MatriceDeDistancesAdn) matrice).ajouterDistancesEvolutivesAdn(alignementAdn); // distances évolutives ADN
	            //((MatriceDeDistancesAdn) matrice).ajouterDistancesObserveesAdn(alignementAdn); //distances observées ADN 
	            ((MatriceDeDistancesAdn) matrice).ajouterNomsSequences(alignementAdn);
	        } else {
	            JOptionPane.showMessageDialog(null, "Aucun alignement trouvé", "Erreur", JOptionPane.ERROR_MESSAGE);
	            return;
	        }
	        matrice.afficherMatrice();        
	        nj = new AlgoNj(matrice); // Création de l'arbre NJ	                
	        fichierNewick = new FichierFormatNewick(); // Générer le format Newick
	        String newick = fichierNewick.genererNewick(nj);
	        System.out.println("Format Newick NJ généré : " + newick);		        
	        String cheminNewick = demanderCheminFichier("Enregistrer l'arbre NJ au format de Newick et SVG");	                
	        fichierNewick.setFormatNewick(newick); 
	        fichierNewick.genererFichierNewick(cheminNewick);        	       
	        String cheminSvg = cheminNewick.replace(".nwk",".svg");        
	        fichierSvg = new FichierImageArbre(fichierNewick); // Générer le fichier SVG
	        fichierSvg.genererImageArbreNjEnracine(cheminNewick, cheminSvg); // pour générer NJ enraciné point moyen
	        //fichierSvg.genererImageArbreNjNonEnracine(cheminNewick, cheminSvg); // pour générer NJ non enraciné
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
	
	/**
     * Vérifie que l'alignement a été réalisé.
     *
     * @throws IllegalStateException si l'alignement protéique n'est pas réalisé 
     *         alors que l'objet est une protéine.
     * @throws IllegalStateException si l'alignement ADN n'est pas réalisé 
     *         alors que l'objet est de l'ADN.
     */
	private void verifierAlignement() {
	    if (isProtein && alignementProteine == null) {
	        throw new IllegalStateException("Erreur : alignement protéique non réalisé.");
	    } else if (isDna && alignementAdn == null) {
	        throw new IllegalStateException("Erreur : alignement ADN non réalisé.");
	    }
	}
	
	/**
    * Demande à l'utilisateur de choisir un chemin de fichier 
    * pour enregistrer le résultat.
    *
    * @param titre Le titre de la boîte de dialogue pour le choix du chemin.
    * @return Le chemin du fichier choisi par l'utilisateur.
    * @throws IOException si la sauvegarde est annulée par l'utilisateur.
    */
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