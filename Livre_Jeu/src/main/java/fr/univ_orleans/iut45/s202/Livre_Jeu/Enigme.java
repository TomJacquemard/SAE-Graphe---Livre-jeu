package fr.univ_orleans.iut45.s202.Livre_Jeu;

public class Enigme {
    private String intitule;
    private double duree;
    private int difficultee;

    public Enigme(String intitule, double duree, int difficultee){
        this.intitule = intitule;
        this.duree = duree;
        this.difficultee = difficultee;
    }

    public double getDuree(){
        return this.duree;
    }

    public int getDifficultee(){
        return this.difficultee;
    }

    public String getIntitule(){
        return this.intitule;
    }
}
