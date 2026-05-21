package fr.univ_orleans.iut45.s202.Livre_Jeu;

import java.util.List;
import java.util.Random;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class LivreJeu extends Livre{
    private List<ObjetJeu> lesObjets;
    private List<ObjetJeu> objetsRecuperes;
    private List<PageJeu> lesPagesDuJeu;
    private Graph<PageJeu, DefaultWeightedEdge> graph;

    public LivreJeu(String titre, int nbPages) {
        super(titre, nbPages);
        this.lesObjets = new ArrayList<>();
        this.objetsRecuperes = new ArrayList<>();
        this.lesPagesDuJeu = new ArrayList<>();
        this.graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
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
                pageEnPlacement.ajouteEnigme(new Enigme("Lorem Ipsum", dureeEngime, dureeEngime)); //création et ajout de l'énigme en parallèle de l'ajout de la page
            }
            pagesValides.add(pageEnPlacement);
            //A la fin de la boucle while, toutes les pages seront placées, sauf la page d'entrée
        }

        List<PageJeu> pagesIsolees = this.pagesSansSource();
        if (pagesIsolees.size()>0){ //si il y des pages isolées...
            for (PageJeu pIsolee : pagesIsolees){ //on relie la page d'entrée à ces pages
                pageEntree.ajoutePage(pIsolee);
                int dureeEngime = 1+random.nextInt(20); //Choix aléatoire de la durée de l'Enigme 
                pageEntree.ajouteEnigme(new Enigme("Lorem Ipsum", dureeEngime, dureeEngime)); //création et ajout de l'énigme
            }
        }
        else{ //sinon je relie la page d'entrée à quelques pages
            int nbPagesChoisies = 1 + random.nextInt(4); //4 pages maximum
            List<PageJeu> pagesChoisies = pagesValides.subList(0, nbPagesChoisies); 
            for(PageJeu p : pagesChoisies){
                pageEntree.ajoutePage(p); 
                int dureeEngime = 1+random.nextInt(20); 
                pageEntree.ajouteEnigme(new Enigme("Lorem Ipsum", dureeEngime, dureeEngime)); //création et ajout de l'énigme en parallèle de l'ajout de la page
            }
        }

        //Enfin on rajoute quelques pages menant vers le début
        int nbPagesChoisies = 1 + random.nextInt(4); 
        List<PageJeu> pagesChoisies = pagesValides.subList(0, nbPagesChoisies); 
        for(PageJeu p : pagesChoisies){
            p.ajoutePage(pageEntree); 
            int dureeEngime = 1+random.nextInt(20); 
            p.ajouteEnigme(new Enigme("Lorem Ipsum", dureeEngime, dureeEngime));
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
                
                this.graph.setEdgeWeight(nouvelleEdge,enigmes.get(i).getDuree()); //on attribut le poids a la nouvelle arrete
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

    public List<PageJeu> getPagesJeu(){ //obligée de recréer plusieurs méthodes de livre en rapport avec les pages car je veux obtenir des PageJeu et avec les methodes de Livre j'obtenais des Pages
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
}