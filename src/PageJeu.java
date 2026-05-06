import java.util.List;
import java.util.ArrayList;

public class PageJeu extends Livre{
    private boolean sortie;
    private List<PageJeu> pageSuivantes;

    public PageJeu(boolean sortie, ObjetJeu objet){
        this.sortie = sortie;
        
    }

}