package Modele.evenement;

import java.util.Random;

/** Tire un evenement au hasard selon les poids du catalogue. */
public final class TirageEvenement {

    private TirageEvenement() {
        // Classe utilitaire.
    }

    /**
     * Tire un evenement au hasard selon les poids du catalogue.
     *
     * @param aleatoire source de hasard
     * @return l'evenement tire
     */
    public static Evenement tirer(Random aleatoire) {
        int tirage = aleatoire.nextInt(CatalogueEvenements.POIDS_TOTAL);
        int cumul = 0;
        for (CatalogueEvenements.Entree entree : CatalogueEvenements.ENTREES) {
            cumul += entree.poids;
            if (tirage < cumul) {
                return entree.fabrique.get();
            }
        }
        // ne devrait pas arriver
        return CatalogueEvenements.ENTREES[0].fabrique.get();
    }
}
