package Modele.militaire;

/**
 * Les 4 types d'unites militaires. Chaque type a des stats d'attaque et de
 * defense de base, ainsi que le niveau minimum de Caserne requis pour le
 * recruter. Les avantages entre types (Pierre-Feuille-Ciseaux) sont geres
 * par TableAvantages.
 */
public enum TypeUnite {

    INFANTERIE_LEGERE("Infanterie legere", 10, 8, 1),
    ARCHER("Archer", 12, 6, 2),
    LANCIER("Lancier", 8, 12, 3),
    CAVALERIE_LOURDE("Cavalerie lourde", 15, 10, 4);

    private final String libelle;
    private final int attaqueBase;
    private final int defenseBase;
    private final int niveauCaserneRequis;

    TypeUnite(String libelle, int attaqueBase, int defenseBase, int niveauCaserneRequis) {
        this.libelle = libelle;
        this.attaqueBase = attaqueBase;
        this.defenseBase = defenseBase;
        this.niveauCaserneRequis = niveauCaserneRequis;
    }

    public String libelle() {
        return this.libelle;
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
