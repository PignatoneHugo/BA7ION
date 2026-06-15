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

    public Vainqueur vainqueur() {
        return this.vainqueur;
    }

    public int pertesAttaquant() {
        return this.pertesAttaquant;
    }

    public int pertesDefenseur() {
        return this.pertesDefenseur;
    }

    public double puissanceAttaquant() {
        return this.puissanceAttaquant;
    }

    public double puissanceDefenseur() {
        return this.puissanceDefenseur;
    }

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
