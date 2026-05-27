package fr.univ_orleans.iut45.s202.Livre_Jeu;

import java.util.List;
import java.util.ArrayList;

public class PageJeu extends Page {

    private boolean sortie;
    // Liste des pages directement accessibles depuis cette page
    private List<PageJeu> pageSuivantes;
    private ObjetJeu objet;
    // Liste des énigmes associées à chaque transition (même index que pageSuivantes)
    private List<Enigme> enigmes;

    public PageJeu(int numero, String contenu, boolean sortie, ObjetJeu objet){
        super(numero, contenu);
        this.sortie = sortie;
        this.objet = objet;
        this.pageSuivantes = new ArrayList<>();
        this.enigmes = new ArrayList<>();
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

    public void setObjet(ObjetJeu ob) {
        this.objet = ob;
    }
    
    public List<PageJeu> getPagesSuivantes(){
        return this.pageSuivantes;
    }

    // Ajoute une page cible à la liste des destinations possibles
    public void ajoutePage(PageJeu page){
        this.pageSuivantes.add(page);
    }

    public boolean contientObjet(){
        return this.objet != null;
    }

    // Ajoute l'énigme requise pour valider la transition
    public void ajouteEnigme(Enigme enigme){
        this.enigmes.add(enigme);
    }
}