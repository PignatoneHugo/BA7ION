package Modele.infrastructure;

import Modele.royaume.Royaume;

/**
 * Tour de Guet : donne de l'information sur les armees ennemies en approche.
 * Aucune production de ressource. Sera consultee lors de la phase EtatCombatsSubis
 * quand le module combat sera branche.
 */
public class TourDeGuet extends Batiment {

    @Override
    public TypeBatiment type() {
        return TypeBatiment.TOUR_GUET;
    }

    @Override
    protected void appliquerProduction(Royaume royaume) {
        // Pas de production : la Tour de Guet ne fait que de l'observation.
    }

    /** Distance en tours a laquelle la tour detecte les armees ennemies. */
    public int porteeDetection() {
        return this.niveau;
    }
}
