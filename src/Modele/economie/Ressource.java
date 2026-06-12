package Modele.economie;

/**
 * Les 5 ressources du jeu, avec leur libelle d'affichage.
 */
public enum Ressource {

    OR("Or"),
    NOURRITURE("Nourriture"),
    BOIS("Bois"),
    PIERRE("Pierre"),
    SAVOIR("Savoir");

    private final String libelle;

    Ressource(String libelle) {
        this.libelle = libelle;
    }

    public String libelle() {
        return this.libelle;
    }
}
