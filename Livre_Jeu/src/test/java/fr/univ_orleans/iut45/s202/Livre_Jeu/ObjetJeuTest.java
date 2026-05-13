package fr.univ_orleans.iut45.s202.Livre_Jeu;

import static org.junit.Assert.assertEquals;
import org.junit.Test;


public class ObjetJeuTest {
    ObjetJeu objet = new ObjetJeu("Ballon",false);

    @Test
    public void testGetNom() {
        assertEquals("Ballon",objet.getNom());
    }

    @Test 
    public void testEstRecupere() {
        assertEquals(false, objet.estRecupere());
    }

    @Test 
    public void testRecupereObjet() {
        assertEquals(true, objet.recupererObjet());
    }


}
