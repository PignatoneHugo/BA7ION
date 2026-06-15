package Modele.evenement;

import java.util.function.Supplier;

/**
 * Liste des evenements tirables avec leur poids.
 * Pour en ajouter un : creer la classe et l'enregistrer ici.
 */
public final class CatalogueEvenements {

    private CatalogueEvenements() {
        // Classe utilitaire.
    }

    /** Une entree : un fournisseur d'evenement et son poids. */
    public static class Entree {
        public final Supplier<Evenement> fabrique;
        public final int poids;

        Entree(Supplier<Evenement> fabrique, int poids) {
            this.fabrique = fabrique;
            this.poids = poids;
        }
    }

    // poids plus eleve = plus probable
    public static final Entree[] ENTREES = {
        new Entree(Epidemie::new, 2),
        new Entree(Secheresse::new, 2),
        new Entree(FilonDor::new, 1),
        new Entree(Refugies::new, 2),
        new Entree(BonneRecolte::new, 2),
        new Entree(AttaqueBrigands::new, 1)
    };

    /** Somme des poids. */
    public static final int POIDS_TOTAL = calculerPoidsTotal();

    private static int calculerPoidsTotal() {
        int total = 0;
        for (Entree e : ENTREES) {
            total += e.poids;
        }
        return total;
    }
}
