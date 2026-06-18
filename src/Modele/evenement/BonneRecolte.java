package Modele.evenement;

/** Bonne recolte : evenement positif, stocker ou faire la fete. */
public class BonneRecolte extends Evenement {

    /**
     * Cree l'evenement Bonne recolte avec ses deux choix.
     */
    public BonneRecolte() {
        super("Bonne recolte !",
                "Les saisons sont clementes, les fermiers ramenent une"
                + " recolte exceptionnelle.");

        ajouterChoix(new Choix(
                "Constituer des reserves (+100 nourriture, +3 moral)",
                new EffetStocker()));
        ajouterChoix(new Choix(
                "Organiser une grande fete (-50 or, +10 moral)",
                new EffetSimple(-50, 0, 10)));
    }

    private static class EffetStocker implements EffetEvenement {
        @Override
        public void appliquer(Modele.royaume.Royaume royaume, java.util.Random aleatoire) {
            royaume.tresor().ajouter(Modele.economie.Ressource.NOURRITURE, 100);
            royaume.moral().ajuster(3);
        }
    }
}
