package Modele.militaire;

// Posture de combat : change les multiplicateurs d'attaque/defense.
// CONTOURNEMENT ignore les remparts.
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

    /**
     * Renvoie le libelle affichable de la posture.
     *
     * @return le libelle de la posture
     */
    public String libelle() {
        return this.libelle;
    }

    /**
     * Renvoie le multiplicateur d'attaque de la posture.
     *
     * @return le multiplicateur d'attaque
     */
    public double multAttaque() {
        return this.multAttaque;
    }

    /**
     * Renvoie le multiplicateur de defense de la posture.
     *
     * @return le multiplicateur de defense
     */
    public double multDefense() {
        return this.multDefense;
    }

    /**
     * Indique si les remparts comptent avec cette posture.
     *
     * @return true si les remparts sont pris en compte
     */
    public boolean utiliseRemparts() {
        return this.utiliseRemparts;
    }
}
