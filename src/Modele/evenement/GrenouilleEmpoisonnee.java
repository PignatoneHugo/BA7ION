package Modele.evenement;

/**
 * Evenement scripte : la grenouille empoisonnee. Declenche a un tour precis
 * (une seule fois) et marque le debut des combats. Trois choix possibles.
 */
public class GrenouilleEmpoisonnee extends Evenement {

    public GrenouilleEmpoisonnee() {
        super("Grenouille empoisonnee : la guerre approche !",
                "Une etrange grenouille aux couleurs venimeuses a ete trouvee"
                + " dans le puits du village. L'eau est contaminee et les"
                + " habitants tombent malades. Les anciens y voient un funeste"
                + " presage : les royaumes voisins fourbissent leurs armes et"
                + " les hostilites peuvent desormais commencer. Que decidez-vous ?");

        ajouterChoix(new Choix(
                "Faire bouillir l'eau du puits (-30 or, -1 moral)",
                new EffetSimple(-30, 0, -1)));

        ajouterChoix(new Choix(
                "Capturer la grenouille et soigner les malades (-50 or)",
                new EffetSimple(-50, 0, 0)));

        ajouterChoix(new Choix(
                "Ne rien faire (1 mort, -3 moral)",
                new EffetSimple(0, 1, -3)));
    }
}
