package Modele.economie;

/**
 * Les 5 ressources du jeu. L'or, la nourriture, le bois et la pierre sont les
 * ressources de base. Le savoir est genere uniquement par les Erudits et sert
 * a activer les decrets (cf. Epic 4).
 *
 * Chaque ressource possede une cle i18n pour son nom localise.
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
