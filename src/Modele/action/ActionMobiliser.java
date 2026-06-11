package Modele.action;

import Modele.economie.Ressource;
import Modele.infrastructure.Batiment;
import Modele.infrastructure.TypeBatiment;
import Modele.militaire.TypeUnite;
import Modele.population.Role;
import Modele.royaume.Royaume;

import config.Equilibrage;

/**
 * Action qui equipe des recrues (Role.SOLDAT) en unites combattantes et
 * paie le cout en or. La recrue est d'abord assignee dans l'onglet
 * Economie (gratuit, juste une reaffectation INACTIF -> SOLDAT), puis
 * equipee ici. Au Sprint 3 : seul TypeUnite.INFANTERIE_LEGERE est
 * recrutable (les autres types viendront avec leurs casernes specialisees).
 */
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
        // Verifie que la Caserne a le niveau requis pour ce type d'unite.
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
        // La recrue quitte le pool de population et devient une unite
        // combattante. On la retire definitivement (les soldats au combat
        // sont comptes dans Armee, plus dans Population).
        royaume.population().reaffecter(Role.SOLDAT, Role.INACTIF, recruesNecessaires);
        royaume.population().retirerInactifs(recruesNecessaires);
        royaume.armee().recruter(this.type, this.effectif);
    }

    @Override
    public String description() {
        return "action.mobiliser." + this.type.name().toLowerCase();
    }
}
