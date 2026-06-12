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
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import Modele.combat.BatailleResolue;
import Modele.combat.RapportCombat;
import Modele.economie.Ressource;
import Modele.partie.Partie;
import Modele.royaume.Royaume;
import Vue.theme.BoutonMedieval;
import Vue.theme.Palette;
import Vue.theme.Polices;

/**
 * Popup dediee aux rapports de combat du tour : une grande carte par
 * bataille avec verdict colore, pertes des deux cotes, pertes civiles
 * et butin. S'affiche avant le bilan de fin de tour.
 */
public class DialogueRapportCombat extends JDialog {

    private static final long serialVersionUID = 1L;

    public DialogueRapportCombat(Frame parent, Partie partie, int numeroTour) {
        super(parent, "Rapport de combat", true);
        setUndecorated(true);
        setLayout(new BorderLayout());

        JPanel panneau = new JPanel(new BorderLayout(0, 12)) {
            private static final long serialVersionUID = 1L;
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, new Color(24, 8, 8),
                        0, getHeight(), new Color(8, 4, 2)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        panneau.setOpaque(true);
        panneau.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Palette.ROUGE_BANNIERE, 2),
                BorderFactory.createEmptyBorder(20, 26, 18, 26)));

        // === Tête ===
        JPanel tete = new JPanel(new BorderLayout());
        tete.setOpaque(false);
        JLabel surTitre = new JLabel(
                "Cris d'armes".toUpperCase(),
                SwingConstants.CENTER);
        surTitre.setFont(Polices.PETIT_LABEL.deriveFont(11f));
        surTitre.setForeground(new Color(140, 70, 40));
        tete.add(surTitre, BorderLayout.NORTH);
        JLabel titre = new JLabel(
                "Combats du tour" + " " + numeroTour,
                SwingConstants.CENTER);
        titre.setFont(Polices.SECTION.deriveFont(26f));
        titre.setForeground(Palette.OR_CLAIR);
        titre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Palette.OR_FONCE),
                BorderFactory.createEmptyBorder(4, 0, 12, 0)));
        tete.add(titre, BorderLayout.CENTER);
        panneau.add(tete, BorderLayout.NORTH);

        // === Liste des batailles ===
        JPanel liste = new JPanel();
        liste.setOpaque(false);
        liste.setLayout(new BoxLayout(liste, BoxLayout.Y_AXIS));

        Royaume joueur = partie.joueur();
        List<BatailleResolue> resolues = partie.batraillesDuTour();
        boolean premier = true;
        for (BatailleResolue b : resolues) {
            if (b.attaquant() != joueur && b.defenseur() != joueur) {
                continue;
            }
            if (!premier) {
                liste.add(Box.createVerticalStrut(10));
            }
            premier = false;
            liste.add(creerCarteBataille(b, joueur));
        }

        JScrollPane scroll = new JScrollPane(liste);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        panneau.add(scroll, BorderLayout.CENTER);

        // === Pied : Continuer ===
        JPanel pied = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pied.setOpaque(false);
        pied.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        BoutonMedieval continuer = new BoutonMedieval(
                "Continuer",
                BoutonMedieval.Style.PRIMAIRE);
        continuer.setPreferredSize(new Dimension(220, 40));
        continuer.addActionListener(e -> dispose());
        pied.add(continuer);
        panneau.add(pied, BorderLayout.SOUTH);

        setContentPane(panneau);
        pack();
        // Borne de taille
        int w = Math.max(720, getWidth());
        int h = Math.min(620, Math.max(380, getHeight()));
        setSize(w, h);
        setLocationRelativeTo(parent);
    }

    /** Une carte detaillee par bataille. */
    private JPanel creerCarteBataille(BatailleResolue b, Royaume joueur) {
        boolean joueurAttaque = (b.attaquant() == joueur);
        Royaume adv = joueurAttaque ? b.defenseur() : b.attaquant();

        boolean joueurGagne;
        boolean egalite = b.rapport().vainqueur() == RapportCombat.Vainqueur.EGALITE;
        if (egalite) {
            joueurGagne = false;
        } else {
            joueurGagne = (b.rapport().vainqueur() == RapportCombat.Vainqueur.ATTAQUANT
                    && joueurAttaque)
                    || (b.rapport().vainqueur() == RapportCombat.Vainqueur.DEFENSEUR
                    && !joueurAttaque);
        }

        // Couleur dominante selon issue
        Color couleurIssue;
        String texteIssue;
        if (egalite) {
            couleurIssue = Palette.OR;
            texteIssue = "EGALITE";
        } else if (joueurGagne) {
            couleurIssue = Palette.VERT_POSITIF;
            texteIssue = "VICTOIRE";
        } else {
            couleurIssue = Palette.ROUGE_DANGER;
            texteIssue = "DEFAITE";
        }

        JPanel carte = new JPanel(new BorderLayout(0, 6));
        carte.setOpaque(true);
        carte.setBackground(new Color(14, 8, 4));
        carte.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(couleurIssue, 2),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)));

        // === En-tete : adversaire + verdict ===
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        String sens = joueurAttaque
                ? "Vous attaquez"
                : "Attaque de";
        JLabel titreBat = new JLabel(sens + " " + adv.nom());
        titreBat.setFont(Polices.SECTION.deriveFont(14f));
        titreBat.setForeground(Palette.OR_CLAIR);
        header.add(titreBat, BorderLayout.WEST);

        JLabel issue = new JLabel(texteIssue, SwingConstants.RIGHT);
        issue.setFont(Polices.SECTION.deriveFont(16f));
        issue.setForeground(couleurIssue);
        header.add(issue, BorderLayout.EAST);

        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Palette.OR_FONCE),
                BorderFactory.createEmptyBorder(0, 0, 4, 0)));
        carte.add(header, BorderLayout.NORTH);

        // === Corps : 2 colonnes (joueur / adversaire) ===
        JPanel corps = new JPanel(new GridLayout(1, 2, 12, 0));
        corps.setOpaque(false);

        int pertesJoueur = joueurAttaque ? b.rapport().pertesAttaquant()
                : b.rapport().pertesDefenseur();
        int pertesAdv = joueurAttaque ? b.rapport().pertesDefenseur()
                : b.rapport().pertesAttaquant();
        int effAvantJoueur = joueurAttaque ? b.effectifAvantAttaquant()
                : b.effectifAvantDefenseur();
        int effAvantAdv = joueurAttaque ? b.effectifAvantDefenseur()
                : b.effectifAvantAttaquant();
        int civilsJoueur = !joueurAttaque ? b.pertesCivilesDefenseur() : 0;
        int civilsAdv = joueurAttaque ? b.pertesCivilesDefenseur() : 0;

        // Colonne joueur
        corps.add(creerColonneCamp(
                "Votre royaume",
                effAvantJoueur, pertesJoueur, civilsJoueur,
                joueurAttaque ? null : b.butin(),
                !egalite && !joueurGagne,
                joueurAttaque));

        // Colonne adversaire
        corps.add(creerColonneCamp(
                adv.nom(),
                effAvantAdv, pertesAdv, civilsAdv,
                joueurAttaque ? b.butin() : null,
                !egalite && joueurGagne,
                !joueurAttaque));

        carte.add(corps, BorderLayout.CENTER);
        return carte;
    }

    /**
     * Une moitie de carte de bataille : nom du camp + pertes militaires +
     * pertes civiles + butin (pris ou perdu).
     */
    private JPanel creerColonneCamp(String nom, int effAvant,
                                    int pertesMil, int civils,
                                    Map<Ressource, Integer> butin,
                                    boolean estPerdant, boolean estAttaquant) {
        JPanel col = new JPanel(new BorderLayout(0, 4));
        col.setOpaque(true);
        col.setBackground(new Color(8, 4, 2));
        col.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(40, 24, 12), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));

        // Tete
        JPanel tete = new JPanel(new BorderLayout());
        tete.setOpaque(false);
        JLabel labelNom = new JLabel(nom);
        labelNom.setFont(Polices.LABEL.deriveFont(13f));
        labelNom.setForeground(Palette.OR_CLAIR);
        tete.add(labelNom, BorderLayout.WEST);
        if (estAttaquant) {
            JLabel labelRole = new JLabel(
                    "ATTAQUANT",
                    SwingConstants.RIGHT);
            labelRole.setFont(Polices.PETIT_LABEL);
            labelRole.setForeground(Palette.TEXTE_TERTIAIRE);
            tete.add(labelRole, BorderLayout.EAST);
        }
        col.add(tete, BorderLayout.NORTH);

        // Corps : effectif initial + pertes + civils + butin
        JPanel infos = new JPanel();
        infos.setOpaque(false);
        infos.setLayout(new BoxLayout(infos, BoxLayout.Y_AXIS));

        // Effectif engage dans la bataille + ce qu'il reste apres pertes
        int restant = Math.max(0, effAvant - pertesMil);
        if (estPerdant) {
            restant = 0; // armee aneantie
        }
        infos.add(ligneInfo(
                "Troupes engagees",
                effAvant + " → " + restant,
                Palette.TEXTE_PRIMAIRE));
        infos.add(Box.createVerticalStrut(2));

        infos.add(ligneInfo(
                "Pertes militaires",
                "-" + pertesMil + (estPerdant ? " (anéantis)" : ""),
                Palette.ROUGE_DANGER));

        if (civils > 0) {
            infos.add(Box.createVerticalStrut(2));
            infos.add(ligneInfo(
                    "Pertes civiles",
                    "-" + civils,
                    Palette.ROUGE_DANGER));
        }

        if (butin != null && !butin.isEmpty()) {
            infos.add(Box.createVerticalStrut(2));
            StringBuilder bsb = new StringBuilder();
            boolean first = true;
            for (Map.Entry<Ressource, Integer> e : butin.entrySet()) {
                if (!first) bsb.append(", ");
                bsb.append("+").append(e.getValue()).append(" ")
                        .append(e.getKey().libelle());
                first = false;
            }
            String labelButin = estAttaquant ? "Butin pris" : "Butin perdu";
            infos.add(ligneInfo(
                    labelButin,
                    bsb.toString(),
                    estAttaquant ? Palette.VERT_POSITIF : Palette.ROUGE_DANGER));
        }

        col.add(infos, BorderLayout.CENTER);
        return col;
    }

    private JPanel ligneInfo(String label, String valeur, Color couleurValeur) {
        JPanel l = new JPanel(new BorderLayout(8, 0));
        l.setOpaque(false);
        JLabel lLabel = new JLabel(label);
        lLabel.setFont(Polices.PETIT_LABEL);
        lLabel.setForeground(Palette.TEXTE_SECONDAIRE);
        l.add(lLabel, BorderLayout.WEST);
        JLabel lValeur = new JLabel(valeur, SwingConstants.RIGHT);
        lValeur.setFont(Polices.LABEL.deriveFont(12f));
        lValeur.setForeground(couleurValeur);
        l.add(lValeur, BorderLayout.EAST);
        return l;
    }
}
