package fr.univ_orleans.iut45.s202.Livre_Jeu;

public class Enigme {
    private String intitule;
    private double duree;

    public Enigme(String intitule, double duree){
        this.intitule = intitule;
        this.duree = duree;
    }


    public double getDuree(){
        return this.duree;
    }


    public String getIntitule(){
        return this.intitule;
    }
}
