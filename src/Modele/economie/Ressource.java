package Modele.economie;

/**
 * Catalogue des ressources economiques produites et consommees par un royaume.
 *
 * Chaque ressource est associee a une cle d'internationalisation qui permet
 * d'obtenir son libelle localise via le Traducteur.
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

    /**
     * @return cle de traduction du nom de la ressource (ex. {@code "ressource.or"})
     */
    public String cleI18n() {
        return this.cleI18n;
    }
}
