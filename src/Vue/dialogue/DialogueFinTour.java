package Vue.dialogue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Modele.combat.BatailleResolue;
import Modele.combat.RapportCombat;
import Modele.economie.Ressource;
import Modele.infrastructure.Batiment;
import Modele.infrastructure.TypeBatiment;
import Modele.partie.BilanTour;
import Modele.partie.Partie;
import Modele.royaume.Royaume;
import Vue.theme.BoutonMedieval;
import Vue.theme.Palette;
import Vue.theme.Polices;

/** Recap de fin de tour : variations des ressources, population et moral. */
public class DialogueFinTour extends JDialog {

    private static final long serialVersionUID = 1L;

    private final Partie partie;

    /**
     * Cree la popup de recap de fin de tour.
     *
     * @param parent la fenetre parente
     * @param avant l'etat du royaume avant le tour
     * @param apres l'etat du royaume apres le tour
     * @param numeroTourTermine le numero du tour qui vient de finir
     * @param partie la partie en cours
     */
    public DialogueFinTour(Frame parent, BilanTour avant, Royaume apres,
                           int numeroTourTermine, Partie partie) {
        super(parent,
                "Fin du tour" + " " + numeroTourTermine,
                true);
        setUndecorated(true);
        setLayout(new BorderLayout());
        this.partie = partie;

        // panneau avec fond degrade
        JPanel panneau = new JPanel(new BorderLayout(0, 12)) {
            private static final long serialVersionUID = 1L;
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                Graphics2D g2 = (Graphics2D) graphics.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, new Color(20, 14, 6),
                        0, getHeight(), new Color(8, 6, 2)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        panneau.setOpaque(true);
        panneau.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Palette.OR, 2),
                BorderFactory.createEmptyBorder(22, 28, 22, 28)));

        panneau.add(creerTete(numeroTourTermine), BorderLayout.NORTH);
        panneau.add(creerCorps(avant, apres), BorderLayout.CENTER);
        panneau.add(creerPied(), BorderLayout.SOUTH);

        setContentPane(panneau);
        pack();
        // largeur minimale
        if (getWidth() < 720) {
            setSize(720, getHeight());
        }
        setLocationRelativeTo(parent);
    }

    private JPanel creerTete(int numeroTour) {
        JPanel tete = new JPanel(new BorderLayout());
        tete.setOpaque(false);

        JLabel surTitre = new JLabel(
                "Rapport du souverain".toUpperCase(),
                SwingConstants.CENTER);
        surTitre.setFont(Polices.PETIT_LABEL.deriveFont(11f));
        surTitre.setForeground(new Color(106, 72, 32));
        tete.add(surTitre, BorderLayout.NORTH);

        JLabel titre = new JLabel(
                "Fin du tour" + " " + numeroTour,
                SwingConstants.CENTER);
        titre.setFont(Polices.SECTION.deriveFont(28f));
        titre.setForeground(Palette.OR_CLAIR);
        titre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Palette.OR_FONCE),
                BorderFactory.createEmptyBorder(4, 0, 14, 0)));
        tete.add(titre, BorderLayout.CENTER);
        return tete;
    }

    private JPanel creerCorps(BilanTour avant, Royaume apres) {
        JPanel corps = new JPanel();
        corps.setLayout(new javax.swing.BoxLayout(corps,
                javax.swing.BoxLayout.Y_AXIS));
        corps.setOpaque(false);

        corps.add(creerBlocRessources(avant, apres));
        corps.add(javax.swing.Box.createVerticalStrut(12));
        corps.add(creerBlocPopulation(avant, apres));

        JPanel blocBat = creerBlocBatiments(avant, apres);
        if (blocBat != null) {
            corps.add(javax.swing.Box.createVerticalStrut(12));
            corps.add(blocBat);
        }
        // les combats sont affiches dans DialogueRapportCombat, pas ici
        return corps;
    }

    // liste les batiments qui ont change ce tour (null si rien)
    private JPanel creerBlocBatiments(BilanTour avant, Royaume apres) {
        java.util.List<String[]> lignes = new java.util.ArrayList<>();
        for (Batiment batiment : apres.batiments()) {
            TypeBatiment typeBatiment = batiment.type();
            int niveauAvant = avant.niveau(typeBatiment);
            int niveauApres = batiment.niveau();
            boolean enChantierAvant = avant.enChantier(typeBatiment);
            boolean enChantierApres = batiment.enChantier();

            String nomBat = typeBatiment.libelle();
            if (niveauApres > niveauAvant) {
                // amelioration finie
                lignes.add(new String[]{nomBat,
                        "Amelioration terminee",
                        "Niv. " + niveauAvant + " → Niv. " + niveauApres});
            } else if (!enChantierAvant && enChantierApres) {
                // chantier lance
                lignes.add(new String[]{nomBat,
                        "Chantier demarre",
                        "(" + batiment.toursRestants() + " tours)"});
            } else if (enChantierAvant && enChantierApres) {
                // chantier en cours
                lignes.add(new String[]{nomBat,
                        "Chantier en cours",
                        "(" + batiment.toursRestants() + " tours restants)"});
            }
        }
        if (lignes.isEmpty()) {
            return null;
        }

        JPanel bloc = creerCadre("Batiments");
        JPanel grille = new JPanel(new GridLayout(0, 1, 0, 4));
        grille.setOpaque(false);
        for (String[] ligne : lignes) {
            grille.add(creerLigneBatiment(ligne[0], ligne[1], ligne[2]));
        }
        bloc.add(grille, BorderLayout.CENTER);
        return bloc;
    }

    private JPanel creerLigneBatiment(String nom, String etat, String detail) {
        JPanel ligne = new JPanel(new BorderLayout(8, 0));
        ligne.setOpaque(true);
        ligne.setBackground(new Color(10, 8, 4));
        ligne.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(30, 22, 10), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));

        JLabel labelNom = new JLabel(nom);
        labelNom.setFont(Polices.LABEL.deriveFont(13f));
        labelNom.setForeground(Palette.OR_CLAIR);
        ligne.add(labelNom, BorderLayout.WEST);

        JLabel labelEtat = new JLabel(etat, SwingConstants.CENTER);
        labelEtat.setFont(Polices.LABEL.deriveFont(12f));
        labelEtat.setForeground(Palette.TEXTE_PRIMAIRE);
        ligne.add(labelEtat, BorderLayout.CENTER);

        JLabel labelDetail = new JLabel(detail, SwingConstants.RIGHT);
        labelDetail.setFont(Polices.VALEUR.deriveFont(13f));
        labelDetail.setForeground(Palette.VERT_POSITIF);
        ligne.add(labelDetail, BorderLayout.EAST);
        return ligne;
    }

    private JPanel creerBlocRessources(BilanTour avant, Royaume apres) {
        JPanel bloc = creerCadre("Bilan des ressources");

        JPanel grille = new JPanel(new GridLayout(0, 1, 0, 4));
        grille.setOpaque(false);

        for (Ressource ressource : Ressource.values()) {
            int v0 = avant.ressource(ressource);
            int v1 = apres.tresor().quantite(ressource);
            grille.add(creerLigneRessource(ressource, v0, v1));
        }
        bloc.add(grille, BorderLayout.CENTER);
        return bloc;
    }

    private JPanel creerLigneRessource(Ressource ressource, int avant, int apres) {
        int delta = apres - avant;

        JPanel ligne = new JPanel(new BorderLayout(8, 0));
        ligne.setOpaque(true);
        ligne.setBackground(new Color(10, 8, 4));
        ligne.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(30, 22, 10), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));

        // nom a gauche
        JLabel nom = new JLabel(ressource.libelle());
        nom.setFont(Polices.LABEL.deriveFont(13f));
        nom.setForeground(couleurRessource(ressource));
        ligne.add(nom, BorderLayout.WEST);

        // a droite : avant -> apres (delta)
        JPanel droite = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        droite.setOpaque(false);

        JLabel valeurs = new JLabel(avant + "  →  " + apres);
        valeurs.setFont(Polices.VALEUR.deriveFont(14f));
        valeurs.setForeground(Palette.TEXTE_PRIMAIRE);
        droite.add(valeurs);

        JLabel labelDelta = new JLabel(formatDelta(delta));
        labelDelta.setFont(Polices.VALEUR.deriveFont(14f));
        labelDelta.setForeground(couleurDelta(delta));
        labelDelta.setPreferredSize(new Dimension(70, 20));
        labelDelta.setHorizontalAlignment(SwingConstants.RIGHT);
        droite.add(labelDelta);

        ligne.add(droite, BorderLayout.EAST);
        return ligne;
    }

    private JPanel creerBlocPopulation(BilanTour avant, Royaume apres) {
        JPanel bloc = creerCadre("Royaume");

        JPanel grille = new JPanel(new GridLayout(1, 2, 10, 0));
        grille.setOpaque(false);

        // population = civils + soldats
        int popAvant = avant.populationTotale() + avant.effectifArmee();
        int popApres = apres.population().total() + apres.armee().effectifTotal();

        grille.add(creerCarteStat(
                "Population",
                popAvant,
                popApres,
                false));
        grille.add(creerCarteStat(
                "Moral",
                avant.moral(),
                apres.moral().valeur(),
                true));

        bloc.add(grille, BorderLayout.CENTER);
        return bloc;
    }

    private JPanel creerCarteStat(String titre, int avant, int apres,
                                  boolean afficherPourcent) {
        int delta = apres - avant;

        JPanel carte = new JPanel(new BorderLayout(0, 4));
        carte.setOpaque(true);
        carte.setBackground(new Color(10, 8, 4));
        carte.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(30, 22, 10), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        JLabel labelTitre = new JLabel(titre.toUpperCase(), SwingConstants.CENTER);
        labelTitre.setFont(Polices.PETIT_LABEL);
        labelTitre.setForeground(new Color(106, 72, 32));
        carte.add(labelTitre, BorderLayout.NORTH);

        String suffixe = afficherPourcent ? "%" : "";
        JLabel valeurs = new JLabel(
                avant + suffixe + "  →  " + apres + suffixe,
                SwingConstants.CENTER);
        valeurs.setFont(Polices.VALEUR.deriveFont(16f));
        valeurs.setForeground(Palette.TEXTE_PRIMAIRE);
        carte.add(valeurs, BorderLayout.CENTER);

        JLabel labelDelta = new JLabel(formatDelta(delta) + suffixe,
                SwingConstants.CENTER);
        labelDelta.setFont(Polices.VALEUR.deriveFont(14f));
        labelDelta.setForeground(couleurDelta(delta));
        carte.add(labelDelta, BorderLayout.SOUTH);
        return carte;
    }

    private JPanel creerCadre(String titre) {
        JPanel cadre = new JPanel(new BorderLayout(0, 8));
        cadre.setOpaque(true);
        cadre.setBackground(new Color(14, 10, 4));
        cadre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Palette.OR_FONCE, 1),
                BorderFactory.createEmptyBorder(10, 14, 12, 14)));

        JLabel sousTitre = new JLabel(titre.toUpperCase());
        sousTitre.setFont(Polices.PETIT_LABEL.deriveFont(11f));
        sousTitre.setForeground(Palette.OR);
        sousTitre.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        cadre.add(sousTitre, BorderLayout.NORTH);
        return cadre;
    }

    private JPanel creerPied() {
        JPanel pied = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        pied.setOpaque(false);
        pied.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        BoutonMedieval continuer = new BoutonMedieval(
                "Continuer",
                BoutonMedieval.Style.PRIMAIRE);
        continuer.setPreferredSize(new Dimension(260, 44));
        continuer.addActionListener(evenement -> dispose());
        pied.add(continuer);
        return pied;
    }

    private String formatDelta(int delta) {
        if (delta > 0) return "+" + delta;
        if (delta < 0) return String.valueOf(delta);
        return "=";
    }

    private Color couleurDelta(int delta) {
        if (delta > 0) return Palette.VERT_POSITIF;
        if (delta < 0) return Palette.ROUGE_DANGER;
        return new Color(120, 100, 70);
    }

    private Color couleurRessource(Ressource ressource) {
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
