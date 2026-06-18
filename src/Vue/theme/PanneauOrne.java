package Vue.theme;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/** Panneau sombre avec bordure doree et petits losanges aux coins. */
public class PanneauOrne extends JPanel {

    private static final long serialVersionUID = 1L;

    private final boolean coinsOrnementaux;

    /**
     * Cree un panneau orne, avec ou sans losanges aux coins.
     *
     * @param coinsOrnementaux vrai pour dessiner les losanges aux coins
     */
    public PanneauOrne(boolean coinsOrnementaux) {
        this.coinsOrnementaux = coinsOrnementaux;
        setOpaque(true);
        setBackground(Palette.FOND_PANNEAU);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Palette.OR_FONCE, 2),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
    }

    /**
     * Cree un panneau orne avec les losanges aux coins.
     */
    public PanneauOrne() {
        this(true);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (!this.coinsOrnementaux) {
            return;
        }
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int largeur = getWidth();
        int hauteur = getHeight();
        int taille = 8;
        Color or = Palette.OR;

        // un losange a chaque coin
        g2.setColor(or);
        dessinerLosange(g2, 0, 0, taille);
        dessinerLosange(g2, largeur, 0, taille);
        dessinerLosange(g2, 0, hauteur, taille);
        dessinerLosange(g2, largeur, hauteur, taille);

        g2.dispose();
    }

    private void dessinerLosange(Graphics2D g2, int cx, int cy, int taille) {
        int[] xs = { cx, cx + taille, cx, cx - taille };
        int[] ys = { cy - taille, cy, cy + taille, cy };
        g2.fillPolygon(xs, ys, 4);
    }
}
