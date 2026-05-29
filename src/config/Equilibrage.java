package config;

import java.util.EnumMap;
import java.util.Map;

import Modele.economie.Ressource;

/**
 * Toutes les constantes numeriques du jeu (production, consommation, stocks
 * initiaux, etc.). Regroupees ici pour eviter les nombres magiques dans le code.
 */
public final class Equilibrage {

    private Equilibrage() {
        // Classe utilitaire, ne pas instancier.
    }

    // POPULATION INITIALE

    public static final int POPULATION_INITIALE = 10;
    public static final int CAPACITE_LOGEMENT_INITIALE = 20;

    // STOCKS INITIAUX ET CAPACITES MAX

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

    public static int stockInitial(Ressource r) {
        return STOCKS_INITIAUX.get(r);
    }

    public static int capaciteInitiale(Ressource r) {
        return CAPACITES_INITIALES.get(r);
    }

    // PRODUCTION (par habitant et par tour)

    public static final int PRODUCTION_NOURRITURE_PAR_FERMIER = 2;
    public static final int PRODUCTION_PIERRE_PAR_MINEUR = 1;
    public static final int PRODUCTION_OR_PAR_MINEUR = 1;
    public static final int PRODUCTION_BOIS_PAR_BUCHERON = 2;

    // CONSOMMATION

    public static final int CONSOMMATION_NOURRITURE_PAR_HABITANT = 1;

    // BONUS PAR NIVEAU DE BATIMENT

    /** Bonus de production agricole par niveau de Ferme au-dessus de 1. */
    public static final double BONUS_FERME_PAR_NIVEAU = 0.10;

    // EVENEMENTS

    /** Tour auquel l'evenement de test du Sprint 1 se declenche. */
    public static final int TOUR_EVENEMENT_TEST = 2;
}
