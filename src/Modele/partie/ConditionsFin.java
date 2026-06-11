package Modele.partie;

import Modele.economie.Ressource;
import Modele.royaume.Royaume;

import config.Equilibrage;

/**
 * Verifie les conditions de fin de partie pour le royaume joueur.
 *
 * Conditions de defaite :
 *  - Population totale tombe a 0 (ou en dessous du seuil de defaite)
 *  - Moral tombe en dessous du seuil de defaite
 *
 * Conditions de victoire :
 *  - Or atteint le seuil de victoire par prosperite
 *  - Tous les bots sont elimines (population 0 sur chaque bot)
 *
 * Si aucune condition n'est remplie avant le TOUR_MAX, la partie se
 * termine en match nul.
 */
public final class ConditionsFin {

    private ConditionsFin() {
        // Classe utilitaire.
    }

    public enum Etat {
        EN_COURS,
        VICTOIRE,
        DEFAITE
    }

    /** Evalue l'etat de la partie en se basant sur le royaume joueur. */
    public static Etat evaluer(Partie partie) {
        Royaume joueur = partie.joueur();

        // Defaite : population effondree.
        if (joueur.population().total() <= Equilibrage.POPULATION_MIN_DEFAITE) {
            return Etat.DEFAITE;
        }

        // Defaite : moral effondre.
        if (joueur.moral().valeur() <= Equilibrage.MORAL_MIN_DEFAITE) {
            return Etat.DEFAITE;
        }

        // Victoire : or accumule.
        if (joueur.tresor().quantite(Ressource.OR)
                >= Equilibrage.OR_VICTOIRE_PROSPERITE) {
            return Etat.VICTOIRE;
        }

        // Victoire : tous les bots elimines.
        boolean tousBotsElimines = true;
        for (Royaume bot : partie.bots()) {
            if (bot.population().total() > 0) {
                tousBotsElimines = false;
                break;
            }
        }
        if (tousBotsElimines && !partie.bots().isEmpty()) {
            return Etat.VICTOIRE;
        }

        // Match nul au tour max.
        if (partie.numeroTour() >= Equilibrage.TOUR_MAX) {
            return Etat.DEFAITE;
        }

        return Etat.EN_COURS;
    }
}
