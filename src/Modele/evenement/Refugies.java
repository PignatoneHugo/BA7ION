package Modele.evenement;

import java.util.Random;

import Modele.economie.Ressource;
import Modele.population.Role;
import Modele.royaume.Royaume;

/**
 * Refugies : un groupe d'habitants demande l'asile. Les accueillir augmente
 * la population mais coute en nourriture ; les refuser blesse le moral.
 */
public class Refugies extends Evenement {

    public Refugies() {
        super("evenement.refugies.titre", "evenement.refugies.description");

        ajouterChoix(new Choix(
                "evenement.refugies.choix.accueillir",
                new EffetAccueillir()));
        ajouterChoix(new Choix(
                "evenement.refugies.choix.refuser",
                new EffetSimple(0, 0, -8)));
    }

    private static class EffetAccueillir implements EffetEvenement {
        @Override
        public void appliquer(Royaume r, Random a) {
            r.population().ajouterInactifs(3);
            r.tresor().retirer(Ressource.NOURRITURE, 30);
            r.population().reaffecter(Role.INACTIF, Role.INACTIF, 0); // no-op
            r.moral().ajuster(3);
        }
        @Override
        public boolean peutEtreApplique(Royaume r) {
            return r.tresor().contient(Ressource.NOURRITURE, 30);
        }
    }
}
