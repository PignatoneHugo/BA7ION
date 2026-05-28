package Modele.partie.etat;

import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.partie.Partie;
import Modele.royaume.Royaume;

/**
 * Phase de consommation : chaque royaume retire de son tresor la nourriture
 * necessaire a ses habitants. En cas de penurie, la {@link Royaume} gere
 * elle-meme la baisse d'effectifs.
 *
 * Transition : enchaine sur {@link EtatFinTour}.
 */
public class EtatConsommation implements EtatTour {

    @Override
    public void executer(Partie partie) {
        for (Royaume r : partie.tousLesRoyaumes()) {
            r.appliquerConsommationCivile();
        }
        partie.notifier(new Notification(TypeNotification.PHASE_CHANGEE, this.nomCle()));
    }

    @Override
    public EtatTour suivant() {
        return new EtatFinTour();
    }

    @Override
    public String nomCle() {
        return "phase.consommation";
    }
}
