package Modele.militaire;

// Les 4 types d'unites avec leurs stats et le niveau de caserne requis.
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

    // Niveau de caserne necessaire pour recruter ce type.
    public int niveauCaserneRequis() {
        return this.niveauCaserneRequis;
    }
}
