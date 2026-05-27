#!/bin/bash
mvn compile
mvn exec:java -Dexec.mainClass="fr.univ_orleans.iut45.s202.Livre_Jeu.App"

# Permet de convertir un fichier .dot en pdf
# dot -Tpdf graph.dot -o graph.pdf