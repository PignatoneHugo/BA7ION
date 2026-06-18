package Vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.util.EnumMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Modele.economie.Ressource;
import Modele.notification.Notification;
import Modele.partie.Partie;
import Modele.royaume.Royaume;
import Vue.theme.BoutonMedieval;
import Vue.theme.Palette;
import Vue.theme.Polices;

/** Bandeau du haut : les 5 ressources, le tour, la population, le moral et le bouton fin de tour. */
public class VueHUD extends JPanel implements Observer {

    private static final long serialVersionUID = 1L;

    private final Partie partie;
    private final Royaume royaumeJoueur;

    private final JLabel labelTour;
    private final JLabel labelPopulation;
    private final JLabel labelMoral;
    private final Map<Ressource, CompteurRessource> compteurs;
    private final BoutonMedieval boutonFinTour;

    /**
     * Cree le bandeau du haut et s'abonne aux changements de la partie.
     *
     * @param partie la partie en cours
     */
    public VueHUD(Partie partie) {
        this.partie = partie;
        this.royaumeJoueur = partie.joueur();
        this.compteurs = new EnumMap<>(Ressource.class);

        setOpaque(true);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, Palette.OR),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        setPreferredSize(new Dimension(1280, 90));

        // les 5 ressources a gauche
        JPanel gauche = new JPanel(new GridLayout(1, Ressource.values().length, 12, 0));
        gauche.setOpaque(false);
        for (Ressource ressource : Ressource.values()) {
            CompteurRessource compteur = new CompteurRessource(ressource);
            this.compteurs.put(ressource, compteur);
            gauche.add(compteur);
        }
        add(gauche, BorderLayout.WEST);

        // au centre : tour + population + moral
        JPanel centre = new JPanel(new GridLayout(1, 3, 10, 0));
        centre.setOpaque(false);

        this.labelTour = creerEncadre("Tour", "1", Palette.OR_CLAIR, 100);
        centre.add(this.labelTour);

        this.labelPopulation = creerEncadre("Population", "10 / 20",
                Palette.TEXTE_PRIMAIRE, 120);
        centre.add(this.labelPopulation);

        this.labelMoral = creerEncadre("Moral", "50 / 100",
                Palette.TEXTE_PRIMAIRE, 100);
        centre.add(this.labelMoral);

        add(centre, BorderLayout.CENTER);

        // bouton fin de tour a droite
        JPanel droite = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        droite.setOpaque(false);
        this.boutonFinTour = new BoutonMedieval("Fin de tour",
                BoutonMedieval.Style.DANGER);
        this.boutonFinTour.setPreferredSize(new Dimension(150, 64));
        droite.add(this.boutonFinTour);
        add(droite, BorderLayout.EAST);

