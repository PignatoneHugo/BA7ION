package Vue.theme;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import javax.swing.JToggleButton;

/** Bouton bascule du theme : fond or quand il est selectionne. */
public class ToggleMedieval extends JToggleButton {

    private static final long serialVersionUID = 1L;

    /**
     * Cree un bouton bascule avec le texte donne.
     *
     * @param texte le texte du bouton
     */
    public ToggleMedieval(String texte) {
        super(texte);
        setFont(Polices.LABEL.deriveFont(13f));
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
        setForeground(Palette.TEXTE_PRIMAIRE);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int largeur = getWidth();
        int hauteur = getHeight();

        // couleurs selon l'etat
        Color hautFond;
        Color basFond;
        Color bordure;
        Color couleurTexte;

        if (isSelected()) {
            // selectionne : fond or
            hautFond = Palette.OR_CLAIR;
            basFond = Palette.OR_FONCE;
            bordure = Palette.OR_TRES_CLAIR;
            couleurTexte = new Color(20, 12, 4);
        } else if (getModel().isRollover()) {
            // survol
            hautFond = new Color(60, 44, 22);
            basFond = new Color(28, 20, 10);
            bordure = Palette.OR;
            couleurTexte = Palette.OR_CLAIR;
        } else {
            // normal
            hautFond = new Color(40, 28, 14);
            basFond = new Color(16, 10, 4);
            bordure = Palette.OR_FONCE;
            couleurTexte = Palette.TEXTE_PRIMAIRE;
        }

        g2.setPaint(new GradientPaint(0, 0, hautFond, 0, hauteur, basFond));
        g2.fillRect(0, 0, largeur, hauteur);

        // liseré or en haut
        if (isSelected()) {
            g2.setColor(Palette.OR_TRES_CLAIR);
            g2.fillRect(0, 0, largeur, 2);
        }

        // bordure
        g2.setColor(bordure);
        g2.drawRect(0, 0, largeur - 1, hauteur - 1);

        setForeground(couleurTexte);
        g2.dispose();

        super.paintComponent(graphics);
    }
}
