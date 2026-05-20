package fr.univ_orleans.iut45.s202.Livre_Jeu;

import java.util.*;
import static org.junit.Assert.assertEquals;
import org.junit.Test;



public class TestLivreJeu {
    List<ObjetJeu> lesObjets = new ArrayList<>();
    List<ObjetJeu> objetsRecuperes = new ArrayList<>();
    List<PageJeu> pages = new ArrayList<>();
   
    
    LivreJeu lj = new LivreJeu("titre", 3);

    @Test
    public void getListObjects(){
        assertEquals(lesObjets, lj.getListeObjets());
    }

    @Test
    public void getObjetsRecuperes(){
        assertEquals(objetsRecuperes, lj.getObjetsRecuperes());
    }

    @Test 
    public void recupererObjet(ObjetJeu object){
        assertEquals(objetsRecuperes, object.estRecupere());
    }

    @Test
    public void getListePageJeu(){
        assertEquals(pages, lj.getListePageJeu());
    }
    }
