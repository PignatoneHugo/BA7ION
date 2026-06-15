package Modele.partie.etat;

import java.util.ArrayList;
import java.util.List;

import Modele.combat.Bataille;
import Modele.combat.EffetsCombat;
import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.partie.Partie;
import Modele.royaume.Royaume;

// Phase ou les bots attaquent le joueur.
public class EtatCombatsSubis implements EtatTour {

    @Override
    public void executer(Partie partie) {
        // Rien tant que les combats ne sont pas ouverts.
        if (partie.combatsAutorises()) {
            Royaume joueur = partie.joueur();
            for (Royaume bot : partie.bots()) {
                // Copie car on va modifier la liste.
                List<Bataille> aResoudre = new ArrayList<>(bot.bataillesOffensives());
                for (Bataille b : aResoudre) {
                    if (b.defenseur() == joueur) {
                        EffetsCombat.appliquer(b, partie);
                        bot.bataillesOffensives().remove(b);
                    }
                }
            }
        }
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
