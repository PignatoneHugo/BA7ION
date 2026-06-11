package Modele.partie.etat;

import Modele.ia.StrategieIA;
import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.partie.Partie;
import Modele.royaume.Royaume;

/**
 * Phase pendant laquelle les bots jouent leur tour. Chaque bot consulte sa
 * StrategieIA et empile/execute des Actions dans la foulee.
 *
 * Au Sprint 3 : seule StrategieEquilibree est implementee.
 */
public class EtatTourIA implements EtatTour {

    @Override
    public void executer(Partie partie) {
        for (Royaume bot : partie.bots()) {
            StrategieIA strategie = bot.strategieIA();
            if (strategie != null) {
                strategie.jouerTour(bot, partie);
            }
        }
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
