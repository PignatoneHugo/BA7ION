package config;

// Difficulte choisie au depart : change l'or initial du joueur.
public enum Difficulte {

    FACILE("Facile", 0),
    NORMAL("Normale", -300),
    DIFFICILE("Difficile", -450);

    private final String libelle;
    private final int bonusOrInitial;

    Difficulte(String libelle, int bonusOrInitial) {
        this.libelle = libelle;
        this.bonusOrInitial = bonusOrInitial;
    }

    /**
     * Renvoie le libelle affichable de la difficulte.
     *
     * @return le libelle de la difficulte
     */
    public String libelle() {
        return this.libelle;
    }

    /**
     * Renvoie le bonus ou malus d'or initial lie a la difficulte.
     *
     * @return le montant d'or ajoute (positif) ou retire (negatif) au depart
     */
    public int bonusOrInitial() {
        return this.bonusOrInitial;
    }
}
