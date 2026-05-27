package fr.univ_orleans.iut45.s202.Livre_Jeu;

import java.util.List;
import java.util.ArrayList;

public class Livre {

    protected String titre;
    protected int nbPages;
    

    public Livre(String titre, int nbPages){
        this.titre = titre;
        
    }
    
    public String getTitre(){
        return this.titre;
    }

    public int getNbPages(){
        return this.nbPages;
    }


    @Override
    public boolean equals(Object l){
        if (l == null){return false;}
        if (this == l){return true;}
        if (!(l instanceof Livre)){return false;} // CORRIGÉ : Livre au lieu de Page
        Livre tmp = (Livre) l;
        return this.titre.equals(tmp.titre) && this.nbPages == tmp.nbPages;
    }

    @Override
    public String toString(){
        return "Le livre '" + this.titre + "' avec " + this.nbPages + " pages.";
    }
}