package Modele.infrastructure;

import Modele.economie.Ressource;
import Modele.population.Role;
import Modele.royaume.Royaume;

import config.Equilibrage;

// Scierie : produit du bois selon le nombre de bucherons.
public class Scierie extends Batiment {

    @Override
    public TypeBatiment type() {
        return TypeBatiment.SCIERIE;
    }

    @Override
    protected void appliquerProduction(Royaume royaume) {
        int bucherons = royaume.population().effectif(Role.BUCHERON);
        if (bucherons == 0) {
            return;
        }
        int productionBase = bucherons * Equilibrage.PRODUCTION_BOIS_PAR_BUCHERON;
        double bonusNiveau = 1.0 + Equilibrage.BONUS_SCIERIE_PAR_NIVEAU * (this.niveau - 1);
        if (this.endommage) {
            bonusNiveau *= 0.5;
        }
        int productionFinale = (int) Math.round(productionBase * bonusNiveau);
        royaume.tresor().ajouter(Ressource.BOIS, productionFinale);
    }
}
