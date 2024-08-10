package programme.utilisateur;
import javax.swing.*;

import alignement.sequences.AlignementMultiple;
import alignement.sequences.AlignementMultipleAdn;
import alignement.sequences.AlignementMultipleProteine;

import java.awt.*;
//import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class InterfaceUtilisateur {
	private static boolean isProtein = false;
	
	public static JFrame creerFenetrePrincipale(String nomFenetrePrincipale, String titreLogiciel) {
		JFrame fenetrePrincipale = new JFrame(nomFenetrePrincipale);
	    fenetrePrincipale.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    fenetrePrincipale.setSize(1200, 1200);
	    // Créer le panel principal
	    JPanel panelPrincipal = creerPanelPrincipal(titreLogiciel);
		panelPrincipal.setBackground(new java.awt.Color(28,144,153));
		JPanel panelChoix = creerPanelChoix("Choix du type de séquences : ",
		        "Protéines", e-> choixProteines(fenetrePrincipale), 
		        "Nucléotides", e-> choixNucleotides(fenetrePrincipale));
	    // Ajouter le panel principal à la fenêtre
		fenetrePrincipale.setLayout(new BorderLayout());
	    fenetrePrincipale.add(panelPrincipal, BorderLayout.NORTH); 
	    fenetrePrincipale.add(panelChoix, BorderLayout.CENTER);
	    fenetrePrincipale.setVisible(true);
	    return fenetrePrincipale;
	}
	
	public static JFrame creerFenetreSecondaire(String nomFenetreSecondaire2, String titreLogiciel, String sousTitreChoix, 
			String bouton1, ActionListener actionBouton1,
			String bouton2, ActionListener actionBouton2,  JFrame fenetrePrincipale, String boutonRetour, ActionListener actionBoutonRetour) {
		JFrame fenetreSecondaire = new JFrame(nomFenetreSecondaire2);
		fenetreSecondaire.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		fenetreSecondaire.setSize(1200, 1200);
		JPanel panelPrincipal = creerPanelPrincipal(titreLogiciel);
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

	private static JPanel creerPanelPrincipal(String titre) {
        // Panel principal avec BoxLayout pour disposer les composants verticalement
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        Font policeTitre = new Font("TimesRoman", Font.BOLD, 36);
        JLabel label = new JLabel(titre, JLabel.CENTER);
        label.setFont(policeTitre);
        label.setForeground(Color.BLACK);
        // Ajout d'espacement vertical entre les labels
        label.setAlignmentX(Component.CENTER_ALIGNMENT);  
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 100))); // hauteur d'espace
        panelPrincipal.add(label);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 100))); // hauteur d'espace
        return panelPrincipal;
    }
	
	private static JPanel creerPanelChoix(String titreChoix, String nomBouton1, ActionListener actionBouton1, 
		String nomBouton2, ActionListener actionBouton2) {
		JPanel panelChoix = new JPanel();
		panelChoix.setLayout(new BoxLayout(panelChoix, BoxLayout.Y_AXIS));
		Font policeSousTitre = new Font("TimesRoman", Font.BOLD, 26);
		JLabel sousTitre = new JLabel(titreChoix, JLabel.CENTER);  
		sousTitre.setFont(policeSousTitre);
		sousTitre.setForeground(Color.BLACK); 
		sousTitre.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelChoix.add(Box.createRigidArea(new Dimension(0, 50)));  
		panelChoix.add(sousTitre);
		panelChoix.add(Box.createRigidArea(new Dimension(0, 50)));  
		// Ajouter les boutons au panel choix
		panelChoix.add(creerPanel2Boutons(nomBouton1, actionBouton1, nomBouton2, actionBouton2));
		return panelChoix;
	}
	
	private static JPanel creerPanel2Boutons(String nomBouton1, ActionListener actionBouton1, String nomBouton2, ActionListener actionBouton2) {
	    // Panel des boutons
	    JPanel panelBoutons = new JPanel(); 
	    Font policeBouton = new Font("TimesRoman", Font.BOLD, 24);
	    JButton bouton1 = new JButton(nomBouton1);
	    JButton bouton2 = new JButton(nomBouton2);	        
	    bouton1.setFont(policeBouton);
	    bouton2.setFont(policeBouton);	        
	    // Changer la couleur du texte et la couleur de fond des boutons
	    bouton1.setForeground(Color.BLACK);
	    bouton1.setBackground(new java.awt.Color(28,144,153));
	    bouton2.setForeground(Color.BLACK);
	    bouton2.setBackground(new java.awt.Color(28,144,153));	      
	    Dimension tailleBouton = new Dimension(400, 100);
	    bouton1.setPreferredSize(tailleBouton);
	    bouton2.setPreferredSize(tailleBouton);	
	    panelBoutons.add(Box.createRigidArea(new Dimension(50, 50))); 
	    panelBoutons.add(bouton1);
	    panelBoutons.add(Box.createRigidArea(new Dimension(50, 50))); 
	    panelBoutons.add(bouton2);
	    // Ajouter les actions des boutons
	    bouton1.addActionListener(actionBouton1);
	    bouton2.addActionListener(actionBouton2);
	    return panelBoutons;
	}
	
	private static JPanel creerPanelBoutonRetour(JFrame parentFenetre) {
		JPanel panelBoutonRetour = new JPanel();    
		panelBoutonRetour.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 50));
	    //panelBoutons.setLayout(new GridLayout(2, 1, 100, 100));   
	    Font policeBouton = new Font("TimesRoman", Font.ITALIC, 24);
	    JButton boutonRetour = new JButton("Retour");        
	    boutonRetour.setFont(policeBouton);
	    // Changer la couleur du texte et la couleur de fond des boutons
	    boutonRetour.setForeground(Color.BLACK);
	    boutonRetour.setBackground(new java.awt.Color(189, 189, 189));	      
	    Dimension tailleBouton = new Dimension(400, 100);
	    boutonRetour.setPreferredSize(tailleBouton);
	    panelBoutonRetour.add(boutonRetour);
	    boutonRetour.addActionListener(e -> {
	        parentFenetre.setVisible(true);
	        ((JFrame) SwingUtilities.getWindowAncestor(boutonRetour)).dispose();
	    });
	    return panelBoutonRetour;
	}
	
	private static JPanel creerPanelChampsAvecLabel(String nomSaisie, JTextField saisie, ActionListener actionBoutonValider) {
	    JPanel panelChamps = new JPanel();
	    panelChamps.setLayout(new BoxLayout(panelChamps, BoxLayout.Y_AXIS));
	    JLabel label = new JLabel(nomSaisie);
	    label.setFont(new Font("TimesRoman", Font.BOLD, 26));
	    label.setAlignmentX(Component.CENTER_ALIGNMENT);
	    //JTextField numAccessions = new JTextField(80); // Taille ajustée
	    saisie.setBackground(Color.white);
	    saisie.setPreferredSize(new Dimension(100, 50)); // Largeur maximale ajustée, hauteur ajustée
	    Font policeBoutonValider = new Font("TimesRoman", Font.BOLD, 20);
	    JButton boutonValider = new JButton("Valider");
	    boutonValider.setFont(policeBoutonValider);
	    boutonValider.setBackground(new java.awt.Color(28,144,153));
	    boutonValider.setPreferredSize(new Dimension(120,60));
	    boutonValider.addActionListener(actionBoutonValider);       
	    JPanel panelSaisie = new JPanel();
	    panelSaisie.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Espacement ajusté
	    panelSaisie.add(saisie);
	    panelSaisie.add(boutonValider);
	    panelChamps.add(Box.createRigidArea(new Dimension(0, 50)));  
	    panelChamps.add(label);
	    panelChamps.add(Box.createRigidArea(new Dimension(0, 50))); // Espacement entre le label et le champ de saisie
	    panelChamps.add(panelSaisie); // Ajout du panel de saisie avec champ de texte et bouton
	    return panelChamps;
	}
	
	private static void choixProteines(JFrame parentFenetre) {
		isProtein = true;
		JFrame fenetreProteine = creerFenetreSecondaire("Séquences protéiques", 
				"Logiciel de reconstruction d'arbres phylogénétiques", 
				"Chargement des séquences : ", 
				"N° accession UniProt",
				e -> choixNumAccessions(parentFenetre), // saisie numeros d'accessions : méthode
				"Fichier fasta",
				e -> choixFasta(parentFenetre), parentFenetre, null, null);
		fenetreProteine.setVisible(true);
        parentFenetre.setVisible(false);
	}
	
	private static void choixNucleotides(JFrame parentFenetre) {
		isProtein = false;
		JFrame fenetreADN = creerFenetreSecondaire("Séquences nucléotidiques", 
				"Logiciel de reconstruction d'arbres phylogénétiques", 
				"Chargement des séquences : ", 
				"N° accession Genbank", 
				e -> choixNumAccessions(parentFenetre), // saisie numeros d'accessions : méthode
				"Fichier fasta", 
				e -> choixFasta(parentFenetre), // chargement du fasta
				parentFenetre, null, null);
		fenetreADN.setVisible(true);
		parentFenetre.setVisible(false);
	}
	
	private static void choixNumAccessions(JFrame parentFenetre) {
        JFrame fenetreNumAccessions = new JFrame("Numéros d'accessions");
        fenetreNumAccessions.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fenetreNumAccessions.setSize(1200, 1200);
        JPanel panelPrincipal = creerPanelPrincipal("Logiciel de reconstruction d'arbres phylogénétiques");
        panelPrincipal.setBackground(new java.awt.Color(28,144,153));
        fenetreNumAccessions.add(panelPrincipal, BorderLayout.NORTH);
        JTextField numAccessions = new JTextField(80);
        ActionListener actionNumAccessions = e -> { 
        	ArrayList<String> numerosAccessions = AlignementMultiple.traiterSaisieNumAccessions(numAccessions);
            if (!numerosAccessions.isEmpty()) {
                if (isProtein) {
                    try {
						AlignementMultipleProteine.multipleAlignementProteine(numerosAccessions);
					} catch (Exception e1) {
						gestionException(e1, "Erreur -> lors de l'alignement multiple des séquences protéiques.");
					}
                } else {
                	try {
						AlignementMultipleAdn.multipleAlignementAdn(numerosAccessions);
					} catch (Exception e1) {
						gestionException(e1, "Erreur -> lors de l'alignement multiple des séquences d'ADN.");
					}
                }
            }
        };
        // Ajouter le panneau des champs avec le label et l'ActionListener
        JPanel panelChamps = creerPanelChampsAvecLabel("Saisie des numéros d'accessions (séparés par un espace) : ", numAccessions, actionNumAccessions);
        fenetreNumAccessions.add(panelChamps, BorderLayout.CENTER);       
        JPanel panelBoutonRetour = creerPanelBoutonRetour(fenetreNumAccessions);
        fenetreNumAccessions.add(panelBoutonRetour, BorderLayout.SOUTH);
        fenetreNumAccessions.setVisible(true);
        parentFenetre.setVisible(false);
    }
        
	private static void choixFasta(JFrame parentFenetre) {
		JFrame fenetreFasta = new JFrame ("Fichier fasta");
		fenetreFasta.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		fenetreFasta.setSize(1200, 1200);
		JPanel panelPrincipal = creerPanelPrincipal("Logiciel de reconstruction d'arbres phylogénétiques");
        panelPrincipal.setBackground(new java.awt.Color(28,144,153));
        fenetreFasta.add(panelPrincipal, BorderLayout.NORTH);
        JTextField fasta = new JTextField(80);
        ActionListener actionFasta = e -> {
        	File fichierFasta = AlignementMultiple.traiterCheminFasta(fasta);
        	if (fichierFasta != null) {
        		if (isProtein) {
        			try {
						AlignementMultipleProteine.multipleAlignementProteine(fichierFasta);
					} catch (Exception e1) {
						gestionException(e1, "Erreur -> lors de l'alignement multiple des séquences protéiques.");
						e1.printStackTrace();
					}
        		} else {
        			try {
						AlignementMultipleAdn.multipleAlignementAdn(fichierFasta);
					} catch (Exception e1) {
						gestionException(e1, "Erreur -> lors de l'alignement multiple des séquences d'ADN.");
						e1.printStackTrace();
					}
        		}
        	}
        };
        // Ajouter le panneau des champs avec le label
        JPanel panelChamps = creerPanelChampsAvecLabel("Saisie du chemin du fichier fasta (chemin absolu) : ", fasta, actionFasta);
        fenetreFasta.add(panelChamps, BorderLayout.CENTER);
        JPanel panelBoutonRetour = creerPanelBoutonRetour(fenetreFasta);
        fenetreFasta.add(panelBoutonRetour, BorderLayout.SOUTH);
        fenetreFasta.setVisible(true);
        parentFenetre.setVisible(false);
    }
	
	 private static void gestionException(Exception e, String message) {
	        // Afficher un message d'erreur à l'utilisateur
	        JOptionPane.showMessageDialog(null, message + "\n" + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
	    }
}