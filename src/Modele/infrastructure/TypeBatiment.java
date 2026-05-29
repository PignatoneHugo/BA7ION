package Modele.infrastructure;

/**
 * Les 9 batiments du jeu. Chacun a une cle i18n pour son nom traduit.
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
