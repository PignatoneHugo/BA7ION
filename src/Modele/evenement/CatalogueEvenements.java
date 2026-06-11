package Modele.evenement;

import java.util.function.Supplier;

/**
 * Catalogue des evenements disponibles avec leur poids relatif pour le
 * tirage. Un evenement est instancie a la demande via un Supplier pour
 * eviter de partager le meme objet entre plusieurs tirages.
 *
 * Pour ajouter un evenement : creer la classe + l'enregistrer ici.
 */
public final class CatalogueEvenements {

    private CatalogueEvenements() {
        // Classe utilitaire.
    }

    /** Une entree du catalogue : un fournisseur d'evenement et son poids. */
    public static class Entree {
        public final Supplier<Evenement> fabrique;
        public final int poids;

        Entree(Supplier<Evenement> fabrique, int poids) {
            this.fabrique = fabrique;
            this.poids = poids;
        }
    }

    /**
     * Toutes les entrees du catalogue. Le poids influence la probabilite
     * de tirage : un poids 3 = trois fois plus probable qu'un poids 1.
     */
    public static final Entree[] ENTREES = {
        new Entree(Epidemie::new, 2),
        new Entree(Secheresse::new, 2),
        new Entree(FilonDor::new, 1),
        new Entree(Refugies::new, 2),
        new Entree(BonneRecolte::new, 2),
        new Entree(AttaqueBrigands::new, 1)
    };

    /** Somme des poids, calculee une seule fois. */
    public static final int POIDS_TOTAL = calculerPoidsTotal();

    private static int calculerPoidsTotal() {
        int total = 0;
        for (Entree e : ENTREES) {
            total += e.poids;
        }
        return total;
    }
}
