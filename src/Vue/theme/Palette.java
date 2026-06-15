package Vue.theme;

import java.awt.Color;

/** Toutes les couleurs du theme medieval, au meme endroit. */
public final class Palette {

    private Palette() {
        // classe utilitaire
    }

    // fonds
    public static final Color FOND_HAUT = new Color(40, 32, 24);
    public static final Color FOND_BAS = new Color(15, 10, 8);
    public static final Color FOND_PANNEAU = new Color(10, 8, 6);
    public static final Color FOND_PANNEAU_CLAIR = new Color(20, 16, 12);
    public static final Color VOILE_SOMBRE = new Color(0, 0, 0, 180);

    // or
    public static final Color OR = new Color(200, 134, 10);
    public static final Color OR_CLAIR = new Color(240, 192, 48);
    public static final Color OR_TRES_CLAIR = new Color(255, 240, 160);
    public static final Color OR_FONCE = new Color(139, 90, 0);

    // texte (couleurs claires pour bien lire sur fond sombre)
    public static final Color TEXTE_PRIMAIRE = new Color(240, 208, 128);
    public static final Color TEXTE_SECONDAIRE = new Color(173, 130, 69);
    public static final Color TEXTE_TERTIAIRE = new Color(184, 125, 56);

    // bordures
    public static final Color BORDURE_OR = new Color(200, 134, 10);
    public static final Color BORDURE_FONCEE = new Color(42, 24, 8);

    // rouge (danger, banniere)
    public static final Color ROUGE_BANNIERE = new Color(139, 0, 0);
    public static final Color ROUGE_DANGER = new Color(242, 40, 40);
    public static final Color ROUGE_CLAIR = new Color(224, 64, 48);

    // vert (positif, nourriture)
    public static final Color VERT_POSITIF = new Color(112, 192, 48);
    public static final Color VERT_FONCE = new Color(42, 90, 0);

    // bleu (militaire, decret)
    public static final Color BLEU_MILITAIRE = new Color(78, 118, 235);
    public static final Color BLEU_NUIT = new Color(0, 0, 106);

    // une couleur par ressource
    public static final Color OR_RESSOURCE = new Color(240, 192, 48);
    public static final Color NOURRITURE_RESSOURCE = new Color(112, 208, 48);
    public static final Color BOIS_RESSOURCE = new Color(192, 112, 48);
    public static final Color PIERRE_RESSOURCE = new Color(170, 170, 170);
    public static final Color SAVOIR_RESSOURCE = new Color(169, 94, 225);

    // boutons
    public static final Color BOUTON_FOND = new Color(45, 35, 28);
    public static final Color BOUTON_FOND_SURVOL = new Color(70, 55, 40);
    public static final Color BOUTON_FOND_DESACTIVE = new Color(25, 20, 16);
    public static final Color BOUTON_DANGER_FOND = new Color(58, 8, 8);
    public static final Color BOUTON_DANGER_SURVOL = new Color(92, 16, 16);

    // champs de saisie
    public static final Color CHAMP_FOND = new Color(38, 30, 24);
    public static final Color CHAMP_TEXTE = new Color(235, 205, 140);
}
