package Modele.partie.etat;

import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.partie.Partie;

/**
 * Phase finale : incremente le compteur de tour, declenche l'autosauvegarde
 * et notifie la fin du cycle.
 *
 * Transition : revient sur {@link EtatPlanification} pour le tour suivant.
 */
public class EtatFinTour implements EtatTour {

    @Override
    public void executer(Partie partie) {
        partie.incrementerTour();
        partie.notifier(new Notification(TypeNotification.TOUR_TERMINE, partie.numeroTour()));
    }

    @Override
    public EtatTour suivant() {
        return new EtatPlanification();
    }

    @Override
    public String nomCle() {
        return "phase.fin_tour";
    }
}
