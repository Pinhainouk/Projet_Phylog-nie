#!/bin/bash

if [ $# -ne 1 ]; then
  echo "Usage: $0 <nom_troncon>"
  exit 1
fi

nom_troncon=$1 #saisir par exemple en argument "env_1-100"

output_file="sequences_"${nom_troncon}".fasta"
fasta_directory="/home/elodie/Documents/Projet_tuteure_phylogenie/Sequences/"

for file in "${fasta_directory}"*_"${nom_troncon}".fasta; do
  cat "$file" >> "$output_file"
  # Ajoute une ligne vide entre les séquences concaténées
  #echo "" >> "$output_file"
done
echo "Concatenation réalisée. $output_file"
