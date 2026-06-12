package Modele.evenement;

import java.util.Random;

import Modele.economie.Ressource;
import Modele.royaume.Royaume;

/**
 * Attaque de brigands : des pillards s'attaquent au royaume. Soit on les
 * combat (risque de pertes), soit on paie une rancon, soit on subit
 * passivement.
 */
public class AttaqueBrigands extends Evenement {

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
        public void appliquer(Royaume r, Random a) {
            // Combat improvise : pertes humaines aleatoires (0 a 3).
            int pertes = a.nextInt(4);
            if (pertes > 0) {
                r.population().retirerHabitants(pertes, a);
            }
            r.moral().ajuster(2);
        }
    }

    private static class EffetSubir implements EffetEvenement {
        @Override
        public void appliquer(Royaume r, Random a) {
            r.tresor().retirer(Ressource.OR, 50);
            r.tresor().retirer(Ressource.NOURRITURE, 30);
            r.moral().ajuster(-5);
        }
        @Override
        public boolean peutEtreApplique(Royaume r) {
            return r.tresor().contient(Ressource.OR, 50)
                    && r.tresor().contient(Ressource.NOURRITURE, 30);
        }
    }
}
