package Modele.infrastructure;

import Modele.royaume.Royaume;

// Tour de Guet : detecte les armees ennemies en approche, pas de production.
public class TourDeGuet extends Batiment {

    /**
     * Donne le type du batiment.
     *
     * @return le type tour de guet
     */
    @Override
    public TypeBatiment type() {
        return TypeBatiment.TOUR_GUET;
    }

    @Override
    protected void appliquerProduction(Royaume royaume) {
        // Rien : la tour ne fait qu'observer.
    }

    /**
     * Donne la portee de detection des armees ennemies.
     *
     * @return la portee de detection en tours
     */
    public int porteeDetection() {
        return this.niveau;
    }
}
