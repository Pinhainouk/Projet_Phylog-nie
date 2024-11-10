# script_root.R

# Récupération des arguments de ligne de commande
args = commandArgs(trailingOnly = TRUE)
cheminFichierNewick <- args[1]
cheminFichierSvg <- args[2]
titre_arbre <- args[3]

# Chargement du package ape
library(ape)

# Lire l'arbre depuis le fichier Newick
arbre = read.tree(cheminFichierNewick)

# Ouvrir un périphérique graphique pour SVG
svg(cheminFichierSvg, width = 14, height = 10)

# Générer le graphique de l'arbre
plot(arbre, type = "u", main = titre_arbre, cex.main = 1.6, edge.width=2, cex = 0.8, tip.color = "aquamarine4", font = 4)
# axisPhylo()
# plot(tr,type="p",root.edge=TRUE) # default phylogramm
# plot(tr,type="c",root.edge=TRUE) # cladogramm
# plot(tr,type="f",root.edge=TRUE) # fan
# plot(tr,type="u",root.edge=TRUE) # unrooted
# plot(tr,type="r",root.edge=TRUE) # radial

# Fermer le périphérique graphique sans la sortie null device 1 qui indique que R a fermé le périphérique graphique.
invisible(dev.off())

# Valeurs bootstrap: calculs avec R Décomposition en clades
# boot.phylo(phy, x, FUN, B = 100, block = 1, trees = FALSE, quiet = FALSE) # fait une analyse bootstrap complète
# type="p", 
# use.edge.length=FALSE
#root.edge=TRUE
