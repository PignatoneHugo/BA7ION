package Modele.evenement;

/**
 * Filon d'or : un mineur decouvre un filon. Evenement positif, peu
 * importe le choix (juste de la saveur).
 */
public class FilonDor extends Evenement {

    public FilonDor() {
        super("Filon d'or !",
                "Un mineur a decouvert un filon exceptionnel. Comment l'exploiter ?");

        ajouterChoix(new Choix(
                "Exploiter immediatement (+400 or, +5 moral)",
                new EffetSimple(400, 0, 5)));

        ajouterChoix(new Choix(
                "Investir dans le materiel (+250 or, +10 moral)",
                new EffetSimple(250, 0, 10)));
    }
}
