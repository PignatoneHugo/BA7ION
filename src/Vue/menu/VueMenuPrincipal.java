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

import Vue.i18n.Traducteur;
import Vue.theme.BoutonMedieval;
import Vue.theme.Palette;
import Vue.theme.Polices;

/**
 * Ecran d'accueil ultra-stylise medieval : ciel nocturne avec lune et
 * etoiles, montagnes en silhouette, chateau fort au centre, torches et
 * bordures ornementales.
 *
 * Le dessin est entierement programmatique (Java2D) : pas d'image externe
 * a charger. Le rendu reste fluide grace au cache aleatoire (etoiles
 * stockees une fois pour toutes).
 */
public class VueMenuPrincipal extends JPanel {

    private static final long serialVersionUID = 1L;

    private final BoutonMedieval boutonNouvellePartie;
    private final BoutonMedieval boutonOptions;
    private final BoutonMedieval boutonQuitter;

    // Positions des etoiles, generees une fois pour toutes
    private final int[] etoilesX;
    private final int[] etoilesY;
    private final float[] etoilesAlpha;

    public VueMenuPrincipal() {
        setOpaque(true);
        setBackground(Palette.FOND_BAS);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Generation des etoiles (deterministe grace au seed fixe)
        Random r = new Random(42);
        int nbEtoiles = 60;
        this.etoilesX = new int[nbEtoiles];
        this.etoilesY = new int[nbEtoiles];
        this.etoilesAlpha = new float[nbEtoiles];
        for (int i = 0; i < nbEtoiles; i++) {
            this.etoilesX[i] = r.nextInt(1280);
            this.etoilesY[i] = r.nextInt(180);
            this.etoilesAlpha[i] = 0.4f + r.nextFloat() * 0.6f;
        }

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;

        // ESPACE EN HAUT pour la lune et le titre
        c.gridy = 0;
        c.insets = new Insets(60, 0, 8, 0);
        JLabel titre = new JLabel(Traducteur.t("menu.titre"), SwingConstants.CENTER);
        titre.setFont(Polices.TITRE.deriveFont(80f));
        titre.setForeground(Palette.OR);
        add(titre, c);

        // Sous-titre
        c.gridy = 1;
        c.insets = new Insets(0, 0, 350, 0);
        JLabel sousTitre = new JLabel(Traducteur.t("menu.sous_titre"), SwingConstants.CENTER);
        sousTitre.setFont(Polices.SOUS_TITRE.deriveFont(18f));
        sousTitre.setForeground(Palette.TEXTE_PRIMAIRE);
        add(sousTitre, c);

        c.insets = new Insets(10, 0, 10, 0);

        // BOUTONS
        this.boutonNouvellePartie = BoutonMedieval.primaire(Traducteur.t("menu.nouvelle_partie"));
        this.boutonNouvellePartie.setPreferredSize(new Dimension(320, 54));
        c.gridy = 2;
        add(this.boutonNouvellePartie, c);

        this.boutonOptions = BoutonMedieval.primaire(Traducteur.t("menu.options"));
        this.boutonOptions.setPreferredSize(new Dimension(320, 54));
        c.gridy = 3;
        add(this.boutonOptions, c);

        this.boutonQuitter = BoutonMedieval.primaire(Traducteur.t("menu.quitter"));
        this.boutonQuitter.setPreferredSize(new Dimension(320, 54));
        c.gridy = 4;
        add(this.boutonQuitter, c);

        // Footer credits
        c.gridy = 5;
        c.insets = new Insets(40, 0, 0, 0);
        JLabel credits = new JLabel(Traducteur.t("menu.credits"), SwingConstants.CENTER);
        credits.setFont(Polices.CREDITS);
        credits.setForeground(Palette.TEXTE_TERTIAIRE);
        add(credits, c);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // 1. Ciel degrade
        GradientPaint ciel = new GradientPaint(0, 0, Palette.FOND_HAUT, 0, h, Palette.FOND_BAS);
        g2.setPaint(ciel);
        g2.fillRect(0, 0, w, h);

        // 2. Etoiles
        for (int i = 0; i < this.etoilesX.length; i++) {
            int x = (int) ((long) this.etoilesX[i] * w / 1280);
            int y = (int) ((long) this.etoilesY[i] * h / 800);
            g2.setColor(new Color(1.0f, 1.0f, 0.94f, this.etoilesAlpha[i]));
            g2.fillOval(x, y, 2, 2);
        }

        // 3. Lune en haut a droite avec halo
        int lx = w - 180;
        int ly = 110;
        int lr = 50;
        // Halo
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
        // Disque lunaire
        g2.setColor(new Color(245, 232, 160));
        g2.fillOval(lx - lr, ly - lr, lr * 2, lr * 2);
        // Crateres
        g2.setColor(new Color(208, 192, 128, 80));
        g2.fillOval(lx - 15, ly - 12, 14, 14);
        g2.fillOval(lx + 8, ly + 8, 10, 10);
        g2.fillOval(lx + 16, ly - 8, 7, 7);

        // 4. Montagnes en silhouette
        g2.setColor(new Color(8, 6, 8, 200));
        int[] mx = { 0, 80, 160, 240, 320, 400, 480, 560, 640, 720, 800, 880, 960, 1040, 1120, 1200, 1280 };
        int[] my = { 480, 360, 440, 320, 400, 280, 380, 260, 360, 240, 350, 270, 380, 280, 390, 290, 400 };
        // Adapter au width reel
        int[] mxAdapt = new int[mx.length + 2];
        int[] myAdapt = new int[my.length + 2];
        for (int i = 0; i < mx.length; i++) {
            mxAdapt[i] = (int) ((long) mx[i] * w / 1280);
            myAdapt[i] = (int) ((long) my[i] * h / 800);
        }
        mxAdapt[mx.length] = w;
        myAdapt[my.length] = h;
        mxAdapt[mx.length + 1] = 0;
        myAdapt[my.length + 1] = h;
        g2.fillPolygon(mxAdapt, myAdapt, mxAdapt.length);

        // 5. Foret silhouette aux bords
        dessinerForet(g2, 0, w / 8, h);
        dessinerForet(g2, w - w / 8, w, h);

        // 6. Sol (dessine AVANT le chateau pour que le chateau soit pose dessus)
        int yLigneSol = (int) (h * 0.80);
        g2.setColor(new Color(20, 14, 6));
        g2.fillRect(0, yLigneSol, w, h);

        // 7. Chateau central - sa base s'aligne sur la ligne du sol
        dessinerChateau(g2, w / 2, yLigneSol, w, h);

        // 8. Cadre ornemental
        g2.setColor(Palette.OR_FONCE);
        g2.setStroke(new java.awt.BasicStroke(2));
        g2.drawRect(8, 8, w - 16, h - 16);
        g2.setColor(Palette.OR);
        // Coins
        int co = 16;
        for (int dx : new int[] { 8, w - 8 - co }) {
            for (int dy : new int[] { 8, h - 8 - co }) {
                g2.fillPolygon(
                    new int[] { dx, dx + co, dx },
                    new int[] { dy, dy, dy + co },
                    3);
            }
        }

        g2.dispose();
    }

