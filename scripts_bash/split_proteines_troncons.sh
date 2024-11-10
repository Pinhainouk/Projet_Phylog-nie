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
  echo "Découpe de la séquence protéique : $prefix"
  echo -e "$header\n${seq:0:100}" > "/home/elodie/Documents/Projet_tuteure_phylogenie/Sequences/Sequences_prot/${prefix}_env_1-100.fasta"
  echo -e "$header\n${seq:100:100}" > "/home/elodie/Documents/Projet_tuteure_phylogenie/Sequences/Sequences_prot/${prefix}_env_101-200.fasta"
  echo -e "$header\n${seq:200:100}" > "/home/elodie/Documents/Projet_tuteure_phylogenie/Sequences/Sequences_prot/${prefix}_env_201-300.fasta"
  echo -e "$header\n${seq:300:100}" > "/home/elodie/Documents/Projet_tuteure_phylogenie/Sequences/Sequences_prot/${prefix}_env_301-400.fasta"
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
