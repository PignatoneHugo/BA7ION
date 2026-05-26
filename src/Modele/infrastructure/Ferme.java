package Modele.infrastructure;

import Modele.economie.Ressource;
import Modele.population.Role;
import Modele.royaume.Royaume;

import config.Equilibrage;

/**
 * Ferme : ameliore la production de nourriture des Fermiers et la capacite de
 * stockage de nourriture (Epic 3, US-INFRA-14).
 *
 * Au Sprint 1, on calcule uniquement la production en fonction du nombre de
 * Fermiers, sans encore appliquer les bonus de niveau ni les decrets.
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
        int production = fermiers * Equilibrage.PRODUCTION_NOURRITURE_PAR_FERMIER;
        // Bonus de niveau (Template Method : la formule est centralisee ici).
        double bonusNiveau = 1.0 + Equilibrage.BONUS_FERME_PAR_NIVEAU * (this.niveau - 1);
        if (this.endommage) {
            bonusNiveau *= 0.5;
        }
        int productionFinale = (int) Math.round(production * bonusNiveau);
        royaume.tresor().ajouter(Ressource.NOURRITURE, productionFinale);
    }
}
