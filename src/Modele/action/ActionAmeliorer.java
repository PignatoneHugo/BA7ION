package Modele.action;

import Modele.infrastructure.Batiment;
import Modele.infrastructure.TypeBatiment;
import Modele.royaume.Royaume;

// Lance un chantier d'amelioration sur un batiment.
// Le cout est paye a la planification par le controleur, pas ici.
public class ActionAmeliorer implements Action {

    private final TypeBatiment type;

    /**
     * Cree une action d'amelioration pour un type de batiment.
     *
     * @param type le type de batiment a ameliorer
     * @throws IllegalArgumentException si le type est null
     */
    public ActionAmeliorer(TypeBatiment type) {
        if (type == null) {
            throw new IllegalArgumentException("Le type de batiment ne peut pas etre null.");
        }
        this.type = type;
    }

    /**
     * Renvoie le type de batiment a ameliorer.
     *
     * @return le type de batiment
     */
    public TypeBatiment type() {
        return this.type;
    }

    /**
     * Verifie que le batiment existe et peut etre ameliore.
     *
     * @param royaume le royaume concerne
     * @return true si l'amelioration est possible
     */
    @Override
    public boolean estExecutable(Royaume royaume) {
        Batiment batiment = royaume.batiment(this.type);
        return batiment != null && batiment.peutEtreAmeliore();
    }

    /**
     * Demarre le chantier d'amelioration du batiment.
     *
     * @param royaume le royaume concerne
     */
    @Override
    public void executer(Royaume royaume) {
        Batiment batiment = royaume.batiment(this.type);
        batiment.demarrerChantier();
    }

    /**
     * Renvoie l'identifiant texte de l'action.
     *
     * @return la description de l'action
     */
    @Override
    public String description() {
        return "action.ameliorer." + this.type.name().toLowerCase();
    }
}
