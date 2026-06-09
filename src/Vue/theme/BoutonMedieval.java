package Vue.theme;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 * Bouton stylise medieval : fond sombre, bordure doree, effet de survol
 * qui eclaircit le fond et le texte.
 *
 * Utiliser les constructeurs pour creer un bouton "primaire" (bordure
 * doree) ou "secondaire" (bordure brun fonce) ou "danger" (bordure rouge).
 */
public class BoutonMedieval extends JButton {

    private static final long serialVersionUID = 1L;

    public enum Style { PRIMAIRE, SECONDAIRE, DANGER }

    private final Style style;
    private final Color couleurFondNormal;
    private final Color couleurFondSurvol;
    private final Color couleurTexteNormal;
    private final Color couleurTexteSurvol;
    private final Color couleurBordure;

    public BoutonMedieval(String texte, Style style) {
        super(texte);
        this.style = style;

        switch (style) {
            case PRIMAIRE:
                this.couleurFondNormal = Palette.BOUTON_FOND;
                this.couleurFondSurvol = Palette.BOUTON_FOND_SURVOL;
                this.couleurTexteNormal = Palette.OR;
                this.couleurTexteSurvol = Palette.OR_CLAIR;
                this.couleurBordure = Palette.OR;
                setFont(Polices.BOUTON_PRINCIPAL);
                break;
            case DANGER:
                this.couleurFondNormal = Palette.BOUTON_DANGER_FOND;
                this.couleurFondSurvol = Palette.BOUTON_DANGER_SURVOL;
                this.couleurTexteNormal = Palette.ROUGE_CLAIR;
                this.couleurTexteSurvol = new Color(255, 96, 80);
                this.couleurBordure = Palette.ROUGE_DANGER;
                setFont(Polices.BOUTON_PRINCIPAL.deriveFont(Font.BOLD, 14f));
                break;
            case SECONDAIRE:
            default:
                this.couleurFondNormal = new Color(30, 20, 8);
                this.couleurFondSurvol = new Color(50, 35, 16);
                this.couleurTexteNormal = Palette.TEXTE_SECONDAIRE;
                this.couleurTexteSurvol = Palette.TEXTE_PRIMAIRE;
                this.couleurBordure = Palette.BORDURE_FONCEE;
                setFont(Polices.BOUTON_SECONDAIRE);
                break;
        }

        setForeground(this.couleurTexteNormal);
        setBackground(this.couleurFondNormal);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(this.couleurBordure, 2),
                BorderFactory.createEmptyBorder(6, 16, 6, 16)));
        setFocusPainted(false);
        setOpaque(true);
        setContentAreaFilled(true);
        setBorderPainted(true);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    setBackground(couleurFondSurvol);
                    setForeground(couleurTexteSurvol);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(couleurFondNormal);
                setForeground(couleurTexteNormal);
            }
        });
    }

    public BoutonMedieval(String texte) {
        this(texte, Style.PRIMAIRE);
    }

    public Style style() {
        return this.style;
    }

    /** Reinitialise le rendu apres un changement d'etat enable/disable. */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            setBackground(this.couleurFondNormal);
            setForeground(this.couleurTexteNormal);
        } else {
            setBackground(Palette.BOUTON_FOND_DESACTIVE);
            setForeground(Palette.TEXTE_TERTIAIRE);
        }
    }

    /** Helper : cree un bouton primaire de taille standard. */
    public static BoutonMedieval primaire(String texte) {
        BoutonMedieval b = new BoutonMedieval(texte, Style.PRIMAIRE);
        b.setPreferredSize(new Dimension(280, 50));
        return b;
    }

    /** Helper : cree un bouton secondaire compact. */
    public static BoutonMedieval secondaire(String texte) {
        BoutonMedieval b = new BoutonMedieval(texte, Style.SECONDAIRE);
        b.setPreferredSize(new Dimension(180, 36));
        return b;
    }
}