        rafraichir();
        this.partie.addObserver(this);
        this.royaumeJoueur.addObserver(this);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // fond degrade
        GradientPaint grad = new GradientPaint(0, 0, new Color(30, 20, 8),
                0, getHeight(), new Color(10, 8, 4));
        g2.setPaint(grad);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }

    // petit encadre "titre / valeur"
    private JLabel creerEncadre(String titre, String valeur, Color couleur, int largeur) {
        JLabel label = new JLabel("<html><div style='text-align:center;'>"
                + "<span style='font-size:10px; color:#6a4820;'>"
                + titre.toUpperCase()
                + "</span><br><span style='font-size:18px;'>" + valeur
                + "</span></div></html>", SwingConstants.CENTER);
        label.setForeground(couleur);
        label.setFont(Polices.LABEL);
        label.setPreferredSize(new Dimension(largeur, 68));
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Palette.OR_FONCE, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        label.setOpaque(true);
        label.setBackground(new Color(8, 6, 10, 180));
        return label;
    }

    /**
     * Renvoie le bouton de fin de tour.
     *
     * @return le bouton de fin de tour
     */
    public BoutonMedieval boutonFinTour() {
        return this.boutonFinTour;
    }

    /**
     * Met a jour le bandeau quand le modele change.
     *
     * @param observable l'objet observe
     * @param arg la notification recue
     */
    @Override
    public void update(Observable observable, Object arg) {
        if (!(arg instanceof Notification)) {
            return;
        }
        Notification notification = (Notification) arg;
        switch (notification.type()) {
            case TRESOR_CHANGE:
            case POPULATION_CHANGEE:
            case MORAL_CHANGE:
            case TOUR_TERMINE:
            case TOUR_DEMARRE:
            case PHASE_CHANGEE:
                rafraichir();
                break;
            default:
                break;
        }
    }

    private void rafraichir() {
        for (Ressource ressource : Ressource.values()) {
            this.compteurs.get(ressource).rafraichir(
                    this.royaumeJoueur.tresor().quantite(ressource),
                    this.royaumeJoueur.tresor().capaciteMax(ressource));
        }

        this.labelTour.setText("<html><div style='text-align:center;'>"
                + "<span style='font-size:10px; color:#6a4820;'>"
                + "Tour".toUpperCase()
                + "</span><br><span style='font-size:22px;'>"
                + this.partie.numeroTour()
                + "</span></div></html>");

        // population = civils + soldats
        int total = this.royaumeJoueur.population().total()
                + this.royaumeJoueur.armee().effectifTotal();
        int cap = this.royaumeJoueur.population().capaciteLogement();
        this.labelPopulation.setText("<html><div style='text-align:center;'>"
                + "<span style='font-size:10px; color:#6a4820;'>"
                + "Population".toUpperCase()
                + "</span><br><span style='font-size:18px;'>"
                + total + " / " + cap + "</span></div></html>");

        int moral = this.royaumeJoueur.moral().valeur();
        Color couleurMoral = moral >= 60 ? Palette.VERT_POSITIF
                : moral >= 30 ? Palette.OR_CLAIR : Palette.ROUGE_CLAIR;
        String couleurHex = String.format("#%02x%02x%02x",
                couleurMoral.getRed(), couleurMoral.getGreen(), couleurMoral.getBlue());
        this.labelMoral.setText("<html><div style='text-align:center;'>"
                + "<span style='font-size:10px; color:#6a4820;'>"
                + "Moral".toUpperCase()
                + "</span><br><span style='font-size:18px; color:" + couleurHex + ";'>"
                + moral + " / 100</span></div></html>");
    }

    // un compteur de ressource : rond + nom + quantite/max
    private static class CompteurRessource extends JPanel {
        private static final long serialVersionUID = 1L;

        private final Ressource ressource;
        private final Color couleur;
        private int quantite;
        private int capacite;

        CompteurRessource(Ressource ressource) {
            this.ressource = ressource;
            this.couleur = couleurDe(ressource);
            setOpaque(false);
            setPreferredSize(new Dimension(132, 68));
        }

        void rafraichir(int quantite, int capacite) {
            this.quantite = quantite;
            this.capacite = capacite;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int largeur = getWidth();
            int hauteur = getHeight();

            // le rond a gauche
            int diam = 36;
            int cx = 4;
            int cy = (hauteur - diam) / 2;
            g2.setColor(this.couleur.darker().darker());
            g2.fillOval(cx, cy, diam, diam);
            g2.setColor(this.couleur);
            g2.setStroke(new java.awt.BasicStroke(2));
            g2.drawOval(cx, cy, diam, diam);
            g2.fillOval(cx + 6, cy + 6, diam - 12, diam - 12);

            // lettre au centre
            g2.setColor(this.couleur.darker());
            g2.setFont(Polices.VALEUR.deriveFont(14f));
            String lettre = String.valueOf(this.ressource.name().charAt(0));
            int fw = g2.getFontMetrics().stringWidth(lettre);
            g2.drawString(lettre, cx + diam / 2 - fw / 2, cy + diam / 2 + 5);

            // nom de la ressource
            int xText = cx + diam + 8;
            g2.setColor(Palette.TEXTE_TERTIAIRE);
            g2.setFont(Polices.PETIT_LABEL);
            g2.drawString(this.ressource.libelle().toUpperCase(), xText, 14);

            // la valeur
            g2.setColor(this.couleur);
            g2.setFont(Polices.VALEUR);
            g2.drawString(String.valueOf(this.quantite), xText, 35);

            // barre de remplissage
            int barX = xText;
            int barY = 44;
            int barW = largeur - xText - 8;
            int barH = 6;
            g2.setColor(new Color(26, 16, 6));
            g2.fillRoundRect(barX, barY, barW, barH, 4, 4);
            g2.setColor(Palette.BORDURE_FONCEE);
            g2.drawRoundRect(barX, barY, barW, barH, 4, 4);
            int rempli = this.capacite > 0
                    ? (int) ((long) this.quantite * barW / this.capacite)
                    : 0;
            if (rempli > 0) {
                g2.setColor(this.couleur);
                g2.fillRoundRect(barX, barY, Math.min(rempli, barW), barH, 4, 4);
            }
            // le max
            g2.setColor(Palette.TEXTE_TERTIAIRE);
            g2.setFont(Polices.PETIT_LABEL);
            String cap = "/ " + this.capacite;
            g2.drawString(cap, barX, barY + 18);

            g2.dispose();
        }

        private static Color couleurDe(Ressource ressource) {
            switch (ressource) {
                case OR: return Palette.OR_RESSOURCE;
                case NOURRITURE: return Palette.NOURRITURE_RESSOURCE;
                case BOIS: return Palette.BOIS_RESSOURCE;
                case PIERRE: return Palette.PIERRE_RESSOURCE;
                case SAVOIR: return Palette.SAVOIR_RESSOURCE;
                default: return Palette.TEXTE_PRIMAIRE;
            }
        }
    }
}
