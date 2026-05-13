import java.util.ArrayList;
import java.util.List;

public class Livre{
    protected String titre;
    private List<Page> pages;
    protected int nbPages;

    public Livre(String titre, int nbPages){
        this.titre = titre;
        this.nbPages = nbPages;
        this.pages = new ArrayList<>();
        for (int i=1; i<=nbPages; i++){
            pages.add(new Page(i, "Lorem ipsum"));
        }
    }

    public String getTitre(){
        return this.titre;
    }

    public List<Page> getPages(){
        return this.pages;
    }

    public int getNbPages(){
        return this.nbPages;
    }

    public Page getPageNum(int numPage) throws IndexOutOfBoundsException{
        for (Page p : this.pages){
            if (p.getNumero() == numPage){
                return p;
            }
        }
        throw new IndexOutOfBoundsException("Page inexistante");
    }
}