package config;

// Difficulte choisie au depart : change l'or initial du joueur.
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

    public int bonusOrInitial() {
        return this.bonusOrInitial;
    }
}
