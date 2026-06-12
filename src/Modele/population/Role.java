package Modele.population;

/**
 * Roles possibles d'un habitant. INACTIF est le role par defaut.
 */
public enum Role {

    INACTIF("Inactif"),
    FERMIER("Fermier"),
    MINEUR("Mineur"),
    BUCHERON("Bucheron"),
    ERUDIT("Erudit"),
    /** Recrue assignee a l'armee, en attente d'etre equipee en unite combattante. */
    SOLDAT("Soldat (recrue)");

    private final String libelle;

    Role(String libelle) {
        this.libelle = libelle;
    }

    public String libelle() {
        return this.libelle;
    }
}
