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
import Vue.i18n.Traducteur;
import Vue.theme.BoutonMedieval;
import Vue.theme.Palette;
import Vue.theme.Polices;

/**
 * Recap modal en fin de tour : variations des 5 ressources, evolution
 * de la population et du moral. Sobre, lisible, un seul bouton Continuer.
 */
public class DialogueFinTour extends JDialog {

    private static final long serialVersionUID = 1L;

    private final Partie partie;

    public DialogueFinTour(Frame parent, BilanTour avant, Royaume apres,
                           int numeroTourTermine, Partie partie) {
        super(parent,
                Traducteur.t("fin_tour.titre") + " " + numeroTourTermine,
                true);
        setUndecorated(true);
        setLayout(new BorderLayout());
        this.partie = partie;

        // Panneau fond medieval
        JPanel panneau = new JPanel(new BorderLayout(0, 12)) {
            private static final long serialVersionUID = 1L;
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
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
        // Garantit une largeur minimale agreable meme si les libelles courts
        if (getWidth() < 720) {
            setSize(720, getHeight());
        }
        setLocationRelativeTo(parent);
    }

    private JPanel creerTete(int numeroTour) {
        JPanel tete = new JPanel(new BorderLayout());
        tete.setOpaque(false);

        JLabel surTitre = new JLabel(
                Traducteur.t("fin_tour.sur_titre").toUpperCase(),
                SwingConstants.CENTER);
        surTitre.setFont(Polices.PETIT_LABEL.deriveFont(11f));
        surTitre.setForeground(new Color(106, 72, 32));
        tete.add(surTitre, BorderLayout.NORTH);

        JLabel titre = new JLabel(
                Traducteur.t("fin_tour.titre") + " " + numeroTour,
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
        // Les batailles sont desormais affichees dans DialogueRapportCombat
        // (avant ce dialogue), donc on ne duplique plus la section ici.
        return corps;
    }

    /**
     * Liste les batailles resolues ce tour, cote joueur uniquement
     * (attaques subies ou lancees). Retourne null si rien.
     */
    private JPanel creerBlocCombats() {
        if (this.partie == null || this.partie.batraillesDuTour().isEmpty()) {
            return null;
        }
        Royaume joueur = this.partie.joueur();

        java.util.List<BatailleResolue> implication = new java.util.ArrayList<>();
        for (BatailleResolue b : this.partie.batraillesDuTour()) {
            if (b.attaquant() == joueur || b.defenseur() == joueur) {
                implication.add(b);
            }
        }
        if (implication.isEmpty()) {
            return null;
        }

        JPanel bloc = creerCadre(Traducteur.t("fin_tour.batailles"));
        JPanel grille = new JPanel(new GridLayout(0, 1, 0, 4));
        grille.setOpaque(false);
        for (BatailleResolue b : implication) {
            grille.add(creerLigneBataille(b, joueur));
        }
        bloc.add(grille, BorderLayout.CENTER);
        return bloc;
    }

    private JPanel creerLigneBataille(BatailleResolue b, Royaume joueur) {
        boolean joueurAttaque = (b.attaquant() == joueur);
        String adversaire = (joueurAttaque ? b.defenseur() : b.attaquant()).nom();

        // Issue
        String issue;
        Color couleurIssue;
        if (b.rapport().vainqueur() == RapportCombat.Vainqueur.EGALITE) {
            issue = Traducteur.t("fin_tour.combat.egalite");
            couleurIssue = Palette.OR;
        } else {
            boolean joueurGagne =
                    (b.rapport().vainqueur() == RapportCombat.Vainqueur.ATTAQUANT
                            && joueurAttaque)
                    || (b.rapport().vainqueur() == RapportCombat.Vainqueur.DEFENSEUR
                            && !joueurAttaque);
            issue = joueurGagne
                    ? Traducteur.t("fin_tour.combat.victoire")
                    : Traducteur.t("fin_tour.combat.defaite");
            couleurIssue = joueurGagne
                    ? Palette.VERT_POSITIF
                    : Palette.ROUGE_DANGER;
        }

        // Detail : pertes + butin + civils
        int pertesJoueur = joueurAttaque ? b.rapport().pertesAttaquant()
                : b.rapport().pertesDefenseur();
        int pertesAdv = joueurAttaque ? b.rapport().pertesDefenseur()
                : b.rapport().pertesAttaquant();

        StringBuilder detail = new StringBuilder();
        detail.append("−").append(pertesJoueur)
                .append(" / +").append("0  •  ennemi −")
                .append(pertesAdv);
        if (b.pertesCivilesDefenseur() > 0) {
            if (joueurAttaque) {
                detail.append("  •  ").append(pertesAdv > 0 ? "" : "")
                        .append(b.pertesCivilesDefenseur()).append(" civils ennemis");
            } else {
                detail.append("  •  ").append(b.pertesCivilesDefenseur())
                        .append(" civils perdus");
            }
        }
        if (!b.butin().isEmpty()) {
            detail.append("  •  butin : ");
            boolean first = true;
            for (Map.Entry<Ressource, Integer> e : b.butin().entrySet()) {
                if (!first) detail.append(", ");
                detail.append(e.getValue()).append(" ")
                        .append(Traducteur.t(e.getKey().cleI18n()));
                first = false;
            }
        }

        JPanel ligne = new JPanel(new BorderLayout(8, 0));
        ligne.setOpaque(true);
        ligne.setBackground(new Color(10, 8, 4));
        ligne.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(30, 22, 10), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));

        // Gauche : adversaire + sens (attaque ou defense)
        String sens = joueurAttaque ? "→" : "←";
        JLabel labelAdv = new JLabel(sens + " " + adversaire);
        labelAdv.setFont(Polices.LABEL.deriveFont(13f));
        labelAdv.setForeground(Palette.OR_CLAIR);
        ligne.add(labelAdv, BorderLayout.WEST);

        // Centre : detail
        JLabel labelDetail = new JLabel(detail.toString());
        labelDetail.setFont(Polices.LABEL.deriveFont(11f));
        labelDetail.setForeground(Palette.TEXTE_SECONDAIRE);
        labelDetail.setHorizontalAlignment(SwingConstants.CENTER);
        ligne.add(labelDetail, BorderLayout.CENTER);

        // Droite : issue
        JLabel labelIssue = new JLabel(issue, SwingConstants.RIGHT);
        labelIssue.setFont(Polices.VALEUR.deriveFont(13f));
        labelIssue.setForeground(couleurIssue);
        ligne.add(labelIssue, BorderLayout.EAST);
        return ligne;
    }

