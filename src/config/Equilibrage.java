package config;

import java.util.EnumMap;
import java.util.Map;

import Modele.economie.Ressource;
import Modele.infrastructure.TypeBatiment;

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
    public static final int PRODUCTION_SAVOIR_PAR_ERUDIT = 1;

    // CONSOMMATION

    public static final int CONSOMMATION_NOURRITURE_PAR_HABITANT = 1;

    // BONUS PAR NIVEAU DE BATIMENT

    /** Bonus de production par niveau de batiment au-dessus de 1 (Ferme, Mine, Scierie, Bibliotheque). */
    public static final double BONUS_FERME_PAR_NIVEAU = 0.10;
    public static final double BONUS_MINE_PAR_NIVEAU = 0.10;
    public static final double BONUS_SCIERIE_PAR_NIVEAU = 0.10;
    public static final double BONUS_BIBLIOTHEQUE_PAR_NIVEAU = 0.10;

    /** Capacite de logement supplementaire par niveau d'Habitations au-dessus de 1. */
    public static final int CAPACITE_LOGEMENT_PAR_NIVEAU_HABITATIONS = 10;

    // MORAL

    /** Valeur de moral au demarrage d'une partie. */
    public static final int MORAL_INITIAL = 50;
    public static final int MORAL_MIN = 0;
    public static final int MORAL_MAX = 100;

    /** Perte de moral par habitant mort de famine. */
    public static final int IMPACT_MORAL_PAR_FAMINE = -2;

    // AMELIORATION DE BATIMENT

    /** Niveau maximum qu'un batiment peut atteindre. */
    public static final int NIVEAU_MAX_BATIMENT = 5;

    /** Duree en tours d'un chantier d'amelioration. */
    public static final int DUREE_CHANTIER_AMELIORATION = 2;

    /**
     * Retourne le cout en ressources pour ameliorer un batiment vers le
     * niveau cible donne. Le cout grandit lineairement avec le niveau.
     */
    public static Map<Ressource, Integer> coutAmelioration(TypeBatiment type, int niveauCible) {
        Map<Ressource, Integer> cout = new EnumMap<>(Ressource.class);
        switch (type) {
            case FERME:
                cout.put(Ressource.OR, 100 * niveauCible);
                cout.put(Ressource.BOIS, 50 * niveauCible);
                break;
            case MINE:
                cout.put(Ressource.OR, 100 * niveauCible);
                cout.put(Ressource.PIERRE, 50 * niveauCible);
                break;
            case SCIERIE:
                cout.put(Ressource.OR, 100 * niveauCible);
                cout.put(Ressource.BOIS, 50 * niveauCible);
                break;
            case HABITATIONS:
                cout.put(Ressource.OR, 80 * niveauCible);
                cout.put(Ressource.BOIS, 80 * niveauCible);
                break;
            case CASERNE:
                cout.put(Ressource.OR, 150 * niveauCible);
                cout.put(Ressource.PIERRE, 100 * niveauCible);
                break;
            case REMPARTS:
                cout.put(Ressource.OR, 150 * niveauCible);
                cout.put(Ressource.PIERRE, 150 * niveauCible);
                break;
            case MARCHE:
                cout.put(Ressource.OR, 120 * niveauCible);
                cout.put(Ressource.BOIS, 50 * niveauCible);
                break;
            case BIBLIOTHEQUE:
                cout.put(Ressource.OR, 200 * niveauCible);
                cout.put(Ressource.BOIS, 100 * niveauCible);
                break;
            case TOUR_GUET:
                cout.put(Ressource.OR, 100 * niveauCible);
                cout.put(Ressource.PIERRE, 100 * niveauCible);
                break;
        }
        return cout;
    }

    // EVENEMENTS

    /** Probabilite qu'un evenement aleatoire se declenche a chaque tour (apres le tour 1). */
    public static final double PROBABILITE_EVENEMENT_PAR_TOUR = 0.30;
}
