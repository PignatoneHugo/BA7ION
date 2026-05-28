package Modele.population;

/**
 * Affectation possible d'un habitant. Le role definit ce qu'un habitant
 * produit ou represente pour le royaume.
 *
 * {@link #INACTIF} est l'affectation par defaut : un habitant inactif ne
 * produit rien mais reste disponible pour etre reaffecte a tout moment.
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

    /**
     * @return cle de traduction du nom du role (ex. {@code "role.fermier"})
     */
    public String cleI18n() {
        return this.cleI18n;
    }
}
