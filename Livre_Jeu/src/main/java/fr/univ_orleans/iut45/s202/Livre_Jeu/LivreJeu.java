package fr.univ_orleans.iut45.s202.Livre_Jeu;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;


// Gestion des images Java standard
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

// Imports OpenPDF (Attention à bien prendre l'Image d'ici, pas de java.awt)
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image; 
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;
import java.awt.Color;

/**
 * Classe gérant la structure globale d'un livre-jeu.
 * 
 * Elle synchronise une arborescence locale stockée dans les pages
 * avec un modèle de graphe orienté et pondéré via la bibliothèque JGraphT.
 * 
 */
public class LivreJeu extends Livre {
    
    private List<ObjetJeu> lesObjets;
    private List<ObjetJeu> objetsRecuperes;
    private List<PageJeu> lesPagesDuJeu;
    private Graph<PageJeu, DefaultWeightedEdge> graph; 

    /**
     * CONSTRUCTEUR : Initialisation et configuration du livre-jeu.
     * @param titre
     * Le titre principal attribué à l'œuvre.
     * @param nbPages
     * Le nombre total de pages à instancier au démarrage.
     * @param choixGenerateur
     * L'identifiant textuel de la méthode à utiliser pour l'algorithme de génération.
     * @return Une instance configurée de LivreJeu contenant ses pages et son graphe.
     */
    public LivreJeu(String titre, int nbPages, String choixGenerateur,int nbObjets) {
        super(titre, nbPages);
        this.lesObjets = new ArrayList<>();
        this.objetsRecuperes = new ArrayList<>();
        this.lesPagesDuJeu = new ArrayList<>();
        this.graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        PageJeu pageDeSortie = new PageJeu(nbPages, "Page fin", true);
        PageJeu pageEntree = new PageJeu(1, "Page d'entrée", false);

        this.lesPagesDuJeu.add(pageEntree);
         this.lesPagesDuJeu.add(pageDeSortie);
        for (int i = 2; i<nbPages; i++){ //création des pages + objets
            this.lesPagesDuJeu.add(new PageJeu(i, "Lorem ipsum", false));
        }

        Random random = new Random();
        for(int i = 1; i<=nbObjets;i++){
            ObjetJeu objetCourant = new ObjetJeu("Objet " + i); //création de l'objet que l'on ajoute sur la page

            boolean indiceOk = false;

            while(!(indiceOk)){ //tant que je n'ai pas sélectionné une page ne contenant pas déjà un objet...
                int indiceAleatoire = random.nextInt(lesPagesDuJeu.size());
                PageJeu pageChoisie = lesPagesDuJeu.get(indiceAleatoire);

                if(!(pageChoisie.contientObjet())){ 
                    pageChoisie.setObjet(objetCourant);
                    this.lesObjets.add(objetCourant);
                    System.out.println("[GENERATOR] Objet '" + objetCourant.getNom() + "' placé secrètement sur la Page n°" + pageChoisie.getNumero());
                    indiceOk = true;
                }
            }

        }

        if (choixGenerateur.equals("genererLivreJeu_1")){
            genererLivreJeu_1(pageDeSortie, pageEntree); //génération des liens entre les pages (pageSuivantes) + enigmes
        }
        remplirGraphe(); //retranscription des liens entre les pages dans le graphe avec les addVertex et addEdge
    }


    /**
     * CONSTRUCTEUR MANUEL POUR TESTS : Initialisation et configuration d'un LivreJeu de manière manuelle pour mener à bien les tests de TestLivreJeu
     * Les objets ne seront pas placés sur les pages car il n'y en a pas besoin pour les tests de LivreJEU
     * 
     * */
    
    public LivreJeu(String titre,int nbObjets, List<PageJeu> lesPagesDuJeu, List<ObjetJeu> lesObjets,  Graph<PageJeu, DefaultWeightedEdge> graph){
        super(titre, lesPagesDuJeu.size());
        this.lesObjets = lesObjets;
        this.objetsRecuperes = new ArrayList<>();
        this.lesPagesDuJeu = lesPagesDuJeu;
        this.graph = graph;
        remplirGraphe();
    }


