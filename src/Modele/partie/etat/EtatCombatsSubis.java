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

    /**
     * Resout les batailles ou les bots attaquent le joueur, si les combats sont ouverts.
     *
     * @param partie la partie sur laquelle agir
     */
    @Override
    public void executer(Partie partie) {
        // Rien tant que les combats ne sont pas ouverts.
        if (partie.combatsAutorises()) {
            Royaume joueur = partie.joueur();
            for (Royaume bot : partie.bots()) {
                // Copie car on va modifier la liste.
                List<Bataille> aResoudre = new ArrayList<>(bot.bataillesOffensives());
                for (Bataille bataille : aResoudre) {
                    if (bataille.defenseur() == joueur) {
                        EffetsCombat.appliquer(bataille, partie);
                        bot.bataillesOffensives().remove(bataille);
                    }
                }
            }
        }
        partie.notifier(new Notification(TypeNotification.PHASE_CHANGEE, this.nomCle()));
    }

    /**
     * Renvoie la phase suivante, les combats offensifs.
     *
     * @return la phase des combats offensifs
     */
    @Override
    public EtatTour suivant() {
        return new EtatCombatsOffensifs();
    }

    /**
     * Renvoie la cle de traduction du nom de cette phase.
     *
     * @return la cle du nom de la phase
     */
    @Override
    public String nomCle() {
        return "phase.combats_subis";
    }
}
