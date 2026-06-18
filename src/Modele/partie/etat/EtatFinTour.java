package Modele.partie.etat;

import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.partie.ConditionsFin;
import Modele.partie.Partie;

// Derniere phase : incremente le tour et verifie la fin de partie.
public class EtatFinTour implements EtatTour {

    /**
     * Incremente le tour et verifie les conditions de fin de partie.
     *
     * @param partie la partie sur laquelle agir
     */
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

    /**
     * Renvoie la phase suivante, la planification du tour d'apres.
     *
     * @return la phase de planification
     */
    @Override
    public EtatTour suivant() {
        return new EtatPlanification();
    }

    /**
     * Renvoie la cle de traduction du nom de cette phase.
     *
     * @return la cle du nom de la phase
     */
    @Override
    public String nomCle() {
        return "phase.fin_tour";
    }
}
