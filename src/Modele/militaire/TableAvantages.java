package Modele.militaire;

/**
 * Table des avantages entre types d'unites (Pierre-Feuille-Ciseaux).
 *
 * Chaine d'avantages :
 *   Cavalerie > Archer > Infanterie > Lancier > Cavalerie
 *
 * Une unite qui a l'avantage sur sa cible voit son attaque multipliee par
 * BONUS_AVANTAGE (1.5 par defaut, soit +50%).
 */
public final class TableAvantages {

    public static final double BONUS_AVANTAGE = 1.5;

    private TableAvantages() {
        // Classe utilitaire.
    }

    /** Retourne le multiplicateur d'attaque de l'attaquant contre le defenseur. */
    public static double bonusContre(TypeUnite attaquant, TypeUnite defenseur) {
        if (attaquant == TypeUnite.CAVALERIE_LOURDE && defenseur == TypeUnite.ARCHER) {
            return BONUS_AVANTAGE;
        }
        if (attaquant == TypeUnite.ARCHER && defenseur == TypeUnite.INFANTERIE_LEGERE) {
            return BONUS_AVANTAGE;
        }
        if (attaquant == TypeUnite.INFANTERIE_LEGERE && defenseur == TypeUnite.LANCIER) {
            return BONUS_AVANTAGE;
        }
        if (attaquant == TypeUnite.LANCIER && defenseur == TypeUnite.CAVALERIE_LOURDE) {
            return BONUS_AVANTAGE;
        }
        return 1.0;
    }
}
