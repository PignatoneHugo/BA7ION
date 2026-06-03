package Modele.infrastructure;

import Modele.royaume.Royaume;

/**
 * Remparts : donne un bonus defensif lors des combats subis.
 * Aucune production de ressource. Le bonus est lu par ResolveurCombat
 * quand le module combat sera branche dans le cycle de tour.
 */
public class Remparts extends Batiment {

    @Override
    public TypeBatiment type() {
        return TypeBatiment.REMPARTS;
    }

    @Override
    protected void appliquerProduction(Royaume royaume) {
        // Pas de production : les remparts agissent passivement
        // en defense lors de la phase EtatCombatsSubis.
    }

    /** Bonus defensif a appliquer dans le ResolveurCombat (en pourcentage). */
    public int bonusDefensif() {
        if (this.endommage) {
            return 5 * (this.niveau - 1);
        }
        return 10 * this.niveau;
    }
}
