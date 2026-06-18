package Modele.ia;

// Cree les strategies d'IA.
public final class FabriqueIA {

    private FabriqueIA() {
    }

    /**
     * Cree la strategie equilibree.
     *
     * @return une nouvelle strategie equilibree
     */
    public static StrategieIA creerEquilibree() {
        return new StrategieEquilibree();
    }
}
