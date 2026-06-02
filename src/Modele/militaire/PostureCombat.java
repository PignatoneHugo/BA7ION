package Modele.militaire;

/**
 * Posture choisie pour un combat. Modifie les puissances d'attaque et de
 * defense, et determine si le bonus des remparts s'applique.
 *
 * - ATTAQUE : favorise l'offensive au detriment de la defense
 * - DEFENSE : favorise la defense au detriment de l'offensive
 * - CONTOURNEMENT : equilibre, ignore le bonus des remparts (utile pour
 *   attaquer une cite fortifiee)
 */
public enum PostureCombat {

    ATTAQUE("posture.attaque", 1.2, 0.9, true),
    DEFENSE("posture.defense", 0.9, 1.3, true),
    CONTOURNEMENT("posture.contournement", 1.0, 1.0, false);

    private final String cleI18n;
    private final double multAttaque;
    private final double multDefense;
    private final boolean utiliseRemparts;

    PostureCombat(String cleI18n, double multAttaque, double multDefense, boolean utiliseRemparts) {
        this.cleI18n = cleI18n;
        this.multAttaque = multAttaque;
        this.multDefense = multDefense;
        this.utiliseRemparts = utiliseRemparts;
    }

    public String cleI18n() {
        return this.cleI18n;
    }

    public double multAttaque() {
        return this.multAttaque;
    }

    public double multDefense() {
        return this.multDefense;
    }

    /** True si le bonus des remparts s'applique avec cette posture. */
    public boolean utiliseRemparts() {
        return this.utiliseRemparts;
    }
}
