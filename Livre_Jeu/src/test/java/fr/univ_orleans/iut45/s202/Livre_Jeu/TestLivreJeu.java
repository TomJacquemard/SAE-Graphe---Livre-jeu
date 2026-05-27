package fr.univ_orleans.iut45.s202.Livre_Jeu;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import static org.junit.Assert.assertEquals;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.nio.AttributeType;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;
import org.junit.Test;

public class TestLivreJeu {
    List<ObjetJeu> lesObjets = new ArrayList<>();
    List<ObjetJeu> objetsRecuperes = new ArrayList<>();
    List<PageJeu> pages = new ArrayList<>();
   
    
    LivreJeu lj = new LivreJeu("titre", 8, "genererLivreJeu_1",4);

    @Test
    public void generationLivreJeu1() throws IOException{
         DOTExporter<PageJeu,DefaultWeightedEdge> exporter = new DOTExporter<>();
         exporter.setVertexAttributeProvider((x) -> Map.of("label", new DefaultAttribute<>(x, AttributeType.STRING)));
		 exporter.exportGraph(lj.getGraphe(), new FileWriter("graph.dot"));
        //une fois qu'on obtient le fichier .dot exécuter cette commande comme en tp de graphes pour convertir en pdf
        //dot -Tpdf graph.dot -o graph.pdf
    };

    //     // Création des pages
    //     PageJeu p1 = new PageJeu(1, new Enigme("Début", 2));
    //     PageJeu p2 = new PageJeu(2, new Enigme("Forêt", 5));
    //     PageJeu p3 = new PageJeu(3, new Enigme("Village", 3));
    //     PageJeu p4 = new PageJeu(4, new Enigme("Temple", 7));
    //     PageJeu p5 = new PageJeu(5, new Enigme("Sortie", 1));

    //     // Objets
    //     ObjetJeu cle = new ObjetJeu("Clé");
    //     ObjetJeu carte = new ObjetJeu("Carte");

    //     p2.ajouterObjet(cle);
    //     p4.ajouterObjet(carte);

    //     // Liens entre pages
    //     p1.ajouterPageSuivante(p2);
    //     p1.ajouterPageSuivante(p3);

    //     p2.ajouterPageSuivante(p4);

    //     p3.ajouterPageSuivante(p4);
    //     p3.ajouterPageSuivante(p1);

    //     p4.ajouterPageSuivante(p5);

    //     // Ajout au livre
    //     livre.ajouterPage(p1);
    //     livre.ajouterPage(p2);
    //     livre.ajouterPage(p3);
    //     livre.ajouterPage(p4);
    //     livre.ajouterPage(p5);

    //     livre.setPageDepart(p1);
    //     livre.setPageSortie(p5);

    //     livre.ajouterObjetNecessaire(cle);
    //     livre.ajouterObjetNecessaire(carte);

    //     // =========================
    //     // TEST ALGORITHME GLOUTON
    //     // =========================

    //     System.out.println("===== SOLUTION GLOUTONNE =====");

    //     List<PageJeu> solutionGloutonne =
    //             livre.rechercheSolutionGloutonne();

    //     afficherSolution(solutionGloutonne);

    //     // =========================
    //     // TEST ALGORITHME COMPLET
    //     // =========================

    //     System.out.println("\n===== SOLUTION COMPLETE =====");

    //     List<PageJeu> solutionComplete =
    //             livre.rechercheSolutionComplete();

    //     afficherSolution(solutionComplete);
    

    // public static void afficherSolution(List<PageJeu> solution) {

    //     if (solution == null || solution.isEmpty()) {
    //         System.out.println("Aucune solution trouvée");
    //         return;
    //     }

    //     int tempsTotal = 0;

    //     for (PageJeu page : solution) {

    //         System.out.print(page.getNumero() + " ");

    //         tempsTotal += page.getEnigmes()
    //                 .getTempsResolution();
    //     }

    //     System.out.println();
    //     System.out.println("Longueur : " + solution.size());
    //     System.out.println("Temps total : " + tempsTotal);
    // }
    
    @Test
    public void getListObjects(){
        assertEquals(lesObjets, lj.getListeObjets());
    }

    @Test
    public void testGetObjetsRecuperes(){
        assertEquals(objetsRecuperes, lj.getObjetsRecuperes());
    }

    //@Test 
    //public void testRecupererObjet(){
      //  ObjetJeu object = new ObjetJeu("Objet Test");
      //  assertEquals(objetsRecuperes, object.estRecupere());
    //}

    @Test
    public void testGetPagesJeu(){
        assertEquals(pages, lj.getPagesJeu());
    }
    }
