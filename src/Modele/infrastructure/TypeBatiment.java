package Modele.infrastructure;

/**
 * Catalogue des batiments constructibles dans un royaume. Chaque type est
 * associe a une cle d'internationalisation pour son libelle localise.
 */
public enum TypeBatiment {

    FERME("batiment.ferme"),
    MINE("batiment.mine"),
    SCIERIE("batiment.scierie"),
    HABITATIONS("batiment.habitations"),
    CASERNE("batiment.caserne"),
    REMPARTS("batiment.remparts"),
    MARCHE("batiment.marche"),
    BIBLIOTHEQUE("batiment.bibliotheque"),
    TOUR_GUET("batiment.tour_guet");

    private final String cleI18n;

    TypeBatiment(String cleI18n) {
        this.cleI18n = cleI18n;
    }

    /**
     * @return cle de traduction du nom du batiment (ex. {@code "batiment.ferme"})
     */
    public String cleI18n() {
        return this.cleI18n;
    }
}
