package Modele.ia;

import Modele.partie.Partie;
import Modele.royaume.Royaume;

// Strategie de jeu d'un bot.
public interface StrategieIA {

    // Joue le tour du bot.
    void jouerTour(Royaume bot, Partie partie);
}
