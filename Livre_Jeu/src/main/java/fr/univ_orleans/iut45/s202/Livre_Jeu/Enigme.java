package fr.univ_orleans.iut45.s202.Livre_Jeu;

public class Enigme {
    private String intitule;
    private double duree;
    private int difficulte;

    public Enigme(String intitule, double duree, int difficulte){
        this.intitule = intitule;
        this.duree = duree;
        this.difficulte = difficulte;
    }

    public Enigme(String intitule, double duree){
        this.intitule = intitule;
        this.duree = duree;
        this.difficulte = 0;
    }


    public double getDuree(){
        return this.duree;
    }


    public String getIntitule(){
        return this.intitule;
    }
}
