package Modele.infrastructure;

import Modele.royaume.Royaume;

import config.Equilibrage;

// Habitations : ne produit rien, fixe la capacite de logement selon le niveau.
public class Habitations extends Batiment {

    /**
     * Donne le type du batiment.
     *
     * @return le type habitations
     */
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
