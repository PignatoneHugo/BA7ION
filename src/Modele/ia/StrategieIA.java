package Modele.ia;

import Modele.partie.Partie;
import Modele.royaume.Royaume;

// Strategie de jeu d'un bot.
public interface StrategieIA {

    /**
     * Joue le tour du bot.
     *
     * @param bot le royaume controle par l'IA
     * @param partie la partie en cours
     */
    void jouerTour(Royaume bot, Partie partie);
}
