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

    ATTAQUE("Attaque", 1.2, 0.9, true),
    DEFENSE("Defense", 0.9, 1.3, true),
    CONTOURNEMENT("Contournement", 1.0, 1.0, false);

    private final String libelle;
    private final double multAttaque;
    private final double multDefense;
    private final boolean utiliseRemparts;

    PostureCombat(String libelle, double multAttaque, double multDefense, boolean utiliseRemparts) {
        this.libelle = libelle;
        this.multAttaque = multAttaque;
        this.multDefense = multDefense;
        this.utiliseRemparts = utiliseRemparts;
    }

    public String libelle() {
        return this.libelle;
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
