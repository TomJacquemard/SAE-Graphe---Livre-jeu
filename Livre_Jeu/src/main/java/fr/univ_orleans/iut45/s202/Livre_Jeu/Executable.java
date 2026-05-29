package fr.univ_orleans.iut45.s202.Livre_Jeu;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.nio.AttributeType;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;

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
        int numAlgo = 0;
        int nbPages = 0;

        while(!paramValides){

            try{
            
                System.out.println("En attente de la saisie utilisateur...");
                numAlgo = scanner.nextInt();
                if (numAlgo != 1 && numAlgo !=2){
                    System.out.println("Vous devez entrer 1 ou 2 selon l'algoritme de votre choix.");
                }
                else{
                    System.out.println("Vous avez choisi, l'algorithme de génération numéro : " + numAlgo);
                    paramValides = true;
                }
            }
            catch(Exception e){
                System.out.println("Veuillez entrer un nombre.");
                scanner.nextLine();
            }
            } //le numéro de l'algo de génération à été choisi
        
        System.out.println("\nLa génération va débuter, choisissez vos paramètres :");
        scanner.nextLine(); // sinon la saisie utilisateur n'est pas lue a cause du nextInt() précédent
        System.out.println("Titre du livre : ");
        String titreLivre = scanner.nextLine();
        
        paramValides = false;
        while(!paramValides){
            if(numAlgo == 2){System.out.println("Vous avez sélectionné l'algorithme numéro 2, il faut que vous saisissiez un nombre de pages >2.");}
            try{
                System.out.println("Combien de pages : ");
                nbPages = scanner.nextInt();
                if(numAlgo == 2){
                    if(nbPages>2){
                        paramValides = true;
                    }
                    else{System.out.println("Il faut rentrer un nb de pacges >2");}
                }
                else{paramValides=true;}
            }
            catch(Exception e){
                System.out.println("Veuillez rentrez un nombre. \n");
                scanner.nextLine();
            }
        }

        int nbObjets = 0;
        paramValides = false;
        while(!paramValides){
            try{
                System.out.println("Combien d'objets : ");
                nbObjets = scanner.nextInt();
                if(nbObjets>nbPages){
                System.out.println("Veuillez saisir un nombre d'objets inférieur au nombre de pages.");
                scanner.nextLine();
            }
                else{
                paramValides = true;
                }
            }
            catch(Exception e){
                System.out.println("Veuillez rentrez un nombre. \n");
                scanner.nextLine();
            }
        }

        System.out.println("Génération lancée");
        LivreJeu livreJeu = null;
        if (numAlgo == 1){
            livreJeu =new LivreJeu(titreLivre, nbPages, "genererLivreJeu_1", nbObjets);
        }
        else{
            livreJeu = new LivreJeu(titreLivre, nbPages, "genererLivreJeu2", nbObjets);
        }

        boolean generationReussie = false;
        try{
        System.out.println("\nGénération réussie !");
        System.out.println("Conversion du livre jeu en .dot...");
        DOTExporter<PageJeu,DefaultWeightedEdge> exporter = new DOTExporter<>();
        exporter.setVertexAttributeProvider((x) -> Map.of("label", new DefaultAttribute<>(x, AttributeType.STRING)));
		exporter.exportGraph(livreJeu.getGraphe(), new FileWriter("graph.dot"));
        generationReussie =true;
        }
        catch(IOException e){
            
            System.out.println("Erreur lors de la création du fichier. Veuillez relancer l'exécutable.");
        }

        if(generationReussie){
        System.out.println("Génération du pdf indiquant les solutions...");
        livreJeu.exporterEnPDF();
        System.out.println("Solutions générées ! Exécutez la commande dot -Tpdf graph.dot -o graph.pdf pour convertir le fichier.dot de votre répertoire en pdf !");
        System.out.println("Vous pourrez ensuite ouvrir les pdf de votre répértoire pour observer le graphe et ses solutions.");
        }
    }


    
}
