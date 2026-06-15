package Modele.action;

import Modele.militaire.TypeUnite;
import Modele.population.Role;
import Modele.royaume.Royaume;

import config.Equilibrage;

// Inverse de ActionMobiliser : retire des soldats et les remet en recrues.
// Pas de remboursement de l'or.
public class ActionDemobiliser implements Action {

    private final TypeUnite type;
    private final int effectif;

    public ActionDemobiliser(TypeUnite type, int effectif) {
        if (type == null || effectif <= 0) {
            throw new IllegalArgumentException("Type non null et effectif > 0 requis.");
        }
        this.type = type;
        this.effectif = effectif;
    }

    public TypeUnite type() {
        return this.type;
    }

    public int effectif() {
        return this.effectif;
    }

    @Override
    public boolean estExecutable(Royaume royaume) {
        return royaume.armee().effectifParType(this.type) >= this.effectif;
    }

    @Override
    public void executer(Royaume royaume) {
        int retire = royaume.armee().retirer(this.type, this.effectif);
        int recrues = retire * Equilibrage.HABITANTS_PAR_SOLDAT;
        // le soldat redevient une recrue
        royaume.population().ajouterInactifs(recrues);
        royaume.population().reaffecter(Role.INACTIF, Role.SOLDAT, recrues);
    }

    @Override
    public String description() {
        return "action.demobiliser." + this.type.name().toLowerCase();
    }
}
