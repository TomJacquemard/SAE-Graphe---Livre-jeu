package fr.univ_orleans.iut45.s202.Livre_Jeu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;


public class LivreJeu extends Livre{
    private List<ObjetJeu> lesObjets;
    private List<ObjetJeu> objetsRecuperes;
    private List<PageJeu> pages;
    private List<PageJeu> lesPagesDuJeu;
    private Graph<PageJeu, DefaultWeightedEdge> graph;


    public LivreJeu(String titre, int nbPages) {
        super(titre, nbPages);
        this.lesObjets = new ArrayList<>();
        this.objetsRecuperes = new ArrayList<>();
        this.graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        this.lesPagesDuJeu = new ArrayList<>();

        this.pages = new ArrayList<>();
    }

    public List<PageJeu> getListePageJeu() {
        return this.pages;
    }


    public void genererLivreJeu_1(PageJeu pageDeSortie, PageJeu pageEntree){

        List<PageJeu> pagesValides = new ArrayList<>(); //pagesValides = pages placées dans le graphe
        pagesValides.add(pageDeSortie);

        List<PageJeu> pagesAPlacer = new ArrayList<>(this.getPagesJeu()); //-> marche pas car getPages() renvoit une LIst<Page> et pas une List<PageJeu>
        pagesAPlacer.remove(pageEntree);
        pagesAPlacer.remove(pageDeSortie);
        Random random = new Random();

        while (!(pagesAPlacer.size()==0)){ //tant qu'il reste des pages à placer
            PageJeu pageEnPlacement = pagesAPlacer.remove(0);
            Collections.shuffle(pagesValides); //je mélange la liste à chaque fois pour plus de hasard sur les liens entre les arrêtes
            
            //Prblm ici, une page pourrait être reliée à énormément d'autres si on tombe pile sur pagesValides
            int nbPagesChoisies = 1 + random.nextInt(pagesValides.size()); //choisira un nb de pages entre 1 et pagesValide.size() (+1 pour éviter d'obtenir 0)
            List<PageJeu> pagesChoisies = pagesValides.subList(0, nbPagesChoisies); //on sélectionne les pages vers lesquelles la pageEnPlacement va mener

            for(PageJeu p : pagesChoisies){
                pageEnPlacement.ajoutePage(p); //on ajoute la page voisine aux pagesSuivantes de pageEnPlacement
                int dureeEngime = 1+random.nextInt(20); //Choix aléatoire de la durée de l'Enigme menant à la page voisine p
                pageEnPlacement.ajouteEnigme(new Enigme("Lorem Ipsum",dureeEngime)); //création et ajout de l'énigme en parallèle de l'ajout de la page
            }
            pagesValides.add(pageEnPlacement);
            //A la fin de la boucle while, toutes les pages seront placées, sauf la page d'entrée
        }

        List<PageJeu> pagesIsolees = this.pagesSansSource();
        if (pagesIsolees.size()>0){ //si il y des pages isolées...
            for (PageJeu pIsolee : pagesIsolees){ //on relie la page d'entrée à ces pages
                pageEntree.ajoutePage(pIsolee);
                int dureeEngime = 1+random.nextInt(20); //Choix aléatoire de la durée de l'Enigme 
                pageEntree.ajouteEnigme(new Enigme("Lorem Ipsum",dureeEngime)); //création et ajout de l'énigme
            }
        }
        else{ //sinon je relie la page d'entrée à quelques pages
            int nbPagesChoisies = 1 + random.nextInt(4); //4 pages maximum
            List<PageJeu> pagesChoisies = pagesValides.subList(0, nbPagesChoisies); 
            for(PageJeu p : pagesChoisies){
                pageEntree.ajoutePage(p); 
                int dureeEngime = 1+random.nextInt(20); 
                pageEntree.ajouteEnigme(new Enigme("Lorem Ipsum",dureeEngime)); //création et ajout de l'énigme en parallèle de l'ajout de la page
            }
        }

        //Enfin on rajoute quelques pages menant vers le début
        int nbPagesChoisies = 1 + random.nextInt(4); 
        List<PageJeu> pagesChoisies = pagesValides.subList(0, nbPagesChoisies); 
        for(PageJeu p : pagesChoisies){
            p.ajoutePage(pageEntree); 
            int dureeEngime = 1+random.nextInt(20); 
            p.ajouteEnigme(new Enigme("Lorem Ipsum",dureeEngime));
            }
    }

    public void remplirGraphe(){
        //On rempli les vertex et edge selon la liste lesPagesDuJeu
        for(PageJeu pageCourante : lesPagesDuJeu){
            this.graph.addVertex(pageCourante);
            List<PageJeu> pagesSuivantes = pageCourante.getPagesSuivantes();
            List<Enigme> enigmes = pageCourante.getEnigmes();
            for(int i = 0; i<pagesSuivantes.size(); i ++){
                DefaultWeightedEdge nouvelleEdge = this.graph.addEdge(pageCourante, pagesSuivantes.get(i));
                
                this.graph.setEdgeWeight(nouvelleEdge,enigmes.get(i).getDifficultee()); //on attribut le poids a la nouvelle arrete
            }
        }
    }