    /**
     * GÉNÉRATEUR : Création aléatoire des liaisons narratives du livre.
     * Relie l'ensemble des pages entre elles à partir de la fin, puis raccorde 
     * les pages orphelines à l'entrée et injecte des boucles de retour.
     * @param pageDeSortie L'instance de la page terminale marquant la victoire.
     * @param pageEntree L'instance de la première page où débute le joueur.
     *
     *  @return Cette méthode ne renvoie rien (void), elle modifie directement l'état des objets.
     */
    public void genererLivreJeu_1(PageJeu pageDeSortie, PageJeu pageEntree) {
        List<PageJeu> pagesValides = new ArrayList<>(); 
        pagesValides.add(pageDeSortie);

        List<PageJeu> pagesAPlacer = new ArrayList<>(this.getPagesJeu()); 
        pagesAPlacer.remove(pageEntree);
        pagesAPlacer.remove(pageDeSortie);
        Random random = new Random();
        // -----------------------------------------------------

        while (!(pagesAPlacer.isEmpty())) {  //tant qu'il reste des pages à placer
            PageJeu pageEnPlacement = pagesAPlacer.remove(0);
            Collections.shuffle(pagesValides); //mélange de la liste à chaque fois pour plus de hasard sur les arrêtes
            
            int nbPagesChoisies = 1 + random.nextInt(pagesValides.size()); //choisira un nb de pages entre 1 et pagesValide.size() (+1 pour éviter d'obtenir 0)
            //Une page pourrait donc être reliée à toutes les autres déjà placées si on tombe pile sur pagesValides
            List<PageJeu> pagesChoisies = pagesValides.subList(0, nbPagesChoisies);//on sélectionne les pages vers lesquelles la pageEnPlacement va mener

            for (PageJeu p : pagesChoisies) {
                pageEnPlacement.ajoutePage(p); //on ajoute la page voisine aux pagesSuivantes de pageEnPlacement
                int dureeEnigme = 1 + random.nextInt(20);//Choix aléatoire de la durée de l'Enigme menant à la page voisine p
                Enigme e = new Enigme("Lorem Ipsum", dureeEnigme);//création et ajout de l'énigme 
                pageEnPlacement.ajouteEnigme(e);
                
            }
            pagesValides.add(pageEnPlacement);
            //A la fin de la boucle while, toutes les pages seront placées, sauf la page d'entrée
        }

        List<PageJeu> pagesIsolees = this.pagesSansSource();
        if (pagesIsolees.size() > 1) {  //si il y des pages isolées... >1 car la page d'entrée est pour le moment FORCEMENT isolée + c'est aussi le cas pour la dernière page que l'on a ajouté normalement cette condition est donc toujours vérifiée
            for (PageJeu pIsolee : pagesIsolees) { //on relie la page d'entrée à ces pages isolées
                if (!pIsolee.equals(pageEntree)) {
                    pageEntree.ajoutePage(pIsolee);
                    int dureeEnigme = 1 + random.nextInt(20);//Choix aléatoire de la durée de l'Enigme 
                    pageEntree.ajouteEnigme(new Enigme("Lorem Ipsum", dureeEnigme));//création et ajout de l'énigme
                }
            }
        } 

        //Enfin on rajoute quelques pages menant vers le début ET quelques pages voisines de la sortie car sinon l'algo tel quel ne permet pas à la sortie d'avoir des voisines
        int nbPagesChoisies = 1 + random.nextInt(Math.min(4, pagesValides.size())); 
        List<PageJeu> pagesChoisies = pagesValides.subList(0, nbPagesChoisies); 
        for (PageJeu p : pagesChoisies) {
            if (!p.equals(pageEntree)) {
                p.ajoutePage(pageEntree);
                int dureeEnigme = 1 + random.nextInt(20);
                p.ajouteEnigme(new Enigme("Lorem Ipsum", dureeEnigme));
            }
        }
        pagesValides.add(pageEntree);//la page d'entrée devient placée

        pagesValides.remove(pageDeSortie); //on remove pour éviter le cas (très rare) où on tomberait sur l'indice aléatoire 1 qui subList la page de sortie (on ne relie pas la page de sortie à elle même !)
        nbPagesChoisies = 1 + random.nextInt(Math.min(4, pagesValides.size())); 
        pagesChoisies = pagesValides.subList(0, nbPagesChoisies); 
        for (PageJeu p : pagesChoisies) {
            if (!p.equals(pageDeSortie)) { //sécurité mais normalement impossible de retomber sur pageDeSortie
                pageDeSortie.ajoutePage(p);
                int dureeEnigme = 1 + random.nextInt(20);
                pageDeSortie.ajouteEnigme(new Enigme("Lorem Ipsum", dureeEnigme));
                }
            }
    }

    /**
     * ANALYSE : Recherche des pages n'ayant aucun chemin d'accès entrant.
     * Parcourt l'ensemble du graphe mathématique pour trouver les sommets 
     * dont le degré de connectivité entrant est égal à zéro.
     * @return Une List contenant les objets PageJeu dits "orphelins".
     */
    public List<PageJeu> pagesSansSource() {
        List<PageJeu> pagesSansSource = new ArrayList<>();
        boolean aUneSource = false;
        for (PageJeu pCourante : this.lesPagesDuJeu){
            aUneSource=false;
            for (PageJeu p : this.lesPagesDuJeu) {
                if (p.getPagesSuivantes().contains(pCourante)){
                        aUneSource=true;
                    }
                }
                if(!(aUneSource)&& !(pagesSansSource.contains(pCourante))){pagesSansSource.add(pCourante);} //si je n'ai trouvé aucune source à la fin de mon parcours pour la page courante je l'ajoute aux pages sans source
            
        }
        return pagesSansSource;
    }

