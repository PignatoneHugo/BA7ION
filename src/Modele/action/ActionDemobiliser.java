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

    /**
     * Cree une action de demobilisation d'un type de soldats.
     *
     * @param type le type de soldats a demobiliser
     * @param effectif le nombre de soldats a demobiliser
     * @throws IllegalArgumentException si le type est null ou l'effectif n'est pas positif
     */
    public ActionDemobiliser(TypeUnite type, int effectif) {
        if (type == null || effectif <= 0) {
            throw new IllegalArgumentException("Type non null et effectif > 0 requis.");
        }
        this.type = type;
        this.effectif = effectif;
    }

    /**
     * Renvoie le type de soldats a demobiliser.
     *
     * @return le type de soldats
     */
    public TypeUnite type() {
        return this.type;
    }

    /**
     * Renvoie le nombre de soldats a demobiliser.
     *
     * @return l'effectif a demobiliser
     */
    public int effectif() {
        return this.effectif;
    }

    /**
     * Verifie que l'armee a assez de soldats de ce type.
     *
     * @param royaume le royaume concerne
     * @return true si la demobilisation est possible
     */
    @Override
    public boolean estExecutable(Royaume royaume) {
        return royaume.armee().effectifParType(this.type) >= this.effectif;
    }

    /**
     * Retire les soldats de l'armee et les remet en recrues.
     *
     * @param royaume le royaume concerne
     */
    @Override
    public void executer(Royaume royaume) {
        int retire = royaume.armee().retirer(this.type, this.effectif);
        int recrues = retire * Equilibrage.HABITANTS_PAR_SOLDAT;
        // le soldat redevient une recrue
        royaume.population().ajouterInactifs(recrues);
        royaume.population().reaffecter(Role.INACTIF, Role.SOLDAT, recrues);
    }

    /**
     * Renvoie l'identifiant texte de l'action.
     *
     * @return la description de l'action
     */
    @Override
    public String description() {
        return "action.demobiliser." + this.type.name().toLowerCase();
    }
}
