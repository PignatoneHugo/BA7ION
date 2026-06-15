package Modele.infrastructure;

import Modele.economie.Ressource;
import Modele.population.Role;
import Modele.royaume.Royaume;

import config.Equilibrage;

// Mine : produit de la pierre et de l'or selon le nombre de mineurs.
public class Mine extends Batiment {

    @Override
    public TypeBatiment type() {
        return TypeBatiment.MINE;
    }

    @Override
    protected void appliquerProduction(Royaume royaume) {
        int mineurs = royaume.population().effectif(Role.MINEUR);
        if (mineurs == 0) {
            return;
        }
        int pierreBase = mineurs * Equilibrage.PRODUCTION_PIERRE_PAR_MINEUR;
        int orBase = mineurs * Equilibrage.PRODUCTION_OR_PAR_MINEUR;

        double bonusNiveau = 1.0 + Equilibrage.BONUS_MINE_PAR_NIVEAU * (this.niveau - 1);
        if (this.endommage) {
            bonusNiveau *= 0.5;
        }
        int pierreFinale = (int) Math.round(pierreBase * bonusNiveau);
        int orFinal = (int) Math.round(orBase * bonusNiveau);

        royaume.tresor().ajouter(Ressource.PIERRE, pierreFinale);
        royaume.tresor().ajouter(Ressource.OR, orFinal);
    }
}
