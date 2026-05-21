package fr.univ_orleans.iut45.s202.Livre_Jeu;

import java.util.List;
import java.util.ArrayList;


public class LivreJeu extends Livre{
    private List<ObjetJeu> lesObjets;
    private List<ObjetJeu> objetsRecuperes;
    private List<PageJeu> pages;

    public LivreJeu(String titre, int nbPages) {
        super(titre, nbPages);
        this.lesObjets = new ArrayList<>();
        this.objetsRecuperes = new ArrayList<>();
        this.pages = new ArrayList<>();
    }

    public List<PageJeu> getListePageJeu() {
        return this.pages;
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
        if (!this.pages.contains(pageEntree)|| !this.pages.contains(pageSortie) {
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
        int nbPagesIntermediaires = 3
        PageJeu pageCourante = pageEntree;

        for (int i =0; i<=nbPagesIntermediaires; i++) {
            PageJeu pageSuivante = pagesDisponibles.remove(0);
            pageCourante.ajoutePage(pageSuivante) // crée l'arc 
            pageCourante = pageSuivante;
        }

        //On connecte la dernière page courante à la page de sortie 
        pageCourante.ajoutePage(pageSortie);

        //Pour ajouter maintenant de façon hasardeuse le reste de pages

        for (PageJeu pageAleatoire : pagesDisponibles){
            //On sélectionne une source au hasard parmi toutes les pages du livre pour créer un arc avec une des pages restantes de pagesDisponibles
            PageJeu pageSource = this.pages.get((int) (Math.random() * this.pages.size()));
            pageSource.ajoutePage(pageAleatoire);

            // Pour éviter qu'une page soit isolée, elle doit mener vers l'entrée 
            pageAleatoire.ajoutePage(pageEntree);

            //La page de sortie doit pouvoir rediriger vers l'entrée : mettre en avant le fait que si on réussit à arriver à la fin d'un livre on peut directement reprendre au début
            pageSortie.ajoutePage(pageEntree);

        }
}