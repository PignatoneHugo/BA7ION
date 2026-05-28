package Modele.partie.etat;

import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.partie.Partie;

/**
 * Phase d'attente du joueur : aucune resolution n'a lieu. Le joueur affecte
 * sa population, planifie ses actions et ajuste sa politique jusqu'a ce
 * qu'il declenche la fin du tour.
 *
 * Transition : enchaine sur {@link EtatProduction}.
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
