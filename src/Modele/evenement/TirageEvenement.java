package Modele.evenement;

import java.util.Random;

/**
 * Tirage pondere d'un evenement parmi ceux du CatalogueEvenements.
 * Chaque evenement a une probabilite proportionnelle a son poids.
 */
public final class TirageEvenement {

    private TirageEvenement() {
        // Classe utilitaire.
    }

    /**
     * Tire un evenement au hasard selon les poids du catalogue.
     *
     * @param aleatoire generateur aleatoire seedable
     * @return une nouvelle instance d'evenement
     */
    public static Evenement tirer(Random aleatoire) {
        int tirage = aleatoire.nextInt(CatalogueEvenements.POIDS_TOTAL);
        int cumul = 0;
        for (CatalogueEvenements.Entree e : CatalogueEvenements.ENTREES) {
            cumul += e.poids;
            if (tirage < cumul) {
                return e.fabrique.get();
            }
        }
        // Ne devrait jamais arriver si le catalogue est non vide.
        return CatalogueEvenements.ENTREES[0].fabrique.get();
    }
}
