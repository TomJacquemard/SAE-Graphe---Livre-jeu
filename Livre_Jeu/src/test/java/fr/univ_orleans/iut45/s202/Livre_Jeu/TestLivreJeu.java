package fr.univ_orleans.iut45.s202.Livre_Jeu;

<<<<<<<<< Temporary merge branch 1
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.nio.AttributeType;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;

public class TestLivreJeu {

    LivreJeu lj;
    LivreJeu lj2;
    ObjetJeu objet;
    PageJeu page;

    @Before
    public void init() {

        objet = new ObjetJeu("clé");
        page = new PageJeu(0, null, false);
        page.setObjet(objet);

        List<PageJeu> pagesInit = new ArrayList<>();
        pagesInit.add(page);

        List<ObjetJeu> objetsInit = new ArrayList<>();
        objetsInit.add(objet);

        SimpleDirectedWeightedGraph<PageJeu, DefaultWeightedEdge> grapheInit = 
            new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        // initialisation avec des listes vides et pas null.
        lj = new LivreJeu("titre", 10, pagesInit, objetsInit, grapheInit);
        
    }

    @Test
    public void getListObjects() {

        assertEquals(1, lj.getListeObjets().size());
        assertTrue(lj.getListeObjets().contains(objet));
    }

    @Test
    public void getObjetsRecuperes() {

        assertEquals(0, lj.getObjetsRecuperes().size());
    }

    public void generationLivreJeu1() throws IOException{
         DOTExporter<PageJeu,DefaultWeightedEdge> exporter = new DOTExporter<>();
         exporter.setVertexAttributeProvider((x) -> Map.of("label", new DefaultAttribute<>(x, AttributeType.STRING)));
         exporter.exportGraph(lj.getGraphe(), new FileWriter("graph.dot"));
    }

    // =========================================================================
    // MÉTHODE OUTIL POUR LES NOUVEAUX TESTS (Calcul du chrono exact d'un chemin)
    // =========================================================================
    private double calculerTempsChemin(List<PageJeu> chemin) {
        if (chemin == null || chemin.size() < 2) return 0.0;
        double total = 0.0;
        for (int i = 0; i < chemin.size() - 1; i++) {
            DefaultWeightedEdge edge = lj.getGraphe().getEdge(chemin.get(i), chemin.get(i+1));
            if (edge != null) {
                total += lj.getGraphe().getEdgeWeight(edge);
            }
        }
        return total;
    }

    // =========================================================================
    // CAS 1 : LE GRAPHE LINÉAIRE
    // =========================================================================
    @Test
    public void testCas1_LineaireIdeal() {
        /* ---------------------------------------------------------------------
         * EXPLICATION DU GRAPH : Graphe Linéaire (En chaîne)
         * C'est un livre "tunnel" sans aucun embranchement. Chaque page mène à 
         * la suivante jusqu'à la sortie (1 -> 2 -> 3 -> 4).
         *
         * RÉSULTATS ATTENDUS DU CAS 1 :
         * -> Glouton      : [Page 1, Page 2, Page 3, Page 4] | Temps : 30.0s | Objets : 2
         * -> Dijkstra     : [Page 1, Page 2, Page 3, Page 4] | Temps : 30.0s | Objets : 2
         * -> Backtracking : [Page 1, Page 2, Page 3, Page 4] | Temps : 30.0s | Objets : 2
         * C'est le cas témoin : les 3 algos doivent trouver exactement le même chemin.
         * --------------------------------------------------------------------- */
        lj.supprimerTout();
        lj.setGraphe(new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class));

        PageJeu p1 = new PageJeu(1, "Départ", false);
        PageJeu p2 = new PageJeu(2, "Salle Epée", false);
        PageJeu p3 = new PageJeu(3, "Salle Bouclier", false);
        PageJeu p4 = new PageJeu(4, "Sortie", true);

        p2.setObjet(new ObjetJeu("Epee"));
        p3.setObjet(new ObjetJeu("Bouclier"));

        lj.ajouterPage(p1); 
        lj.ajouterPage(p2);
        lj.ajouterPage(p3); 
        lj.ajouterPage(p4);

        lj.ajouterLien(p1, p2, 10.0);
        lj.ajouterLien(p2, p3, 15.0);
        lj.ajouterLien(p3, p4, 5.0);

        List<PageJeu> resGlouton = lj.algorithmeGloutonCorrect();
        List<PageJeu> resDijkstra = lj.algorithmeDijkstraCorrect();
        List<PageJeu> resBacktrack = lj.algorithmeCombinatoireComplet();

        assertEquals(4, resGlouton.size());
        assertEquals(30.0, calculerTempsChemin(resGlouton), 0.01);

        assertEquals(4, resDijkstra.size());
        assertEquals(30.0, calculerTempsChemin(resDijkstra), 0.01);

