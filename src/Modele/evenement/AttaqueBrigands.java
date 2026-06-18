package Modele.evenement;

import java.util.Random;

import Modele.economie.Ressource;
import Modele.royaume.Royaume;

/** Brigands : les combattre, payer une rancon ou subir. */
public class AttaqueBrigands extends Evenement {

    /**
     * Cree l'evenement Attaque de brigands avec ses trois choix.
     */
    public AttaqueBrigands() {
        super("Attaque de brigands !",
                "Des pillards rodent autour du royaume. Comment reagir ?");

        ajouterChoix(new Choix(
                "Les combattre (0 a 3 morts aleatoires, +2 moral)",
                new EffetCombattre()));
        ajouterChoix(new Choix(
                "Payer une rancon (-150 or)",
                new EffetSimple(-150, 0, 0)));
        ajouterChoix(new Choix(
                "Laisser faire (-50 or, -30 nourriture, -5 moral)",
                new EffetSubir()));
    }

    private static class EffetCombattre implements EffetEvenement {
        @Override
        public void appliquer(Royaume royaume, Random aleatoire) {
            // 0 a 3 morts au hasard
            int pertes = aleatoire.nextInt(4);
            if (pertes > 0) {
                royaume.population().retirerHabitants(pertes, aleatoire);
            }
            royaume.moral().ajuster(2);
        }
    }

    private static class EffetSubir implements EffetEvenement {
        @Override
        public void appliquer(Royaume royaume, Random aleatoire) {
            royaume.tresor().retirer(Ressource.OR, 50);
            royaume.tresor().retirer(Ressource.NOURRITURE, 30);
            royaume.moral().ajuster(-5);
        }
        @Override
        public boolean peutEtreApplique(Royaume royaume) {
            return royaume.tresor().contient(Ressource.OR, 50)
                    && royaume.tresor().contient(Ressource.NOURRITURE, 30);
        }
    }
}
