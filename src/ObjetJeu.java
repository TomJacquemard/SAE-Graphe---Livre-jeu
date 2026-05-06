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

    public void recuperer(){
        this.recupere = true;
    }
}