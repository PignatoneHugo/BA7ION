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
import Vue.i18n.Traducteur;
import Vue.theme.BoutonMedieval;
import Vue.theme.Palette;
import Vue.theme.Polices;

/**
 * Bandeau permanent en haut de la fenetre de jeu, style medieval :
 * fond pierre sombre, icones circulaires colorees pour les 5 ressources,
 * compteur de tour or, indicateurs population et moral, gros bouton "Fin
 * de tour" rouge danger.
 *
 * Observer de Partie (tour) et du Royaume joueur (ressources, pop, moral).
 */
public class VueHUD extends JPanel implements Observer {

    private static final long serialVersionUID = 1L;

    private final Partie partie;
    private final Royaume royaumeJoueur;

    private final JLabel labelTour;
    private final JLabel labelPopulation;
    private final JLabel labelMoral;
    private final Map<Ressource, CompteurRessource> compteurs;
    private final BoutonMedieval boutonFinTour;

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

        // === Bloc de gauche : 5 ressources ===
        JPanel gauche = new JPanel(new GridLayout(1, Ressource.values().length, 12, 0));
        gauche.setOpaque(false);
        for (Ressource r : Ressource.values()) {
            CompteurRessource c = new CompteurRessource(r);
            this.compteurs.put(r, c);
            gauche.add(c);
        }
        add(gauche, BorderLayout.WEST);

        // === Bloc central : tour + population + moral ===
        // GridLayout(1, 3) : garantit que les 3 encadres sont toujours
        // visibles, meme si la fenetre est etroite (sur portable). Ils se
        // partagent l'espace dispo equitablement.
        JPanel centre = new JPanel(new GridLayout(1, 3, 10, 0));
        centre.setOpaque(false);

        this.labelTour = creerEncadre(Traducteur.t("hud.tour"), "1", Palette.OR_CLAIR, 100);
        centre.add(this.labelTour);

        this.labelPopulation = creerEncadre(Traducteur.t("population.total"), "10 / 20",
                Palette.TEXTE_PRIMAIRE, 120);
        centre.add(this.labelPopulation);

        this.labelMoral = creerEncadre(Traducteur.t("moral.titre"), "50 / 100",
                Palette.TEXTE_PRIMAIRE, 100);
        centre.add(this.labelMoral);

        add(centre, BorderLayout.CENTER);

        // === Bloc droit : bouton fin de tour ===
        JPanel droite = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        droite.setOpaque(false);
        this.boutonFinTour = new BoutonMedieval(Traducteur.t("app.fin_tour"),
                BoutonMedieval.Style.DANGER);
        this.boutonFinTour.setPreferredSize(new Dimension(150, 64));
        droite.add(this.boutonFinTour);
        add(droite, BorderLayout.EAST);

        rafraichir();
        this.partie.addObserver(this);
        this.royaumeJoueur.addObserver(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Fond pierre degrade vertical
        GradientPaint grad = new GradientPaint(0, 0, new Color(30, 20, 8),
                0, getHeight(), new Color(10, 8, 4));
        g2.setPaint(grad);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }

    /** Crée un encadré "label / valeur" centré et bordé d'or. */
    private JLabel creerEncadre(String titre, String valeur, Color couleur, int largeur) {
        JLabel l = new JLabel("<html><div style='text-align:center;'>"
                + "<span style='font-size:10px; color:#6a4820;'>"
                + titre.toUpperCase()
                + "</span><br><span style='font-size:18px;'>" + valeur
                + "</span></div></html>", SwingConstants.CENTER);
        l.setForeground(couleur);
        l.setFont(Polices.LABEL);
        l.setPreferredSize(new Dimension(largeur, 68));
        l.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Palette.OR_FONCE, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        l.setOpaque(true);
        l.setBackground(new Color(8, 6, 10, 180));
        return l;
    }

