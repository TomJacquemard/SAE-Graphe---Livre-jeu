package fr.univ_orleans.iut45.s202.Livre_Jeu;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class LivreTest {
    private String titre = "Livre 1";
    private int nbPages = 10;
    private List<Page> listePage;
    private Livre l = new Livre(this.titre, this.nbPages);

    public LivreTest(){
        this.listePage = new ArrayList<>();
        for (int i=1; i<=nbPages; i++){
            listePage.add(new Page(i, "Lorem ipsum"));
        }
    }

    @Test
    public void testGetTitre(){
        assertEquals(this.titre, this.l.getTitre());
    }

    @Test
    public void testGetPages(){
        for (int i=1; i<=nbPages; i++){
            this.l.getPageNum(i).modifierContenu("Lorem ipsum");
        }
        assertEquals(this.listePage, this.l.getPages());
    }

    @Test
    public void testGetNbPages(){
        assertEquals(this.nbPages, l.getNbPages());
    }

    @Test
    public void testGetPageNum(){
        System.out.println(this.listePage.get(0));
        System.out.println(this.l.getPageNum(1));
        try {
            assertEquals(this.listePage.get(0), this.l.getPageNum(1));
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
    }
}
