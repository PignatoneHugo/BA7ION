package Modele.partie.etat;

import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.partie.Partie;
import Modele.royaume.Royaume;

// Phase qui execute les actions planifiees, royaume par royaume.
public class EtatActionsDifferees implements EtatTour {

    /**
     * Execute les actions planifiees de chaque royaume.
     *
     * @param partie la partie sur laquelle agir
     */
    @Override
    public void executer(Partie partie) {
        for (Royaume royaume : partie.tousLesRoyaumes()) {
            int compte = royaume.fileActions().executerToutes(royaume);
            if (compte > 0) {
                royaume.notifierTresorChange();
                royaume.notifierBatimentsChanges();
            }
            royaume.notifierFileActionsChangee();
        }
        partie.notifier(new Notification(TypeNotification.PHASE_CHANGEE, this.nomCle()));
    }

    /**
     * Renvoie la phase suivante, les combats subis.
     *
     * @return la phase des combats subis
     */
    @Override
    public EtatTour suivant() {
        return new EtatCombatsSubis();
    }

    /**
     * Renvoie la cle de traduction du nom de cette phase.
     *
     * @return la cle du nom de la phase
     */
    @Override
    public String nomCle() {
        return "phase.actions_differees";
    }
}
