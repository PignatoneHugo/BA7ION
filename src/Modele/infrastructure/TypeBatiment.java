package Modele.infrastructure;

/**
 * Les 9 batiments du royaume, tous presents au niveau 1 des le tour 1.
 * Aucune construction ni demolition n'est possible : seules les ameliorations
 * et les reparations existent (cf. Epic 3 du cahier des charges).
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

    public String cleI18n() {
        return this.cleI18n;
    }
}
