package Modele.action;

import Modele.infrastructure.Batiment;
import Modele.infrastructure.TypeBatiment;
import Modele.royaume.Royaume;

// Lance un chantier d'amelioration sur un batiment.
// Le cout est paye a la planification par le controleur, pas ici.
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
