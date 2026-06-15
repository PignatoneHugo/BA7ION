package Modele.evenement;

import java.util.Random;

import Modele.royaume.Royaume;

/** Effet d'un choix sur le royaume. Le Random sert pour les effets aleatoires. */
public interface EffetEvenement {

    void appliquer(Royaume royaume, Random aleatoire);

    // vrai par defaut ; les effets payants le redefinissent
    default boolean peutEtreApplique(Royaume royaume) {
        return true;
    }
}
