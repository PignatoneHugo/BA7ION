package Modele.partie.etat;

import Modele.evenement.Evenement;
import Modele.evenement.GrenouilleEmpoisonnee;
import Modele.evenement.TirageEvenement;
import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.partie.Partie;

import config.Equilibrage;

// Phase qui peut declencher un evenement aleatoire (sauf au tour 1).
public class EtatEvenement implements EtatTour {

    /**
     * Peut declencher un evenement aleatoire ou scripte (sauf au tour 1).
     *
     * @param partie la partie sur laquelle agir
     */
    @Override
    public void executer(Partie partie) {
        if (!partie.enAttenteEvenement()) {
            if (estTourGrenouilleEmpoisonnee(partie)) {
                // Evenement scripte : une seule fois.
                partie.marquerGrenouilleEmpoisonneeDeclenchee();
                partie.declencherEvenement(new GrenouilleEmpoisonnee());
            } else if (partie.numeroTour() > 1
                    && partie.aleatoire().nextDouble() < Equilibrage.PROBABILITE_EVENEMENT_PAR_TOUR) {
                Evenement evenement = TirageEvenement.tirer(partie.aleatoire());
                partie.declencherEvenement(evenement);
            }
        }
        partie.notifier(new Notification(TypeNotification.PHASE_CHANGEE, this.nomCle()));
    }

    // True si la grenouille doit se declencher ce tour.
    private boolean estTourGrenouilleEmpoisonnee(Partie partie) {
        return partie.numeroTour() == Equilibrage.TOUR_GRENOUILLE_EMPOISONNEE
                && !partie.grenouilleEmpoisonneeDeclenchee();
    }

    /**
     * Renvoie la phase suivante, la fin du tour.
     *
     * @return la phase de fin de tour
     */
    @Override
    public EtatTour suivant() {
        return new EtatFinTour();
    }

    /**
     * Renvoie la cle de traduction du nom de cette phase.
     *
     * @return la cle du nom de la phase
     */
    @Override
    public String nomCle() {
        return "phase.evenement";
    }
}
