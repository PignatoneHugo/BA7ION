package Modele.action;

import Modele.economie.Ressource;
import Modele.royaume.Royaume;

import config.Equilibrage;

// Recrute un habitant : coute de la nourriture, ajoute un inactif s'il reste de la place.
public class ActionRecruterVillageois implements Action {

    /**
     * Verifie qu'il y a assez de nourriture et de la place pour loger un habitant.
     *
     * @param royaume le royaume concerne
     * @return true si le recrutement est possible
     */
    @Override
    public boolean estExecutable(Royaume royaume) {
        if (!royaume.tresor().contient(Ressource.NOURRITURE,
                Equilibrage.COUT_NOURRITURE_PAR_VILLAGEOIS)) {
            return false;
        }
        return royaume.population().total() < royaume.population().capaciteLogement();
    }

    /**
     * Paie la nourriture et ajoute un habitant inactif.
     *
     * @param royaume le royaume concerne
     */
    @Override
    public void executer(Royaume royaume) {
        royaume.tresor().retirer(Ressource.NOURRITURE,
                Equilibrage.COUT_NOURRITURE_PAR_VILLAGEOIS);
        royaume.population().ajouterInactifs(1);
    }

    /**
     * Renvoie l'identifiant texte de l'action.
     *
     * @return la description de l'action
     */
    @Override
    public String description() {
        return "action.recruter_villageois";
    }
}
