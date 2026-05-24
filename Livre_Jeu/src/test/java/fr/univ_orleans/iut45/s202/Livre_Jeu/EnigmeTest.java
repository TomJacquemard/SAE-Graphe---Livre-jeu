package fr.univ_orleans.iut45.s202.Livre_Jeu;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EnigmeTest {
    private String intitule = "Enigme 1";
    private int difficultee = 3;
    private Enigme e = new Enigme(this.intitule, this.difficultee);

    @Test
    public void testGetIntitule(){
        assertEquals(this.intitule, e.getIntitule());
    }

    @Test
    public void testGetDifficultee(){
        assertEquals(this.difficultee, e.getDifficultee());
    }
}
