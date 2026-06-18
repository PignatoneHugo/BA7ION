package Modele.infrastructure;

import Modele.royaume.Royaume;

// Caserne : sert au recrutement de soldats, pas de production.
public class Caserne extends Batiment {

    /**
     * Donne le type du batiment.
     *
     * @return le type caserne
     */
    @Override
    public TypeBatiment type() {
        return TypeBatiment.CASERNE;
    }

    @Override
    protected void appliquerProduction(Royaume royaume) {
        // Rien : le recrutement se fait via une Action du joueur.
    }
}
