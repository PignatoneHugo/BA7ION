package Modele.evenement;

import java.util.Random;

import Modele.royaume.Royaume;

/** Effet d'un choix sur le royaume. Le Random sert pour les effets aleatoires. */
public interface EffetEvenement {

    /**
     * Applique l'effet sur le royaume.
     *
     * @param royaume le royaume modifie
     * @param aleatoire source de hasard pour les effets aleatoires
     */
    void appliquer(Royaume royaume, Random aleatoire);

    /**
     * Indique si l'effet peut etre applique au royaume.
     *
     * @param royaume le royaume concerne
     * @return vrai par defaut ; les effets payants le redefinissent
     */
    default boolean peutEtreApplique(Royaume royaume) {
        return true;
    }
}
