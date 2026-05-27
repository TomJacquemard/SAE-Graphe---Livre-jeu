package fr.univ_orleans.iut45.s202.Livre_Jeu;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class LivreTest {
    private String titre = "Livre 1";
    private int nbPages = 10;
    private Livre l = new Livre(this.titre, this.nbPages);

    @Test
    public void testGetTitre(){
        assertEquals(this.titre, this.l.getTitre());
    }


    @Test
    public void testGetNbPages(){
        assertEquals(this.nbPages, l.getNbPages());
    }

}
