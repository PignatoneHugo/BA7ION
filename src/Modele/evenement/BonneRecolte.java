package Modele.evenement;

/** Bonne recolte : evenement positif, stocker ou faire la fete. */
public class BonneRecolte extends Evenement {

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
        public void appliquer(Modele.royaume.Royaume r, java.util.Random a) {
            r.tresor().ajouter(Modele.economie.Ressource.NOURRITURE, 100);
            r.moral().ajuster(3);
        }
    }
}