    public void remplirGraphe(){
        //On rempli les vertex et edge selon la liste lesPagesDuJeu
        for(PageJeu pageCourante : lesPagesDuJeu){
            this.graph.addVertex(pageCourante);
        }
        
        for(PageJeu pageCourante : lesPagesDuJeu){ //obligé de parcourir en deux temps : car addVertex(pageA,pageB) nécessite deux pages déjà dans le graphe
            List<PageJeu> pagesSuivantes = pageCourante.getPagesSuivantes();
            List<Enigme> enigmes = pageCourante.getEnigmes();
            for(int i = 0; i<pagesSuivantes.size(); i ++){
                DefaultWeightedEdge nouvelleEdge = this.graph.addEdge(pageCourante, pagesSuivantes.get(i));
                
                this.graph.setEdgeWeight(nouvelleEdge,enigmes.get(i).getDuree()); //on attribut le poids a la nouvelle arrete
            }
        }
    }

    /**
     * GETTER : Accès à la modélisation sous forme de graphe.
     * @return L'objet Graph (JGraphT) représentant l'ossature du livre-jeu.
     * 
     * 
     * 
     */
    public Graph<PageJeu, DefaultWeightedEdge> getGraphe() {
        return this.graph;
    }

    /**
     * Accès à la liste de l'ensemble des pages.
     * @return La List complète de toutes les instances de PageJeu.
     */
    public List<PageJeu> getPagesJeu() {
        return this.lesPagesDuJeu;
    }

    /**
     * RECHERCHE : Extraction d'une page précise à partir de son identifiant numérique.
     * @param numPage Le numéro de la page cible recherchée au sein de la liste.
     * 
     * @return L'objet PageJeu possédant le bon numéro.
     * @throws IndexOutOfBoundsException Si aucun élément de la liste ne correspond au numéro transmis.
     */
    public PageJeu getPageNumero(int numPage) throws IndexOutOfBoundsException { 
        for (PageJeu p : this.lesPagesDuJeu) {
            if (p.getNumero() == numPage) {
                return p;
            }
        }
        throw new IndexOutOfBoundsException("Page inexistante dans ce livre-jeu.");
    }

    /**
     * GETTER : Liste du catalogue des objets disponibles dans le jeu.
     * * @return 
     * La List de tous les objets de jeu configurés à la création.
     */
    public List<ObjetJeu> getListeObjets() {
        return this.lesObjets;
    }

    /**
     * GETTER : Liste de l'inventaire possédé par le joueur.
     * * @return 
     * La List des objets collectés avec succès au cours de la partie.
     */
    public List<ObjetJeu> getObjetsRecuperes() {
        return this.objetsRecuperes;
    }

    /**
     * ACTION : Ramassage et validation d'un objet.
     * 
     * Active le statut interne de l'objet ciblé puis, si l'opération réussit, 
     * l'insère au sein de la liste des équipements acquis.
     * 
     * @param objet L'instance de l'objet que le joueur tente de collecter.
     * @return Cette méthode ne renvoie rien (void), elle met à jour l'inventaire interne.
     */
    public void recupererObjet(ObjetJeu objet) { 
        objet.recupererObjet();
        if (objet.estRecupere()) {
            objetsRecuperes.add(objet);
        }
    }

