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
 * Phase de resolution des combats offensifs : le joueur attaque un bot.
 * On parcourt les batailles offensives du joueur et on les resout via
 * EffetsCombat (qui applique pertes militaires, pertes civiles cote
 * defenseur perdant, et butin cote attaquant vainqueur).
 */
public class EtatCombatsOffensifs implements EtatTour {

    @Override
    public void executer(Partie partie) {
        Royaume joueur = partie.joueur();
        List<Bataille> aResoudre = new ArrayList<>(joueur.bataillesOffensives());
        for (Bataille b : aResoudre) {
            EffetsCombat.appliquer(b, partie);
        }
        joueur.viderBataillesOffensives();
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
