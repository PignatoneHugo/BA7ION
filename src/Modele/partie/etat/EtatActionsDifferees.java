package Modele.partie.etat;

import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.partie.Partie;
import Modele.royaume.Royaume;

// Phase qui execute les actions planifiees, royaume par royaume.
public class EtatActionsDifferees implements EtatTour {

    @Override
    public void executer(Partie partie) {
        for (Royaume r : partie.tousLesRoyaumes()) {
            int compte = r.fileActions().executerToutes(r);
            if (compte > 0) {
                r.notifierTresorChange();
                r.notifierBatimentsChanges();
            }
            r.notifierFileActionsChangee();
        }
        partie.notifier(new Notification(TypeNotification.PHASE_CHANGEE, this.nomCle()));
    }

    @Override
    public EtatTour suivant() {
        return new EtatCombatsSubis();
    }

    @Override
    public String nomCle() {
        return "phase.actions_differees";
    }
}
