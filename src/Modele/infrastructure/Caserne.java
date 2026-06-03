package Modele.infrastructure;

import Modele.royaume.Royaume;

/**
 * Caserne : permettra le recrutement de soldats.
 * Aucune production de ressource. La logique de recrutement sera ajoutee
 * quand le module militaire sera branche dans le cycle de tour.
 */
public class Caserne extends Batiment {

    @Override
    public TypeBatiment type() {
        return TypeBatiment.CASERNE;
    }

    @Override
    protected void appliquerProduction(Royaume royaume) {
        // Pas de production passive : la Caserne sert au recrutement actif
        // via une Action declenchee par le joueur.
    }
}
