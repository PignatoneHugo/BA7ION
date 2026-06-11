package Modele.evenement;

/**
 * Filon d'or : un mineur decouvre un filon. Evenement positif, peu
 * importe le choix (juste de la saveur).
 */
public class FilonDor extends Evenement {

    public FilonDor() {
        super("evenement.filon_or.titre", "evenement.filon_or.description");

        // Exploiter immediatement : gain immediat important.
        ajouterChoix(new Choix(
                "evenement.filon_or.choix.exploiter",
                new EffetSimple(400, 0, 5)));

        // Investir dans le materiel : moindre gain mais bonus moral.
        ajouterChoix(new Choix(
                "evenement.filon_or.choix.investir",
                new EffetSimple(250, 0, 10)));
    }
}