    public List<PageJeu> rechercheSolutionGloutonne(double borneMax) {
        List<PageJeu> cheminCalcule = new ArrayList<>();
        List<ObjetJeu> objetsAAttraper = new ArrayList<>(this.getListeObjets());
        
        // La page de départ est TOUJOURS la première de la liste
        PageJeu pageCourante = this.lesPagesDuJeu.get(0);
        cheminCalcule.add(pageCourante);

        // Historique pour éviter de boucler à l'infini entre deux pages
        List<PageJeu> visitees = new ArrayList<>();
        visitees.add(pageCourante);

        while (!pageCourante.estSortie()) {
            List<PageJeu> voisines = pageCourante.getPagesSuivantes();
            List<Enigme> enigmes = pageCourante.getEnigmes();
            
            if (voisines == null || voisines.isEmpty()) {
                break; // Cul-de-sac
            }

            PageJeu prochainePage = null;
            double tempsMin = Double.MAX_VALUE;
            boolean cibleObjetTrouvee = false;

            // ÉTAPE 1 : Recherche prioritaire d'une page avec un objet non récupéré
            for (int i = 0; i < voisines.size(); i++) {
                PageJeu voisine = voisines.get(i);
                Enigme enigme = enigmes.get(i);

                if (enigme.getDuree() > borneMax || visitees.contains(voisine)) {
                    continue; 
                }

                if (!objetsAAttraper.isEmpty() && voisine.contientObjet() && objetsAAttraper.contains(voisine.getObjet())) {
                    if (enigme.getDuree() < tempsMin) {
                        tempsMin = enigme.getDuree();
                        prochainePage = voisine;
                        cibleObjetTrouvee = true;
                    }
                }
            }

            // ÉTAPE 2 : Si aucun objet n'est à proximité, choix du chemin le plus rapide
            if (!cibleObjetTrouvee) {
                for (int i = 0; i < voisines.size(); i++) {
                    PageJeu voisine = voisines.get(i);
                    Enigme enigme = enigmes.get(i);

                    if (enigme.getDuree() > borneMax || visitees.contains(voisine)) {
                        continue;
                    }

                    if (enigme.getDuree() < tempsMin) {
                        tempsMin = enigme.getDuree();
                        prochainePage = voisine;
                    }
                }
            }

            // Déplacement sur la page sélectionnée
            if (prochainePage != null) {
                pageCourante = prochainePage;
                cheminCalcule.add(pageCourante);
                visitees.add(pageCourante);
                
                if (pageCourante.contientObjet()) {
                    objetsAAttraper.remove(pageCourante.getObjet());
                }
            } else {
                // Sécurité : si bloqué, on prend la sortie si elle est accessible immédiatement
                for (PageJeu v : voisines) {
                    if (v.estSortie()) {
                        cheminCalcule.add(v);
                        return cheminCalcule;
                    }
                }
                break;
            }
        }

        return cheminCalcule;
    }

    
    /**
     * Algorithme Glouton.
     * Cet algorithme garantit le respect des règles du jeu : collecter impérativement tous 
     * les objets avant de s'orienter vers la sortie. 
     * Phase 1 : Il se déplace vers les pages contenant un objet requis.
     * Phase 2 : Dès que l'inventaire est plein, il bascule sur une recherche gloutonne de la sortie.
     * @param borneMax La durée maximale d'une énigme autorisée pour franchir un arc.
     * @return Une List de PageJeu validant les conditions de victoire.
     */
    public List<PageJeu> algorithmeGloutonCorrect(double borneMax) {
        List<PageJeu> chemin = new ArrayList<>();
        List<ObjetJeu> objetsAAttraper = new ArrayList<>(this.getListeObjets());
        
        PageJeu pageCourante = this.lesPagesDuJeu.get(0);
        chemin.add(pageCourante);

        List<PageJeu> visiteesGlobal = new ArrayList<>();
        visiteesGlobal.add(pageCourante);

        while (!objetsAAttraper.isEmpty()) {
            List<PageJeu> voisines = pageCourante.getPagesSuivantes();
            List<Enigme> enigmes = pageCourante.getEnigmes();
            if (voisines == null || voisines.isEmpty()) break;

            PageJeu prochainePage = null;
            double tempsMin = Double.MAX_VALUE;

            for (int i = 0; i < voisines.size(); i++) {
                PageJeu v = voisines.get(i);
                Enigme e = enigmes.get(i);
                if (e.getDuree() > borneMax || visiteesGlobal.contains(v)) continue;

                if (v.contientObjet() && objetsAAttraper.contains(v.getObjet())) {
                    if (e.getDuree() < tempsMin) {
                        tempsMin = e.getDuree();
                        prochainePage = v;
                    }
                }
            }

            if (prochainePage == null) {
                for (int i = 0; i < voisines.size(); i++) {
                    PageJeu v = voisines.get(i);
                    Enigme e = enigmes.get(i);
                    if (e.getDuree() > borneMax || visiteesGlobal.contains(v) || v.estSortie()) continue;

                    if (e.getDuree() < tempsMin) {
                        tempsMin = e.getDuree();
                        prochainePage = v;
                    }
                }
            }

            if (prochainePage != null) {
                pageCourante = prochainePage;
                chemin.add(pageCourante);
                visiteesGlobal.add(pageCourante);
                if (pageCourante.contientObjet()) {
                    objetsAAttraper.remove(pageCourante.getObjet());
                }
            } else {
                break; 
            }
        }

        while (!pageCourante.estSortie()) {
            List<PageJeu> voisines = pageCourante.getPagesSuivantes();
            List<Enigme> enigmes = pageCourante.getEnigmes();
            if (voisines == null || voisines.isEmpty()) break;

            PageJeu prochainePage = null;
            double tempsMin = Double.MAX_VALUE;

            for (int i = 0; i < voisines.size(); i++) {
                PageJeu v = voisines.get(i);
                Enigme e = enigmes.get(i);
                if (e.getDuree() < tempsMin) {
                    tempsMin = e.getDuree();
                    prochainePage = v;
                }
            }

            if (prochainePage != null) {
                pageCourante = prochainePage;
                chemin.add(pageCourante);
            } else {
                break;
            }
        }
        System.out.println("DEBUG - Nombre d'objets total dans le livre : " + this.getListeObjets().size());
for (PageJeu p : this.lesPagesDuJeu) {
    if (p.contientObjet()) {
        System.out.println("DEBUG - La page " + p.getNumero() + " contient l'objet : " + p.getObjet().getNom());
    }
}
        return chemin;
    }

