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

    /**
     * Cree une action de mobilisation d'un type de soldats.
     *
     * @param type le type de soldats a mobiliser
     * @param effectif le nombre de soldats a mobiliser
     * @throws IllegalArgumentException si le type est null ou l'effectif n'est pas positif
     */
    public ActionMobiliser(TypeUnite type, int effectif) {
        if (type == null || effectif <= 0) {
            throw new IllegalArgumentException("Type non null et effectif > 0 requis.");
        }
        this.type = type;
        this.effectif = effectif;
    }

    /**
     * Renvoie le type de soldats a mobiliser.
     *
     * @return le type de soldats
     */
    public TypeUnite type() {
        return this.type;
    }

    /**
     * Renvoie le nombre de soldats a mobiliser.
     *
     * @return l'effectif a mobiliser
     */
    public int effectif() {
        return this.effectif;
    }

    /**
     * Verifie l'or, les recrues disponibles et le niveau de la caserne.
     *
     * @param royaume le royaume concerne
     * @return true si la mobilisation est possible
     */
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

    /**
     * Paie le cout en or et transforme les recrues en soldats combattants.
     *
     * @param royaume le royaume concerne
     */
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

    /**
     * Renvoie l'identifiant texte de l'action.
     *
     * @return la description de l'action
     */
    @Override
    public String description() {
        return "action.mobiliser." + this.type.name().toLowerCase();
    }
}
