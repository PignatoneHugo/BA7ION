package Modele.infrastructure;

import Modele.royaume.Royaume;

// Marche : echange une ressource contre une autre. Le taux s'ameliore
// avec le niveau (3 pour 1 au niv 1, 1 pour 1 au niv 5).
public class Marche extends Batiment {

    @Override
    public TypeBatiment type() {
        return TypeBatiment.MARCHE;
    }

    @Override
    protected void appliquerProduction(Royaume royaume) {
        // Rien : le Marche reagit aux echanges du joueur.
    }

    // Nombre d'unites a donner pour en recevoir 1 (baisse avec le niveau).
    public double tauxEchange() {
        return Math.max(1.0, 3.0 - (this.niveau - 1) * 0.5);
    }

    // Quantite recue pour montantSource unites donnees.
    public int quantiteRecue(int montantSource) {
        if (montantSource <= 0) {
            return 0;
        }
        return (int) Math.floor(montantSource / tauxEchange());
    }
}
