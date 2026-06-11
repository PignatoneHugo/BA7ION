package Modele.evenement;

import Modele.royaume.Royaume;

/**
 * Choix propose au joueur lors d'un evenement. Chaque choix associe un
 * libelle (via cle i18n) a un effet sur le royaume.
 */
public class Choix {

    private final String cleI18n;
    private final EffetEvenement effet;

    public Choix(String cleI18n, EffetEvenement effet) {
        if (cleI18n == null || effet == null) {
            throw new IllegalArgumentException("cleI18n et effet ne peuvent pas etre null.");
        }
        this.cleI18n = cleI18n;
        this.effet = effet;
    }

    public String cleI18n() {
        return this.cleI18n;
    }

    public EffetEvenement effet() {
        return this.effet;
    }

    /** True si le royaume a les ressources requises pour ce choix. */
    public boolean peutEtreChoisi(Royaume royaume) {
        return this.effet.peutEtreApplique(royaume);
    }
}
