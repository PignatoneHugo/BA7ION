package Vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Vue.theme.Palette;
import Vue.theme.Polices;

/** Barre du bas : affiche un petit message sur la derniere action. */
public class VueStatusBar extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JLabel labelMessage;

    /**
     * Cree la barre de statut avec un message par defaut.
     */
    public VueStatusBar() {
        setOpaque(true);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1280, 40));
        setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        this.labelMessage = new JLabel("Pret a jouer");
        this.labelMessage.setFont(Polices.LABEL.deriveFont(java.awt.Font.ITALIC, 13f));
        this.labelMessage.setForeground(Palette.TEXTE_PRIMAIRE);
        add(this.labelMessage, BorderLayout.WEST);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int largeur = getWidth();
        int hauteur = getHeight();

        // fond degrade
        GradientPaint grad = new GradientPaint(0, 0, new Color(30, 22, 12),
                0, hauteur, new Color(10, 8, 4));
        g2.setPaint(grad);
        g2.fillRect(0, 0, largeur, hauteur);

        // liseré doré en haut
        g2.setColor(Palette.OR);
        g2.fillRect(0, 0, largeur, 2);
        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillRect(0, 2, largeur, 1);

        g2.dispose();
    }

    /**
     * Change le message affiche dans la barre.
     *
     * @param texte le message a afficher
     */
    public void setMessage(String texte) {
        this.labelMessage.setText(texte != null ? texte : "");
    }
}
