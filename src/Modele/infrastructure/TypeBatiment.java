package Modele.infrastructure;

// Les 9 types de batiments du jeu.
public enum TypeBatiment {

    FERME("Ferme"),
    MINE("Mine"),
    SCIERIE("Scierie"),
    HABITATIONS("Habitations"),
    CASERNE("Caserne"),
    REMPARTS("Remparts"),
    MARCHE("Marche"),
    BIBLIOTHEQUE("Bibliotheque"),
    TOUR_GUET("Tour de Guet");

    private final String libelle;

    TypeBatiment(String libelle) {
        this.libelle = libelle;
    }

    /**
     * Donne le nom affichable du type de batiment.
     *
     * @return le libelle du type de batiment
     */
    public String libelle() {
        return this.libelle;
    }
}
