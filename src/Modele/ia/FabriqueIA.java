package Modele.ia;

/**
 * Fabrique de strategies d'IA. Pattern Factory.
 * Au Sprint 3 : une seule strategie (equilibree), mais l'interface est en
 * place pour ajouter Agressif / Defensif / Commercant plus tard.
 */
public final class FabriqueIA {

    private FabriqueIA() {
        // Classe utilitaire.
    }

    /** Cree une instance de la strategie equilibree (la seule disponible). */
    public static StrategieIA creerEquilibree() {
        return new StrategieEquilibree();
    }
}
