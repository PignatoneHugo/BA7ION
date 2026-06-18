package Modele.partie.etat;

import Modele.ia.StrategieIA;
import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.partie.Partie;
import Modele.royaume.Royaume;

// Phase ou chaque bot joue son tour via sa strategie.
public class EtatTourIA implements EtatTour {

    /**
     * Fait jouer chaque bot via sa strategie IA.
     *
     * @param partie la partie sur laquelle agir
     */
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

    /**
     * Renvoie la phase suivante, les evenements.
     *
     * @return la phase des evenements
     */
    @Override
    public EtatTour suivant() {
        return new EtatEvenement();
    }

    /**
     * Renvoie la cle de traduction du nom de cette phase.
     *
     * @return la cle du nom de la phase
     */
    @Override
    public String nomCle() {
        return "phase.tour_ia";
    }
}
