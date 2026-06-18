package Modele.population;

import config.Equilibrage;

// Moral de la population, valeur entre 0 et 100.
public class Moral {

    private int valeur;

    /**
     * Cree un moral a la valeur initiale par defaut.
     */
    public Moral() {
        this(Equilibrage.MORAL_INITIAL);
    }

    /**
     * Cree un moral a une valeur donnee, bornee entre 0 et 100.
     *
     * @param valeurInitiale la valeur de depart
     */
    public Moral(int valeurInitiale) {
        this.valeur = clamp(valeurInitiale);
    }

    /**
     * Donne la valeur actuelle du moral.
     *
     * @return la valeur du moral
     */
    public int valeur() {
        return this.valeur;
    }

    /**
     * Ajoute un delta au moral, qui peut etre negatif.
     *
     * @param delta la variation a appliquer
     * @return la variation reellement appliquee
     */
    public int ajuster(int delta) {
        int avant = this.valeur;
        this.valeur = clamp(this.valeur + delta);
        return this.valeur - avant;
    }

    /**
     * Force la valeur du moral, utilise au chargement d'une sauvegarde.
     *
     * @param valeur la valeur a fixer
     */
    public void definir(int valeur) {
        this.valeur = clamp(valeur);
    }

    private static int clamp(int valeurACadrer) {
        return Math.max(Equilibrage.MORAL_MIN, Math.min(Equilibrage.MORAL_MAX, valeurACadrer));
    }
}
