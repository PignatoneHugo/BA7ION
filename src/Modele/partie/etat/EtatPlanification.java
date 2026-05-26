package Modele.partie.etat;

import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.partie.Partie;

/**
 * Phase pendant laquelle le joueur affecte sa population, planifie ses actions
 * (Action a executer en differe) et ajuste sa politique. Aucune resolution
 * n'a lieu : on attend que le joueur clique "Fin de tour".
 *
 * En sortie de cette phase, on enchaine sur EtatProduction.
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
