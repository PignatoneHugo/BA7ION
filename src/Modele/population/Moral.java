package Modele.population;

import config.Equilibrage;

/**
 * Moral de la population d'un royaume. Valeur entre 0 et 100,
 * bornee automatiquement par ajuster().
 *
 * Le moral baisse en cas de famine ou de taxes elevees,
 * et monte avec des taxes faibles ou des evenements positifs.
 */
public class Moral {

    private int valeur;

    public Moral() {
        this(Equilibrage.MORAL_INITIAL);
    }

    public Moral(int valeurInitiale) {
        this.valeur = clamp(valeurInitiale);
    }

    public int valeur() {
        return this.valeur;
    }

    /**
     * Modifie le moral du delta donne (positif ou negatif), borne aux extremes.
     * Retourne la variation reellement appliquee.
     */
    public int ajuster(int delta) {
        int avant = this.valeur;
        this.valeur = clamp(this.valeur + delta);
        return this.valeur - avant;
    }

    private static int clamp(int v) {
        return Math.max(Equilibrage.MORAL_MIN, Math.min(Equilibrage.MORAL_MAX, v));
    }
}
