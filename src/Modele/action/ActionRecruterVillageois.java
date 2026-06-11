package Modele.action;

import Modele.economie.Ressource;
import Modele.royaume.Royaume;

import config.Equilibrage;

/**
 * Action qui recrute un nouvel habitant : retire 100 nourriture du tresor
 * et ajoute 1 inactif a la population.
 *
 * Conditions :
 *  - le royaume a au moins COUT_NOURRITURE_PAR_VILLAGEOIS nourriture
 *  - il reste de la place dans la capacite de logement
 */
public class ActionRecruterVillageois implements Action {

    @Override
    public boolean estExecutable(Royaume royaume) {
        if (!royaume.tresor().contient(Ressource.NOURRITURE,
                Equilibrage.COUT_NOURRITURE_PAR_VILLAGEOIS)) {
            return false;
        }
        return royaume.population().total() < royaume.population().capaciteLogement();
    }

    @Override
    public void executer(Royaume royaume) {
        royaume.tresor().retirer(Ressource.NOURRITURE,
                Equilibrage.COUT_NOURRITURE_PAR_VILLAGEOIS);
        royaume.population().ajouterInactifs(1);
    }

    @Override
    public String description() {
        return "action.recruter_villageois";
    }
}
