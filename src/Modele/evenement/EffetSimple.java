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

    /**
     * Cree un effet simple avec ses variations.
     *
     * @param deltaOr variation d'or (negatif = perte)
     * @param habitantsPerdus nombre d'habitants perdus
     * @param deltaMoral variation de moral (negatif = perte)
     */
    public EffetSimple(int deltaOr, int habitantsPerdus, int deltaMoral) {
        this.deltaOr = deltaOr;
        this.habitantsPerdus = habitantsPerdus;
        this.deltaMoral = deltaMoral;
    }

    /**
     * Applique les variations d'or, de population et de moral au royaume.
     *
     * @param royaume le royaume modifie
     * @param aleatoire source de hasard pour le retrait d'habitants
     */
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

    /**
     * Indique si le royaume a assez d'or quand l'effet coute de l'or.
     *
     * @param royaume le royaume concerne
     * @return vrai si l'effet peut etre applique
     */
    @Override
    public boolean peutEtreApplique(Royaume royaume) {
        if (this.deltaOr < 0) {
            return royaume.tresor().contient(Ressource.OR, -this.deltaOr);
        }
        return true;
    }
}
