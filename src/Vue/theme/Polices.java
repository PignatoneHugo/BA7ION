package Vue.theme;

import java.awt.Font;

/** Les polices du theme, toutes en Serif. */
public final class Polices {

    private Polices() {
        // classe utilitaire
    }

    // titre principal
    public static final Font TITRE = new Font(Font.SERIF, Font.BOLD, 64);

    // sous-titre
    public static final Font SOUS_TITRE = new Font(Font.SERIF, Font.ITALIC, 16);

    // titre de section
    public static final Font SECTION = new Font(Font.SERIF, Font.BOLD, 14);

    // label normal
    public static final Font LABEL = new Font(Font.SERIF, Font.PLAIN, 13);

    // petit label
    public static final Font PETIT_LABEL = new Font(Font.SERIF, Font.PLAIN, 10);

    // valeur numerique
    public static final Font VALEUR = new Font(Font.SERIF, Font.BOLD, 16);

    // petite valeur
    public static final Font PETITE_VALEUR = new Font(Font.SERIF, Font.BOLD, 12);

    // texte de bouton principal
    public static final Font BOUTON_PRINCIPAL = new Font(Font.SERIF, Font.BOLD, 18);

    // texte de bouton secondaire
    public static final Font BOUTON_SECONDAIRE = new Font(Font.SERIF, Font.PLAIN, 14);

    // credits
    public static final Font CREDITS = new Font(Font.SERIF, Font.ITALIC, 11);
}
