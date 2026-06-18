package Modele.evenement;

import java.util.Random;

import Modele.economie.Ressource;
import Modele.population.Role;
import Modele.royaume.Royaume;

/** Refugies : les accueillir (+population, -nourriture) ou les refuser (-moral). */
public class Refugies extends Evenement {

    /**
     * Cree l'evenement Refugies avec ses deux choix.
     */
    public Refugies() {
        super("Refugies",
                "Un groupe d'habitants fuit la guerre voisine et demande"
                + " l'asile dans votre royaume.");

        ajouterChoix(new Choix(
                "Les accueillir (+3 habitants, -30 nourriture, +3 moral)",
                new EffetAccueillir()));
        ajouterChoix(new Choix(
                "Les refuser (-8 moral)",
                new EffetSimple(0, 0, -8)));
    }

    private static class EffetAccueillir implements EffetEvenement {
        @Override
        public void appliquer(Royaume royaume, Random aleatoire) {
            royaume.population().ajouterInactifs(3);
            royaume.tresor().retirer(Ressource.NOURRITURE, 30);
            royaume.population().reaffecter(Role.INACTIF, Role.INACTIF, 0); // no-op
            royaume.moral().ajuster(3);
        }
        @Override
        public boolean peutEtreApplique(Royaume royaume) {
            return royaume.tresor().contient(Ressource.NOURRITURE, 30);
        }
    }
}
