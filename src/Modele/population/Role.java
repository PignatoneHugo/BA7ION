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
    ESPION("role.espion");

    private final String cleI18n;

    Role(String cleI18n) {
        this.cleI18n = cleI18n;
    }

    public String cleI18n() {
        return this.cleI18n;
    }
}
