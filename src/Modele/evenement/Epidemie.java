package Modele.evenement;

/**
 * Evenement Epidemie : une maladie virulente touche le royaume.
 * Trois choix possibles, chacun avec un compromis different entre or,
 * pertes humaines et impact sur le moral.
 */
public class Epidemie extends Evenement {

    public Epidemie() {
        super("evenement.epidemie.titre", "evenement.epidemie.description");

        // Confinement : on paie pour eviter les pertes humaines, moral en baisse.
        ajouterChoix(new Choix(
                "evenement.epidemie.choix.confinement",
                new EffetSimple(-50, 0, -5)));

        // Soigner : on paie cher mais on sauve la majorite, peu de pertes.
        ajouterChoix(new Choix(
                "evenement.epidemie.choix.soigner",
                new EffetSimple(-200, 2, -2)));

        // Ignorer : pas de cout mais beaucoup de morts et moral effondre.
        ajouterChoix(new Choix(
                "evenement.epidemie.choix.ignorer",
                new EffetSimple(0, 5, -10)));
    }
}
