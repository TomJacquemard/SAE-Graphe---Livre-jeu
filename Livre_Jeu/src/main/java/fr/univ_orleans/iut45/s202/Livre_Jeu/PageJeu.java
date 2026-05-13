package fr.univ_orleans.iut45.s202.Livre_Jeu;

import java.util.List;
import java.util.ArrayList;

public class PageJeu extends Page{
    private boolean sortie;
    private List<PageJeu> pageSuivantes;
    private ObjetJeu objet;
    private List<Enigme> enigmes;

    public PageJeu(int numero, String contenu, boolean sortie, ObjetJeu objet){
        super(numero, contenu);
        this.sortie = sortie;
        this.objet = objet;
        this.pageSuivantes = new ArrayList<>();
        this.enigmes = new ArrayList<>();
    }

    public boolean estSortie(){
        return sortie;
    }
    
    public List<PageJeu> getPagesSuivantes(){
        return this.pageSuivantes;
    }

    public void ajoutePage(PageJeu page){
        this.pageSuivantes.add(page);
    }

    public ObjetJeu getObjet(){
        return objet;
    }

    public boolean contientObjet(){
        if (this.objet != null){
            return true;
        }
        else{
            return false;
        }
    }
    
    public List<Enigme> getEnigmes(){
        return this.enigmes;
    }

    public void ajouteEnigme(Enigme enigme){
        this.enigmes.add(enigme);
    }
}