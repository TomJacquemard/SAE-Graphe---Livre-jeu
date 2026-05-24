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
   
    
    LivreJeu lj = new LivreJeu("titre", 8, "genererLivreJeu_1");

    @Test
    public void generationLivreJeu1() throws IOException{
         DOTExporter<PageJeu,DefaultWeightedEdge> exporter = new DOTExporter<>();
         exporter.setVertexAttributeProvider((x) -> Map.of("label", new DefaultAttribute<>(x, AttributeType.STRING)));
		 exporter.exportGraph(lj.getGraphe(), new FileWriter("graph.dot"));
        //une fois qu'on obtient le fichier .dot exécuter cette commande comme en tp de graphes pour convertir en pdf
        //dot -Tpdf graph.dot -o graph.pdf
    }
    
    // @Test
    // public void getListObjects(){
    //     assertEquals(lesObjets, lj.getListeObjets());
    // }

    // @Test
    // public void getObjetsRecuperes(){
    //     assertEquals(objetsRecuperes, lj.getObjetsRecuperes());
    // }

    // @Test 
    // public void recupererObjet(ObjetJeu object){
    //     assertEquals(objetsRecuperes, object.estRecupere());
    // }

    // @Test
    // public void getListePageJeu(){
    //     assertEquals(pages, lj.getListePageJeu());
    // }
    }
