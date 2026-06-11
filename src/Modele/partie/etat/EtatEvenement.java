package Modele.partie.etat;

import Modele.evenement.Evenement;
import Modele.evenement.TirageEvenement;
import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.partie.Partie;

import config.Equilibrage;

/**
 * Phase qui peut declencher un evenement aleatoire.
 *
 * A chaque tour (sauf le tour 1, ou on laisse le joueur prendre ses marques),
 * on tire un nombre aleatoire : s'il est inferieur a la probabilite definie
 * dans Equilibrage, un evenement est tire dans le CatalogueEvenements
 * (selon les poids relatifs) et stocke dans la Partie comme "en attente".
 *
 * Le ControleurPartie detecte l'etat "en attente", ouvre le dialogue modal
 * et reprend la chaine apres reponse du joueur.
 */
public class EtatEvenement implements EtatTour {

    @Override
    public void executer(Partie partie) {
        boolean peutTirer = partie.numeroTour() > 1 && !partie.enAttenteEvenement();
        if (peutTirer && partie.aleatoire().nextDouble() < Equilibrage.PROBABILITE_EVENEMENT_PAR_TOUR) {
            Evenement evenement = TirageEvenement.tirer(partie.aleatoire());
            partie.declencherEvenement(evenement);
        }
        partie.notifier(new Notification(TypeNotification.PHASE_CHANGEE, this.nomCle()));
    }

    @Override
    public EtatTour suivant() {
        return new EtatFinTour();
    }

    @Override
    public String nomCle() {
        return "phase.evenement";
    }
}
