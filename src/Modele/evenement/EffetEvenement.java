package Modele.evenement;

import Modele.royaume.Royaume;

/**
 * Effet appliquable sur un royaume quand le joueur choisit une option
 * d'un evenement. Une implementation = un comportement (ex : "perdre X or").
 */
public interface EffetEvenement {

    /** Applique l'effet sur le royaume. */
    void appliquer(Royaume royaume);
}
