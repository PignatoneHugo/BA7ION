package Vue.theme;

import java.awt.Font;

/**
 * Polices standardisees du theme medieval.
 * Toutes en Serif pour le ton epoque, avec des tailles standardisees.
 */
public final class Polices {

    private Polices() {
        // Classe utilitaire.
    }

    /** Titre principal (BA7ION, FIN DE REGNE...) */
    public static final Font TITRE = new Font(Font.SERIF, Font.BOLD, 64);

    /** Sous-titre (gestion et strategie...) */
    public static final Font SOUS_TITRE = new Font(Font.SERIF, Font.ITALIC, 16);

    /** Titre de section (ROYAUME, ARMEE, RESSOURCES...) */
    public static final Font SECTION = new Font(Font.SERIF, Font.BOLD, 14);

    /** Label normal */
    public static final Font LABEL = new Font(Font.SERIF, Font.PLAIN, 13);

    /** Petit label */
    public static final Font PETIT_LABEL = new Font(Font.SERIF, Font.PLAIN, 10);

    /** Valeur numerique (compteurs, scores) */
    public static final Font VALEUR = new Font(Font.SERIF, Font.BOLD, 16);

    /** Petite valeur */
    public static final Font PETITE_VALEUR = new Font(Font.SERIF, Font.BOLD, 12);

    /** Texte de bouton principal */
    public static final Font BOUTON_PRINCIPAL = new Font(Font.SERIF, Font.BOLD, 18);

    /** Texte de bouton secondaire */
    public static final Font BOUTON_SECONDAIRE = new Font(Font.SERIF, Font.PLAIN, 14);

    /** Credits, copyright */
    public static final Font CREDITS = new Font(Font.SERIF, Font.ITALIC, 11);
}
