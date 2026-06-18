package Modele.infrastructure;

import Modele.royaume.Royaume;

// Marche : echange une ressource contre une autre. Le taux s'ameliore
// avec le niveau (3 pour 1 au niv 1, 1 pour 1 au niv 5).
public class Marche extends Batiment {

    /**
     * Donne le type du batiment.
     *
     * @return le type marche
     */
    @Override
    public TypeBatiment type() {
        return TypeBatiment.MARCHE;
    }

    @Override
    protected void appliquerProduction(Royaume royaume) {
        // Rien : le Marche reagit aux echanges du joueur.
    }

    /**
     * Donne le nombre d'unites a donner pour en recevoir une, qui baisse avec le niveau.
     *
     * @return le taux d'echange
     */
    public double tauxEchange() {
        return Math.max(1.0, 3.0 - (this.niveau - 1) * 0.5);
    }

    /**
     * Calcule la quantite recue pour un certain nombre d'unites donnees.
     *
     * @param montantSource le nombre d'unites donnees
     * @return la quantite recue en echange
     */
    public int quantiteRecue(int montantSource) {
        if (montantSource <= 0) {
            return 0;
        }
        return (int) Math.floor(montantSource / tauxEchange());
    }
}
