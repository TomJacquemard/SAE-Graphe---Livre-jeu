package fr.univ_orleans.iut45.s202.Livre_Jeu;
import java.util.List;
import java.util.ArrayList;

public class Livre {

    protected String titre;
    protected int nbPages;
    

    public Livre(String titre, int nbPages){
        this.titre = titre;
<<<<<<< HEAD
        this.nbPages = nbPages;
<<<<<<< HEAD
        this.pages = new ArrayList<>();
        for (int i=1; i<=nbPages; i++){
            this.pages.add(new Page(i, "Lorem ipsum"));
        }

        //this.pages = new ArrayList<>();
        //for (int i=1; i<=nbPages; i++){
            //pages.add(new Page(i, "Lorem ipsum"));
        //}


        this.nbPages=nbPages;
        

    }
    
    public String getTitre(){
        return this.titre;
    }


    public int getNbPages(){
        return this.nbPages;
    }

    public Page getPageNum(int numPage) throws IndexOutOfBoundsException{
        for (Page p : this.pages){
            if (p.getNumero() == numPage){
                return p;
            }
        }
        throw new IndexOutOfBoundsException("Page inexistante");
    }

=======
>>>>>>> devLaura

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
<<<<<<< HEAD

=======
    //public Page getPageNum(int numPage) throws IndexOutOfBoundsException{
        //for (Page p : this.pages){
            //if (p.getNumero() == numPage){
                //return p;
            //}
        //}
        //throw new IndexOutOfBoundsException("Page inexistante");
    //}
>>>>>>> devLaura
=======
>>>>>>> devLaura
}