    /**
     * Algorithme de Dijkstra Exhaustif et Correct.
     * Calcule le chemin optimal absolu en utilisant l'algorithme des plus courts chemins de Dijkstra.
     * Pour respecter la contrainte des objets, cet algorithme calcule par étapes le chemin optimal :
     * d'abord du départ vers l'objet A, puis de l'objet A vers l'objet B, puis vers la sortie.
     * @return Une List de PageJeu représentant le chemin optimal absolu respectant les règles.
     */
    public List<PageJeu> algorithmeDijkstraCorrect() {
        List<PageJeu> cheminComplet = new ArrayList<>();
        List<PageJeu> etapes = new ArrayList<>();
        
        etapes.add(this.lesPagesDuJeu.get(0)); 
        
        for (PageJeu p : this.lesPagesDuJeu) {
            if (p.contientObjet() && p.getNumero() != 1 && !p.estSortie()) {
                etapes.add(p);
            }
        }
        
        PageJeu pageSortie = null;
        for (PageJeu p : this.lesPagesDuJeu) {
            if (p.estSortie()) {
                pageSortie = p;
                break;
            }
        }
        if (pageSortie != null) etapes.add(pageSortie);

        org.jgrapht.alg.shortestpath.DijkstraShortestPath<PageJeu, DefaultWeightedEdge> dijkstra = 
            new org.jgrapht.alg.shortestpath.DijkstraShortestPath<>(this.graph);

        for (int i = 0; i < etapes.size() - 1; i++) {
            org.jgrapht.GraphPath<PageJeu, DefaultWeightedEdge> path = dijkstra.getPath(etapes.get(i), etapes.get(i+1));
            if (path != null) {
                List<PageJeu> subPath = path.getVertexList();
                if (i > 0 && !cheminComplet.isEmpty()) {
                    subPath.remove(0); 
                }
                cheminComplet.addAll(subPath);
            }
        }
        return cheminComplet;
    }

    private List<PageJeu> meilleurCheminComplet;
    private double meilleurTempsComplet;

    /**
     * Algorithme de Recherche Combinatoire Complète (Backtracking).
     * Explore l'intégralité de l'arbre des possibles de façon récursive.
     * Il teste toutes les alternatives de routes et mémorise le chemin qui valide 
     * l'intégralité des objectifs (objets collectés + sortie atteinte) avec le temps cumulé minimal.
     * @return La List de PageJeu du chemin parfait trouvé par force brute.
     */
    public List<PageJeu> algorithmeCombinatoireComplet() {
        this.meilleurCheminComplet = null;
        this.meilleurTempsComplet = Double.MAX_VALUE;
        
        List<PageJeu> cheminEnCours = new ArrayList<>();
        PageJeu depart = this.lesPagesDuJeu.get(0);
        cheminEnCours.add(depart);
        
        int totalObjetsDuJeu = 0;
        for(PageJeu p : this.lesPagesDuJeu) {
            if(p.contientObjet()) totalObjetsDuJeu++;
        }

        backtrackingRecursive(depart, cheminEnCours, 0.0, totalObjetsDuJeu);
        return this.meilleurCheminComplet;
    }

    /**
     * Méthode récursive interne pour le calcul du backtracking.
     */
    private void backtrackingRecursive(PageJeu nœud, List<PageJeu> cheminEnCours, double tempsAccumule, int totalObjets) {
        if (tempsAccumule >= this.meilleurTempsComplet) return;

        if (nœud.estSortie()) {
            int objetsCollectes = 0;
            for (PageJeu p : cheminEnCours) {
                if (p.contientObjet()) objetsCollectes++;
            }

            if (objetsCollectes == totalObjets) {
                this.meilleurTempsComplet = tempsAccumule;
                this.meilleurCheminComplet = new ArrayList<>(cheminEnCours);
            }
            return;
        }

        List<PageJeu> voisines = nœud.getPagesSuivantes();
        List<Enigme> enigmes = nœud.getEnigmes();
        if (voisines == null) return;

        for (int i = 0; i < voisines.size(); i++) {
            PageJeu voisine = voisines.get(i);
            
            if (cheminEnCours.contains(voisine)) continue; 

            double coutArc = enigmes.get(i).getDuree();
            
            cheminEnCours.add(voisine);
            backtrackingRecursive(voisine, cheminEnCours, tempsAccumule + coutArc, totalObjets);
            cheminEnCours.remove(cheminEnCours.size() - 1);
        }
    }

