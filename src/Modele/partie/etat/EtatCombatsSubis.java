package Modele.partie.etat;

import java.util.ArrayList;
import java.util.List;

import Modele.combat.Bataille;
import Modele.combat.EffetsCombat;
import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.partie.Partie;
import Modele.royaume.Royaume;

/**
 * Phase de resolution des combats subis : les bots attaquent le joueur.
 * Pour chaque bot, on parcourt ses batailles offensives dont la cible est
 * le joueur et on les resout via EffetsCombat (qui applique les pertes
 * militaires, les pertes civiles si defaite defensive et le butin si
 * victoire offensive).
 */
public class EtatCombatsSubis implements EtatTour {

    @Override
    public void executer(Partie partie) {
        Royaume joueur = partie.joueur();
        for (Royaume bot : partie.bots()) {
            // Copie defensive : la liste sera modifiee.
            List<Bataille> aResoudre = new ArrayList<>(bot.bataillesOffensives());
            for (Bataille b : aResoudre) {
                if (b.defenseur() == joueur) {
                    EffetsCombat.appliquer(b, partie);
                    bot.bataillesOffensives().remove(b);
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
