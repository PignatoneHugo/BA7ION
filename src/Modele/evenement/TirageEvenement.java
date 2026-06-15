package Modele.evenement;

import java.util.Random;

/** Tire un evenement au hasard selon les poids du catalogue. */
public final class TirageEvenement {

    private TirageEvenement() {
        // Classe utilitaire.
    }

    public static Evenement tirer(Random aleatoire) {
        int tirage = aleatoire.nextInt(CatalogueEvenements.POIDS_TOTAL);
        int cumul = 0;
        for (CatalogueEvenements.Entree e : CatalogueEvenements.ENTREES) {
            cumul += e.poids;
            if (tirage < cumul) {
                return e.fabrique.get();
            }
        }
        // ne devrait pas arriver
        return CatalogueEvenements.ENTREES[0].fabrique.get();
    }
}
