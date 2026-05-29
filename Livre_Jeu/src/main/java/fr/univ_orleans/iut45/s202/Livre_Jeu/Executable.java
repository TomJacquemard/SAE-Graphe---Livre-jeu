package fr.univ_orleans.iut45.s202.Livre_Jeu;

import java.util.Scanner;

public class Executable {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("======================================");
        System.out.println(" Bienvenue sur l'application LivreJeu");
        System.out.println("======================================\n");
        System.out.println("Développée par LIMA MAI SANGO Graça, PAJOT Thomas, EVRAT--CLERC Laura, JACQUEMARS Tom et BEAUHAIRE Erwan");
        System.out.println("Elèves de l'info 14A\n");

        System.out.println("Entrez 1 ou 2 selon l'algorithme de génération que vous voulez utiliser : \n");
        System.out.println(" -1 Algorithme de génération partant de la page de sortie");
        System.out.println(" -2 Algorithme créant un chemin sûr avant d'ajouter les autres pages.");

        boolean paramValides = false;
        while(!paramValides){
            try{
                System.out.println("En attente de la saisie utilisateur...");
                int numAlgo = scanner.nextInt();
                if (numAlgo != 1 && numAlgo !=2){
                    System.out.println("Vous devez entrer 1 ou 2 selon l'algoritme de votre choix.");
                }
                else{
                    System.out.println("Vous avez choisi, l'algorithme de génération numéro : " + numAlgo);
                }
            }
            catch(NumberFormatException e){
                System.out.println("Veuillez entrer un nombre.");
            }
            } //le numéro de l'algo de génération à été choisi
        
        


        }


    
}