    /**
     * Liste les batiments qui ont change ce tour (niveau monte ou
     * chantier demarre/termine). Retourne null si rien a montrer.
     */
    private JPanel creerBlocBatiments(BilanTour avant, Royaume apres) {
        java.util.List<String[]> lignes = new java.util.ArrayList<>();
        for (Batiment b : apres.batiments()) {
            TypeBatiment t = b.type();
            int niveauAvant = avant.niveau(t);
            int niveauApres = b.niveau();
            boolean enChantierAvant = avant.enChantier(t);
            boolean enChantierApres = b.enChantier();

            String nomBat = Traducteur.t(t.cleI18n());
            if (niveauApres > niveauAvant) {
                // Amelioration terminee ce tour
                lignes.add(new String[]{nomBat,
                        Traducteur.t("fin_tour.bat.ameliore"),
                        "Niv. " + niveauAvant + " → Niv. " + niveauApres});
            } else if (!enChantierAvant && enChantierApres) {
                // Nouveau chantier lance ce tour
                lignes.add(new String[]{nomBat,
                        Traducteur.t("fin_tour.bat.chantier_lance"),
                        "(" + b.toursRestants() + " tours)"});
            } else if (enChantierAvant && enChantierApres) {
                // Chantier continue (compteur descend)
                lignes.add(new String[]{nomBat,
                        Traducteur.t("fin_tour.bat.chantier_avance"),
                        "(" + b.toursRestants() + " tours restants)"});
            }
        }
        if (lignes.isEmpty()) {
            return null;
        }

        JPanel bloc = creerCadre(Traducteur.t("fin_tour.batiments"));
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
        JPanel bloc = creerCadre(Traducteur.t("fin_tour.ressources"));

        JPanel grille = new JPanel(new GridLayout(0, 1, 0, 4));
        grille.setOpaque(false);

        for (Ressource r : Ressource.values()) {
            int v0 = avant.ressource(r);
            int v1 = apres.tresor().quantite(r);
            grille.add(creerLigneRessource(r, v0, v1));
        }
        bloc.add(grille, BorderLayout.CENTER);
        return bloc;
    }

    private JPanel creerLigneRessource(Ressource r, int avant, int apres) {
        int delta = apres - avant;

        JPanel ligne = new JPanel(new BorderLayout(8, 0));
        ligne.setOpaque(true);
        ligne.setBackground(new Color(10, 8, 4));
        ligne.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(30, 22, 10), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));

        // Nom ressource (gauche)
        JLabel nom = new JLabel(Traducteur.t(r.cleI18n()));
        nom.setFont(Polices.LABEL.deriveFont(13f));
        nom.setForeground(couleurRessource(r));
        ligne.add(nom, BorderLayout.WEST);

        // Bloc droite : ancien -> nouveau (delta)
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
        JPanel bloc = creerCadre(Traducteur.t("fin_tour.population"));

        JPanel grille = new JPanel(new GridLayout(1, 2, 10, 0));
        grille.setOpaque(false);

        // Population = civils + soldats (les unites combattantes font toujours
        // partie du royaume, juste plus du pool civil).
        int popAvant = avant.populationTotale() + avant.effectifArmee();
        int popApres = apres.population().total() + apres.armee().effectifTotal();

        grille.add(creerCarteStat(
                Traducteur.t("population.total"),
                popAvant,
                popApres,
                false));
        grille.add(creerCarteStat(
                Traducteur.t("moral.titre"),
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

        JLabel l = new JLabel(titre.toUpperCase(), SwingConstants.CENTER);
        l.setFont(Polices.PETIT_LABEL);
        l.setForeground(new Color(106, 72, 32));
        carte.add(l, BorderLayout.NORTH);

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
                Traducteur.t("fin_tour.continuer"),
                BoutonMedieval.Style.PRIMAIRE);
        continuer.setPreferredSize(new Dimension(260, 44));
        continuer.addActionListener(e -> dispose());
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

    private Color couleurRessource(Ressource r) {
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
