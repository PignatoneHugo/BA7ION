package Modele.action;

import Modele.combat.Bataille;
import Modele.militaire.PostureCombat;
import Modele.royaume.Royaume;

// Declare une attaque contre un autre royaume.
// Ajoute juste une bataille en attente, resolue plus tard.
public class ActionAttaquer implements Action {

    private final Royaume cible;
    private final PostureCombat posture;

    /**
     * Cree une attaque contre une cible avec la posture d'attaque par defaut.
     *
     * @param cible le royaume a attaquer
     */
    public ActionAttaquer(Royaume cible) {
        this(cible, PostureCombat.ATTAQUE);
    }

    /**
     * Cree une attaque contre une cible avec une posture donnee.
     *
     * @param cible le royaume a attaquer
     * @param posture la posture de l'attaque
     * @throws IllegalArgumentException si la cible est null
     */
    public ActionAttaquer(Royaume cible, PostureCombat posture) {
        if (cible == null) {
            throw new IllegalArgumentException("La cible ne peut pas etre null.");
        }
        this.cible = cible;
        this.posture = posture != null ? posture : PostureCombat.ATTAQUE;
    }

    /**
     * Renvoie le royaume cible de l'attaque.
     *
     * @return le royaume cible
     */
    public Royaume cible() {
        return this.cible;
    }

    /**
     * Renvoie la posture de l'attaque.
     *
     * @return la posture de combat
     */
    public PostureCombat posture() {
        return this.posture;
    }

    /**
     * Verifie que l'attaque est possible contre la cible.
     *
     * @param royaume le royaume concerne
     * @return true si l'attaque est executable
     */
    @Override
    public boolean estExecutable(Royaume royaume) {
        if (royaume.armee().estVide() || royaume == this.cible) {
            return false;
        }
        // une seule attaque par cible par tour
        if (royaume.aAttaquePlanifieeContre(this.cible)) {
            return false;
        }
        // inutile d'attaquer une cible deja eliminee
        if (this.cible.population().total() <= 0) {
            return false;
        }
        return true;
    }

    /**
     * Ajoute une bataille offensive en attente contre la cible.
     *
     * @param royaume le royaume concerne
     */
    @Override
    public void executer(Royaume royaume) {
        Bataille bataille = new Bataille(royaume, this.cible, this.posture);
        royaume.ajouterBatailleOffensive(bataille);
    }

    /**
     * Renvoie l'identifiant texte de l'action.
     *
     * @return la description de l'action
     */
    @Override
    public String description() {
        return "action.attaquer";
    }
}
