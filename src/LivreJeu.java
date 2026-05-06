import java.util.List;
import java.util.ArrayList;


public class LivreJeu extends Livre{
    private List<ObjetJeu> lesObjets;
    private List<ObjetJeu> objetsRecuperes;

    public LivreJeu(List<ObjetJeu> lesObjets, List<ObjetJeu> objetRecuperes) {
        this.lesObjets = new ArrayList<>();
        this.objetsRecuperes = new ArrayList<>();
    }

    public List<ObjetJeu> getListeObjets() {
        return this.lesObjets;
    }

    public List<ObjetJeu> getObjetsRecuperes() {
        for (ObjetJeu o : lesObjets) {
            if (o.estRecupere) {
                objetsRecuperes.add(o);
            }
        }
        return objetsRecuperes;
    }

     // AJoute l'objet dans la liste des objets récupérés. 

    public void recupererObjet(ObjetJeu objet) { 
        if (objet.recupererObjet()) {
            objetsRecuperes.add(objet);
        }
    }
}