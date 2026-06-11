package Modele.ia;

import Modele.partie.Partie;
import Modele.royaume.Royaume;

/**
 * Strategie de jeu d'un bot. Implementation du pattern Strategy.
 * Chaque bot recoit une StrategieIA et l'utilise pour jouer son tour.
 */
public interface StrategieIA {

    /** Joue le tour du bot : affecte sa population, ameliore, recrute, attaque. */
    void jouerTour(Royaume bot, Partie partie);
}