        assertEquals(4, resBacktrack.size());
        assertEquals(30.0, calculerTempsChemin(resBacktrack), 0.01);
    }

    // =========================================================================
    // CAS 2 : LE GRAPHE DIVERGENT
    // =========================================================================
    @Test
    public void testCas2_ChoixCornelien() {
        /* ---------------------------------------------------------------------
         * EXPLICATION DU GRAPH : Graphe Divergent (Séparation puis Jonction)
         * Au départ (Page 1), deux choix s'offrent au joueur : la page 2 ou la page 3.
         * La page 2 s'ouvre très vite (2s) mais sa suite est affreusement longue (50s).
         * La page 3 demande un gros effort initial (20s) mais sa fin est immédiate (5s).
         *
         * RÉSULTATS ATTENDUS DU CAS 2 :
         * -> Glouton      : [Page 1, Page 2, Page 4] | Temps : 52.0s (Choix court-termiste mauvais)
         * -> Dijkstra     : [Page 1, Page 3, Page 4] | Temps : 25.0s (Vision globale optimale)
         * -> Backtracking : [Page 1, Page 3, Page 4] | Temps : 25.0s (Vision globale optimale)
         * Le Glouton doit échouer par rapport aux deux autres algorithmes globaux.
         * --------------------------------------------------------------------- */
        lj.supprimerTout();
        lj.setGraphe(new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class));

        PageJeu p1 = new PageJeu(1, "Départ", false);
        PageJeu p2 = new PageJeu(2, "Branche Gloutonne Mauvaise", false);
        PageJeu p3 = new PageJeu(3, "Branche Longue Optimale", false);
        PageJeu p4 = new PageJeu(4, "Sortie", true);

        p2.setObjet(new ObjetJeu("Clef"));
        p3.setObjet(new ObjetJeu("Clef"));

        lj.ajouterPage(p1); 
        lj.ajouterPage(p2);
        lj.ajouterPage(p3); 
        lj.ajouterPage(p4);

        lj.ajouterLien(p1, p2, 2.0);
        lj.ajouterLien(p2, p4, 50.0);
        lj.ajouterLien(p1, p3, 20.0);
        lj.ajouterLien(p3, p4, 5.0);

        List<PageJeu> resGlouton = lj.algorithmeGloutonCorrect();
        List<PageJeu> resDijkstra = lj.algorithmeDijkstraCorrect();
        List<PageJeu> resBacktrack = lj.algorithmeCombinatoireComplet();

        assertEquals(52.0, calculerTempsChemin(resGlouton), 0.01);
        assertEquals(25.0, calculerTempsChemin(resDijkstra), 0.01);
        assertEquals(25.0, calculerTempsChemin(resBacktrack), 0.01);
        assertEquals(3, resDijkstra.get(1).getNumero());
    }

    // =========================================================================
    // CAS 3 : LE GRAPHE AVEC CUL-DE-SAC
    // =========================================================================
    @Test
    public void testCas3_CulDeSac() {
        /* ---------------------------------------------------------------------
         * EXPLICATION DU GRAPH : Graphe Arborescent Brisé (Cul-de-sac)
         * Un objet indispensable est placé en Page 2, mais la Page 2 est un 
         * cul-de-sac sans flèche de retour vers le reste du livre. 
         * On ne peut donc pas à la fois ramasser l'objet et atteindre la sortie (Page 4).
         *
         * RÉSULTATS ATTENDUS DU CAS 3 :
         * -> Glouton      : [] ou taille <= 2 (Bloqué définitivement en Page 2)
         * -> Dijkstra     : [] (Liste vide - Reconnait l'absence de chemin complet valide)
         * -> Backtracking : [] (Liste vide - Reconnait l'absence de chemin complet valide)
         * Ce test valide que tes algorithmes ne plantent pas (pas de NullPointerException)
         * et retournent proprement une liste vide quand le livre est sans solution.
         * --------------------------------------------------------------------- */
        lj.supprimerTout();
        lj.setGraphe(new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class));

        PageJeu p1 = new PageJeu(1, "Départ", false);
        PageJeu p2 = new PageJeu(2, "Impasse avec Objet", false);
        PageJeu p3 = new PageJeu(3, "Route vers Sortie", false);
        PageJeu p4 = new PageJeu(4, "Sortie", true);

        p2.setObjet(new ObjetJeu("ArtefactIntrouvable"));

        lj.ajouterPage(p1); 
        lj.ajouterPage(p2);
        lj.ajouterPage(p3); 
        lj.ajouterPage(p4);

        lj.ajouterLien(p1, p2, 5.0);
        lj.ajouterLien(p1, p3, 5.0);
        lj.ajouterLien(p3, p4, 5.0);

        assertTrue(lj.algorithmeGloutonCorrect().isEmpty() || lj.algorithmeGloutonCorrect().size() <= 2); 
        assertTrue(lj.algorithmeDijkstraCorrect().isEmpty());
        assertTrue(lj.algorithmeCombinatoireComplet().isEmpty());
    }

    // =========================================================================
    // CAS 4 : LE GRAPHE CYCLIQUE
    // =========================================================================
    @Test
    public void recupererObjet() {

        lj.recupererObjet(objet);
        assertTrue(lj.getObjetsRecuperes().contains(objet));
>>>>>>>>> Temporary merge branch 2
    }

    @Test
    public void getListePageJeu() {

        assertEquals(1, lj.getListePageJeu().size());
        assertTrue(lj.getListePageJeu().contains(page));
    }
}
