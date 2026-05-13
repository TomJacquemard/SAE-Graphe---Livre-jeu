package fr.univ_orleans.iut45.s202.Livre_Jeu;

public class Enigme {
    private String intitule;
    private int difficultee;

    public Enigme(String intitule, int difficultee){
        this.intitule = intitule;
        this.difficultee = difficultee;
    }

    public int getDifficultee(){
        return this.difficultee;
    }

    public String getIntitule(){
        return this.intitule;
    }
}