    /** Dessine une bande de sapins entre les abscisses xMin et xMax. */
    private void dessinerForet(Graphics2D g2, int xMin, int xMax, int h) {
        g2.setColor(new Color(3, 2, 2));
        int baseY = (int) (h * 0.78);
        int largeur = 30;
        for (int x = xMin - 10; x < xMax + 10; x += largeur - 6) {
            int hauteur = 40 + (x * 13) % 50;
            int[] tx = { x, x + largeur / 2, x + largeur };
            int[] ty = { baseY, baseY - hauteur, baseY };
            g2.fillPolygon(tx, ty, 3);
        }
    }

    /**
     * Dessine un chateau fort simplifie centre sur (cx, cy_base).
     * Composition : donjon central, 2 tours laterales, courtine, portail.
     */
    private void dessinerChateau(Graphics2D g2, int cx, int cyBase, int w, int h) {
        int echelle = Math.min(w, h * 2);
        int unite = echelle / 32; // Unite de base pour les dimensions

        // Couleurs
        Color pierre = new Color(28, 22, 18);
        Color pierreSombre = new Color(14, 10, 8);
        Color fenetre = new Color(208, 120, 0);
        Color fenetreGlow = new Color(255, 168, 64, 120);

        // === COURTINE (mur central entre les tours) ===
        int courtineL = 14 * unite;
        int courtineH = 6 * unite;
        int courtineX = cx - courtineL / 2;
        int courtineY = cyBase - courtineH;
        g2.setColor(pierre);
        g2.fillRect(courtineX, courtineY, courtineL, courtineH);
        // Creneaux courtine
        dessinerCreneaux(g2, courtineX, courtineY, courtineL, unite, pierreSombre);

        // === TOUR GAUCHE ===
        int tourL = 4 * unite;
        int tourH = 10 * unite;
        int tourGX = cx - courtineL / 2 - tourL + unite;
        int tourGY = cyBase - tourH;
        g2.setColor(pierre);
        g2.fillRect(tourGX, tourGY, tourL, tourH);
        dessinerCreneaux(g2, tourGX - unite / 2, tourGY, tourL + unite, unite, pierreSombre);
        // Fenetre tour gauche
        g2.setColor(fenetre);
        g2.fillRoundRect(tourGX + tourL / 3, tourGY + tourH / 4,
                tourL / 3, tourH / 4, 8, 8);

        // === TOUR DROITE ===
        int tourDX = cx + courtineL / 2 - unite;
        int tourDY = cyBase - tourH;
        g2.setColor(pierre);
        g2.fillRect(tourDX, tourDY, tourL, tourH);
        dessinerCreneaux(g2, tourDX - unite / 2, tourDY, tourL + unite, unite, pierreSombre);
        // Fenetre tour droite
        g2.setColor(fenetre);
        g2.fillRoundRect(tourDX + tourL / 3, tourDY + tourH / 4,
                tourL / 3, tourH / 4, 8, 8);

        // === DONJON CENTRAL ===
        int donjonL = 6 * unite;
        int donjonH = 14 * unite;
        int donjonX = cx - donjonL / 2;
        int donjonY = cyBase - donjonH;
        g2.setColor(pierre);
        g2.fillRect(donjonX, donjonY, donjonL, donjonH);
        dessinerCreneaux(g2, donjonX - unite / 2, donjonY, donjonL + unite, unite, pierreSombre);
        // Fleche du donjon
        int fY = donjonY - 4 * unite;
        g2.setColor(pierreSombre);
        g2.fillPolygon(
            new int[] { donjonX + donjonL / 2, donjonX, donjonX + donjonL },
            new int[] { fY, donjonY, donjonY },
            3);
        // Banniere sur la fleche
        g2.setColor(Palette.ROUGE_BANNIERE);
        int bX = donjonX + donjonL / 2 + 2;
        g2.fillPolygon(
            new int[] { bX, bX + unite, bX },
            new int[] { fY + unite, fY + unite + unite / 2, fY + 2 * unite },
            3);

        // Fenetres donjon (illuminees)
        g2.setColor(fenetre);
        int fenL = unite;
        int fenH = (int) (1.5 * unite);
        // Grande fenetre haute centrale
        g2.fillRoundRect(donjonX + donjonL / 2 - fenL,
                donjonY + 2 * unite, fenL * 2, fenH, 8, 8);
        g2.setColor(fenetreGlow);
        g2.fillOval(donjonX + donjonL / 2 - fenL - 5,
                donjonY + 2 * unite - 5, fenL * 2 + 10, fenH + 10);
        // Fenetres laterales
        g2.setColor(fenetre);
        g2.fillRoundRect(donjonX + unite / 2, donjonY + 5 * unite,
                fenL, fenH, 6, 6);
        g2.fillRoundRect(donjonX + donjonL - fenL - unite / 2,
                donjonY + 5 * unite, fenL, fenH, 6, 6);

        // === PORTAIL central dans la courtine ===
        int portL = (int) (2.5 * unite);
        int portH = 4 * unite;
        int portX = cx - portL / 2;
        int portY = cyBase - portH;
        g2.setColor(new Color(3, 2, 6));
        g2.fillArc(portX, portY - portL / 2, portL, portL, 0, 180);
        g2.fillRect(portX, portY - portL / 4, portL, portH);
        // Bordure portail
        g2.setColor(Palette.OR_FONCE);
        g2.setStroke(new java.awt.BasicStroke(2));
        g2.drawArc(portX, portY - portL / 2, portL, portL, 0, 180);

        // === TORCHES au pied des tours ===
        dessinerTorche(g2, tourGX + tourL + unite / 2, tourGY + tourH / 2);
        dessinerTorche(g2, tourDX - unite / 2, tourDY + tourH / 2);
    }

