package fr.univ_orleans.iut45.s202.Livre_Jeu;

import java.util.List;
import java.util.ArrayList;


public class LivreJeu extends Livre{
    private List<ObjetJeu> lesObjets;
    private List<ObjetJeu> objetsRecuperes;

    public LivreJeu(String titre, int nbPages) {
        super(titre, nbPages);
        this.lesObjets = new ArrayList<>();
        this.objetsRecuperes = new ArrayList<>();
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