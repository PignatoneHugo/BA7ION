package Modele.militaire;

// Avantages entre unites facon pierre-feuille-ciseaux.
// Cavalerie > Archer > Infanterie > Lancier > Cavalerie.
public final class TableAvantages {

    public static final double BONUS_AVANTAGE = 1.5;

    private TableAvantages() {
    }

    /**
     * Donne le multiplicateur d'attaque de l'attaquant contre le defenseur.
     *
     * @param attaquant le type de l'unite qui attaque
     * @param defenseur le type de l'unite qui defend
     * @return le bonus d'attaque, ou 1.0 s'il n'y a pas d'avantage
     */
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
