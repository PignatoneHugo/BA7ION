package Modele.economie;

/**
 * Niveau de taxation choisi par le joueur.
 * Chaque niveau donne un revenu d'or par habitant et un impact sur le moral.
 */
public enum NiveauTaxes {

    FAIBLE("taxes.faible", 1, 2),
    NORMAL("taxes.normal", 2, 0),
    ELEVE("taxes.eleve", 3, -3);

    private final String cleI18n;
    private final int orParHabitant;
    private final int impactMoralParTour;

    NiveauTaxes(String cleI18n, int orParHabitant, int impactMoralParTour) {
        this.cleI18n = cleI18n;
        this.orParHabitant = orParHabitant;
        this.impactMoralParTour = impactMoralParTour;
    }

    public String cleI18n() {
        return this.cleI18n;
    }

    public int orParHabitant() {
        return this.orParHabitant;
    }

    public int impactMoralParTour() {
        return this.impactMoralParTour;
    }
}
