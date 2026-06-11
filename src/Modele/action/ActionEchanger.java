package Modele.action;

import Modele.economie.Ressource;
import Modele.infrastructure.Marche;
import Modele.infrastructure.TypeBatiment;
import Modele.royaume.Royaume;

/**
 * Action immediate qui convertit {@code montantSource} unites de la
 * ressource {@code source} en unites de la ressource {@code cible},
 * au taux defini par le niveau du Marche.
 */
public class ActionEchanger implements Action {

    private final Ressource source;
    private final Ressource cible;
    private final int montantSource;

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

    public Ressource source() {
        return this.source;
    }

    public Ressource cible() {
        return this.cible;
    }

    public int montantSource() {
        return this.montantSource;
    }

    @Override
    public boolean estExecutable(Royaume royaume) {
        // Le Marche est present a la creation du royaume (niveau 1) mais
        // verifions tout de meme. Et l'echange doit produire au moins 1 unite.
        Marche marche = (Marche) royaume.batiment(TypeBatiment.MARCHE);
        if (marche == null) {
            return false;
        }
        if (!royaume.tresor().contient(this.source, this.montantSource)) {
            return false;
        }
        return marche.quantiteRecue(this.montantSource) > 0;
    }

    @Override
    public void executer(Royaume royaume) {
        Marche marche = (Marche) royaume.batiment(TypeBatiment.MARCHE);
        int recus = marche.quantiteRecue(this.montantSource);
        royaume.tresor().retirer(this.source, this.montantSource);
        royaume.tresor().ajouter(this.cible, recus);
    }

    @Override
    public String description() {
        return "action.echanger." + this.source.name().toLowerCase()
                + "_vers_" + this.cible.name().toLowerCase();
    }
}
