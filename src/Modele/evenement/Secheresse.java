package Modele.evenement;

import java.util.Random;

import Modele.economie.Ressource;
import Modele.royaume.Royaume;

/** Secheresse : payer de l'aide, serrer les ceintures ou tout abandonner. */
public class Secheresse extends Evenement {

    /**
     * Cree l'evenement Secheresse avec ses trois choix.
     */
    public Secheresse() {
        super("Secheresse",
                "Une vague de chaleur frappe les terres. Les recoltes sont"
                + " compromises.");

        ajouterChoix(new Choix(
                "Demander de l'aide aux voisins (-150 or, -20 nourriture, -2 moral)",
                new EffetAide()));
        ajouterChoix(new Choix(
                "Serrer les ceintures (-80 nourriture, -5 moral)",
                new EffetSubir()));
        // choix toujours dispo (gros malus moral) si plus d'or ni de nourriture
        ajouterChoix(new Choix(
                "Abandonner le royaume a son sort (-12 moral)",
                new EffetSimple(0, 0, -12)));
    }

    private static class EffetAide implements EffetEvenement {
        @Override
        public void appliquer(Royaume royaume, Random aleatoire) {
            royaume.tresor().retirer(Ressource.OR, 150);
            royaume.tresor().retirer(Ressource.NOURRITURE, 20);
            royaume.moral().ajuster(-2);
        }
        @Override
        public boolean peutEtreApplique(Royaume royaume) {
            return royaume.tresor().contient(Ressource.OR, 150)
                    && royaume.tresor().contient(Ressource.NOURRITURE, 20);
        }
    }

    private static class EffetSubir implements EffetEvenement {
        @Override
        public void appliquer(Royaume royaume, Random aleatoire) {
            royaume.tresor().retirer(Ressource.NOURRITURE, 80);
            royaume.moral().ajuster(-5);
        }
        @Override
        public boolean peutEtreApplique(Royaume royaume) {
            return royaume.tresor().contient(Ressource.NOURRITURE, 80);
        }
    }
}
