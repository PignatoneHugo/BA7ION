package Modele.militaire;

/**
 * Les 4 types d'unites militaires. Chaque type a des stats d'attaque et de
 * defense de base. Les avantages entre types (Pierre-Feuille-Ciseaux) sont
 * geres par TableAvantages.
 */
public enum TypeUnite {

    INFANTERIE_LEGERE("unite.infanterie_legere", 10, 8, 1),
    ARCHER("unite.archer", 12, 6, 2),
    LANCIER("unite.lancier", 8, 12, 3),
    CAVALERIE_LOURDE("unite.cavalerie_lourde", 15, 10, 4);

    private final String cleI18n;
    private final int attaqueBase;
    private final int defenseBase;
    private final int niveauCaserneRequis;

    TypeUnite(String cleI18n, int attaqueBase, int defenseBase, int niveauCaserneRequis) {
        this.cleI18n = cleI18n;
        this.attaqueBase = attaqueBase;
        this.defenseBase = defenseBase;
        this.niveauCaserneRequis = niveauCaserneRequis;
    }

    public String cleI18n() {
        return this.cleI18n;
    }

    public int attaqueBase() {
        return this.attaqueBase;
    }

    public int defenseBase() {
        return this.defenseBase;
    }

    /** Niveau de Caserne requis pour pouvoir recruter ce type d'unite. */
    public int niveauCaserneRequis() {
        return this.niveauCaserneRequis;
    }
}
