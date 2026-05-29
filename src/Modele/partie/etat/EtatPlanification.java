package Modele.partie.etat;

import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.partie.Partie;

/**
 * Phase d'attente : le joueur planifie ses actions.
 * Aucun traitement, on attend juste qu'il clique "Fin de tour".
 */
public class EtatPlanification implements EtatTour {

    @Override
    public void executer(Partie partie) {
        partie.notifier(new Notification(TypeNotification.PHASE_CHANGEE, this.nomCle()));
    }

    @Override
    public EtatTour suivant() {
        return new EtatProduction();
    }

    @Override
    public String nomCle() {
        return "phase.planification";
    }
}
