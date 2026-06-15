package Modele.population;

// Roles possibles d'un habitant (INACTIF par defaut).
public enum Role {

    INACTIF("Inactif"),
    FERMIER("Fermier"),
    MINEUR("Mineur"),
    BUCHERON("Bucheron"),
    ERUDIT("Erudit"),
    SOLDAT("Soldat (recrue)");

    private final String libelle;

    Role(String libelle) {
        this.libelle = libelle;
    }

    public String libelle() {
        return this.libelle;
    }
}
