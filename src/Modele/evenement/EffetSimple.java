package Modele.evenement;

import java.util.Random;

import Modele.economie.Ressource;
import Modele.royaume.Royaume;

/**
 * Effet generique : modifie or, population et moral d'un coup.
 * Les valeurs negatives sont des pertes.
 */
public class EffetSimple implements EffetEvenement {

    private final int deltaOr;
    private final int habitantsPerdus;
    private final int deltaMoral;

    public EffetSimple(int deltaOr, int habitantsPerdus, int deltaMoral) {
        this.deltaOr = deltaOr;
        this.habitantsPerdus = habitantsPerdus;
        this.deltaMoral = deltaMoral;
    }

    @Override
    public void appliquer(Royaume royaume, Random aleatoire) {
        if (this.deltaOr > 0) {
            royaume.tresor().ajouter(Ressource.OR, this.deltaOr);
        } else if (this.deltaOr < 0) {
            royaume.tresor().retirer(Ressource.OR, -this.deltaOr);
        }
        if (this.habitantsPerdus > 0) {
            royaume.population().retirerHabitants(this.habitantsPerdus, aleatoire);
        }
        if (this.deltaMoral != 0) {
            royaume.moral().ajuster(this.deltaMoral);
        }
    }

    @Override
    public boolean peutEtreApplique(Royaume royaume) {
        if (this.deltaOr < 0) {
            return royaume.tresor().contient(Ressource.OR, -this.deltaOr);
        }
        return true;
    }
}
