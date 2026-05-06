import java.util.List;
import java.util.ArrayList;

public class PageJeu extends Livre{
    private boolean sortie;
    private List<PageJeu> pageSuivantes;
    private ObjetJeu objet;
    private List<Enigme> enigmes;

    public PageJeu(boolean sortie, ObjetJeu objet){
        this.sortie = sortie;
        this.objet = objet;
    }

    public List<Enigme> getEnigmes(){
        return enigmes;
    }

    public ObjetJeu getObjet(){
        return objet;
    }

    public boolean estSortie(){
        return sortie;
    }

    public boolean contientObjet(){
        if (this.objet != null){
            return true;
        }
        else{
            return false;
        }
    }
}