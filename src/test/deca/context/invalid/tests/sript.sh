#!/bin/bash

# Récupération des arguments
string=$1
character=$2

# Boucle sur chaque fichier dans le répertoire courant
for file in *; do
  # Vérifie si le fichier contient le caractère donné
  if grep -q $character $file; then
    # Écrit la chaîne de caractères à la fin du fichier
    echo $string >> $file
    # Affiche le nom du fichier sur lequel la chaîne a été écrite
    echo "String written to $file"
  fi
done

