package fr.univ_orleans.iut45.s202.Livre_Jeu;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class TestLivreJeu {

    LivreJeu lj;
    ObjetJeu objet;
    PageJeu page;

    @Before
    public void init() {

        lj = new LivreJeu("titre", 3);

        objet = new ObjetJeu("clé");
        page = new PageJeu(0, null, false, objet);

        lj.getListeObjets().add(objet);
        lj.getListePageJeu().add(page);
    }

    @Test
    public void getListObjects() {

        assertEquals(1, lj.getListeObjets().size());
        assertTrue(lj.getListeObjets().contains(objet));
    }

    @Test
    public void getObjetsRecuperes() {

        assertEquals(0, lj.getObjetsRecuperes().size());
    }

    @Test
    public void recupererObjet() {

        lj.recupererObjet(objet);
        assertTrue(lj.getObjetsRecuperes().contains(objet));
    }

    @Test
    public void getListePageJeu() {

        assertEquals(1, lj.getListePageJeu().size());
        assertTrue(lj.getListePageJeu().contains(page));
    }
}
