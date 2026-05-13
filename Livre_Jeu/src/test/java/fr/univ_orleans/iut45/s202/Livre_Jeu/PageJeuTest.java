package fr.univ_orleans.iut45.s202.Livre_Jeu;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class PageJeuTest {

    String contenu = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    PageJeu pageJeu1 = new PageJeu(1, contenu, false, null); //pages sans objet, n'est pas la sortie
    PageJeu pageJeu2 = new PageJeu(2, contenu, false, null);
    PageJeu pageJeu3 = new PageJeu(3, contenu, false, null);

    PageJeu pageJeuSortie =  new PageJeu(3, contenu, true, null);//page sortie

    ObjetJeu objet = new ObjetJeu("A");
    PageJeu pageJeuObjet = new PageJeu(1, contenu, false, objet); //page avec objet, n'est pas la sortie

    Enigme enigme1 = new Enigme("Enigme 1", 2,2);// ENLEVER DIFFICULTE DANS ENIGME CAR duree=difficulte
    Enigme enigme2 = new Enigme("Enigme 2", 4, 4);


    @Test
    public void testAjoutePage(){ //teste aussi le guetteur getPagesSuivantes
        pageJeu1.ajoutePage(pageJeu2);
        pageJeu1.ajoutePage(pageJeu3);

        List<Page> listePages = new ArrayList<>();
        listePages.add(pageJeu2);
        listePages.add(pageJeu3);

        assertEquals(pageJeu1.getPagesSuivantes(),listePages);
    }
    
    @Test
    public void testAjouteEnigme(){ //teste aussi le getteur getEnigmes
        pageJeu1.ajouteEnigme(enigme1);
        pageJeu1.ajouteEnigme(enigme2);

        List<Enigme> listeEnigmes = new ArrayList<>();
        listeEnigmes.add(enigme1);
        listeEnigmes.add(enigme2);

        assertEquals(pageJeu1.getEnigmes(),listeEnigmes);
    }


    @Test
    public void testEstSortie(){
        assertEquals(pageJeuSortie.estSortie(), true);
    }

    @Test
    public void testContientObjet(){
        assertEquals(pageJeuObjet.contientObjet(), true);
    }

    @Test
    public void testGetObjet(){
        assertEquals(pageJeuObjet.getObjet(), objet);
    }

    
}
