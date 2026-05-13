package fr.univ_orleans.iut45.s202.Livre_Jeu;

public class Page{
    protected int numero;
    protected String contenu;

    public Page(int numero, String contenu){
        this.numero = numero;
        this.contenu = contenu;
    }

    public int getNumero(){
        return this.numero;
    }

    public void lire(){
        System.out.println(this.contenu);
    }

    @Override
    public boolean equals(Object p){
        if (p == null){return false;}
        if (this == p){return true;}
        if (!(p instanceof Page)){return false;}
        Page tmp = (Page) p;
        return this.numero == tmp.numero && this.contenu.equals(tmp.contenu);
    }
}