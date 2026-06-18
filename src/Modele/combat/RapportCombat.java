package Modele.combat;

// Resultat d'un combat : vainqueur, pertes et puissances calculees.
public class RapportCombat {

    public enum Vainqueur {
        ATTAQUANT,
        DEFENSEUR,
        EGALITE
    }

    private final Vainqueur vainqueur;
    private final int pertesAttaquant;
    private final int pertesDefenseur;
    private final double puissanceAttaquant;
    private final double puissanceDefenseur;

    /**
     * Cree le rapport d'un combat resolu.
     *
     * @param vainqueur le vainqueur du combat
     * @param pertesAttaquant les pertes de l'attaquant
     * @param pertesDefenseur les pertes du defenseur
     * @param puissanceAttaquant la puissance calculee de l'attaquant
     * @param puissanceDefenseur la puissance calculee du defenseur
     */
    public RapportCombat(Vainqueur vainqueur,
                         int pertesAttaquant,
                         int pertesDefenseur,
                         double puissanceAttaquant,
                         double puissanceDefenseur) {
        this.vainqueur = vainqueur;
        this.pertesAttaquant = pertesAttaquant;
        this.pertesDefenseur = pertesDefenseur;
        this.puissanceAttaquant = puissanceAttaquant;
        this.puissanceDefenseur = puissanceDefenseur;
    }

    /**
     * Renvoie le vainqueur du combat.
     *
     * @return le vainqueur
     */
    public Vainqueur vainqueur() {
        return this.vainqueur;
    }

    /**
     * Renvoie les pertes de l'attaquant.
     *
     * @return le nombre de soldats perdus par l'attaquant
     */
    public int pertesAttaquant() {
        return this.pertesAttaquant;
    }

    /**
     * Renvoie les pertes du defenseur.
     *
     * @return le nombre de soldats perdus par le defenseur
     */
    public int pertesDefenseur() {
        return this.pertesDefenseur;
    }

    /**
     * Renvoie la puissance calculee de l'attaquant.
     *
     * @return la puissance de l'attaquant
     */
    public double puissanceAttaquant() {
        return this.puissanceAttaquant;
    }

    /**
     * Renvoie la puissance calculee du defenseur.
     *
     * @return la puissance du defenseur
     */
    public double puissanceDefenseur() {
        return this.puissanceDefenseur;
    }

    /**
     * Renvoie une description texte du rapport.
     *
     * @return le rapport sous forme de texte
     */
    @Override
    public String toString() {
        return "RapportCombat[vainqueur=" + this.vainqueur
                + ", pertesA=" + this.pertesAttaquant
                + ", pertesD=" + this.pertesDefenseur
                + ", puissanceA=" + String.format("%.1f", this.puissanceAttaquant)
                + ", puissanceD=" + String.format("%.1f", this.puissanceDefenseur)
                + "]";
    }
}
