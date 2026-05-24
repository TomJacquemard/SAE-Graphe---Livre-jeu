import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.jgrapht.nio.AttributeType;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.csv.CSVFormat;
import org.jgrapht.nio.csv.CSVImporter;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.util.SupplierUtil;


public class Executable {
    public static void main(String[] args) {
         LivreJeu livre = new LivreJeu("Livre Jeu test", 15);
         DOTExporter<String,DefaultWeightedEdge> exporter = new DOTExporter;
           