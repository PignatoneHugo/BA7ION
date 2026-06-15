package Modele.partie.etat;

import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.partie.ConditionsFin;
import Modele.partie.Partie;

// Derniere phase : incremente le tour et verifie la fin de partie.
public class EtatFinTour implements EtatTour {

    @Override
    public void executer(Partie partie) {
        partie.incrementerTour();

        ConditionsFin.Etat etat = ConditionsFin.evaluer(partie);
        if (etat == ConditionsFin.Etat.VICTOIRE) {
            partie.notifier(new Notification(TypeNotification.PARTIE_GAGNEE,
                    partie.numeroTour()));
        } else if (etat == ConditionsFin.Etat.DEFAITE) {
            partie.notifier(new Notification(TypeNotification.PARTIE_PERDUE,
                    partie.numeroTour()));
        }

        partie.notifier(new Notification(TypeNotification.TOUR_TERMINE,
                partie.numeroTour()));
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