    public BoutonMedieval boutonFinTour() {
        return this.boutonFinTour;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(arg instanceof Notification)) {
            return;
        }
        Notification n = (Notification) arg;
        switch (n.type()) {
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
        for (Ressource r : Ressource.values()) {
            this.compteurs.get(r).rafraichir(
                    this.royaumeJoueur.tresor().quantite(r),
                    this.royaumeJoueur.tresor().capaciteMax(r));
        }

        this.labelTour.setText("<html><div style='text-align:center;'>"
                + "<span style='font-size:10px; color:#6a4820;'>"
                + Traducteur.t("hud.tour").toUpperCase()
                + "</span><br><span style='font-size:22px;'>"
                + this.partie.numeroTour()
                + "</span></div></html>");

        // Population affichee = civils + soldats. Les unites combattantes
        // font toujours partie du royaume, juste plus du pool civil.
        int total = this.royaumeJoueur.population().total()
                + this.royaumeJoueur.armee().effectifTotal();
        int cap = this.royaumeJoueur.population().capaciteLogement();
        this.labelPopulation.setText("<html><div style='text-align:center;'>"
                + "<span style='font-size:10px; color:#6a4820;'>"
                + Traducteur.t("population.total").toUpperCase()
                + "</span><br><span style='font-size:18px;'>"
                + total + " / " + cap + "</span></div></html>");

        int moral = this.royaumeJoueur.moral().valeur();
        Color couleurMoral = moral >= 60 ? Palette.VERT_POSITIF
                : moral >= 30 ? Palette.OR_CLAIR : Palette.ROUGE_CLAIR;
        String couleurHex = String.format("#%02x%02x%02x",
                couleurMoral.getRed(), couleurMoral.getGreen(), couleurMoral.getBlue());
        this.labelMoral.setText("<html><div style='text-align:center;'>"
                + "<span style='font-size:10px; color:#6a4820;'>"
                + Traducteur.t("moral.titre").toUpperCase()
                + "</span><br><span style='font-size:18px; color:" + couleurHex + ";'>"
                + moral + " / 100</span></div></html>");
    }

    // ============================================================
    // Petit composant : icone circulaire + nom + quantite/max
    // ============================================================
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
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Cercle icone a gauche
            int diam = 36;
            int cx = 4;
            int cy = (h - diam) / 2;
            g2.setColor(this.couleur.darker().darker());
            g2.fillOval(cx, cy, diam, diam);
            g2.setColor(this.couleur);
            g2.setStroke(new java.awt.BasicStroke(2));
            g2.drawOval(cx, cy, diam, diam);
            g2.fillOval(cx + 6, cy + 6, diam - 12, diam - 12);

            // Lettre au centre du cercle
            g2.setColor(this.couleur.darker());
            g2.setFont(Polices.VALEUR.deriveFont(14f));
            String lettre = String.valueOf(this.ressource.name().charAt(0));
            int fw = g2.getFontMetrics().stringWidth(lettre);
            g2.drawString(lettre, cx + diam / 2 - fw / 2, cy + diam / 2 + 5);

            // Label nom de ressource
            int xText = cx + diam + 8;
            g2.setColor(Palette.TEXTE_TERTIAIRE);
            g2.setFont(Polices.PETIT_LABEL);
            g2.drawString(Traducteur.t(this.ressource.cleI18n()).toUpperCase(), xText, 14);

            // Valeur
            g2.setColor(this.couleur);
            g2.setFont(Polices.VALEUR);
            g2.drawString(String.valueOf(this.quantite), xText, 35);

            // Barre de progression
            int barX = xText;
            int barY = 44;
            int barW = w - xText - 8;
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
            // Capacite max en petit
            g2.setColor(Palette.TEXTE_TERTIAIRE);
            g2.setFont(Polices.PETIT_LABEL);
            String cap = "/ " + this.capacite;
            g2.drawString(cap, barX, barY + 18);

            g2.dispose();
        }

        private static Color couleurDe(Ressource r) {
            switch (r) {
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
