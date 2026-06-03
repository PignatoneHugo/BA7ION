package Modele.infrastructure;

import Modele.royaume.Royaume;

/**
 * Marche : permet d'echanger des ressources contre de l'or et inversement.
 * Aucune production passive. Les echanges sont declenches par une Action
 * du joueur (ActionEchanger), avec un taux qui s'ameliore au fil des niveaux.
 */
public class Marche extends Batiment {

    @Override
    public TypeBatiment type() {
        return TypeBatiment.MARCHE;
    }

    @Override
    protected void appliquerProduction(Royaume royaume) {
        // Pas de production passive : le Marche reagit aux Actions d'echange.
    }
}
