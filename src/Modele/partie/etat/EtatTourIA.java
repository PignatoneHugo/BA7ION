package Modele.partie.etat;

import Modele.ia.StrategieIA;
import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.partie.Partie;
import Modele.royaume.Royaume;

// Phase ou chaque bot joue son tour via sa strategie.
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
