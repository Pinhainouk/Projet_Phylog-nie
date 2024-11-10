#!/bin/bash

# Vérifie que le fichier d'entrée est fourni
# Vérifie que nombre d'argument passés au script ($#) est différent de 1.
# Si c'est vrai l'utilisateur n'a pas fourni le nb correct d'argument alors exit
# $0 représente le nom du script en cours d'exécution
# l'utilisateur doit fournir un fichier fasta comme argument
if [ $# -ne 1 ]; then
  echo "Usage: $0 <fasta_file>"
  exit 1
fi

fasta_file=$1

# Vérifie que le fichier fasta existe
if [ ! -f $fasta_file ]; then
  echo "Le fichier $fasta_file n'existe pas."
  exit 1
fi

# Fonction pour découper une séquence en tronçons avec variables locales
split_sequence() {
  local seq=$1
  local header=$2
  local prefix=$3
  echo "Découpe de la séquence ADN : $prefix"
  #-e permet d'interpréter les caractères d'échappement dans la chaine de caractères
  #\n = saut de ligne
  #chaine de caractères contenant variable header et extrait une sous chaine à partir de seq
  #300 position de départ dans seq (index 0) et 300 pour longueur à extraire à partir de départ
  echo -e "$header\n${seq:299:300}" > "/home/elodie/Documents/Projet_tuteure_phylogenie/Sequences/Sequences_adn/${prefix}_300-600.fasta"
  echo -e "$header\n${seq:599:300}" > "/home/elodie/Documents/Projet_tuteure_phylogenie/Sequences/Sequences_adn/${prefix}_600-900.fasta"
}

# Lire le fichier FASTA
seq=""
header=""
#IFS= : Assure que read ne supprime pas les espaces en début et fin de ligne.
#-r : Préserve les backslashes dans les données lues.
while IFS= read -r line; do
  #Vérifie si la ligne commence par >, c'est l'en-tête de la séquence.
  if [[ $line == \>* ]]; then
    if [ -n "$seq" ]; then
      # Si $seq n'est pas vide, traiter la séquence précédente
      # supprime tous les caractères de contrôle, d'espacement et autres caractères non imprimables de header
      prefix=$(echo "$header" | tr -d '[:space:][:cntrl:]')
      split_sequence "$seq" "$header" "$prefix"
    fi
    header=$line
    seq=""
  else
    seq+=$line
  fi
  done < "$fasta_file"
# Traiter la dernière séquence
# Si seq est non vide alors
if [ -n "$seq" ]; then
  prefix=$(echo "$header" | tr -d '[:space:][:cntrl:]')
  split_sequence "$seq" "$header" "$prefix"
fi
