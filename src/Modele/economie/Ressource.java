package Modele.economie;

/**
 * Les 5 ressources du jeu. Chacune a une cle i18n pour son nom traduit.
 */
public enum Ressource {

    OR("ressource.or"),
    NOURRITURE("ressource.nourriture"),
    BOIS("ressource.bois"),
    PIERRE("ressource.pierre"),
    SAVOIR("ressource.savoir");

    private final String cleI18n;

    Ressource(String cleI18n) {
        this.cleI18n = cleI18n;
    }

    public String cleI18n() {
        return this.cleI18n;
    }
}
