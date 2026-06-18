package Modele.partie.etat;

import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.partie.Partie;

// Phase d'attente : le joueur planifie, rien a faire ici.
public class EtatPlanification implements EtatTour {

    /**
     * Signale le passage en phase de planification, sans autre traitement.
     *
     * @param partie la partie sur laquelle agir
     */
    @Override
    public void executer(Partie partie) {
        partie.notifier(new Notification(TypeNotification.PHASE_CHANGEE, this.nomCle()));
    }

    /**
     * Renvoie la phase suivante, la production.
     *
     * @return la phase de production
     */
    @Override
    public EtatTour suivant() {
        return new EtatProduction();
    }

    /**
     * Renvoie la cle de traduction du nom de cette phase.
     *
     * @return la cle du nom de la phase
     */
    @Override
    public String nomCle() {
        return "phase.planification";
    }
}
