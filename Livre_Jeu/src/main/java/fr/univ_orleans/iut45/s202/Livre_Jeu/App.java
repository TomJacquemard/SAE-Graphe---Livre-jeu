package fr.univ_orleans.iut45.s202.Livre_Jeu;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        try {
            // 1. Initialisation de ton livre jeu
            LivreJeu monLivre = new LivreJeu("La Revanche du Graphe", 15, "genererLivreJeu_2", 2);
            
            // (Ici, assure-toi d'avoir ton code qui charge ou crée les pages et les arêtes)
            // ex: monLivre.chargerGraphe(); ou la création manuelle de tes sommets.

            System.out.println("Génération du rapport PDF en cours...");

            // 2. L'appel magique, simple et sans aucun argument !
            monLivre.exporterEnPDF();

        } catch (Exception e) {
            System.out.println("Erreur lors de l'exécution : " + e.getMessage());
            e.printStackTrace();
        }
    }
}