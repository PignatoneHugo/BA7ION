package Modele.evenement;

/** Une maladie frappe le royaume : trois choix, chacun avec son compromis. */
public class Epidemie extends Evenement {

    /**
     * Cree l'evenement Epidemie avec ses trois choix.
     */
    public Epidemie() {
        super("Epidemie !",
                "Une maladie virulente se propage dans le royaume. Plusieurs"
                + " habitants sont malades. Que decidez-vous ?");

        ajouterChoix(new Choix(
                "Confinement strict (-50 or, -5 moral)",
                new EffetSimple(-50, 0, -5)));

        ajouterChoix(new Choix(
                "Soigner aux frais du royaume (-200 or, 2 morts, -2 moral)",
                new EffetSimple(-200, 2, -2)));

        ajouterChoix(new Choix(
                "Ignorer (5 morts, -10 moral)",
                new EffetSimple(0, 5, -10)));
    }
}
