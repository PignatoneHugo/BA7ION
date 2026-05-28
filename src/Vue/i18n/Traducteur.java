package Vue.i18n;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Point d'entree unique pour l'internationalisation de l'interface. Charge un
 * {@link ResourceBundle} a partir des fichiers de proprietes situes dans le
 * package {@code Vue/i18n/strings_xx.properties} et expose la methode
 * {@link #t(String)} qui traduit une cle en chaine localisee.
 *
 * <p>Toutes les chaines affichees a l'utilisateur doivent passer par cette
 * classe, jamais etre ecrites en dur dans le code des vues.</p>
 *
 * <p>Exemple :</p>
 * <pre>
 *     String titre = Traducteur.t("app.titre");
 * </pre>
 *
 * Si une cle n'est pas presente dans le bundle, la cle elle-meme est
 * retournee : les oublis de traduction sont ainsi immediatement visibles a
 * l'execution sans provoquer d'exception.
 */
public final class Traducteur {

    private static final String BASE = "Vue.i18n.strings";
    private static ResourceBundle bundle = ResourceBundle.getBundle(BASE, Locale.FRENCH);

    private Traducteur() {
        // Classe utilitaire, ne pas instancier.
    }

    /**
     * Change la langue courante de l'interface. Les appels suivants a
     * {@link #t(String)} retournent les traductions dans la nouvelle langue ;
     * les composants Swing deja affiches doivent etre rafraichis par
     * l'appelant.
     *
     * @param locale nouvelle langue cible, non null
     * @throws IllegalArgumentException si {@code locale} est null
     */
    public static void definirLocale(Locale locale) {
        if (locale == null) {
            throw new IllegalArgumentException("Locale ne peut pas etre null.");
        }
        bundle = ResourceBundle.getBundle(BASE, locale);
    }

    /**
     * @return langue actuellement utilisee par le bundle
     */
    public static Locale localeCourante() {
        return bundle.getLocale();
    }

    /**
     * Traduit une cle dans la langue courante.
     *
     * @param cle cle i18n a resoudre (ex. {@code "ressource.or"})
     * @return la traduction associee, ou la cle elle-meme si aucune
     *         traduction n'est definie ; chaine vide si {@code cle} est null
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
