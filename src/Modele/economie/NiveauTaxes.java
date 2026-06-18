package Modele.economie;

// Niveau de taxes choisi par le joueur (or par habitant + effet sur le moral).
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

    /**
     * Donne le nom affichable du niveau de taxes.
     *
     * @return le libelle du niveau de taxes
     */
    public String libelle() {
        return this.libelle;
    }

    /**
     * Donne l'or preleve par habitant a ce niveau.
     *
     * @return l'or par habitant
     */
    public int orParHabitant() {
        return this.orParHabitant;
    }

    /**
     * Donne l'effet de ce niveau de taxes sur le moral a chaque tour.
     *
     * @return l'impact sur le moral par tour
     */
    public int impactMoralParTour() {
        return this.impactMoralParTour;
    }
}