    /**
     * AFFICHAGE : Génère une représentation textuelle détaillée du livre-jeu.
     * <p>
     * Exporte l'état de chaque page, l'objet textuel associé s'il existe, et la liste 
     * de ses liaisons sortantes (pages cibles et caractéristiques de l'énigme).
     * </p>
     * * @return 
     * Une String contenant l'arborescence complète du livre-jeu.
     */
    @Override
    public String toString() {
        String resultat = "==================================================\n";
        resultat = resultat + "   STRUCTURE DU LIVRE-JEU : " + this.getTitre() + "\n";
        resultat = resultat + "==================================================\n";

        for (PageJeu page : this.lesPagesDuJeu) {
            resultat = resultat + "Page n°" + page.getNumero();
            if (page.getNumero() == 1) {
                resultat = resultat + " [ENTRÉE]";
            }
            if (page.estSortie()) {
                resultat = resultat + " [SORTIE/FIN]";
            }
            resultat = resultat + "\n";
            
            String nomObjet = "Aucun";
            if (page.contientObjet()) {
                nomObjet = page.getObjet().getNom();
            }
            
            resultat = resultat + "  -> Contenu : " + page.lire() + "\n";
            resultat = resultat + "  -> Objet présent : " + nomObjet + "\n";
            
            List<PageJeu> suivantes = page.getPagesSuivantes();
            List<Enigme> enigmes = page.getEnigmes();
            
            resultat = resultat + "  -> Chemins sortants (" + suivantes.size() + ") :\n";
            for (int i = 0; i < suivantes.size(); i++) {
                PageJeu suiv = suivantes.get(i);
                Enigme enigme = enigmes.get(i);
                
                resultat = resultat + "     * Vers Page n°" + suiv.getNumero()
                        + " | Énigme: '" + enigme.getIntitule()
                        + "' | Durée: " + enigme.getDuree() + "s"
                        + " | Difficulté: " + enigme.getDuree() + "/5\n";
            }
            resultat = resultat + "--------------------------------------------------\n";
        }
        return resultat;
    }

    /**
     * Algorithme Glouton "Focus Sortie" (Standard).
     * 
     * Cet algorithme effectue un choix glouton local à chaque page en sélectionnant 
     * systématiquement la page voisine accessible possédant l'énigme la plus courte (durée minimale).
     * Il ignore totalement la collecte des objets requis et fonce vers la sortie.
     * 
     * @param borneMax La durée maximale d'une énigme autorisée pour franchir un arc.
     * 
     * @return Une {@link List} de {@link PageJeu} représentant le chemin extrait.
     */
    public List<PageJeu> algorithmeGloutonSortieSeule(double borneMax) {
        List<PageJeu> chemin = new ArrayList<>();
        PageJeu pageCourante = this.lesPagesDuJeu.get(0);
        chemin.add(pageCourante);

        List<PageJeu> visitees = new ArrayList<>();
        visitees.add(pageCourante);

        while (!pageCourante.estSortie()) {
            List<PageJeu> voisines = pageCourante.getPagesSuivantes();
            List<Enigme> enigmes = pageCourante.getEnigmes();
            
            if (voisines == null || voisines.isEmpty()) break; 

            PageJeu prochainePage = null;
            double tempsMin = Double.MAX_VALUE;

            for (int i = 0; i < voisines.size(); i++) {
                PageJeu voisine = voisines.get(i);
                Enigme enigme = enigmes.get(i);

                if (enigme.getDuree() > borneMax || visitees.contains(voisine)) {
                    continue;
                }

                if (enigme.getDuree() < tempsMin) {
                    tempsMin = enigme.getDuree();
                    prochainePage = voisine;
                }
            }

            if (prochainePage != null) {
                pageCourante = prochainePage;
                chemin.add(pageCourante);
                visitees.add(pageCourante);
            } else {
                break; 
            }
        }
        return chemin;
    }



    /**
     * EXPORT COMPLET ET ROBUSTE : Résout l'erreur de ClassCastException.
     * Utilise convertValueToString pour ne pas écraser les objets du graphe.
     */
    public void exporterEnPDF() {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("Rapport_LivreJeu.pdf"));
            document.open();

