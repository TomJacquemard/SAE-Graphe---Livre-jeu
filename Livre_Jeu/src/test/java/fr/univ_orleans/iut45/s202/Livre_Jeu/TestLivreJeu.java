package fr.univ_orleans.iut45.s202.Livre_Jeu;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.*;

import org.junit.Before;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.nio.AttributeType;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;
import org.junit.Test;


public class TestLivreJeu {

    LivreJeu lj;
    ObjetJeu objet;
    PageJeu page;

    @Before
    public void init() {

        lj = new LivreJeu("titre", 3);

        objet = new ObjetJeu("clé");
        page = new PageJeu(0, null, false, objet);

        lj.getListeObjets().add(objet);
        lj.getListePageJeu().add(page);
    }

    @Test
    public void getListObjects(){
        assertEquals(lesObjets, lj.getListeObjets());
    }

    @Test
    public void getObjetsRecuperes() {

        assertEquals(0, lj.getObjetsRecuperes().size());
    }

    public void generationLivreJeu1() throws IOException{
         DOTExporter<PageJeu,DefaultWeightedEdge> exporter = new DOTExporter<>();
         exporter.setVertexAttributeProvider((x) -> Map.of("label", new DefaultAttribute<>(x, AttributeType.STRING)));
		 exporter.exportGraph(lj.getGraphe(), new FileWriter("graph.dot"));
        //une fois qu'on obtient le fichier .dot exécuter cette commande comme en tp de graphes pour convertir en pdf
        //dot -Tpdf graph.dot -o graph.pdf
    }


    @Test
    public void recupererObjet() {

        lj.recupererObjet(objet);
        assertTrue(lj.getObjetsRecuperes().contains(objet));
    }

    @Test
    public void getListePageJeu() {

        assertEquals(1, lj.getListePageJeu().size());
        assertTrue(lj.getListePageJeu().contains(page));
    }
}