package Modele.partie.etat;

import java.util.ArrayList;
import java.util.List;

import Modele.combat.Bataille;
import Modele.combat.EffetsCombat;
import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.partie.Partie;
import Modele.royaume.Royaume;

// Phase ou le joueur attaque les bots.
public class EtatCombatsOffensifs implements EtatTour {

    @Override
    public void executer(Partie partie) {
        // Rien tant que les combats ne sont pas ouverts.
        if (partie.combatsAutorises()) {
            Royaume joueur = partie.joueur();
            List<Bataille> aResoudre = new ArrayList<>(joueur.bataillesOffensives());
            for (Bataille b : aResoudre) {
                EffetsCombat.appliquer(b, partie);
            }
            joueur.viderBataillesOffensives();
        }
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
