package Vue.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Vue.theme.BoutonMedieval;
import Vue.theme.Palette;
import Vue.theme.Polices;

/** Menu d'accueil : un paysage medieval dessine a la main + les boutons. */
public class VueMenuPrincipal extends JPanel {

    private static final long serialVersionUID = 1L;

    private final BoutonMedieval boutonNouvellePartie;
    private final BoutonMedieval boutonCharger;
    private final BoutonMedieval boutonQuitter;

    // positions des etoiles
    private final int[] etoilesX;
    private final int[] etoilesY;
    private final float[] etoilesAlpha;

    /**
     * Cree le menu d'accueil avec son decor et ses boutons.
     */
    public VueMenuPrincipal() {
        setOpaque(true);
        setBackground(Palette.FOND_BAS);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // on genere les etoiles (seed fixe pour qu'elles ne bougent pas)
        Random aleatoire = new Random(42);
        int nbEtoiles = 60;
        this.etoilesX = new int[nbEtoiles];
        this.etoilesY = new int[nbEtoiles];
        this.etoilesAlpha = new float[nbEtoiles];
        for (int indice = 0; indice < nbEtoiles; indice++) {
            this.etoilesX[indice] = aleatoire.nextInt(1280);
            this.etoilesY[indice] = aleatoire.nextInt(180);
            this.etoilesAlpha[indice] = 0.4f + aleatoire.nextFloat() * 0.6f;
        }

        GridBagConstraints contraintes = new GridBagConstraints();
        contraintes.gridx = 0;
        contraintes.fill = GridBagConstraints.HORIZONTAL;

        // titre
        contraintes.gridy = 0;
        contraintes.insets = new Insets(60, 0, 8, 0);
        JLabel titre = new JLabel("BA7ION", SwingConstants.CENTER);
        titre.setFont(Polices.TITRE.deriveFont(80f));
        titre.setForeground(Palette.OR);
        add(titre, contraintes);

        // sous-titre
        contraintes.gridy = 1;
        contraintes.insets = new Insets(0, 0, 350, 0);
        JLabel sousTitre = new JLabel("Simulation de royaume medieval", SwingConstants.CENTER);
        sousTitre.setFont(Polices.SOUS_TITRE.deriveFont(18f));
        sousTitre.setForeground(Palette.TEXTE_PRIMAIRE);
        add(sousTitre, contraintes);

        contraintes.insets = new Insets(10, 0, 10, 0);

        // les boutons
        this.boutonNouvellePartie = BoutonMedieval.primaire("Nouvelle partie");
        this.boutonNouvellePartie.setPreferredSize(new Dimension(320, 54));
        contraintes.gridy = 2;
        add(this.boutonNouvellePartie, contraintes);

        this.boutonCharger = BoutonMedieval.primaire("Charger une sauvegarde");
        this.boutonCharger.setPreferredSize(new Dimension(320, 54));
        contraintes.gridy = 3;
        add(this.boutonCharger, contraintes);

        this.boutonQuitter = BoutonMedieval.primaire("Quitter");
        this.boutonQuitter.setPreferredSize(new Dimension(320, 54));
        contraintes.gridy = 4;
        add(this.boutonQuitter, contraintes);

        // credits en bas
        contraintes.gridy = 5;
        contraintes.insets = new Insets(40, 0, 0, 0);
        JLabel credits = new JLabel("ENSEEIHT 1A - Equipe BA7ION - 2025-2026", SwingConstants.CENTER);
        credits.setFont(Polices.CREDITS);
        credits.setForeground(Palette.TEXTE_TERTIAIRE);
        add(credits, contraintes);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int largeur = getWidth();
        int hauteur = getHeight();

        // le ciel
        GradientPaint ciel = new GradientPaint(0, 0, Palette.FOND_HAUT, 0, hauteur, Palette.FOND_BAS);
        g2.setPaint(ciel);
        g2.fillRect(0, 0, largeur, hauteur);

        // les etoiles
        for (int indice = 0; indice < this.etoilesX.length; indice++) {
            int posX = (int) ((long) this.etoilesX[indice] * largeur / 1280);
            int posY = (int) ((long) this.etoilesY[indice] * hauteur / 800);
            g2.setColor(new Color(1.0f, 1.0f, 0.94f, this.etoilesAlpha[indice]));
            g2.fillOval(posX, posY, 2, 2);
        }

        // la lune
        int lx = largeur - 180;
        int ly = 110;
        int lr = 50;
        // son halo
        Point2D centre = new Point2D.Float(lx, ly);
        float[] fractions = { 0f, 0.5f, 1f };
        Color[] couleurs = {
            new Color(200, 160, 64, 100),
            new Color(200, 160, 64, 40),
            new Color(200, 160, 64, 0)
        };
        RadialGradientPaint halo = new RadialGradientPaint(centre, lr * 2.2f, fractions, couleurs);
        g2.setPaint(halo);
        g2.fillOval(lx - lr * 2, ly - lr * 2, lr * 4, lr * 4);
        // le disque
        g2.setColor(new Color(245, 232, 160));
        g2.fillOval(lx - lr, ly - lr, lr * 2, lr * 2);
        // crateres
        g2.setColor(new Color(208, 192, 128, 80));
        g2.fillOval(lx - 15, ly - 12, 14, 14);
        g2.fillOval(lx + 8, ly + 8, 10, 10);
        g2.fillOval(lx + 16, ly - 8, 7, 7);

        // les montagnes
        g2.setColor(new Color(8, 6, 8, 200));
        int[] mx = { 0, 80, 160, 240, 320, 400, 480, 560, 640, 720, 800, 880, 960, 1040, 1120, 1200, 1280 };
        int[] my = { 480, 360, 440, 320, 400, 280, 380, 260, 360, 240, 350, 270, 380, 280, 390, 290, 400 };
        // on adapte a la largeur reelle
        int[] mxAdapt = new int[mx.length + 2];
        int[] myAdapt = new int[my.length + 2];
        for (int indice = 0; indice < mx.length; indice++) {
            mxAdapt[indice] = (int) ((long) mx[indice] * largeur / 1280);
            myAdapt[indice] = (int) ((long) my[indice] * hauteur / 800);
        }
        mxAdapt[mx.length] = largeur;
        myAdapt[my.length] = hauteur;
        mxAdapt[mx.length + 1] = 0;
        myAdapt[my.length + 1] = hauteur;
        g2.fillPolygon(mxAdapt, myAdapt, mxAdapt.length);

        // les forets sur les bords
        dessinerForet(g2, 0, largeur / 8, hauteur);
        dessinerForet(g2, largeur - largeur / 8, largeur, hauteur);

        // le sol (avant le chateau pour qu'il soit pose dessus)
        int yLigneSol = (int) (hauteur * 0.80);
        g2.setColor(new Color(20, 14, 6));
        g2.fillRect(0, yLigneSol, largeur, hauteur);

        // le chateau
        dessinerChateau(g2, largeur / 2, yLigneSol, largeur, hauteur);

        // le cadre
        g2.setColor(Palette.OR_FONCE);
        g2.setStroke(new java.awt.BasicStroke(2));
        g2.drawRect(8, 8, largeur - 16, hauteur - 16);
        g2.setColor(Palette.OR);
        // les coins
        int co = 16;
        for (int dx : new int[] { 8, largeur - 8 - co }) {
            for (int dy : new int[] { 8, hauteur - 8 - co }) {
                g2.fillPolygon(
                    new int[] { dx, dx + co, dx },
                    new int[] { dy, dy, dy + co },
                    3);
            }
        }

        g2.dispose();
    }

