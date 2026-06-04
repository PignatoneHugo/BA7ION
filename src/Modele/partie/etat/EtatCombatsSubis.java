package Modele.partie.etat;

import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.partie.Partie;

/**
 * Phase de resolution des combats subis (attaques venant des bots).
 *
 * Au Sprint 2 : placeholder, aucune attaque n'est planifiee. La logique
 * complete sera branchee au Sprint 3 quand l'IA des bots saura attaquer
 * et que le ResolveurCombat sera utilise depuis le cycle de tour.
 */
public class EtatCombatsSubis implements EtatTour {

    @Override
    public void executer(Partie partie) {
        // Rien a faire au Sprint 2 : pas de combats a resoudre.
        partie.notifier(new Notification(TypeNotification.PHASE_CHANGEE, this.nomCle()));
    }

    @Override
    public EtatTour suivant() {
        return new EtatCombatsOffensifs();
    }

    @Override
    public String nomCle() {
        return "phase.combats_subis";
    }
}
