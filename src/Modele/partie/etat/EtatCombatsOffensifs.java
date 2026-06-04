package Modele.partie.etat;

import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.partie.Partie;

/**
 * Phase de resolution des combats offensifs (attaques lancees par le joueur).
 *
 * Au Sprint 2 : placeholder, le joueur n'a pas encore d'interface pour
 * declarer une attaque. La logique complete sera branchee au Sprint 3.
 */
public class EtatCombatsOffensifs implements EtatTour {

    @Override
    public void executer(Partie partie) {
        // Rien a faire au Sprint 2 : pas d'attaque planifiee.
        partie.notifier(new Notification(TypeNotification.PHASE_CHANGEE, this.nomCle()));
    }

    @Override
    public EtatTour suivant() {
        return new EtatTourIA();
    }

    @Override
    public String nomCle() {
        return "phase.combats_offensifs";
    }
}