    // une rangee de sapins
    private void dessinerForet(Graphics2D g2, int xMin, int xMax, int hauteurPanneau) {
        g2.setColor(new Color(3, 2, 2));
        int baseY = (int) (hauteurPanneau * 0.78);
        int largeur = 30;
        for (int posX = xMin - 10; posX < xMax + 10; posX += largeur - 6) {
            int hauteur = 40 + (posX * 13) % 50;
            int[] tx = { posX, posX + largeur / 2, posX + largeur };
            int[] ty = { baseY, baseY - hauteur, baseY };
            g2.fillPolygon(tx, ty, 3);
        }
    }

    // un petit chateau (donjon, 2 tours, mur, portail)
    private void dessinerChateau(Graphics2D g2, int cx, int cyBase, int largeur, int hauteur) {
        int echelle = Math.min(largeur, hauteur * 2);
        int unite = echelle / 32; // unite de base

        Color pierre = new Color(28, 22, 18);
        Color pierreSombre = new Color(14, 10, 8);
        Color fenetre = new Color(208, 120, 0);
        Color fenetreGlow = new Color(255, 168, 64, 120);

        // le mur central
        int courtineL = 14 * unite;
        int courtineH = 6 * unite;
        int courtineX = cx - courtineL / 2;
        int courtineY = cyBase - courtineH;
        g2.setColor(pierre);
        g2.fillRect(courtineX, courtineY, courtineL, courtineH);
        dessinerCreneaux(g2, courtineX, courtineY, courtineL, unite, pierreSombre);

        // tour gauche
        int tourL = 4 * unite;
        int tourH = 10 * unite;
        int tourGX = cx - courtineL / 2 - tourL + unite;
        int tourGY = cyBase - tourH;
        g2.setColor(pierre);
        g2.fillRect(tourGX, tourGY, tourL, tourH);
        dessinerCreneaux(g2, tourGX - unite / 2, tourGY, tourL + unite, unite, pierreSombre);
        // sa fenetre
        g2.setColor(fenetre);
        g2.fillRoundRect(tourGX + tourL / 3, tourGY + tourH / 4,
                tourL / 3, tourH / 4, 8, 8);

        // tour droite
        int tourDX = cx + courtineL / 2 - unite;
        int tourDY = cyBase - tourH;
        g2.setColor(pierre);
        g2.fillRect(tourDX, tourDY, tourL, tourH);
        dessinerCreneaux(g2, tourDX - unite / 2, tourDY, tourL + unite, unite, pierreSombre);
        // sa fenetre
        g2.setColor(fenetre);
        g2.fillRoundRect(tourDX + tourL / 3, tourDY + tourH / 4,
                tourL / 3, tourH / 4, 8, 8);

        // le donjon
        int donjonL = 6 * unite;
        int donjonH = 14 * unite;
        int donjonX = cx - donjonL / 2;
        int donjonY = cyBase - donjonH;
        g2.setColor(pierre);
        g2.fillRect(donjonX, donjonY, donjonL, donjonH);
        dessinerCreneaux(g2, donjonX - unite / 2, donjonY, donjonL + unite, unite, pierreSombre);
        // le toit pointu
        int fY = donjonY - 4 * unite;
        g2.setColor(pierreSombre);
        g2.fillPolygon(
            new int[] { donjonX + donjonL / 2, donjonX, donjonX + donjonL },
            new int[] { fY, donjonY, donjonY },
            3);
        // la banniere
        g2.setColor(Palette.ROUGE_BANNIERE);
        int bX = donjonX + donjonL / 2 + 2;
        g2.fillPolygon(
            new int[] { bX, bX + unite, bX },
            new int[] { fY + unite, fY + unite + unite / 2, fY + 2 * unite },
            3);

        // les fenetres du donjon (allumees)
        g2.setColor(fenetre);
        int fenL = unite;
        int fenH = (int) (1.5 * unite);
        // la grande au milieu
        g2.fillRoundRect(donjonX + donjonL / 2 - fenL,
                donjonY + 2 * unite, fenL * 2, fenH, 8, 8);
        g2.setColor(fenetreGlow);
        g2.fillOval(donjonX + donjonL / 2 - fenL - 5,
                donjonY + 2 * unite - 5, fenL * 2 + 10, fenH + 10);
        // les deux du dessous
        g2.setColor(fenetre);
        g2.fillRoundRect(donjonX + unite / 2, donjonY + 5 * unite,
                fenL, fenH, 6, 6);
        g2.fillRoundRect(donjonX + donjonL - fenL - unite / 2,
                donjonY + 5 * unite, fenL, fenH, 6, 6);

        // le portail
        int portL = (int) (2.5 * unite);
        int portH = 4 * unite;
        int portX = cx - portL / 2;
        int portY = cyBase - portH;
        g2.setColor(new Color(3, 2, 6));
        g2.fillArc(portX, portY - portL / 2, portL, portL, 0, 180);
        g2.fillRect(portX, portY - portL / 4, portL, portH);
        // sa bordure
        g2.setColor(Palette.OR_FONCE);
        g2.setStroke(new java.awt.BasicStroke(2));
        g2.drawArc(portX, portY - portL / 2, portL, portL, 0, 180);

        // les torches
        dessinerTorche(g2, tourGX + tourL + unite / 2, tourGY + tourH / 2);
        dessinerTorche(g2, tourDX - unite / 2, tourDY + tourH / 2);
    }

