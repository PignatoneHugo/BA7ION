package Modele.population;

/**
 * Roles possibles d'un habitant. INACTIF est le role par defaut.
 */
public enum Role {

    INACTIF("role.inactif"),
    FERMIER("role.fermier"),
    MINEUR("role.mineur"),
    BUCHERON("role.bucheron"),
    ERUDIT("role.erudit"),
    /** Recrue assignee a l'armee, en attente d'etre equipee en unite combattante. */
    SOLDAT("role.soldat");

    private final String cleI18n;

    Role(String cleI18n) {
        this.cleI18n = cleI18n;
    }

    public String cleI18n() {
        return this.cleI18n;
    }
}
