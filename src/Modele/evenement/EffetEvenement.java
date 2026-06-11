package Modele.evenement;

import java.util.Random;

import Modele.royaume.Royaume;

/**
 * Effet appliquable sur un royaume quand le joueur choisit une option
 * d'un evenement. Une implementation = un comportement (ex : "perdre X or").
 *
 * Le Random est fourni par la Partie pour les effets qui ont besoin d'alea
 * (par exemple le tirage des victimes lors de pertes humaines).
 */
public interface EffetEvenement {

    /** Applique l'effet sur le royaume. */
    void appliquer(Royaume royaume, Random aleatoire);

    /**
     * True si le royaume a les ressources requises pour cet effet.
     * Par defaut true (effet sans cout). Les effets payants l'override.
     */
    default boolean peutEtreApplique(Royaume royaume) {
        return true;
    }
}
