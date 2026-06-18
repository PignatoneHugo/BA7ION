package Modele.action;

import Modele.economie.Ressource;
import Modele.infrastructure.Marche;
import Modele.infrastructure.TypeBatiment;
import Modele.royaume.Royaume;

// Echange une ressource contre une autre au taux du Marche.
public class ActionEchanger implements Action {

    private final Ressource source;
    private final Ressource cible;
    private final int montantSource;

    /**
     * Cree un echange d'une ressource source vers une ressource cible.
     *
     * @param source la ressource a donner
     * @param cible la ressource a recevoir
     * @param montantSource la quantite de ressource source a echanger
     * @throws IllegalArgumentException si source ou cible est null, si elles sont identiques ou si le montant n'est pas positif
     */
    public ActionEchanger(Ressource source, Ressource cible, int montantSource) {
        if (source == null || cible == null) {
            throw new IllegalArgumentException("Source et cible requises.");
        }
        if (source == cible) {
            throw new IllegalArgumentException("Source et cible doivent differer.");
        }
        if (montantSource <= 0) {
            throw new IllegalArgumentException("Le montant doit etre positif.");
        }
        this.source = source;
        this.cible = cible;
        this.montantSource = montantSource;
    }

    /**
     * Renvoie la ressource source de l'echange.
     *
     * @return la ressource source
     */
    public Ressource source() {
        return this.source;
    }

    /**
     * Renvoie la ressource cible de l'echange.
     *
     * @return la ressource cible
     */
    public Ressource cible() {
        return this.cible;
    }

    /**
     * Renvoie la quantite de ressource source a echanger.
     *
     * @return le montant source
     */
    public int montantSource() {
        return this.montantSource;
    }

    /**
     * Verifie qu'il y a un marche, assez de ressource source et un gain d'au moins une unite.
     *
     * @param royaume le royaume concerne
     * @return true si l'echange est possible
     */
    @Override
    public boolean estExecutable(Royaume royaume) {
        // il faut un Marche et que l'echange rapporte au moins 1 unite
        Marche marche = (Marche) royaume.batiment(TypeBatiment.MARCHE);
        if (marche == null) {
            return false;
        }
        if (!royaume.tresor().contient(this.source, this.montantSource)) {
            return false;
        }
        return marche.quantiteRecue(this.montantSource) > 0;
    }

    /**
     * Retire la ressource source et ajoute la ressource cible recue au taux du marche.
     *
     * @param royaume le royaume concerne
     */
    @Override
    public void executer(Royaume royaume) {
        Marche marche = (Marche) royaume.batiment(TypeBatiment.MARCHE);
        int recus = marche.quantiteRecue(this.montantSource);
        royaume.tresor().retirer(this.source, this.montantSource);
        royaume.tresor().ajouter(this.cible, recus);
    }

    /**
     * Renvoie l'identifiant texte de l'action.
     *
     * @return la description de l'action
     */
    @Override
    public String description() {
        return "action.echanger." + this.source.name().toLowerCase()
                + "_vers_" + this.cible.name().toLowerCase();
    }
}
