package Modele.infrastructure;

import Modele.economie.Ressource;
import Modele.population.Role;
import Modele.royaume.Royaume;

import config.Equilibrage;

/**
 * Ferme : produit de la nourriture proportionnellement au nombre de fermiers.
 * Bonus +10% par niveau au-dessus de 1, divise par 2 si endommagee.
 */
public class Ferme extends Batiment {

    @Override
    public TypeBatiment type() {
        return TypeBatiment.FERME;
    }

    @Override
    protected void appliquerProduction(Royaume royaume) {
        int fermiers = royaume.population().effectif(Role.FERMIER);
        if (fermiers == 0) {
            return;
        }
        int productionBase = fermiers * Equilibrage.PRODUCTION_NOURRITURE_PAR_FERMIER;
        double bonusNiveau = 1.0 + Equilibrage.BONUS_FERME_PAR_NIVEAU * (this.niveau - 1);
        if (this.endommage) {
            bonusNiveau *= 0.5;
        }
        int productionFinale = (int) Math.round(productionBase * bonusNiveau);
        royaume.tresor().ajouter(Ressource.NOURRITURE, productionFinale);
    }
}
