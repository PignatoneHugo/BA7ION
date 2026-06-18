package Vue;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Modele.partie.Partie;
import Vue.onglets.OngletEconomie;
import Vue.onglets.OngletInfrastructures;
import Vue.onglets.OngletMarche;
import Vue.onglets.OngletMilitaire;
import Vue.theme.Palette;
import Vue.theme.Polices;

/** Panneau central avec nos propres onglets (Economie, Infra, Militaire, Marche). */
public class VueDashboard extends JPanel {

    private static final long serialVersionUID = 1L;

    private final OngletEconomie ongletEconomie;
    private final OngletInfrastructures ongletInfrastructures;
    private final OngletMilitaire ongletMilitaire;
    private final OngletMarche ongletMarche;

    private final CardLayout cards;
    private final JPanel zoneOnglets;
    private final BarreOnglet barreEconomie;
    private final BarreOnglet barreInfrastructures;
    private final BarreOnglet barreMilitaire;
    private final BarreOnglet barreMarche;

    /**
     * Cree le panneau central avec ses quatre onglets.
     *
     * @param partie la partie en cours
     */
    public VueDashboard(Partie partie) {
        setOpaque(true);
        setBackground(Palette.FOND_BAS);
        setLayout(new BorderLayout());

        this.ongletEconomie = new OngletEconomie(partie.joueur());
        this.ongletInfrastructures = new OngletInfrastructures(partie.joueur());
        this.ongletMilitaire = new OngletMilitaire(partie);
        this.ongletMarche = new OngletMarche(partie.joueur());

        // barre d'onglets
        JPanel barre = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        barre.setOpaque(true);
        barre.setBackground(new Color(20, 14, 6));
        barre.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Palette.OR));
        barre.setPreferredSize(new Dimension(1280, 44));

        this.barreEconomie = new BarreOnglet("Economie", true);
        this.barreInfrastructures = new BarreOnglet("Infrastructures", false);
        this.barreMilitaire = new BarreOnglet("Militaire", false);
        this.barreMarche = new BarreOnglet("Marche", false);

        this.barreEconomie.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evenement) {
                afficherOnglet("eco");
            }
        });
        this.barreInfrastructures.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evenement) {
                afficherOnglet("infra");
            }
        });
        this.barreMilitaire.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evenement) {
                afficherOnglet("mili");
            }
        });
        this.barreMarche.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evenement) {
                afficherOnglet("marche");
            }
        });

        barre.add(this.barreEconomie);
        barre.add(this.barreInfrastructures);
        barre.add(this.barreMilitaire);
        barre.add(this.barreMarche);
        add(barre, BorderLayout.NORTH);

        // zone qui change selon l'onglet
        this.cards = new CardLayout();
        this.zoneOnglets = new JPanel(this.cards);
        this.zoneOnglets.setOpaque(true);
        this.zoneOnglets.setBackground(Palette.FOND_PANNEAU);
        this.zoneOnglets.add(this.ongletEconomie, "eco");
        this.zoneOnglets.add(this.ongletInfrastructures, "infra");
        this.zoneOnglets.add(this.ongletMilitaire, "mili");
        this.zoneOnglets.add(this.ongletMarche, "marche");
        add(this.zoneOnglets, BorderLayout.CENTER);
    }

    private void afficherOnglet(String cle) {
        this.cards.show(this.zoneOnglets, cle);
        this.barreEconomie.setActif(cle.equals("eco"));
        this.barreInfrastructures.setActif(cle.equals("infra"));
        this.barreMilitaire.setActif(cle.equals("mili"));
        this.barreMarche.setActif(cle.equals("marche"));
    }

    /**
     * Renvoie l'onglet Economie.
     *
     * @return l'onglet Economie
     */
    public OngletEconomie ongletEconomie() {
        return this.ongletEconomie;
    }

    /**
     * Renvoie l'onglet Infrastructures.
     *
     * @return l'onglet Infrastructures
     */
    public OngletInfrastructures ongletInfrastructures() {
        return this.ongletInfrastructures;
    }

    /**
     * Renvoie l'onglet Militaire.
     *
     * @return l'onglet Militaire
     */
    public OngletMilitaire ongletMilitaire() {
        return this.ongletMilitaire;
    }

    /**
     * Renvoie l'onglet Marche.
     *
     * @return l'onglet Marche
     */
    public OngletMarche ongletMarche() {
        return this.ongletMarche;
    }

    // un onglet cliquable
    private static class BarreOnglet extends JLabel {
        private static final long serialVersionUID = 1L;
        private boolean actif;

        BarreOnglet(String texte, boolean actif) {
            super(texte.toUpperCase(), SwingConstants.CENTER);
            this.actif = actif;
            setOpaque(false); // on dessine le fond nous-meme
            setFont(Polices.SECTION.deriveFont(13f));
            setPreferredSize(new Dimension(180, 38));
            setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            appliquerStyle();
        }

        void setActif(boolean actif) {
            this.actif = actif;
            appliquerStyle();
            repaint();
        }

        private void appliquerStyle() {
            if (this.actif) {
                setBackground(new Color(45, 32, 12));
                setForeground(Palette.OR_CLAIR);
            } else {
                setBackground(new Color(12, 8, 4));
                setForeground(Palette.TEXTE_SECONDAIRE);
            }
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            // le fond d'abord
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int largeur = getWidth();
            int hauteur = getHeight();

            if (this.actif) {
                GradientPaint grad = new GradientPaint(0, 0, new Color(60, 42, 16),
                        0, hauteur, new Color(30, 20, 6));
                g2.setPaint(grad);
                g2.fillRect(0, 0, largeur, hauteur);
                g2.setColor(Palette.OR);
                g2.fillRect(0, 0, largeur, 3);
                g2.fillRect(0, 0, 1, hauteur);
                g2.fillRect(largeur - 1, 0, 1, hauteur);
            } else {
                g2.setColor(new Color(12, 8, 4));
                g2.fillRect(0, 0, largeur, hauteur);
                g2.setColor(Palette.BORDURE_FONCEE);
                g2.fillRect(0, 0, largeur, 2);
                g2.setColor(new Color(20, 14, 6));
                g2.fillRect(largeur - 1, 0, 1, hauteur);
            }
            g2.dispose();

            // puis le texte par-dessus
            super.paintComponent(graphics);
        }
    }
}
