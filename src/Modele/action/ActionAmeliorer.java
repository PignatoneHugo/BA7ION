package Modele.action;

import Modele.infrastructure.Batiment;
import Modele.infrastructure.TypeBatiment;
import Modele.royaume.Royaume;

/**
 * Action qui demarre un chantier d'amelioration sur un batiment.
 *
 * Le cout en ressources est retire par le controleur au moment de la
 * planification (pas dans executer()). Si l'action est annulee avant
 * execution, le controleur rembourse. Cela evite que le joueur planifie
 * plus d'ameliorations qu'il ne peut payer.
 *
 * estExecutable() verifie donc juste que le batiment peut etre ameliore
 * (pas au niveau max, pas deja en chantier).
 */
public class ActionAmeliorer implements Action {

    private final TypeBatiment type;

    public ActionAmeliorer(TypeBatiment type) {
        if (type == null) {
            throw new IllegalArgumentException("Le type de batiment ne peut pas etre null.");
        }
        this.type = type;
    }

    public TypeBatiment type() {
        return this.type;
    }

    @Override
    public boolean estExecutable(Royaume royaume) {
        Batiment b = royaume.batiment(this.type);
        return b != null && b.peutEtreAmeliore();
    }

    @Override
    public void executer(Royaume royaume) {
        Batiment b = royaume.batiment(this.type);
        b.demarrerChantier();
    }

    @Override
    public String description() {
        return "action.ameliorer." + this.type.name().toLowerCase();
    }
}