    private void dessinerCreneaux(Graphics2D g2, int posX, int posY, int largeur,
                                  int unite, Color couleur) {
        g2.setColor(couleur);
        int largeurCreneau = unite;
        int hauteurCreneau = unite / 2 + 2;
        for (int dx = 0; dx < largeur; dx += largeurCreneau * 2) {
            g2.fillRect(posX + dx, posY - hauteurCreneau, largeurCreneau, hauteurCreneau);
        }
    }

    private void dessinerTorche(Graphics2D g2, int posX, int posY) {
        // le halo
        Point2D centre = new Point2D.Float(posX, posY);
        float[] fractions = { 0f, 1f };
        Color[] couleurs = {
            new Color(208, 96, 16, 140),
            new Color(208, 96, 16, 0)
        };
        RadialGradientPaint halo = new RadialGradientPaint(centre, 50, fractions, couleurs);
        g2.setPaint(halo);
        g2.fillOval(posX - 50, posY - 50, 100, 100);
        // la flamme
        g2.setColor(new Color(232, 96, 16));
        g2.fillOval(posX - 5, posY - 10, 10, 16);
        g2.setColor(new Color(255, 168, 32));
        g2.fillOval(posX - 3, posY - 14, 6, 10);
        g2.setColor(new Color(255, 232, 96));
        g2.fillOval(posX - 2, posY - 16, 4, 6);
    }

    /**
     * Renvoie le bouton Nouvelle partie.
     *
     * @return le bouton Nouvelle partie
     */
    public BoutonMedieval boutonNouvellePartie() {
        return this.boutonNouvellePartie;
    }

    /**
     * Renvoie le bouton Charger une sauvegarde.
     *
     * @return le bouton Charger
     */
    public BoutonMedieval boutonCharger() {
        return this.boutonCharger;
    }

    /**
     * Renvoie le bouton Quitter.
     *
     * @return le bouton Quitter
     */
    public BoutonMedieval boutonQuitter() {
        return this.boutonQuitter;
    }
}
