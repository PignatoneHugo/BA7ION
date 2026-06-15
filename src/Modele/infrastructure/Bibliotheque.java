package Modele.infrastructure;

import Modele.economie.Ressource;
import Modele.population.Role;
import Modele.royaume.Royaume;

import config.Equilibrage;

// Bibliotheque : produit du savoir selon le nombre d'erudits.
public class Bibliotheque extends Batiment {

    @Override
    public TypeBatiment type() {
        return TypeBatiment.BIBLIOTHEQUE;
    }

    @Override
    protected void appliquerProduction(Royaume royaume) {
        int erudits = royaume.population().effectif(Role.ERUDIT);
        if (erudits == 0) {
            return;
        }
        int productionBase = erudits * Equilibrage.PRODUCTION_SAVOIR_PAR_ERUDIT;
        double bonusNiveau = 1.0 + Equilibrage.BONUS_BIBLIOTHEQUE_PAR_NIVEAU * (this.niveau - 1);
        if (this.endommage) {
            bonusNiveau *= 0.5;
        }
        int productionFinale = (int) Math.round(productionBase * bonusNiveau);
        royaume.tresor().ajouter(Ressource.SAVOIR, productionFinale);
    }
}
