package Modele.partie.etat;

import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.partie.Partie;

/**
 * Derniere phase du tour : on incremente le compteur de tour, on declenche
 * eventuellement l'autosauvegarde (Sprint 2+), et on retourne en planification.
 *
 * Au Sprint 1, on declenche aussi un evenement test code en dur au tour 2
 * (cf. plan d'architecture section 8) pour valider le cablage du dialogue
 * d'evenement. Cet evenement test sera remplace par le vrai systeme
 * d'evenements aleatoires en Sprint 2+.
 */
public class EtatFinTour implements EtatTour {

    @Override
    public void executer(Partie partie) {
        partie.incrementerTour();
        partie.notifier(new Notification(TypeNotification.TOUR_TERMINE, partie.numeroTour()));
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
