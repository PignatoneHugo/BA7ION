package Modele.population;

import config.Equilibrage;

// Moral de la population, valeur entre 0 et 100.
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

    // Ajoute delta (peut etre negatif), renvoie la variation reellement appliquee.
    public int ajuster(int delta) {
        int avant = this.valeur;
        this.valeur = clamp(this.valeur + delta);
        return this.valeur - avant;
    }

    // Force la valeur (chargement de sauvegarde).
    public void definir(int valeur) {
        this.valeur = clamp(valeur);
    }

    private static int clamp(int v) {
        return Math.max(Equilibrage.MORAL_MIN, Math.min(Equilibrage.MORAL_MAX, v));
    }
}
