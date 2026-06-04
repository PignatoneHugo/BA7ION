package config;

/**
 * Niveau de difficulte choisi au demarrage de la partie. Ajuste l'or
 * initial du joueur. D'autres effets (IA plus aggressive, evenements plus
 * frequents, etc.) viendront au Sprint 3.
 */
public enum Difficulte {

    FACILE("difficulte.facile", 500),
    NORMAL("difficulte.normal", 0),
    DIFFICILE("difficulte.difficile", -200);

    private final String cleI18n;
    private final int bonusOrInitial;

    Difficulte(String cleI18n, int bonusOrInitial) {
        this.cleI18n = cleI18n;
        this.bonusOrInitial = bonusOrInitial;
    }

    public String cleI18n() {
        return this.cleI18n;
    }

    /** Bonus (ou malus si negatif) d'or applique au demarrage. */
    public int bonusOrInitial() {
        return this.bonusOrInitial;
    }
}
