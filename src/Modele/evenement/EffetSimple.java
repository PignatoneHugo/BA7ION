package Modele.evenement;

import Modele.economie.Ressource;
import Modele.royaume.Royaume;

/**
 * Implementation generique d'un EffetEvenement : modifie l'or, la population
 * et le moral d'un seul coup. Les valeurs negatives signifient une perte.
 *
 * Couvre la majorite des evenements simples sans avoir a creer une classe
 * dediee par effet.
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
    public void appliquer(Royaume royaume) {
        if (this.deltaOr > 0) {
            royaume.tresor().ajouter(Ressource.OR, this.deltaOr);
        } else if (this.deltaOr < 0) {
            royaume.tresor().retirer(Ressource.OR, -this.deltaOr);
        }
        if (this.habitantsPerdus > 0) {
            royaume.population().retirerHabitants(this.habitantsPerdus);
        }
        if (this.deltaMoral != 0) {
            royaume.moral().ajuster(this.deltaMoral);
        }
    }
}
