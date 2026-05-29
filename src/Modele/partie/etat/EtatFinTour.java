package Modele.partie.etat;

import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.partie.Partie;

/**
 * Derniere phase du tour : on incremente le compteur et on revient en planification.
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
