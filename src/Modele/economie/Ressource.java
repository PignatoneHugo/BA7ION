package Modele.economie;

// Les 5 ressources du jeu.
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

    /**
     * Donne le nom affichable de la ressource.
     *
     * @return le libelle de la ressource
     */
    public String libelle() {
        return this.libelle;
    }
}
