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
        assertEquals(this.listePage, this.l.getPages());
    }

    @Test
    public void testGetNbPages(){
        assertEquals(this.nbPages, l.getNbPages());
    }

    @Test
    public void testGetPageNum(){
        try {
            assertEquals(this.listePage.get(1), this.l.getPageNum(1));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
