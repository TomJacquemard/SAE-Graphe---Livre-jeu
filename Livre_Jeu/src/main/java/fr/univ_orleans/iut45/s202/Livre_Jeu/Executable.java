package fr.univ_orleans.iut45.s202.Livre_Jeu;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.nio.AttributeType;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;


public class Executable {
    public static void main(String[] args) throws IOException {
         LivreJeu livre = new LivreJeu("Livre Jeu test", 15,"genererLivreJeu_1");

         DOTExporter<PageJeu,DefaultWeightedEdge> exporter = new DOTExporter<>();
         exporter.setVertexAttributeProvider((x) -> Map.of("label", new DefaultAttribute<>(x, AttributeType.STRING)));
		 exporter.exportGraph(livre.getGraphe(), new FileWriter("graph.dot"));

    }
}
           