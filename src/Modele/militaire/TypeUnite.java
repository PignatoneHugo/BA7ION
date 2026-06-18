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

    /**
     * Renvoie le libelle affichable du type d'unite.
     *
     * @return le libelle du type
     */
    public String libelle() {
        return this.libelle;
    }

    /**
     * Renvoie l'attaque de base du type d'unite.
     *
     * @return l'attaque de base
     */
    public int attaqueBase() {
        return this.attaqueBase;
    }

    /**
     * Renvoie la defense de base du type d'unite.
     *
     * @return la defense de base
     */
    public int defenseBase() {
        return this.defenseBase;
    }

    /**
     * Renvoie le niveau de caserne necessaire pour recruter ce type.
     *
     * @return le niveau de caserne requis
     */
    public int niveauCaserneRequis() {
        return this.niveauCaserneRequis;
    }
}
