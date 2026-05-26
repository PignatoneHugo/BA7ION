package Modele.population;

/**
 * Roles que peut occuper un habitant.
 *
 * Le role INACTIF est le role par defaut : un habitant inactif ne produit rien
 * mais peut etre reaffecte gratuitement. Les autres roles produisent une
 * ressource ou un service.
 *
 * Les soldats ne sont pas representes ici : ils sont geres separement dans
 * {@link Modele.militaire.Armee} car leur "role" porte aussi un type d'unite
 * (Infanterie/Archers/Lanciers/Cavalerie).
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
