# **README : Logiciel de reconstruction d'arbres phylogénétiques.**

## **Description**
Ce document détaille le fonctionnement du logiciel codé en Java, pour réaliser une phylogénie selon deux algorithmes de reconstruction basés sur les distances : UPGMA ou NJ.

## **Les différentes étapes :**
* Se munir d'un jeu de données de séquences (protéines ou ADN) au format FASTA ou de leurs numéros d'accession (UniProt ou GenBank).

    Le jeu de données utilisé pour tester le programme se compose :
    - des séquences protéiques d'Env entières des sous-types A, B, C, D, N, O du VIH-1, des sous-types A et B du VIH-2 et du VIS du chimpanzé et du macaque, 
    - des tronçons 1-100, 101-200, 201-300, 301-400 de ces mêmes séquences pour tous ces virus,
    - des protéines gp120, gp41 et tat de ces mêmes virus, 
    - des tronçons des séquences ADN d'Env 300-600 et 600-900 de ces mêmes virus.

    Les tronçons ont été obtenus à l'aide des scripts Bash:  `split_proteines_troncons.sh`, `split_adn_troncons.sh `, `concat_troncons.sh`.

* Exécuter le logiciel dans Eclipse. L'interface s'ouvre.

* Choisir dans la fenêtre principale, le type de séquences à aligner pour réaliser la phylogénie en cliquant sur l'un des deux boutons : `protéines` ou `ADN`.
En bas de cette fenêtre, un bouton `aide`, récapitulant les étapes, est disponible pour l'utilisateur.

* Une nouvelle fenêtre s'ouvre, choisir le type de chargement des séquences en cliquant sur l'un des deux boutons : soit par `N° d'accession`, soit par `fichier FASTA`.

* Une nouvelle fenêtre s'ouvre avec un champ de texte dans lequel, selon le choix précédent, il faut saisir : 
    - soit les numéros d'accession (séparés par un espace), 
    -   soit le chemin absolu du fichier FASTA (CTRL+V, le clic droit ne fonctionne pas pour coller) 

    Cliquer sur `Aligner`.
Une boite de dialogue s'ouvre indiquant le succès ou une erreur d'alignement.

    Cliquer sur le bouton radio correspond à l'algorithme de reconstruction choisi : 
    - `UPGMA`
    -  `NJ` 
    
    Attention, si aucun alignement n'est réalisé avant, une boite de dialogue indiquant une erreur s'ouvre.

    Cliquer  sur `Générer l'arbre`.
    
    Une boite de dialogue s'ouvre invitant l'utilisateur à choisir l'emplacement pour la sauvegarde de l'arbre dans deux types de fichiers (au format de Newick et au format image SVG).

    L'utilisateur saisit le nom qu'il souhaite donner aux fichiers. Le même nom sera attribué aux deux fichiers avec deux extensions automatiquement créés (que l'utilisateur ne saisit pas) : `.nwk` pour le format de Newick et `.svg` pour le format image de l'arbre. 

    Le nom donné aux fichiers sera également le titre affiché sur l'image de l'arbre enraciné au format SVG généré par le script R selon l'algorithme choisi `script_root_upgma.R`, `script_root_nj.R`.
    Il est possible d'avoir l'arbre NJ non enraciné avec ce script `script_unroot_nj.R`.

    Une boite de dialogue s'ouvre indiquant le succès de l'enregistrement de ces fichiers.
 
* L'image de l'arbre s'ouvre automatiquement à l'écran.

## **Pré-requis à l'utilisation du logiciel :**
Ces outils doivent être installés pour que le logiciel fonctionne.

| Outils | Versions |
|----- |------
| Java | 17.0.12 |
| Eclipse | 4.32.0 |
| Maven | 4.0.0 |
| BioJava | 7.1.1 |
| R | 4.4.1 |

## **Problèmes rencontrés :**
Si les séquences à aligner sont trop longues (10 000 nucléotides pour la séquence ADN Env entière du VIH, par exemple), le programme s'arrête brutalement et une exception apparaît dans la console indiquant un problème de mémoire même en augmentant la taille du tas dans Eclipse.

## **Fichiers de sortie** :
Un fichier `.nwk` avec le format textuel de l'arbre et un fichier `.svg` avec l'image de l'arbre enregistré dans l'ordinateur de l'utilisateur à l'emplacement qu'il a choisi.

## **Glossaire :**
ADN : Acide DésoxyriboNucléique

FASTA : (ou Format Pearson) Format de fichier texte utilisé pour stocker des séquences biologiques de nature nucléique ou protéique.

NJ :  Neighbor-Joining

SVG : Scalable Vector Graphic

VIH-1 (HIV-1)	Virus de l’Immunodéficience Humaine de type 1 (Human Immunodeficiency Virus 1)

VIH-2 (HIV-2)	Virus de l’Immunodéficience Humaine de type 2 (Human Immunodeficiency Virus 2)

VIS (SIV)	Virus de l’Immunodéficience Simienne (Simian Immunodeficiency Virus) 

UPGMA : Unweighted Pair Group Method with mean Arithmetic

