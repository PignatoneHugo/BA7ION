package Modele.economie;

/**
 * Niveau de taxation choisi par le joueur.
 * Chaque niveau donne un revenu d'or par habitant et un impact sur le moral.
 */
public enum NiveauTaxes {

    FAIBLE("Faibles", 1, 2),
    NORMAL("Normales", 2, 0),
    ELEVE("Elevees", 3, -3);

    private final String libelle;
    private final int orParHabitant;
    private final int impactMoralParTour;

    NiveauTaxes(String libelle, int orParHabitant, int impactMoralParTour) {
        this.libelle = libelle;
        this.orParHabitant = orParHabitant;
        this.impactMoralParTour = impactMoralParTour;
    }

    public String libelle() {
        return this.libelle;
    }

    public int orParHabitant() {
        return this.orParHabitant;
    }

    public int impactMoralParTour() {
        return this.impactMoralParTour;
    }
}
