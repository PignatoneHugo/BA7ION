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

    /**
     * Resout les batailles ou le joueur attaque les bots, si les combats sont ouverts.
     *
     * @param partie la partie sur laquelle agir
     */
    @Override
    public void executer(Partie partie) {
        // Rien tant que les combats ne sont pas ouverts.
        if (partie.combatsAutorises()) {
            Royaume joueur = partie.joueur();
            List<Bataille> aResoudre = new ArrayList<>(joueur.bataillesOffensives());
            for (Bataille bataille : aResoudre) {
                EffetsCombat.appliquer(bataille, partie);
            }
            joueur.viderBataillesOffensives();
        }
        partie.notifier(new Notification(TypeNotification.PHASE_CHANGEE, this.nomCle()));
    }

    /**
     * Renvoie la phase suivante, le tour des bots.
     *
     * @return la phase du tour de l'IA
     */
    @Override
    public EtatTour suivant() {
        return new EtatTourIA();
    }

    /**
     * Renvoie la cle de traduction du nom de cette phase.
     *
     * @return la cle du nom de la phase
     */
    @Override
    public String nomCle() {
        return "phase.combats_offensifs";
    }
}
