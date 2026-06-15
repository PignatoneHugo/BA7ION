package Modele.action;

import Modele.economie.Ressource;
import Modele.royaume.Royaume;

import config.Equilibrage;

// Recrute un habitant : coute de la nourriture, ajoute un inactif s'il reste de la place.
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
