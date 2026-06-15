package Modele.infrastructure;

import Modele.royaume.Royaume;

// Remparts : bonus de defense en combat, pas de production.
public class Remparts extends Batiment {

    @Override
    public TypeBatiment type() {
        return TypeBatiment.REMPARTS;
    }

    @Override
    protected void appliquerProduction(Royaume royaume) {
        // Rien : agit seulement en defense pendant les combats.
    }

    // Bonus defensif en pourcentage.
    public int bonusDefensif() {
        if (this.endommage) {
            return 5 * (this.niveau - 1);
        }
        return 10 * this.niveau;
    }
}
