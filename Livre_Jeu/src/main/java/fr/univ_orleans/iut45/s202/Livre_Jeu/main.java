package fr.univ_orleans.iut45.s202.Livre_Jeu;

import java.util.List;
import java.util.Scanner;

public class main {

    static Scanner sc = new Scanner(System.in);
    static LivreJeu jeu = null;
    static PageJeu pageActuelle = null;

    // ─── Utilitaires ──────────────────────────────────────────────────────

    static void pause() {
        System.out.print("\n  [Entrée pour continuer]");
        sc.nextLine();
    }

    static int lireInt(String invite, int min, int max) {
        int val = -1;
        while (val < min || val > max) {
            System.out.print(invite);
            try {
                val = Integer.parseInt(sc.nextLine().trim());
                if (val < min || val > max)
                    System.out.println("  ⚠ Entier entre " + min + " et " + max + " attendu.");
            } catch (NumberFormatException e) {
                System.out.println("  ⚠ Entier entre " + min + " et " + max + " attendu.");
            }
        }
        return val;
    }

    static double lireDouble(String invite) {
        while (true) {
            System.out.print(invite);
            try { return Double.parseDouble(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("  ⚠ Nombre invalide."); }
        }
    }

    static String lireString(String invite) {
        System.out.print(invite);
        return sc.nextLine().trim();
    }

    static void separateur() {
        System.out.println("  ──────────────────────────────────────────────────");
    }

    static void titre(String t) {
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.printf( "║  %-46s║%n", t);
        System.out.println("╚════════════════════════════════════════════════╝\n");
    }

    // Vérifie qu'un jeu est chargé, sinon lève une exception claire
    static void checkJeu() {
        if (jeu == null)
            throw new IllegalStateException("Aucun livre-jeu chargé. Créez-en un d'abord (option 1).");
    }

    // ═════════════════════════════════════════════════════════════════════
    //  MENU PRINCIPAL
    // ═════════════════════════════════════════════════════════════════════

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n╔════════════════════════════════════════════════╗");
            System.out.println("║        📖  GESTIONNAIRE DE LIVRE-JEU           ║");
            System.out.println("╚════════════════════════════════════════════════╝");

            if (jeu != null)
                System.out.println("  Livre actif : \"" + jeu.getTitre() + "\""
                        + " | " + jeu.getPagesJeu().size() + " pages"
                        + " | " + jeu.getListeObjets().size() + " objet(s)\n");
            else
                System.out.println("  Aucun livre-jeu chargé.\n");

            System.out.println("  1. Créer un livre-jeu");
            System.out.println("  2. Naviguer dans le livre-jeu");
            System.out.println("  3. Inspecter / modifier une page");
            System.out.println("  4. Gérer les objets");
            System.out.println("  5. Algorithmes de résolution");
            System.out.println("  6. Exporter le rapport PDF");
            System.out.println("  7. Afficher la structure complète");
            System.out.println("  0. Quitter");

            int choix = lireInt("\n  Votre choix : ", 0, 7);
            try {
                switch (choix) {
            case 1:
                menuCreation();
                break;

            case 2:
                menuNavigation();
                break;

            case 3:
                menuPages();
                break;

            case 4:
                menuObjets();
                break;

            case 5:
                menuAlgorithmes();
                break;

            case 6:
                menuExport();
                break;

            case 7:
                menuStructure();
                break;

            case 0:
                System.out.println("\n  Au revoir ! 👋\n");
                return;
            }
            } catch (IllegalStateException e) {
                System.out.println("\n  ⚠  " + e.getMessage());
                pause();
            }
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    //  1. CRÉATION
    // ═════════════════════════════════════════════════════════════════════

    static void menuCreation() {
        titre("Créer un livre-jeu");

        String titreJeu = lireString("  Titre du livre-jeu : ");
        int nbPages     = lireInt   ("  Nombre de pages    : ", 2, 9999);
        int nbObjets    = lireInt   ("  Nombre d'objets    : ", 0, nbPages - 1);

        System.out.println("\n  Générateurs disponibles :");
        System.out.println("    1. genererLivreJeu_1  (aléatoire avec boucles de retour)");
        System.out.println("    2. genererLivreJeu2   (chemin sûr + bifurcations)");
        int choixGen = lireInt("  Votre choix : ", 1, 2);
        String generateur = (choixGen == 1) ? "genererLivreJeu_1" : "genererLivreJeu2";

        System.out.println("\n  Génération en cours...");
        jeu = new LivreJeu(titreJeu, nbPages, generateur, nbObjets);
        pageActuelle = jeu.getPagesJeu().get(0); // page d'entrée = index 0

        System.out.println("  ✅ \"" + titreJeu + "\" créé avec le générateur [" + generateur + "].");
        System.out.println("  " + jeu.getPagesJeu().size() + " pages générées, "
                + jeu.getListeObjets().size() + " objet(s) placé(s).");
        pause();
    }

    // ═════════════════════════════════════════════════════════════════════
    //  2. NAVIGATION
    // ═════════════════════════════════════════════════════════════════════

    static void menuNavigation() {
        checkJeu();
        if (pageActuelle == null) pageActuelle = jeu.getPagesJeu().get(0);

        while (true) {
            titre("Navigation");
            afficherPageCourante();

            separateur();
            System.out.println("  1. Choisir une page suivante");
            System.out.println("  2. Aller directement à une page (numéro)");
            System.out.println("  3. Revenir à la page d'entrée (P1)");
            System.out.println("  4. Récupérer l'objet de cette page");
            System.out.println("  5. Voir l'inventaire");
            System.out.println("  0. Retour au menu principal");

            int choix = lireInt("  Choix : ", 0, 5);
                switch (choix) {
            case 1:
                choisirPageSuivante();
                break;

            case 2:
                allerAumeNumero();
                break;

            case 3:
                pageActuelle = jeu.getPagesJeu().get(0);
                System.out.println("  ↩  Retour à la page d'entrée (P" + pageActuelle.getNumero() + ").");
                pause();
                break;

            case 4:
                recupererObjetSurPage(pageActuelle);
                break;

            case 5:
                afficherInventaire();
                break;

            case 0:
                return;
        }
        }
    }

    static void afficherPageCourante() {
        System.out.println("  ── Page n°" + pageActuelle.getNumero()
                + (pageActuelle.getNumero() == 1 ? " [ENTRÉE]" : "")
                + (pageActuelle.estSortie() ? " [SORTIE ★]" : "") + " ──");

        // lire() de Page affiche ET retourne le contenu
        pageActuelle.lire();

        // Objet présent ?
        if (pageActuelle.contientObjet()) {
            ObjetJeu obj = pageActuelle.getObjet();
            if (!obj.estRecupere())
                System.out.println("  📦 Objet disponible : " + obj.getNom());
            else
                System.out.println("  📦 Objet déjà récupéré : " + obj.getNom());
        }

        // Pages suivantes
        List<PageJeu> suivantes = pageActuelle.getPagesSuivantes();
        List<Enigme>  enigmes   = pageActuelle.getEnigmes();
        if (suivantes.isEmpty()) {
            System.out.println("  ➡  Aucune suite (cul-de-sac).");
        } else {
            System.out.println("  ➡  Départs possibles (" + suivantes.size() + ") :");
            for (int i = 0; i < suivantes.size(); i++) {
                PageJeu s = suivantes.get(i);
                Enigme  e = enigmes.get(i);
                System.out.printf("      %d. P%-4d  énigme : \"%s\"  durée : %.0fs%s%n",
                        i + 1, s.getNumero(), e.getIntitule(), e.getDuree(),
                        s.estSortie() ? " [SORTIE]" : "");
            }
        }
    }

    static void choisirPageSuivante() {
        List<PageJeu> suivantes = pageActuelle.getPagesSuivantes();
        if (suivantes.isEmpty()) {
            System.out.println("  ⚠  Aucune page suivante depuis cette page.");
            pause(); return;
        }
        int idx = lireInt("  Numéro du choix (0 = annuler) : ", 0, suivantes.size());
        if (idx == 0) return;

        pageActuelle = suivantes.get(idx - 1);
        System.out.println("  → Vous êtes maintenant sur la page " + pageActuelle.getNumero() + ".");
        if (pageActuelle.estSortie())
            System.out.println("  🎉 Vous avez atteint la SORTIE ! Partie terminée.");
        pause();
    }

    static void allerAumeNumero() {
        int num = lireInt("  Numéro de page cible : ", 1, 9999);
        try {
            pageActuelle = jeu.getPageNumero(num); // lève IndexOutOfBoundsException si absent
            System.out.println("  → Téléportation vers P" + num + ".");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("  ⚠  " + e.getMessage());
        }
        pause();
    }

    static void recupererObjetSurPage(PageJeu page) {
        if (!page.contientObjet()) {
            System.out.println("  ℹ  Aucun objet sur cette page.");
        } else {
            ObjetJeu obj = page.getObjet();
            if (obj.estRecupere()) {
                System.out.println("  ℹ  \"" + obj.getNom() + "\" a déjà été récupéré.");
            } else {
                jeu.recupererObjet(obj); // marque estRecupere + ajoute à objetsRecuperes
                System.out.println("  ✅  \"" + obj.getNom() + "\" ajouté à l'inventaire !");
            }
        }
        pause();
    }

    static void afficherInventaire() {
        titre("Inventaire du joueur");
        List<ObjetJeu> inv = jeu.getObjetsRecuperes();
        if (inv.isEmpty()) {
            System.out.println("  Inventaire vide.");
        } else {
            for (ObjetJeu o : inv)
                System.out.println("  ✔  " + o.getNom());
        }
        System.out.println("\n  Total : " + inv.size() + " / " + jeu.getListeObjets().size() + " objet(s)");
        pause();
    }

    // ═════════════════════════════════════════════════════════════════════
    //  3. INSPECTER / MODIFIER UNE PAGE
    // ═════════════════════════════════════════════════════════════════════

    static void menuPages() {
        checkJeu();
        while (true) {
            titre("Inspecter / Modifier une page");
            System.out.println("  1. Lister toutes les pages (résumé)");
            System.out.println("  2. Afficher le détail d'une page");
            System.out.println("  3. Modifier le contenu d'une page");
            System.out.println("  4. Lister les énigmes d'une page");
            System.out.println("  0. Retour");

            int choix = lireInt("  Choix : ", 0, 4);
            switch (choix) {
                case 1 -> listerPages();
                case 2 -> detailPage();
                case 3 -> modifierContenuPage();
                case 4 -> listerEnigmesPage();
                case 0 -> { return; }
            }
        }
    }

    static void listerPages() {
        titre("Liste des pages");
        for (PageJeu p : jeu.getPagesJeu()) {
            String tags = "";
            if (p.getNumero() == 1)  tags += " [ENTRÉE]";
            if (p.estSortie())        tags += " [SORTIE]";
            if (p.contientObjet())    tags += " [📦 " + p.getObjet().getNom() + "]";

            StringBuilder suiv = new StringBuilder();
            for (PageJeu s : p.getPagesSuivantes()) suiv.append("P").append(s.getNumero()).append(" ");

            System.out.printf("  P%-4d %-22s → %s%n",
                    p.getNumero(), tags, suiv.length() == 0 ? "(fin)" : suiv.toString().trim());
        }
        pause();
    }

    static void detailPage() {
        int num = lireInt("  Numéro de la page : ", 1, 9999);
        try {
            PageJeu p = jeu.getPageNumero(num);
            System.out.println();
            // On sauvegarde pageActuelle temporairement pour réutiliser afficherPageCourante
            PageJeu sauvegarde = pageActuelle;
            pageActuelle = p;
            afficherPageCourante();
            pageActuelle = sauvegarde;
        } catch (IndexOutOfBoundsException e) {
            System.out.println("  ⚠  " + e.getMessage());
        }
        pause();
    }

    static void modifierContenuPage() {
        int num = lireInt("  Numéro de la page à modifier : ", 1, 9999);
        try {
            PageJeu p = jeu.getPageNumero(num);
            System.out.println("  Contenu actuel : " + p.lire());
            String nouveau = lireString("  Nouveau contenu : ");
            p.modifierContenu(nouveau);  // méthode de Page
            System.out.println("  ✅  Contenu mis à jour.");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("  ⚠  " + e.getMessage());
        }
        pause();
    }

    static void listerEnigmesPage() {
        int num = lireInt("  Numéro de la page : ", 1, 9999);
        try {
            PageJeu p = jeu.getPageNumero(num);
            titre("Énigmes — Page " + num);
            List<Enigme>  enigmes  = p.getEnigmes();
            List<PageJeu> suivantes = p.getPagesSuivantes();
            if (enigmes.isEmpty()) {
                System.out.println("  Aucune énigme (page sans sortie).");
            } else {
                for (int i = 0; i < enigmes.size(); i++) {
                    Enigme e = enigmes.get(i);
                    System.out.printf("  %d. Vers P%-4d | \"%s\"  (%.0f s)%n",
                            i + 1, suivantes.get(i).getNumero(), e.getIntitule(), e.getDuree());
                }
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("  ⚠  " + e.getMessage());
        }
        pause();
    }

    // ═════════════════════════════════════════════════════════════════════
    //  4. OBJETS
    // ═════════════════════════════════════════════════════════════════════

    static void menuObjets() {
        checkJeu();
        while (true) {
            titre("Gérer les objets");
            System.out.println("  1. Lister tous les objets et leur emplacement");
            System.out.println("  2. Voir l'inventaire du joueur");
            System.out.println("  3. Récupérer l'objet d'une page donnée");
            System.out.println("  0. Retour");

            int choix = lireInt("  Choix : ", 0, 3);
            switch (choix) {
                case 1 -> listerTousObjets();
                case 2 -> afficherInventaire();
                case 3 -> {
                    int num = lireInt("  Numéro de la page : ", 1, 9999);
                    try { recupererObjetSurPage(jeu.getPageNumero(num)); }
                    catch (IndexOutOfBoundsException e) { System.out.println("  ⚠  " + e.getMessage()); pause(); }
                }
                case 0 -> { return; }
            }
        }
    }

    static void listerTousObjets() {
        titre("Tous les objets du jeu");
        List<ObjetJeu> tous = jeu.getListeObjets();
        if (tous.isEmpty()) { System.out.println("  Aucun objet dans ce livre-jeu."); pause(); return; }

        for (ObjetJeu obj : tous) {
            String statut = obj.estRecupere() ? "✔ récupéré" : "○ disponible";
            // Retrouver la page qui contient cet objet
            String page = "?";
            for (PageJeu p : jeu.getPagesJeu()) {
                if (p.contientObjet() && p.getObjet() == obj) { page = "P" + p.getNumero(); break; }
            }
            System.out.printf("  %-25s  page : %-6s  état : %s%n", obj.getNom(), page, statut);
        }
        pause();
    }

    // ═════════════════════════════════════════════════════════════════════
    //  5. ALGORITHMES
    // ═════════════════════════════════════════════════════════════════════

    static void menuAlgorithmes() {
        checkJeu();
        while (true) {
            titre("Algorithmes de résolution");
            System.out.println("  1. Glouton standard        (borne max sur la durée, vise la sortie)");
            System.out.println("  2. Glouton correct         (collecte tous les objets puis sort)");
            System.out.println("  3. Dijkstra séquentiel     (chemin optimal, objets puis sortie)");
            System.out.println("  4. Combinatoire complet    (backtracking exhaustif)");
            System.out.println("  0. Retour");

            int choix = lireInt("  Choix : ", 0, 4);
            if (choix == 0) return;

            List<PageJeu> chemin = null;

            switch (choix) {
                case 1 -> {
                    double borne = lireDouble("  Durée max par énigme (borne) : ");
                    chemin = jeu.algorithmeGloutonSortieSeule(borne);
                }
                case 2 -> {
                    double borne = lireDouble("  Durée max par énigme (borne) : ");
                    chemin = jeu.algorithmeGloutonCorrect(borne);
                }
                case 3 -> chemin = jeu.algorithmeDijkstraCorrect();
                case 4 -> {
                    System.out.println("  ⏳ Calcul en cours (peut être long sur de grands graphes)...");
                    chemin = jeu.algorithmeCombinatoireComplet();
                }
            }

            afficherResultatChemin(chemin);
        }
    }

    static void afficherResultatChemin(List<PageJeu> chemin) {
        separateur();
        if (chemin == null || chemin.isEmpty()) {
            System.out.println("  Aucune solution trouvée.");
            pause(); return;
        }

        // Construction affichage chemin + calcul temps cumulé
        StringBuilder sb = new StringBuilder();
        double tempsCumule = 0.0;
        int objetsCollectes = 0;

        for (int i = 0; i < chemin.size(); i++) {
            PageJeu curr = chemin.get(i);
            sb.append("P").append(curr.getNumero());
            if (i < chemin.size() - 1) {
                sb.append(" → ");
                // Trouver la durée de l'arc curr → curr+1
                PageJeu next = chemin.get(i + 1);
                List<PageJeu> voisines = curr.getPagesSuivantes();
                List<Enigme>  enigmes  = curr.getEnigmes();
                for (int j = 0; j < voisines.size(); j++) {
                    if (voisines.get(j).getNumero() == next.getNumero()) {
                        tempsCumule += enigmes.get(j).getDuree();
                        break;
                    }
                }
            }
            if (curr.contientObjet()) objetsCollectes++;
        }

        PageJeu derniere = chemin.get(chemin.size() - 1);

        System.out.println("  Chemin (" + chemin.size() + " pages, " + (chemin.size()-1) + " arcs) :");
        // Affichage par blocs de 10 pour lisibilité
        String chaine = sb.toString();
        int bloc = 80;
        for (int i = 0; i < chaine.length(); i += bloc)
            System.out.println("  " + chaine.substring(i, Math.min(i + bloc, chaine.length())));

        System.out.printf("%n  Temps cumulé    : %.0f s%n", tempsCumule);
        System.out.println("  Objets collectés : " + objetsCollectes + " / " + jeu.getListeObjets().size());
        System.out.println("  Sortie atteinte  : " + (derniere.estSortie() ? "✅ Oui" : "❌ Non"));
        pause();
    }

    // ═════════════════════════════════════════════════════════════════════
    //  6. EXPORT PDF
    // ═════════════════════════════════════════════════════════════════════

    static void menuExport() {
        checkJeu();
        titre("Export PDF");
        System.out.println("  Génération de Rapport_LivreJeu.pdf en cours...");
        System.out.println("  (Graphe + tableau comparatif des 4 algorithmes)\n");
        jeu.exporterEnPDF(); // votre méthode existante
        System.out.println("\n  ✅  Fichier généré dans le répertoire courant.");
        pause();
    }

    // ═════════════════════════════════════════════════════════════════════
    //  7. STRUCTURE COMPLÈTE (toString de LivreJeu)
    // ═════════════════════════════════════════════════════════════════════

    static void menuStructure() {
        checkJeu();
        titre("Structure complète du livre-jeu");
        System.out.println(jeu.toString()); // votre toString existant
        pause();
    }
}
