package Modele.infrastructure;

import Modele.royaume.Royaume;

import config.Equilibrage;

/**
 * Habitations : ne produit pas de ressource mais fixe la capacite de logement
 * de la population. Recalcule la capacite a chaque tour selon son niveau :
 * niveau 1 = capacite initiale, +10 places par niveau au-dessus.
 *
 * Si endommagee, la capacite supplementaire des niveaux est divisee par 2.
 */
public class Habitations extends Batiment {

    @Override
    public TypeBatiment type() {
        return TypeBatiment.HABITATIONS;
    }

    @Override
    protected void appliquerProduction(Royaume royaume) {
        int bonus = Equilibrage.CAPACITE_LOGEMENT_PAR_NIVEAU_HABITATIONS * (this.niveau - 1);
        if (this.endommage) {
            bonus = bonus / 2;
        }
        int capacite = Equilibrage.CAPACITE_LOGEMENT_INITIALE + bonus;
        royaume.population().definirCapaciteLogement(capacite);
    }
}
