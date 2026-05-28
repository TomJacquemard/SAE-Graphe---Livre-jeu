package fr.univ_orleans.iut45.s202.Livre_Jeu;

import java.util.List;
import java.util.ArrayList;

public class PageJeu extends Page {

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

        //mettre algorithme de création ici pour remplir la liste des pageSuivantes et des enigmes
    
    }

    public List<Enigme> getEnigmes(){
        return this.enigmes;
    }

    public ObjetJeu getObjet(){
        return this.objet;

    }

    public boolean estSortie(){
        return this.sortie;
    }
    
    public List<PageJeu> getPagesSuivantes(){
        return this.pageSuivantes;
    }

    public void ajoutePage(PageJeu page){
        this.pageSuivantes.add(page);
    }

    public boolean contientObjet(){
        if (this.objet != null){
            return true;
        }
        else{
            return false;
        }
    }

    public void ajouteEnigme(Enigme enigme){
        this.enigmes.add(enigme);
    }

}