    private void dessinerCreneaux(Graphics2D g2, int x, int y, int largeur,
                                  int unite, Color couleur) {
        g2.setColor(couleur);
        int largeurCreneau = unite;
        int hauteurCreneau = unite / 2 + 2;
        for (int dx = 0; dx < largeur; dx += largeurCreneau * 2) {
            g2.fillRect(x + dx, y - hauteurCreneau, largeurCreneau, hauteurCreneau);
        }
    }

    private void dessinerTorche(Graphics2D g2, int x, int y) {
        // Halo lumineux
        Point2D centre = new Point2D.Float(x, y);
        float[] fractions = { 0f, 1f };
        Color[] couleurs = {
            new Color(208, 96, 16, 140),
            new Color(208, 96, 16, 0)
        };
        RadialGradientPaint halo = new RadialGradientPaint(centre, 50, fractions, couleurs);
        g2.setPaint(halo);
        g2.fillOval(x - 50, y - 50, 100, 100);
        // Flamme
        g2.setColor(new Color(232, 96, 16));
        g2.fillOval(x - 5, y - 10, 10, 16);
        g2.setColor(new Color(255, 168, 32));
        g2.fillOval(x - 3, y - 14, 6, 10);
        g2.setColor(new Color(255, 232, 96));
        g2.fillOval(x - 2, y - 16, 4, 6);
    }

    public BoutonMedieval boutonNouvellePartie() {
        return this.boutonNouvellePartie;
    }

    public BoutonMedieval boutonOptions() {
        return this.boutonOptions;
    }

    public BoutonMedieval boutonQuitter() {
        return this.boutonQuitter;
    }
}
