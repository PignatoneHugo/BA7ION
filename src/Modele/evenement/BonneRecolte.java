package Modele.evenement;

/**
 * Bonne recolte : un evenement positif sans choix difficile.
 * Les fermiers ont eu de la chance cette saison.
 */
public class BonneRecolte extends Evenement {

    public BonneRecolte() {
        super("evenement.bonne_recolte.titre", "evenement.bonne_recolte.description");

        ajouterChoix(new Choix(
                "evenement.bonne_recolte.choix.stocker",
                new EffetStocker()));
        ajouterChoix(new Choix(
                "evenement.bonne_recolte.choix.fete",
                new EffetSimple(-50, 0, 10)));
    }

    private static class EffetStocker implements EffetEvenement {
        @Override
        public void appliquer(Modele.royaume.Royaume r, java.util.Random a) {
            r.tresor().ajouter(Modele.economie.Ressource.NOURRITURE, 100);
            r.moral().ajuster(3);
        }
    }
}
