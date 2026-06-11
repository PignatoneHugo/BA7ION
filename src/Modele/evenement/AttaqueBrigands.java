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
        super("evenement.brigands.titre", "evenement.brigands.description");

        ajouterChoix(new Choix(
                "evenement.brigands.choix.combattre",
                new EffetCombattre()));
        ajouterChoix(new Choix(
                "evenement.brigands.choix.payer",
                new EffetSimple(-150, 0, 0)));
        ajouterChoix(new Choix(
                "evenement.brigands.choix.subir",
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
