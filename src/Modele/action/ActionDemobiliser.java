package Modele.action;

import Modele.militaire.TypeUnite;
import Modele.population.Role;
import Modele.royaume.Royaume;

import config.Equilibrage;

/**
 * Action symetrique de {@link ActionMobiliser} : retire des soldats du
 * type donne et les renvoie en recrues (Role.SOLDAT). Pas de
 * remboursement d'or -- l'or a deja ete depense pour l'equipement.
 * La recrue peut ensuite etre re-equipee, ou ramenee dans le pool des
 * inactifs via l'onglet Economie (bouton - de la ligne Soldat).
 */
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
        // L'unite demobilisee redevient une recrue (Role.SOLDAT).
        // Population.ajouterInactifs puis reaffectation INACTIF -> SOLDAT.
        royaume.population().ajouterInactifs(recrues);
        royaume.population().reaffecter(Role.INACTIF, Role.SOLDAT, recrues);
    }

    @Override
    public String description() {
        return "action.demobiliser." + this.type.name().toLowerCase();
    }
}
