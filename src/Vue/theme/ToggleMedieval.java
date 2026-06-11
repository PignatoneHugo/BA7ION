package Vue.theme;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import javax.swing.JToggleButton;

/**
 * Bouton bascule au look medieval. Quand selectionne, fond or et texte
 * fonce -- bien plus lisible que le bleu Swing par defaut. Utilise pour
 * le selecteur de niveau de taxes.
 */
public class ToggleMedieval extends JToggleButton {

    private static final long serialVersionUID = 1L;

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
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Fond selon etat (selectionne / hover / normal)
        Color hautFond;
        Color basFond;
        Color bordure;
        Color couleurTexte;

        if (isSelected()) {
            // Selectionne : fond or, bordure or claire, texte noir
            hautFond = Palette.OR_CLAIR;
            basFond = Palette.OR_FONCE;
            bordure = Palette.OR_TRES_CLAIR;
            couleurTexte = new Color(20, 12, 4);
        } else if (getModel().isRollover()) {
            // Survol : fond plus clair
            hautFond = new Color(60, 44, 22);
            basFond = new Color(28, 20, 10);
            bordure = Palette.OR;
            couleurTexte = Palette.OR_CLAIR;
        } else {
            // Normal : fond brun sombre
            hautFond = new Color(40, 28, 14);
            basFond = new Color(16, 10, 4);
            bordure = Palette.OR_FONCE;
            couleurTexte = Palette.TEXTE_PRIMAIRE;
        }

        g2.setPaint(new GradientPaint(0, 0, hautFond, 0, h, basFond));
        g2.fillRect(0, 0, w, h);

        // Liseré or en haut (effet metal)
        if (isSelected()) {
            g2.setColor(Palette.OR_TRES_CLAIR);
            g2.fillRect(0, 0, w, 2);
        }

        // Bordure
        g2.setColor(bordure);
        g2.drawRect(0, 0, w - 1, h - 1);

        setForeground(couleurTexte);
        g2.dispose();

        super.paintComponent(g);
    }
}
