package Modele.infrastructure;

import Modele.royaume.Royaume;

// Remparts : bonus de defense en combat, pas de production.
public class Remparts extends Batiment {

    /**
     * Donne le type du batiment.
     *
     * @return le type remparts
     */
    @Override
    public TypeBatiment type() {
        return TypeBatiment.REMPARTS;
    }

    @Override
    protected void appliquerProduction(Royaume royaume) {
        // Rien : agit seulement en defense pendant les combats.
    }

    /**
     * Donne le bonus de defense apporte par les remparts en combat.
     *
     * @return le bonus defensif en pourcentage
     */
    public int bonusDefensif() {
        if (this.endommage) {
            return 5 * (this.niveau - 1);
        }
        return 10 * this.niveau;
    }
}
