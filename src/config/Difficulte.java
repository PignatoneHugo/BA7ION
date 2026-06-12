package config;

/**
 * Niveau de difficulte choisi au demarrage de la partie. Ajuste l'or
 * initial du joueur.
 */
public enum Difficulte {

    FACILE("Facile", 500),
    NORMAL("Normale", 0),
    DIFFICILE("Difficile", -200);

    private final String libelle;
    private final int bonusOrInitial;

    Difficulte(String libelle, int bonusOrInitial) {
        this.libelle = libelle;
        this.bonusOrInitial = bonusOrInitial;
    }

    public String libelle() {
        return this.libelle;
    }

    /** Bonus (ou malus si negatif) d'or applique au demarrage. */
    public int bonusOrInitial() {
        return this.bonusOrInitial;
    }
}
