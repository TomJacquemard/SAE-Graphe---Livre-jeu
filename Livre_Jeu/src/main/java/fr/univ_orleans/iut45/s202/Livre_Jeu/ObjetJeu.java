package fr.univ_orleans.iut45.s202.Livre_Jeu;

public class ObjetJeu{
    private String nom;
    private boolean recupere = false;

    public ObjetJeu(String nom){
        this.nom = nom;
    }

    public String getNom(){
        return this.nom;
    }

    public boolean estRecupere(){
        return this.recupere;
    }

    public void recupererObjet(){
        this.recupere = true;
    }
}