    public List<PageJeu> pagesSansSource(){
        List<PageJeu> pagesSansSource = new ArrayList<>();
        boolean aUneSource = false;
        for (PageJeu pCourante : this.lesPagesDuJeu){
            aUneSource=false;
            for(PageJeu p : this.lesPagesDuJeu){
                if (p.getPagesSuivantes().contains(pCourante)){
                    aUneSource=true;
                }
            }
            if(!(aUneSource)){pagesSansSource.add(pCourante);} //si je n'ai trouvé aucune source à la fin de mon parcours pour la page courante je l'ajoute aux pages sans source
        }
        return pagesSansSource;
    }

    public List<PageJeu> getPagesJeu(){//obligée de recréer plusieurs méthodes de livre en rapport avec les pages car je veux obtenir des PageJeu et avec les methodes de Livre j'obtenais des Pages
        return this.lesPagesDuJeu;
    }

    public PageJeu getPageNumero(int numPage) throws IndexOutOfBoundsException{ //methode recréée
        for (PageJeu p : this.lesPagesDuJeu){
            if (p.getNumero() == numPage){
                return p;
            }
        }
        throw new IndexOutOfBoundsException("Page inexistante");

    }

    public List<ObjetJeu> getListeObjets() {
        return this.lesObjets;
    }

    public List<ObjetJeu> getObjetsRecuperes() {
        return this.objetsRecuperes;
    }

     // AJoute l'objet dans la liste des objets récupérés. 

    public void recupererObjet(ObjetJeu objet) { 
        objet.recupererObjet();
        if (objet.estRecupere()){
            objetsRecuperes.add(objet);
        }
    }
    
    public void genererLivreJeu2(PageJeu pageEntree, PageJeu pageSortie) {
        /*Vérifier si la liste contient bien la page d'entrée et de sortie en question */
        if (!this.pages.contains(pageEntree)|| !this.pages.contains(pageSortie)) {
            throw new IllegalArgumentException("La page d'entrée ou de sortie n'appartient pas à ce livre");
        }

        /*Isoler les pages intermédiaires en retirant pageEntrée et pageSortie pour créer un chemin sûr */
        List<PageJeu> pagesDisponibles = new ArrayList<>(this.pages);
        pagesDisponibles.remove(pageEntree);
        pagesDisponibles.remove(pageSortie);

        //Mélange des pages intermédiaires pour la notion de hasard
        Collections.shuffle(pagesDisponibles);

        //Construction du chemin sûr en prenant pour départ la page d'entrée 
        //Commençons avec un chemin de trois pages
        List<PageJeu> cheminsSurs = new ArrayList<>(); // je crée la liste qui va contenir au fur et à mesure les pages du chemin
        cheminsSurs.add(pageEntree);
        int nbPagesIntermediaires = Math.min(3, pagesDisponibles.size());
        PageJeu pageCourante = pageEntree;

        for (int i =0; i< nbPagesIntermediaires; i++) {
            PageJeu pageSuivante = pagesDisponibles.remove(0);
            pageCourante.ajoutePage(pageSuivante); // crée l'arc 
            cheminsSurs.add(pageSuivante);
            pageCourante = pageSuivante;
        }

        //On connecte la dernière page courante à la page de sortie 
        pageCourante.ajoutePage(pageSortie);
        cheminsSurs.add(pageSortie);

        //Pour ajouter maintenant de façon hasardeuse le reste de pages
        //Séparation des pages disponibles : une partie pour le chemin piège, l'autre pour allonger le chemin sûr
        //30% piege et 70% chemin allongé.
        int nbPieges = (int) (pagesDisponibles.size()*0.30);

        //Boucle pour les pièges : 

        for (int i=0; i<nbPieges; i++){
            PageJeu pagePiege = pagesDisponibles.remove(0);
            //Choix d'un index pour désigner la source dans la liste de chemins sûrs en excluant la page sortie
            int indexSource = (int) (Math.random() * (cheminsSurs.size() - 1));
            //On sélectionne une source au hasard parmi toutes les pages du livre pour créer un arc avec une des pages restantes de pagesDisponibles
            PageJeu pageSource = cheminsSurs.get(indexSource);
            // On crée l'arc avec la page piège
            pageSource.ajoutePage(pagePiege);
            pagePiege.ajoutePage(pageEntree);
            //On n'ajoute pas cela au chemin sûr, parce que c'est une fausse piste
        }

        //Boucle pour allonger le chemin sûr 
        while (!pagesDisponibles.isEmpty()) {
            PageJeu pageAllongee = pagesDisponibles.remove(0);
            int indexSource2 = (int) (Math.random()*(cheminsSurs.size()-1));
            PageJeu pageSource2 = cheminsSurs.get(indexSource2);
            //On récupère la destination actuelle de page source dans la liste de chemins sûrs
            PageJeu pageDestination = pageSource2.getPagesSuivantes().get(0);
            //On crée l'arc entre la page source et la page allongée
            pageSource2.ajoutePage(pageAllongee);
            //Je connecte la pageAllongée à la desination "actuelle" de la page source
            pageAllongee.ajoutePage(pageDestination);

            // On met à jour notre liste de suivi pour que pageRallonge puisse 
            // elle-même accueillir d'autres pages plus tard --> systeme de prolongation de la liste. 
            cheminsSurs.add(indexSource2 + 1, pageAllongee);

        }
        //Pour avoir un lien entre la page de sortie et celle d'entrée (aucune réciprocité dans l'autre sinon le jeu serait trop facile)
        pageSortie.ajoutePage(pageEntree);
}

}