            // Configuration des couleurs et polices graphiques du document
            Color bleuNuitPDF = new Color(44, 62, 80);
            Color bleuRoiPDF = new Color(41, 128, 185);
            Font fontTitre = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, bleuNuitPDF);
            Font fontSousTitre = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, Color.GRAY);
            Font fontSection = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, bleuRoiPDF);
            Font fontTexte = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.DARK_GRAY);
            Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.WHITE);

            // 1. EN-TÊTE DU DOCUMENT
            document.add(new Paragraph("RAPPORT DE STRUCTURE ET PERFORMANCES", fontTitre));
            document.add(new Paragraph("Généré automatiquement — Format A4 Vertical", fontSousTitre));
            document.add(new Paragraph("\n---------------------------------------------------------------------------\n\n"));

            // 2. REPRÉSENTATION VISUELLE DU GRAPHE
            document.add(new Paragraph("1. Représentation visuelle du graphe", fontSection));
            document.add(new Paragraph("\n"));

            try {
                JGraphXAdapter<PageJeu, DefaultWeightedEdge> graphAdapter = new JGraphXAdapter<PageJeu, DefaultWeightedEdge>(this.graph) {
                    @Override
                    public String convertValueToString(Object cell) {
                        if (cell instanceof com.mxgraph.model.mxCell) {
                            com.mxgraph.model.mxCell mxCell = (com.mxgraph.model.mxCell) cell;
                            Object value = mxCell.getValue();
                            
                            if (mxCell.isVertex() && value instanceof PageJeu) {
                                return String.valueOf(((PageJeu) value).getNumero());
                            }
                            if (mxCell.isEdge() && value instanceof DefaultWeightedEdge) {
                                if (mxCell.getSource() != null && mxCell.getTarget() != null) {
                                    Object srcVal = mxCell.getSource().getValue();
                                    Object tgtVal = mxCell.getTarget().getValue();
                                    if (srcVal instanceof PageJeu && tgtVal instanceof PageJeu) {
                                        PageJeu source = (PageJeu) srcVal;
                                        PageJeu cible = (PageJeu) tgtVal;
                                        List<PageJeu> voisines = source.getPagesSuivantes();
                                        List<Enigme> enigmes = source.getEnigmes();
                                        for (int j = 0; j < voisines.size(); j++) {
                                            if (voisines.get(j).getNumero() == cible.getNumero()) {
                                                return (int) enigmes.get(j).getDuree() + "s";
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        return super.convertValueToString(cell);
                    }
                };

                java.util.Map<String, Object> edgeStyle = graphAdapter.getStylesheet().getDefaultEdgeStyle();
                edgeStyle.put(com.mxgraph.util.mxConstants.STYLE_STROKECOLOR, "#2c3e50");
                edgeStyle.put(com.mxgraph.util.mxConstants.STYLE_STROKEWIDTH, 1.5);
                edgeStyle.put(com.mxgraph.util.mxConstants.STYLE_ENDARROW, com.mxgraph.util.mxConstants.ARROW_CLASSIC);
                edgeStyle.put(com.mxgraph.util.mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, "#FFFFFF");
                edgeStyle.put(com.mxgraph.util.mxConstants.STYLE_FONTCOLOR, "#2c3e50");
                edgeStyle.put(com.mxgraph.util.mxConstants.STYLE_FONTSIZE, 10);
                edgeStyle.put(com.mxgraph.util.mxConstants.STYLE_FONTSTYLE, com.mxgraph.util.mxConstants.FONT_BOLD);

                for (Object vertexCell : graphAdapter.getChildVertices(graphAdapter.getDefaultParent())) {
                    com.mxgraph.model.mxCell cell = (com.mxgraph.model.mxCell) vertexCell;
                    PageJeu page = (PageJeu) cell.getValue();
                    
                    if (page != null) {
                        com.mxgraph.model.mxGeometry geo = cell.getGeometry();
                        geo.setWidth(45);
                        geo.setHeight(45);
                        
                        java.util.Map<String, Object> customStyle = new java.util.HashMap<>();
                        customStyle.put(com.mxgraph.util.mxConstants.STYLE_SHAPE, com.mxgraph.util.mxConstants.SHAPE_ELLIPSE);
                        customStyle.put(com.mxgraph.util.mxConstants.STYLE_PERIMETER, com.mxgraph.util.mxConstants.PERIMETER_ELLIPSE);
                        customStyle.put(com.mxgraph.util.mxConstants.STYLE_FONTCOLOR, "#FFFFFF");
                        customStyle.put(com.mxgraph.util.mxConstants.STYLE_FONTSTYLE, com.mxgraph.util.mxConstants.FONT_BOLD);
                        customStyle.put(com.mxgraph.util.mxConstants.STYLE_FONTSIZE, 12);
                        customStyle.put(com.mxgraph.util.mxConstants.STYLE_VERTICAL_ALIGN, com.mxgraph.util.mxConstants.ALIGN_MIDDLE);
                        customStyle.put(com.mxgraph.util.mxConstants.STYLE_ALIGN, com.mxgraph.util.mxConstants.ALIGN_CENTER);
                        
                        if (page.getNumero() == 1) {
                            customStyle.put(com.mxgraph.util.mxConstants.STYLE_FILLCOLOR, "#27ae60"); 
                            customStyle.put(com.mxgraph.util.mxConstants.STYLE_STROKECOLOR, "#1e8449");
                        } else if (page.estSortie()) {
                            customStyle.put(com.mxgraph.util.mxConstants.STYLE_FILLCOLOR, "#c0392b"); 
                            customStyle.put(com.mxgraph.util.mxConstants.STYLE_STROKECOLOR, "#962d22");
                        } else {
                            customStyle.put(com.mxgraph.util.mxConstants.STYLE_FILLCOLOR, "#2c3e50"); 
                            customStyle.put(com.mxgraph.util.mxConstants.STYLE_STROKECOLOR, "#1a252f");
                        }
                        
                        String styleName = "PageStyle_" + page.getNumero();
                        graphAdapter.getStylesheet().putCellStyle(styleName, customStyle);
                        cell.setStyle(styleName);
                    }
                }

                com.mxgraph.layout.mxCircleLayout layout = new com.mxgraph.layout.mxCircleLayout(graphAdapter);
                layout.setRadius(160); 
                layout.execute(graphAdapter.getDefaultParent());

                BufferedImage img = com.mxgraph.util.mxCellRenderer.createBufferedImage(
                        graphAdapter, null, 1.5, null, true, null);
                
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(img, "png", baos);
                Image imageGraphe = Image.getInstance(baos.toByteArray());
                
                imageGraphe.scaleToFit(500, 350); 
                imageGraphe.setAlignment(Image.ALIGN_CENTER);
                document.add(imageGraphe);

            } catch (Exception imgEx) {
                document.add(new Paragraph("[Schéma Visuel non généré] : " + imgEx.getMessage(), fontSousTitre));
            }
            document.add(new Paragraph("\n\n"));

            // 3. TABLEAU DE SYNTHÈSE ET COMPARATIF DES ALGOS
            document.add(new Paragraph("2. Résultats", fontSection));
            document.add(new Paragraph("\n"));

            // Tableau à 5 colonnes pour plus de précision
            PdfPTable tableau = new PdfPTable(5);
            tableau.setWidthPercentage(100);
            tableau.setWidths(new float[]{3.5f, 4.5f, 1.5f, 2f, 3.5f});

            PdfPCell h1 = new PdfPCell(new Paragraph("Nom de l'algorithme", fontHeader));
            PdfPCell h2 = new PdfPCell(new Paragraph("Chemin calculé", fontHeader));
            PdfPCell h3 = new PdfPCell(new Paragraph("Taille", fontHeader));
            PdfPCell h4 = new PdfPCell(new Paragraph("Temps Total", fontHeader));
            PdfPCell h5 = new PdfPCell(new Paragraph("Objets collectés", fontHeader));

            h1.setBackgroundColor(bleuNuitPDF); h2.setBackgroundColor(bleuNuitPDF);
            h3.setBackgroundColor(bleuNuitPDF); h4.setBackgroundColor(bleuNuitPDF);
            h5.setBackgroundColor(bleuNuitPDF);
            
            tableau.addCell(h1); tableau.addCell(h2); tableau.addCell(h3); tableau.addCell(h4); tableau.addCell(h5);

            // Préparation des listes à évaluer
            java.util.Map<String, List<PageJeu>> listesAlgos = new java.util.LinkedHashMap<>();
            listesAlgos.put("Glouton (Focus Sortie)", this.algorithmeGloutonSortieSeule(100.0));
            listesAlgos.put("Glouton (Correct)", this.algorithmeGloutonCorrect(100.0));
            listesAlgos.put("Dijkstra Séquentiel", this.algorithmeDijkstraCorrect());
            listesAlgos.put("Combinatoire Complet", this.algorithmeCombinatoireComplet());

            // Génération des lignes du tableau de manière dynamique
            for (java.util.Map.Entry<String, List<PageJeu>> entry : listesAlgos.entrySet()) {
                String nomAlgo = entry.getKey();
                List<PageJeu> chemin = entry.getValue();

                if (chemin != null && chemin.size() > 1) {
                    StringBuilder sbChemin = new StringBuilder();
                    StringBuilder sbObjets = new StringBuilder();
                    double tempsCumule = 0.0;

                    for (int i = 0; i < chemin.size(); i++) {
                        PageJeu curr = chemin.get(i);
                        sbChemin.append(curr.getNumero());
                        if (i < chemin.size() - 1) {
                            sbChemin.append("->");
                            
                            PageJeu next = chemin.get(i + 1);
                            List<PageJeu> voisines = curr.getPagesSuivantes();
                            List<Enigme> enigmes = curr.getEnigmes();
                            
                            for (int j = 0; j < voisines.size(); j++) {
                                if (voisines.get(j).getNumero() == next.getNumero()) {
                                    tempsCumule += enigmes.get(j).getDuree();
                                    break;
                                }
                            }
                        }

                        if (curr.contientObjet() && curr.getObjet() != null) {
                            if (sbObjets.length() > 0) sbObjets.append(", ");
                            sbObjets.append(curr.getObjet().getNom());
                        }
                    }

                    int arcs = chemin.size() - 1;
                    String chaineObjets = sbObjets.length() == 0 ? "Aucun" : sbObjets.toString();
                    
                    tableau.addCell(new PdfPCell(new Paragraph(nomAlgo, fontTexte)));
                    tableau.addCell(new PdfPCell(new Paragraph(sbChemin.toString(), fontTexte)));
                    tableau.addCell(new PdfPCell(new Paragraph(arcs + " arcs", fontTexte)));
                    tableau.addCell(new PdfPCell(new Paragraph((int)tempsCumule + " s", fontTexte))); 
                    tableau.addCell(new PdfPCell(new Paragraph(chaineObjets, fontTexte)));
                } else {
                    tableau.addCell(new PdfPCell(new Paragraph(nomAlgo, fontTexte)));
                    PdfPCell cellErreur = new PdfPCell(new Paragraph("⚠️ Chemin introuvable ou bloqué.", fontTexte));
                    cellErreur.setColspan(4);
                    tableau.addCell(cellErreur);
                }
            }

            document.add(tableau);
            System.out.println("[PDF SUCCESS] Le rapport contient désormais le comparatif des 4 algorithmes.");

        } catch (Exception e) {
            System.out.println("[PDF ERROR] " + e.getMessage());
        } finally {
            document.close();
        }
    }
}