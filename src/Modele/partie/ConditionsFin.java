package Modele.partie;

import Modele.economie.Ressource;
import Modele.royaume.Royaume;

import config.Equilibrage;

// Regarde si la partie est gagnee, perdue ou en cours.
public final class ConditionsFin {

    private ConditionsFin() {
    }

    public enum Etat {
        EN_COURS,
        VICTOIRE,
        DEFAITE
    }

    /**
     * Evalue si la partie est gagnee, perdue ou toujours en cours.
     *
     * @param partie la partie a evaluer
     * @return l'etat de fin de partie (en cours, victoire ou defaite)
     */
    public static Etat evaluer(Partie partie) {
        Royaume joueur = partie.joueur();

        // Defaite si plus de population ou moral au plancher.
        if (joueur.population().total() <= Equilibrage.POPULATION_MIN_DEFAITE) {
            return Etat.DEFAITE;
        }
        if (joueur.moral().valeur() <= Equilibrage.MORAL_MIN_DEFAITE) {
            return Etat.DEFAITE;
        }

        // Victoire par l'or.
        if (joueur.tresor().quantite(Ressource.OR)
                >= Equilibrage.OR_VICTOIRE_PROSPERITE) {
            return Etat.VICTOIRE;
        }

        // Victoire si tous les bots sont elimines.
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

        // Tour max atteint.
        if (partie.numeroTour() >= Equilibrage.TOUR_MAX) {
            return Etat.DEFAITE;
        }

        return Etat.EN_COURS;
    }
}
