package Vue.theme;

import java.awt.Color;

/**
 * Palette de couleurs du theme medieval BAS7ION.
 * Tons or, parchemin sombre, brun fonce, rouge sang, bleu nuit.
 *
 * Toutes les couleurs sont des constantes immuables, utilisables partout
 * dans la couche Vue pour garantir la coherence visuelle.
 */
public final class Palette {

    private Palette() {
        // Classe utilitaire.
    }

    // FONDS
    public static final Color FOND_HAUT = new Color(40, 32, 24);
    public static final Color FOND_BAS = new Color(15, 10, 8);
    public static final Color FOND_PANNEAU = new Color(10, 8, 6);
    public static final Color FOND_PANNEAU_CLAIR = new Color(20, 16, 12);
    public static final Color VOILE_SOMBRE = new Color(0, 0, 0, 180);

    // ACCENTS OR
    public static final Color OR = new Color(200, 134, 10);
    public static final Color OR_CLAIR = new Color(240, 192, 48);
    public static final Color OR_TRES_CLAIR = new Color(255, 240, 160);
    public static final Color OR_FONCE = new Color(139, 90, 0);

    // TEXTE
    public static final Color TEXTE_PRIMAIRE = new Color(240, 208, 128);
    public static final Color TEXTE_SECONDAIRE = new Color(160, 120, 64);
    public static final Color TEXTE_TERTIAIRE = new Color(106, 72, 32);

    // BORDURES
    public static final Color BORDURE_OR = new Color(200, 134, 10);
    public static final Color BORDURE_FONCEE = new Color(42, 24, 8);

    // ROUGE (danger, banniere, attaque)
    public static final Color ROUGE_BANNIERE = new Color(139, 0, 0);
    public static final Color ROUGE_DANGER = new Color(192, 32, 32);
    public static final Color ROUGE_CLAIR = new Color(224, 64, 48);

    // VERT (positif, nourriture)
    public static final Color VERT_POSITIF = new Color(112, 192, 48);
    public static final Color VERT_FONCE = new Color(42, 90, 0);

    // BLEU (militaire, eau, decret)
    public static final Color BLEU_MILITAIRE = new Color(64, 96, 192);
    public static final Color BLEU_NUIT = new Color(0, 0, 106);

    // RESSOURCES (couleur par type)
    public static final Color OR_RESSOURCE = new Color(240, 192, 48);
    public static final Color NOURRITURE_RESSOURCE = new Color(112, 208, 48);
    public static final Color BOIS_RESSOURCE = new Color(192, 112, 48);
    public static final Color PIERRE_RESSOURCE = new Color(170, 170, 170);
    public static final Color SAVOIR_RESSOURCE = new Color(144, 80, 192);

    // BOUTONS
    public static final Color BOUTON_FOND = new Color(45, 35, 28);
    public static final Color BOUTON_FOND_SURVOL = new Color(70, 55, 40);
    public static final Color BOUTON_FOND_DESACTIVE = new Color(25, 20, 16);
    public static final Color BOUTON_DANGER_FOND = new Color(58, 8, 8);
    public static final Color BOUTON_DANGER_SURVOL = new Color(92, 16, 16);

    // CHAMPS DE SAISIE
    public static final Color CHAMP_FOND = new Color(38, 30, 24);
    public static final Color CHAMP_TEXTE = new Color(235, 205, 140);
}
