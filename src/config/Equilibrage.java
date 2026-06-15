package config;

import java.util.EnumMap;
import java.util.Map;

import Modele.economie.Ressource;
import Modele.infrastructure.TypeBatiment;

// Toutes les constantes de reglage du jeu.
public final class Equilibrage {

    private Equilibrage() {
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
        CAPACITES_INITIALES.put(Ressource.SAVOIR, 1000);
    }

    public static int stockInitial(Ressource r) {
        return STOCKS_INITIAUX.get(r);
    }

    public static int capaciteInitiale(Ressource r) {
        return CAPACITES_INITIALES.get(r);
    }

    // PRODUCTION (par habitant et par tour)

    public static final int PRODUCTION_NOURRITURE_PAR_FERMIER = 3;
    public static final int PRODUCTION_PIERRE_PAR_MINEUR = 2;
    public static final int PRODUCTION_OR_PAR_MINEUR = 1;
    public static final int PRODUCTION_BOIS_PAR_BUCHERON = 3;
    public static final int PRODUCTION_SAVOIR_PAR_ERUDIT = 2;

    // CONSOMMATION

    public static final int CONSOMMATION_NOURRITURE_PAR_HABITANT = 1;

    // BONUS PAR NIVEAU DE BATIMENT

    // Bonus de production par niveau au-dessus de 1.
    public static final double BONUS_FERME_PAR_NIVEAU = 0.25;
    public static final double BONUS_MINE_PAR_NIVEAU = 0.25;
    public static final double BONUS_SCIERIE_PAR_NIVEAU = 0.25;
    public static final double BONUS_BIBLIOTHEQUE_PAR_NIVEAU = 0.25;

    // Logement en plus par niveau d'Habitations.
    public static final int CAPACITE_LOGEMENT_PAR_NIVEAU_HABITATIONS = 20;

    // MORAL

    public static final int MORAL_INITIAL = 50;
    public static final int MORAL_MIN = 0;
    public static final int MORAL_MAX = 100;

    // Moral perdu par habitant mort de faim.
    public static final int IMPACT_MORAL_PAR_FAMINE = -2;

    // AMELIORATION DE BATIMENT

    public static final int NIVEAU_MAX_BATIMENT = 5;

    // Nombre de tours d'un chantier.
    public static final int DUREE_CHANTIER_AMELIORATION = 2;

    // Cout pour ameliorer un batiment vers le niveau cible.
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

    // Chance d'un evenement aleatoire a chaque tour (apres le tour 1).
    public static final double PROBABILITE_EVENEMENT_PAR_TOUR = 0.15;

    // Tour ou la grenouille empoisonnee se declenche (une seule fois).
    public static final int TOUR_GRENOUILLE_EMPOISONNEE = 6;

    // RECRUTEMENT DE VILLAGEOIS

    // Nourriture pour recruter un habitant.
    public static final int COUT_NOURRITURE_PAR_VILLAGEOIS = 30;

    // RECRUTEMENT MILITAIRE

    // Or pour recruter un soldat.
    public static final int COUT_OR_PAR_SOLDAT = 30;

    // Un soldat consomme un habitant.
    public static final int HABITANTS_PAR_SOLDAT = 1;

    // IA DES BOTS

    // Or a partir duquel l'IA recrute.
    public static final int SEUIL_OR_RECRUTEMENT_IA = 200;

    public static final int TOURS_ENTRE_ATTAQUES_IA = 5;

    // Armee minimum pour qu'un bot attaque.
    public static final int EFFECTIF_MIN_POUR_ATTAQUE_IA = 5;

    // Chance de base qu'un bot attaque le joueur.
    public static final double PROBA_ATTAQUE_IA_BASE = 0.25;

    // COMBATS

    // Les combats deviennent possibles a ce tour (cale sur la grenouille).
    public static final int TOUR_DEBUT_COMBATS = TOUR_GRENOUILLE_EMPOISONNEE;

    // CONSEQUENCES D'UNE BATAILLE

    // % de civils tues chez le defenseur qui perd.
    public static final int PERTES_CIVILES_DEFAITE_PCT = 20;

    // % des ressources du defenseur volees par le vainqueur.
    public static final int BUTIN_VICTOIRE_PCT = 25;

    // Moral perdu quand on perd en defense.
    public static final int IMPACT_MORAL_DEFAITE_DEFENSIVE = 8;

    // CONDITIONS DE FIN DE PARTIE

    // En dessous : defaite.
    public static final int POPULATION_MIN_DEFAITE = 0;
    public static final int MORAL_MIN_DEFAITE = 5;

    // Or pour gagner par prosperite.
    public static final int OR_VICTOIRE_PROSPERITE = 5000;

    // Dernier tour avant match nul.
    public static final int TOUR_MAX = 50;
}
