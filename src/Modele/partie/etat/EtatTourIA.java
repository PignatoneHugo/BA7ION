package Modele.partie.etat;

import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.partie.Partie;

/**
 * Phase pendant laquelle les bots jouent leur tour (planification +
 * execution immediate). Chaque bot consulte sa StrategieIA et empile des
 * Actions dans sa propre file, executees dans la foulee.
 *
 * Au Sprint 2 : placeholder, l'IA des bots sera implementee au Sprint 3.
 */
public class EtatTourIA implements EtatTour {

    @Override
    public void executer(Partie partie) {
        // Rien a faire au Sprint 2 : les bots n'ont pas encore de strategie.
        partie.notifier(new Notification(TypeNotification.PHASE_CHANGEE, this.nomCle()));
    }

    @Override
    public EtatTour suivant() {
        return new EtatEvenement();
    }

    @Override
    public String nomCle() {
        return "phase.tour_ia";
    }
}
