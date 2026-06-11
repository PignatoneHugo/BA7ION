package Modele.action;

import Modele.combat.Bataille;
import Modele.militaire.PostureCombat;
import Modele.royaume.Royaume;

/**
 * Action qui declare une attaque contre un autre royaume. La bataille est
 * effectivement resolue plus tard dans la phase EtatCombatsSubis / Offensifs
 * via ResolveurCombat.
 *
 * L'action ajoute juste une Bataille en attente dans le royaume attaquant ;
 * c'est l'etat de combat qui la transmettra a la Partie.
 */
public class ActionAttaquer implements Action {

    private final Royaume cible;
    private final PostureCombat posture;

    public ActionAttaquer(Royaume cible) {
        this(cible, PostureCombat.ATTAQUE);
    }

    public ActionAttaquer(Royaume cible, PostureCombat posture) {
        if (cible == null) {
            throw new IllegalArgumentException("La cible ne peut pas etre null.");
        }
        this.cible = cible;
        this.posture = posture != null ? posture : PostureCombat.ATTAQUE;
    }

    public Royaume cible() {
        return this.cible;
    }

    public PostureCombat posture() {
        return this.posture;
    }

    @Override
    public boolean estExecutable(Royaume royaume) {
        if (royaume.armee().estVide() || royaume == this.cible) {
            return false;
        }
        // Une seule attaque par cible par tour.
        if (royaume.aAttaquePlanifieeContre(this.cible)) {
            return false;
        }
        // Cible eliminee : pas d'interet.
        if (this.cible.population().total() <= 0) {
            return false;
        }
        return true;
    }

    @Override
    public void executer(Royaume royaume) {
        Bataille bataille = new Bataille(royaume, this.cible, this.posture);
        royaume.ajouterBatailleOffensive(bataille);
    }

    @Override
    public String description() {
        return "action.attaquer";
    }
}
