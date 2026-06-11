package Modele.infrastructure;

import Modele.royaume.Royaume;

/**
 * Marche : permet d'echanger des ressources contre d'autres. Aucune
 * production passive. Les echanges sont declenches par une Action du
 * joueur (ActionEchanger). Le taux s'ameliore avec le niveau :
 *   - niv 1 : 3 unites donnees pour 1 recue (taux defavorable)
 *   - niv 5 : 1 unite donnee pour 1 recue (taux equitable)
 */
public class Marche extends Batiment {

    @Override
    public TypeBatiment type() {
        return TypeBatiment.MARCHE;
    }

    @Override
    protected void appliquerProduction(Royaume royaume) {
        // Pas de production passive : le Marche reagit aux Actions d'echange.
    }

    /**
     * Combien d'unites de la ressource source il faut donner pour
     * obtenir 1 unite de la ressource cible. Decroit avec le niveau.
     */
    public double tauxEchange() {
        // Niv 1 : 3.0  |  Niv 2 : 2.5  |  Niv 3 : 2.0  |  Niv 4 : 1.5  |  Niv 5 : 1.0
        return Math.max(1.0, 3.0 - (this.niveau - 1) * 0.5);
    }

    /** Quantite recue en echange de {@code montantSource} unites donnees. */
    public int quantiteRecue(int montantSource) {
        if (montantSource <= 0) {
            return 0;
        }
        return (int) Math.floor(montantSource / tauxEchange());
    }
}
