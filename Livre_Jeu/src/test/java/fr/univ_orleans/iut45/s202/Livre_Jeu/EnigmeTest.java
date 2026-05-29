package fr.univ_orleans.iut45.s202.Livre_Jeu;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EnigmeTest {
    private String intitule = "Enigme 1";
    private int difficultee = 3;
    private double duree = 0.5;
    private Enigme e = new Enigme(this.intitule, this.duree, this.difficultee);

    @Test
    public void testGetIntitule(){
        assertEquals(this.intitule, e.getIntitule());
    }

    @Test
    public void testGetDuree(){
        assertEquals(this.duree, e.getDuree(),0.0001); // sans le troisième argument le assertEquals(double,double) était deprecated"
    }
}
