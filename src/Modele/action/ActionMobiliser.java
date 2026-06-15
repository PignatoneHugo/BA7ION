package Modele.action;

import Modele.economie.Ressource;
import Modele.infrastructure.Batiment;
import Modele.infrastructure.TypeBatiment;
import Modele.militaire.TypeUnite;
import Modele.population.Role;
import Modele.royaume.Royaume;

import config.Equilibrage;

// Equipe des recrues en soldats combattants et paie le cout en or.
public class ActionMobiliser implements Action {

    private final TypeUnite type;
    private final int effectif;

    public ActionMobiliser(TypeUnite type, int effectif) {
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
        int coutOr = this.effectif * Equilibrage.COUT_OR_PAR_SOLDAT;
        int recruesNecessaires = this.effectif * Equilibrage.HABITANTS_PAR_SOLDAT;

        if (!royaume.tresor().contient(Ressource.OR, coutOr)) {
            return false;
        }
        if (royaume.population().effectif(Role.SOLDAT) < recruesNecessaires) {
            return false;
        }
        // la caserne doit avoir le niveau requis
        Batiment caserne = royaume.batiment(TypeBatiment.CASERNE);
        if (caserne == null
                || caserne.niveau() < this.type.niveauCaserneRequis()) {
            return false;
        }
        return true;
    }

    @Override
    public void executer(Royaume royaume) {
        int coutOr = this.effectif * Equilibrage.COUT_OR_PAR_SOLDAT;
        int recruesNecessaires = this.effectif * Equilibrage.HABITANTS_PAR_SOLDAT;

        royaume.tresor().retirer(Ressource.OR, coutOr);
        // la recrue quitte la population et rejoint l'armee
        royaume.population().reaffecter(Role.SOLDAT, Role.INACTIF, recruesNecessaires);
        royaume.population().retirerInactifs(recruesNecessaires);
        royaume.armee().recruter(this.type, this.effectif);
    }

    @Override
    public String description() {
        return "action.mobiliser." + this.type.name().toLowerCase();
    }
}
