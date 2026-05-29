package Vue.i18n;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Singleton de traduction. Lit les fichiers strings_fr.properties et
 * strings_en.properties et fournit la methode t(cle) pour traduire une chaine.
 *
 * Usage : String titre = Traducteur.t("app.titre");
 *
 * Si la cle n'existe pas, on retourne la cle elle-meme (visible a l'execution).
 */
public final class Traducteur {

    private static final String BASE = "Vue.i18n.strings";
    private static ResourceBundle bundle = ResourceBundle.getBundle(BASE, Locale.FRENCH);

    private Traducteur() {
        // Classe utilitaire.
    }

    /** Change la langue de l'interface. */
    public static void definirLocale(Locale locale) {
        if (locale == null) {
            throw new IllegalArgumentException("Locale ne peut pas etre null.");
        }
        bundle = ResourceBundle.getBundle(BASE, locale);
    }

    public static Locale localeCourante() {
        return bundle.getLocale();
    }

    /** Retourne la traduction de la cle, ou la cle si pas trouvee. */
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
