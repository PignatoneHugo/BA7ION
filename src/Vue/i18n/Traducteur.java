package Vue.i18n;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Singleton de traduction des chaines de l'interface (FR par defaut, EN
 * commutable via {@link #definirLocale(Locale)}).
 *
 * Usage :
 *     String titre = Traducteur.t("app.titre");
 *
 * Toutes les chaines affichees doivent passer par cette classe (cf. plan
 * d'architecture section 10). Les fichiers de proprietes se trouvent dans
 * {@code Vue/i18n/strings_xx.properties}.
 *
 * Si une cle n'est pas traduite, la cle elle-meme est retournee : cela rend
 * les oublis de traduction visibles a l'execution.
 */
public final class Traducteur {

    private static final String BASE = "Vue.i18n.strings";
    private static ResourceBundle bundle = ResourceBundle.getBundle(BASE, Locale.FRENCH);

    private Traducteur() {
        // Classe utilitaire.
    }

    /**
     * Change la langue de l'interface. Effet immediat : les prochains appels
     * a {@link #t(String)} retournent les chaines dans la nouvelle langue.
     * Les composants Swing deja affiches doivent etre rafraichis manuellement
     * (a faire dans Sprint 2+ avec un Observer sur le Traducteur).
     */
    public static void definirLocale(Locale locale) {
        if (locale == null) {
            throw new IllegalArgumentException("Locale ne peut pas etre null.");
        }
        bundle = ResourceBundle.getBundle(BASE, locale);
    }

    public static Locale localeCourante() {
        return bundle.getLocale();
    }

    /**
     * Retourne la traduction de la cle, ou la cle elle-meme si absente.
     */
    public static String t(String cle) {
        if (cle == null) {
            return "";
        }
        try {
            return bundle.getString(cle);
        } catch (MissingResourceException e) {
            return cle;
        }
    }
}
