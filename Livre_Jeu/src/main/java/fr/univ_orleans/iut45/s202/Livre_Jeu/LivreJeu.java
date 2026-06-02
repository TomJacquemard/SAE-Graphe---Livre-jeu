package fr.univ_orleans.iut45.s202.Livre_Jeu;

//rajouter la condition elif(choixGenerateur.equals("genererLivreJeu_2")){} dans le constructeur de LivreJeu
//dans TestLivreJeu.java mettre un test pour la génération du graphe qui va générer un pdf mais changer le titre du pdf graph.pdf en graph2.pdf
//


import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
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

    //compteurs de temps d'executions pour les deux algos de générations
    private int compteurTmpsExec1 = 0; //on va incrémenter ces deux compteurs en fonction des étapes de génération pour vérifier les temps d'exécution. 
    private int compteurTmpsExec2 = 0;

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

        for (int i = 2; i < nbPages; i++) {
            this.lesPagesDuJeu.add(new PageJeu(i, "Lorem ipsum", false));
        }

        Random random = new Random();
        for(int i = 1; i<=nbObjets;i++){
            ObjetJeu objetCourant = new ObjetJeu("Objet " + i); //création de l'objet que l'on ajoute sur la page
            
            this.compteurTmpsExec1 += 1;
            this.compteurTmpsExec2 += 1;

            boolean indiceOk = false;

            while(!(indiceOk)){ //tant que je n'ai pas sélectionné une page ne contenant pas déjà un objet...
                int indiceAleatoire = random.nextInt(lesPagesDuJeu.size());
                PageJeu pageChoisie = lesPagesDuJeu.get(indiceAleatoire);

                this.compteurTmpsExec1 += 1;
                this.compteurTmpsExec2 += 1;

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

        else if(choixGenerateur.equals(("genererLivreJeu2"))) {
            genererLivreJeu2(pageEntree, pageDeSortie);
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

    public int getCompteur1(){
        return this.compteurTmpsExec1;
    }

    public int getCompteur2(){
        return this.compteurTmpsExec2;
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
            
            this.compteurTmpsExec1 += 1;
        
            PageJeu pageEnPlacement = pagesAPlacer.remove(0);
            Collections.shuffle(pagesValides); //mélange de la liste à chaque fois pour plus de hasard sur les arrêtes
            this.compteurTmpsExec1 += pagesValides.size();//car shuffle est en complexité O(n)


            int nbPagesChoisies = 1 + random.nextInt(pagesValides.size()); //choisira un nb de pages entre 1 et pagesValide.size() (+1 pour éviter d'obtenir 0)
            //Une page pourrait donc être reliée à toutes les autres déjà placées si on tombe pile sur pagesValides
            List<PageJeu> pagesChoisies = pagesValides.subList(0, nbPagesChoisies);//on sélectionne les pages vers lesquelles la pageEnPlacement va mener

            for (PageJeu p : pagesChoisies) {

                this.compteurTmpsExec1 += 1;

                pageEnPlacement.ajoutePage(p); //on ajoute la page voisine aux pagesSuivantes de pageEnPlacement
                int dureeEnigme = 1 + random.nextInt(20);//Choix aléatoire de la durée de l'Enigme menant à la page voisine p
                Enigme e = new Enigme("Lorem Ipsum", dureeEnigme);//création et ajout de l'énigme 
                pageEnPlacement.ajouteEnigme(e);
                
            }
            pagesValides.add(pageEnPlacement);
            //A la fin de la boucle while, toutes les pages seront placées, sauf la page d'entrée
            //A la fin de la boucle while, toutes les pages seront placées, sauf la page d'entrée
        }

        List<PageJeu> pagesIsolees = this.pagesSansSource();
        
        if (pagesIsolees.size() > 1) {  //si il y des pages isolées... >1 car la page d'entrée est pour le moment FORCEMENT isolée + c'est aussi le cas pour la dernière page que l'on a ajouté normalement cette condition est donc toujours vérifiée
            for (PageJeu pIsolee : pagesIsolees) { //on relie la page d'entrée à ces pages isolées
                
                this.compteurTmpsExec1 += 1;

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

            this.compteurTmpsExec1 += 1;

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

            this.compteurTmpsExec1 += 1;

            if (!p.equals(pageDeSortie) && (!pageDeSortie.getPagesSuivantes().contains(p))) { //sécurité mais normalement impossible de retomber sur pageDeSortie PARCONTRE DEUXIEME PARTIE DE LA CONDITION IMPORTANT !!! rare avec les grands graphes mais avec le bloc PRECEDENT on pourrait déjà ajouter pageDeSortie.ajouterPage(PageEntree) - ON NE PEUT PAS AJOUTER DEUX FOIS LA MEME ARETE ORIENTEE donc cela provoquerait une erreur
                pageDeSortie.ajoutePage(p);
                int dureeEnigme = 1 + random.nextInt(20);
                pageDeSortie.ajouteEnigme(new Enigme("Lorem Ipsum", dureeEnigme));
                }
            }
    }

     /** SETTER MANUEL POUR TESTS: Change le graphe du livre jeu à des fins de test.
     * @return Rien car inutile de savoir quoi que ce soit.
     */
    public void setGraphe(SimpleDirectedWeightedGraph<PageJeu, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    public void ajouterPage(PageJeu p) {
        this.lesPagesDuJeu.add(p);
        this.graph.addVertex(p);
    }

    public void ajouterLien(PageJeu source,
                        PageJeu destination,
                        double poids) {

        DefaultWeightedEdge edge =
            this.graph.addEdge(source, destination);

        this.graph.setEdgeWeight(edge, poids);
    }

    public void supprimerTout() {
        this.lesPagesDuJeu = new ArrayList<>();
        this.lesObjets = new ArrayList<>();
        this.graph = null;

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

            this.compteurTmpsExec1 += 1;

            aUneSource=false;
            for (PageJeu p : this.lesPagesDuJeu) {

                this.compteurTmpsExec1 += 1;

                if (p.getPagesSuivantes().contains(pCourante)){
                        aUneSource=true;
                    }
                }
                if(!(aUneSource)&& !(pagesSansSource.contains(pCourante))){pagesSansSource.add(pCourante);} //si je n'ai trouvé aucune source à la fin de mon parcours pour la page courante je l'ajoute aux pages sans source
            
        }
        return pagesSansSource;

    }

    public void remplirGraphe(){
        for(PageJeu pageCourante : lesPagesDuJeu){ //obligé de parcourir en deux temps : car addVertex(pageA,pageB) nécessite deux pages déjà dans le graphe
        this.graph.addVertex(pageCourante);
        }
        for(PageJeu pageCourante : lesPagesDuJeu){

            this.compteurTmpsExec1 += 1;
            this.compteurTmpsExec2 += 1;

            List<PageJeu> pagesSuivantes = pageCourante.getPagesSuivantes();
            List<Enigme> enigmes = pageCourante.getEnigmes();
            for(int i = 0; i<pagesSuivantes.size(); i ++){

                this.compteurTmpsExec1 += 1;
                this.compteurTmpsExec2 += 1;

                DefaultWeightedEdge nouvelleEdge = this.graph.addEdge(pageCourante, pagesSuivantes.get(i));
                if(nouvelleEdge!=null){
                this.graph.setEdgeWeight(nouvelleEdge,enigmes.get(i).getDuree()); //on attribut le poids a la nouvelle arrete
                }
            }
        }
    }

    public List<PageJeu> getPagesJeu(){//obligée de recréer plusieurs méthodes de livre en rapport avec les pages car je veux obtenir des PageJeu et avec les methodes de Livre j'obtenais des Pages
        return this.lesPagesDuJeu;
    }

   
    /**
     * GETTER : Accès à la modélisation sous forme de graphe.
     * @return L'objet Graph (JGraphT) représentant l'ossature du livre-jeu.
     */
    public Graph<PageJeu, DefaultWeightedEdge> getGraphe() {
        return this.graph;
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
     * @return La List des objets collectés avec succès au cours de la partie.
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

    
    public void genererLivreJeu2(PageJeu pageEntree, PageJeu pageSortie) {
        /*Vérifier si la liste contient bien la page d'entrée et de sortie en question */
        if (!this.lesPagesDuJeu.contains(pageEntree)|| !this.lesPagesDuJeu.contains(pageSortie)) {
            throw new IllegalArgumentException("La page d'entrée ou de sortie n'appartient pas à ce livre");
        }

        /*Isoler les pages intermédiaires en retirant pageEntrée et pageSortie pour créer un chemin sûr */
        List<PageJeu> pagesDisponibles = new ArrayList<>(this.lesPagesDuJeu);
        pagesDisponibles.remove(pageEntree);
        pagesDisponibles.remove(pageSortie);

        //Mélange des pages intermédiaires pour la notion de hasard
        Collections.shuffle(pagesDisponibles);
        this.compteurTmpsExec2 += pagesDisponibles.size();

        //Construction du chemin sûr en prenant pour départ la page d'entrée 
        //Commençons avec un chemin de trois pages
        List<PageJeu> cheminsSurs = new ArrayList<>(); // je crée la liste qui va contenir au fur et à mesure les pages du chemin
        cheminsSurs.add(pageEntree);
        int nbPagesIntermediaires = Math.min(3, pagesDisponibles.size());
        PageJeu pageCourante = pageEntree;

        for (int i =0; i< nbPagesIntermediaires; i++) {

            this.compteurTmpsExec2 += 1;

            PageJeu pageSuivante = pagesDisponibles.remove(0);
            pageCourante.ajoutePage(pageSuivante); // crée l'arc 
            double duree = 1.0 + (Math.random()*20.0);
            pageCourante.ajouteEnigme(new Enigme("Enigme chemin", duree));
            cheminsSurs.add(pageSuivante);
            pageCourante = pageSuivante;
        }

        //On connecte la dernière page courante à la page de sortie 
        pageCourante.ajoutePage(pageSortie);
        double dureeFinale = 1.0 + (Math.random()*20.0);
        pageCourante.ajouteEnigme(new Enigme("Enigme sortie", dureeFinale));
        cheminsSurs.add(pageSortie);

        //Pour ajouter maintenant de façon hasardeuse le reste de pages
        //Séparation des pages disponibles : une partie pour le chemin piège, l'autre pour allonger le chemin sûr
        //30% piege et 70% chemin allongé.
       
        //Boucle pour allonger le chemin sûr 
        while (!pagesDisponibles.isEmpty()) {

            this.compteurTmpsExec2 += 1;

            PageJeu pageAllongee = pagesDisponibles.remove(0);
            int indexSource = (int) (Math.random()*(cheminsSurs.size()-1));
            PageJeu pageSource2 = cheminsSurs.get(indexSource);

            //On prend une destination aléatoire dans le chemin sûr 
            int indexDestinationAleatoire1 = (int) (Math.random()*(cheminsSurs.size()));
            PageJeu pageDestination = cheminsSurs.get(indexDestinationAleatoire1);

            // Sécurité anti-bouclage immédiat sur soi-même
            if (pageDestination == pageAllongee || pageDestination == pageSource2) {
                pageDestination = pageSortie; 
        }
            //On crée l'arc entre la page source et la page allongée
            pageSource2.ajoutePage(pageAllongee);
            double duree4 = 1.0 + (Math.random()*20.0);
            pageSource2.ajouteEnigme(new Enigme("Enigme de liaison", duree4));
            //Je connecte la pageAllongée à la desination "actuelle" de la page source
            pageAllongee.ajoutePage(pageDestination);
            double duree5 = 1.0 + (Math.random()*20.0);
            pageAllongee.ajouteEnigme(new Enigme("Enigme de liaison", duree5));

            //Choix multiple (50% de chance d'avoir une deuxième arête sortante)
             if (Math.random() > 0.1) {
                int indexDest2 = (int)(Math.random() * cheminsSurs.size());
                PageJeu autreDestination = cheminsSurs.get(indexDest2);
                
                if (autreDestination != pageSource2 && autreDestination != pageAllongee && autreDestination!= pageDestination && !pageSource2.getPagesSuivantes().contains(autreDestination))  {
                        pageSource2.ajoutePage(autreDestination);
                        pageSource2.ajouteEnigme(new Enigme("Deuxième choix de la page", 1.0 + (Math.random() * 20.0)));
            }

            }
            //Choix multiple aussi sur la page Allongée (90% de chances)
            if (Math.random() > 0.1) {
                int indexDest3 = (int)(Math.random() * cheminsSurs.size());
                PageJeu destinationBonus = cheminsSurs.get(indexDest3);
                
                if (destinationBonus != pageSource2 && destinationBonus != pageAllongee && destinationBonus != pageDestination) {
                    pageAllongee.ajoutePage(destinationBonus);
                    pageAllongee.ajouteEnigme(new Enigme("Sortie rallonge B (Bifurcation)", 1.0 + (Math.random() * 20.0)));
                }

            }    

            // On met à jour notre liste de suivi pour que pageRallonge puisse 
            // elle-même accueillir d'autres pages plus tard --> systeme de prolongation de la liste. 
            cheminsSurs.add(pageAllongee);

        }
        //Pour avoir un lien entre la page de sortie et celle d'entrée (aucune réciprocité dans l'autre sinon le jeu serait trop facile)
        pageSortie.ajoutePage(pageEntree);
        double duree6 = 1.0 + (Math.random()*20.0);
        pageSortie.ajouteEnigme(new Enigme("Enigme de fin", duree6));
    }




    public boolean cheminValide(List<PageJeu> chemin) {

        if (chemin == null || chemin.isEmpty()) {
            return false;
        }

        // Vérifie page de départ
        if (!chemin.get(0).equals(this.lesPagesDuJeu.get(0))) {
            return false;
        }

        // Vérifie page de sortie
        /*if (!chemin.get(chemin.size() - 1).equals()) {
            return false;
        }*/

        Set<ObjetJeu> objetsTrouves = new HashSet<>();

        for (int i = 0; i < chemin.size(); i++) {

            PageJeu actuelle = chemin.get(i);

            // Récupération objet
            if (actuelle.getObjet() != null) {
                objetsTrouves.add(actuelle.getObjet());
            }

            // Vérifie transition
            if (i < chemin.size() - 1) {

                PageJeu suivante = chemin.get(i + 1);

                if (!this.graph.containsEdge(actuelle, suivante)) {
                    return false;
                }
            }
        }

        // Vérifie tous les objets récupérés
        return objetsTrouves.containsAll(this.lesObjets);
    }

    // =========================================================================
    // 1. ALGORITHME GLOUTON
    // =========================================================================
    /**
     * Algorithme Glouton basé sur une Heuristique de Proximité Locale.
     * Choix local pur : Analyse les voisins immédiats et se déplace vers celui 
     * qui maximise la chance d'attraper un objet ou de se rapprocher du numéro de sa page cible.
     * * @return Une List de PageJeu représentant le chemin glouton.
     */
    
    public List<PageJeu> algorithmeGloutonCorrect() {
        // Initialisation de la structure accueillant le chemin final
        List<PageJeu> chemin = new ArrayList<>();
        
        // Sécurité : si le graphe est vide, on retourne une liste vide
        if (this.lesPagesDuJeu == null || this.lesPagesDuJeu.isEmpty()) return chemin;

        // Déclaration des pointeurs pour identifier dynamiquement le départ et la sortie
        PageJeu pageCourante = null;
        PageJeu pageSortie = null;
        for (PageJeu p : this.lesPagesDuJeu) {
            if (p.getNumero() == 1) pageCourante = p;
            if (p.estSortie()) pageSortie = p;
        }
        
        // Redondance de sécurité : par défaut, on commence sur le premier élément indexé
        if (pageCourante == null) pageCourante = this.lesPagesDuJeu.get(0);

        // Duplication de l'inventaire global pour suivre notre progression de collecte
        List<ObjetJeu> objetsAAttraper = new ArrayList<>(this.getListeObjets());
        chemin.add(pageCourante);

        // Historique de passage local pour appliquer des pénalités numériques et éviter d'osciller
        List<PageJeu> historiqueVisites = new ArrayList<>();
        historiqueVisites.add(pageCourante);

        // Boucle d'exploration pas à pas
        while (true) {
            // Condition de victoire : inventaire de quête plein et positionné sur la sortie
            if (objetsAAttraper.isEmpty() && pageCourante.estSortie()) {
                break;
            }

            List<PageJeu> voisines = pageCourante.getPagesSuivantes();
            List<Enigme> enigmes = pageCourante.getEnigmes();
            if (voisines == null || voisines.isEmpty()) break;

            // Détermination de notre objectif heuristique actuel
            PageJeu cibleActuelle = pageSortie; // Par défaut, on cherche à sortir
            
            // S'il reste des objets, la boussole s'oriente vers la page contenant l'un d'eux
            if (!objetsAAttraper.isEmpty()) {
                for (PageJeu p : this.lesPagesDuJeu) {
                    if (p.contientObjet() && objetsAAttraper.contains(p.getObjet())) {
                        cibleActuelle = p;
                        break;
                    }
                }
            }

            PageJeu meilleurVoisin = null;
            double meilleurScore = Double.MAX_VALUE;

            // Analyse comparative de toutes les transitions immédiates
            for (int i = 0; i < voisines.size(); i++) {
                PageJeu v = voisines.get(i);
                Enigme e = enigmes.get(i);

                // Pénalité numérique lourde si le voisin a déjà été visité (évite les boucles infinies)
                double penaliteVisite = historiqueVisites.contains(v) ? 1000.0 : 0.0;
                
                // Calcul de l'écart numérique absolu
                double distanceHeuristique = Math.abs(v.getNumero() - cibleActuelle.getNumero());
                
                // Le score glouton combine coût de l'arête, distance théorique et pénalité
                double scoreVoisin = e.getDuree() + distanceHeuristique + penaliteVisite;

                // Règle absolue : si un voisin direct possède l'objet, on force sa sélection immédiate
                if (!objetsAAttraper.isEmpty() && v.contientObjet() && objetsAAttraper.contains(v.getObjet())) {
                    scoreVoisin = -9999.0;
                }

                // Élection du voisin ayant le score minimal
                if (scoreVoisin < meilleurScore) {
                    meilleurScore = scoreVoisin;
                    meilleurVoisin = v;
                }
            }

            // Application du déplacement si un choix a été validé
            if (meilleurVoisin != null) {
                pageCourante = meilleurVoisin;
                chemin.add(pageCourante);
                historiqueVisites.add(pageCourante);
                
                // Si la page contient un trésor attendu, on le raye des objectifs restants
                if (pageCourante.contientObjet()) {
                    objetsAAttraper.remove(pageCourante.getObjet());
                }
            } else {
                break;
            }
        }
        return chemin;
    }

    // =========================================================================
    // 2. ALGORITHME DE DIJKSTRA (PAR SEGMENTS - CORRIGÉ)
    // =========================================================================
    /**
     * Algorithme de Dijkstra Exhaustif et Correct.
     * Calcule le chemin optimal absolu en utilisant l'algorithme des plus courts chemins de Dijkstra.
     * Pour respecter la contrainte des objets, cet algorithme calcule par étapes le chemin optimal :
     * Il relie dynamiquement le point de départ à l'objet le plus proche, puis de cet objet au suivant
     * le plus proche, jusqu'à ce que tous les objets soient collectés, avant de rallier la sortie.
     * @return Une List de PageJeu représentant le chemin optimal absolu respectant les règles.
     */
    public List<PageJeu> algorithmeDijkstraCorrect() {
        List<PageJeu> cheminComplet = new ArrayList<>();
        if (this.lesPagesDuJeu == null || this.lesPagesDuJeu.isEmpty()) return cheminComplet;

        List<PageJeu> pagesAvecObjetsRestants = new ArrayList<>();
        PageJeu pageSortie = null;
        PageJeu positionCourante = null;

        // Identification de la source (1), de l'arrivée et des points d'intérêt (objets)
        for (PageJeu p : this.lesPagesDuJeu) {
            if (p.getNumero() == 1) positionCourante = p;
            if (p.estSortie()) {
                pageSortie = p;
            } else if (p.contientObjet() && p.getNumero() != 1) {
                pagesAvecObjetsRestants.add(p);
            }
        }
        
        if (positionCourante == null) positionCourante = this.lesPagesDuJeu.get(0);
        cheminComplet.add(positionCourante);

        // Instanciation de l'outil de calcul sur notre graphe global
        DijkstraShortestPath<PageJeu, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(this.graph);

        // PHASE 1 : Collecte successive des objets restants
        while (!pagesAvecObjetsRestants.isEmpty()) {
            PageJeu cibleLaPlusProche = null;
            GraphPath<PageJeu, DefaultWeightedEdge> meilleurPathSegment = null;
            double distanceMin = Double.MAX_VALUE;

            // Évaluation mathématique du coût pour rallier chaque objet depuis notre position courante
            for (PageJeu cible : pagesAvecObjetsRestants) {
                GraphPath<PageJeu, DefaultWeightedEdge> path = dijkstra.getPath(positionCourante, cible);
                
                // Sélection du segment valide le plus court
                if (path != null && path.getWeight() < distanceMin) {
                    distanceMin = path.getWeight();
                    meilleurPathSegment = path;
                    cibleLaPlusProche = cible;
                }
            }

            // Si un itinéraire interconnecté vers l'objet le plus proche existe
            if (meilleurPathSegment != null) {
                List<PageJeu> subPath = new ArrayList<>(meilleurPathSegment.getVertexList());
                if (!subPath.isEmpty()) {
                    subPath.remove(0); // Suppression du premier nœud pour éviter d'inscrire des doublons continus
                }
                cheminComplet.addAll(subPath); // Fusion dans l'itinéraire complet
                
                positionCourante = cibleLaPlusProche; // Avancement de la position de référence
                pagesAvecObjetsRestants.remove(cibleLaPlusProche); // Objectif validé
            } else {
                // S'il n'y a aucune liaison physique menant à cet objet (graphe déconnecté)
                System.out.println("[DIJKSTRA] Impossible d'atteindre tous les objets.");
                return new ArrayList<>(); // Interruption propre et retour d'un chemin vide
            }
        }

        // PHASE 2 : Ralliement final vers la sortie du livre-jeu
        if (pageSortie != null) {
            GraphPath<PageJeu, DefaultWeightedEdge> cheminVersSortie = dijkstra.getPath(positionCourante, pageSortie);
            if (cheminVersSortie != null) {
                List<PageJeu> subPath = new ArrayList<>(cheminVersSortie.getVertexList());
                if (!subPath.isEmpty()) {
                    subPath.remove(0); // Nettoyage du nœud pivot pour éviter les doublons
                }
                cheminComplet.addAll(subPath);
            } else {
                System.out.println("[DIJKSTRA] Impossible de rejoindre la sortie.");
                return new ArrayList<>();
            }
        }
        System.out.println(cheminComplet);
        return cheminComplet;
    }

    
    private List<PageJeu> meilleurCheminComplet;
    private double meilleurTempsComplet;


    // =========================================================================
    // 3. ALGORITHME COMBINATOIRE COMPLET (BACKTRACKING (RECHERCHE EXHAUSTIVE))
    // =========================================================================
    /**
     * Algorithme de Recherche Combinatoire Complète (Backtracking).
     * Explore l'intégralité de l'arbre des possibles de façon récursive.
     * Il teste toutes les alternatives de routes et mémorise le chemin qui valide 
     * l'intégralité des objectifs (objets collectés + sortie atteinte) avec le temps cumulé minimal.
     * @return La List de PageJeu du chemin parfait trouvé par force brute.
     */
    public List<PageJeu> algorithmeCombinatoireComplet() {
        // Réinitialisation des records de classe avant de lancer la récursion
        this.meilleurTempsComplet = Double.MAX_VALUE;
        this.meilleurCheminComplet = null;

        PageJeu depart = null;
        int totalObjetsAttendus = this.getListeObjets().size();

        // Recherche du nœud d'entrée du livre-jeu (Page numéro 1)
        for (PageJeu p : this.lesPagesDuJeu) {
            if (p.getNumero() == 1) {
                depart = p;
                break;
            }
        }
        if (depart == null) depart = this.lesPagesDuJeu.get(0);

        // Initialisation de la pile de chemin dynamique
        List<PageJeu> cheminEnCours = new ArrayList<>();
        cheminEnCours.add(depart);

        // Initialisation de notre sac d'objets portés au point de départ
        Set<String> objetsPossedesInitiaux = new HashSet<>();
        if (depart.contientObjet() && depart.getObjet() != null) {
            objetsPossedesInitiaux.add(depart.getObjet().getNom());
        }

        // Compteur d'occurrence de passage pour autoriser les boucles indispensables tout en évitant les cycles infinis
        Map<PageJeu, Integer> compteurVisites = new HashMap<>();
        compteurVisites.put(depart, 1);

        // Lancement de la procédure d'exploration récursive globale
        backtrackingRecursive(depart, cheminEnCours, 0.0, totalObjetsAttendus, objetsPossedesInitiaux, compteurVisites);

        System.out.println(meilleurTempsComplet);

        // Renvoi de la solution optimale trouvée (ou liste vide si aucune configuration n'est valide)
        return this.meilleurCheminComplet != null ? this.meilleurCheminComplet : new ArrayList<>();
    }

    private void backtrackingRecursive(PageJeu noeud, List<PageJeu> cheminEnCours, double tempsAccumule, 
                                      int totalObjetsAttendus, Set<String> objetsPossedes, 
                                      Map<PageJeu, Integer> compteurVisites) {
        
        // ÉLAGAGE (Pruning) : Si le coût actuel égale ou dépasse notre record, on coupe immédiatement la branche
        if (tempsAccumule >= this.meilleurTempsComplet) return;

        // ARRIVÉE SUR LA SORTIE : On examine si les conditions de réussite réglementaires sont remplies
        if (noeud.estSortie()) {
            if (objetsPossedes.size() >= totalObjetsAttendus) {
                // Nouveau record absolu mémorisé physiquement
                this.meilleurTempsComplet = tempsAccumule;
                this.meilleurCheminComplet = new ArrayList<>(cheminEnCours);
            }
            return; // Fin de parcours de cette branche spécifique
        }

        List<PageJeu> voisines = noeud.getPagesSuivantes();
        List<Enigme> enigmes = noeud.getEnigmes();
        if (voisines == null) return;

        // Analyse récursive de toutes les ramifications de transition possibles
        for (int i = 0; i < voisines.size(); i++) {
            PageJeu voisine = voisines.get(i);
            Enigme enigme = enigmes.get(i);

            // BORNE ANTI-CYCLE : Si un nœud a déjà été traversé 3 fois dans cette sous-branche, on refuse d'y retourner
            int visites = compteurVisites.getOrDefault(voisine, 0);
            if (visites >= 3) {
                continue; 
            }

            // MARQUAGE DE L'AVANCEMENT (Do)
            cheminEnCours.add(voisine);
            compteurVisites.put(voisine, visites + 1);
            
            // Actualisation de la sacoche d'objets (nouvelle instance isolée pour préserver les branches sœurs)
            Set<String> nouveauxObjets = new HashSet<>(objetsPossedes);
            if (voisine.contientObjet() && voisine.getObjet() != null) {
                nouveauxObjets.add(voisine.getObjet().getNom());
            }

            // RECURSION : Descente au niveau de profondeur inférieur avec cumul des coûts
            backtrackingRecursive(voisine, cheminEnCours, tempsAccumule + enigme.getDuree(), 
                                  totalObjetsAttendus, nouveauxObjets, compteurVisites);

            // NETTOYAGE ET RETOUR EN ARRIÈRE (Undo / Backtrack)
            compteurVisites.put(voisine, visites);
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
     * @return Une List de PageJeu représentant le chemin extrait.
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
        System.out.println(chemin);
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
            listesAlgos.put("Glouton (Correct)", this.algorithmeGloutonCorrect());
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