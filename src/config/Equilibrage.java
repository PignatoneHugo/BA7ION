package config;

import java.util.EnumMap;
import java.util.Map;

import Modele.economie.Ressource;

/**
 * Constantes numeriques d'equilibrage du jeu. Cette classe centralise toutes
 * les valeurs accessibles aux equilibristes (production, consommation, bonus
 * de niveau, valeurs initiales) afin qu'aucun nombre magique ne traine dans
 * le code metier.
 *
 * Classe utilitaire : tous ses membres sont statiques, son constructeur est
 * prive et elle ne peut pas etre instanciee.
 */
public final class Equilibrage {

    private Equilibrage() {
        // Classe utilitaire, ne pas instancier.
    }

    // ----------------------------------------------------------------------
    // POPULATION INITIALE
    // ----------------------------------------------------------------------

    /** Nombre d'habitants au tour 1, tous places en inactif. */
    public static final int POPULATION_INITIALE = 10;

    /** Capacite de logement disponible des le tour 1. */
    public static final int CAPACITE_LOGEMENT_INITIALE = 20;

    // ----------------------------------------------------------------------
    // STOCKS INITIAUX ET CAPACITES MAX
    // ----------------------------------------------------------------------

    private static final Map<Ressource, Integer> STOCKS_INITIAUX = new EnumMap<>(Ressource.class);
    private static final Map<Ressource, Integer> CAPACITES_INITIALES = new EnumMap<>(Ressource.class);

    static {
        STOCKS_INITIAUX.put(Ressource.OR, 500);
        STOCKS_INITIAUX.put(Ressource.NOURRITURE, 100);
        STOCKS_INITIAUX.put(Ressource.BOIS, 200);
        STOCKS_INITIAUX.put(Ressource.PIERRE, 200);
        STOCKS_INITIAUX.put(Ressource.SAVOIR, 0);

        CAPACITES_INITIALES.put(Ressource.OR, 5000);
        CAPACITES_INITIALES.put(Ressource.NOURRITURE, 1000);
        CAPACITES_INITIALES.put(Ressource.BOIS, 1000);
        CAPACITES_INITIALES.put(Ressource.PIERRE, 1000);
        CAPACITES_INITIALES.put(Ressource.SAVOIR, 999);
    }

    /**
     * @return quantite de la ressource au demarrage d'une partie
     */
    public static int stockInitial(Ressource r) {
        return STOCKS_INITIAUX.get(r);
    }

    /**
     * @return plafond de stockage de la ressource avant toute amelioration
     */
    public static int capaciteInitiale(Ressource r) {
        return CAPACITES_INITIALES.get(r);
    }

    // ----------------------------------------------------------------------
    // PRODUCTION (par habitant et par tour)
    // ----------------------------------------------------------------------

    /** Nourriture produite par chaque fermier au cours d'un tour. */
    public static final int PRODUCTION_NOURRITURE_PAR_FERMIER = 2;

    /** Pierre produite par chaque mineur au cours d'un tour. */
    public static final int PRODUCTION_PIERRE_PAR_MINEUR = 1;

    /** Or produit par chaque mineur au cours d'un tour. */
    public static final int PRODUCTION_OR_PAR_MINEUR = 1;

    /** Bois produit par chaque bucheron au cours d'un tour. */
    public static final int PRODUCTION_BOIS_PAR_BUCHERON = 2;

    // ----------------------------------------------------------------------
    // CONSOMMATION
    // ----------------------------------------------------------------------

    /** Nourriture consommee par chaque habitant civil au cours d'un tour. */
    public static final int CONSOMMATION_NOURRITURE_PAR_HABITANT = 1;

    // ----------------------------------------------------------------------
    // BONUS PAR NIVEAU DE BATIMENT
    // ----------------------------------------------------------------------

    /**
     * Bonus de production agricole par niveau de ferme au-dela du niveau 1.
     * Applique multiplicativement sur la production de base.
     */
    public static final double BONUS_FERME_PAR_NIVEAU = 0.10;

    // ----------------------------------------------------------------------
    // EVENEMENTS PROGRAMMES
    // ----------------------------------------------------------------------

    /** Tour auquel l'evenement de demonstration se declenche. */
    public static final int TOUR_EVENEMENT_TEST = 2;
}
