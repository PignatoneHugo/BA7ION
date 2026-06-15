package Vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Modele.economie.Ressource;
import Modele.partie.ConditionsFin;
import Modele.partie.Partie;
import Modele.royaume.Royaume;
import Vue.theme.BoutonMedieval;
import Vue.theme.Palette;
import Vue.theme.Polices;

import config.Equilibrage;

/** Ecran de fin : victoire/defaite, stats du regne et classement par or. */
public class VueFinPartie extends JPanel {

    private static final long serialVersionUID = 1L;

    private final ConditionsFin.Etat etat;
    private final Partie partie;

    private final BoutonMedieval boutonRejouer;
    private final BoutonMedieval boutonMenuPrincipal;

    public VueFinPartie(Partie partie, ConditionsFin.Etat etat) {
        this.partie = partie;
        this.etat = etat;

        setOpaque(true);
        setBackground(Palette.FOND_BAS);
        setLayout(new BorderLayout(0, 24));
        setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));

        add(creerEnTete(), BorderLayout.NORTH);

        // stats + classement
        JPanel centre = new JPanel(new GridLayout(1, 2, 24, 0));
        centre.setOpaque(false);
        centre.add(creerStats());
        centre.add(creerClassement());
        add(centre, BorderLayout.CENTER);

        // boutons du bas
        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 32, 0));
        boutons.setOpaque(false);

        this.boutonRejouer = new BoutonMedieval(
                "Nouvelle partie", BoutonMedieval.Style.PRIMAIRE);
        this.boutonRejouer.setPreferredSize(new Dimension(220, 50));

        this.boutonMenuPrincipal = new BoutonMedieval(
                "Menu principal", BoutonMedieval.Style.SECONDAIRE);
        this.boutonMenuPrincipal.setPreferredSize(new Dimension(220, 50));

        boutons.add(this.boutonRejouer);
        boutons.add(this.boutonMenuPrincipal);
        add(boutons, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // fond doré si victoire, rouge sombre si defaite
        Color top = this.etat == ConditionsFin.Etat.VICTOIRE
                ? new Color(40, 32, 8) : new Color(40, 8, 8);
        Color bot = Palette.FOND_BAS;
        g2.setPaint(new GradientPaint(0, 0, top, 0, h, bot));
        g2.fillRect(0, 0, w, h);

        // cadre or
        g2.setColor(Palette.OR);
        g2.setStroke(new java.awt.BasicStroke(2));
        g2.drawRect(8, 8, w - 16, h - 16);

        g2.dispose();
    }

    private JPanel creerEnTete() {
        JPanel entete = new JPanel();
        entete.setOpaque(false);
        entete.setLayout(new BorderLayout(0, 12));

        boolean victoire = this.etat == ConditionsFin.Etat.VICTOIRE;
        String titreTexte = victoire ? "Victoire" : "Defaite";

        JLabel titre = new JLabel(titreTexte.toUpperCase(), SwingConstants.CENTER);
        titre.setFont(Polices.TITRE.deriveFont(56f));
        titre.setForeground(victoire ? Palette.OR_CLAIR : Palette.ROUGE_CLAIR);
        entete.add(titre, BorderLayout.CENTER);

        JLabel raison = new JLabel(raisonFin(), SwingConstants.CENTER);
        raison.setFont(Polices.SOUS_TITRE.deriveFont(Font.PLAIN, 18f));
        raison.setForeground(Palette.TEXTE_PRIMAIRE);
        entete.add(raison, BorderLayout.SOUTH);

        return entete;
    }

    // texte selon la raison de fin
    private String raisonFin() {
        Royaume joueur = this.partie.joueur();
        if (this.etat == ConditionsFin.Etat.VICTOIRE) {
            if (joueur.tresor().quantite(Ressource.OR) >= Equilibrage.OR_VICTOIRE_PROSPERITE) {
                return "Vous avez accumule une fortune immense - votre royaume est devenu prospere !";
            }
            return "Vous avez conquis tous les royaumes adverses !";
        }
        // defaite
        if (joueur.population().total() <= Equilibrage.POPULATION_MIN_DEFAITE) {
            return "Votre royaume est depeuple. Plus personne ne vit dans vos terres.";
        }
        if (joueur.moral().valeur() <= Equilibrage.MORAL_MIN_DEFAITE) {
            return "Votre peuple s'est revolte. Le royaume s'effondre.";
        }
        return "Le temps imparti a votre regne est ecoule.";
    }

    // panneau des stats
    private JPanel creerStats() {
        JPanel panel = new JPanel();
        panel.setOpaque(true);
        panel.setBackground(Palette.FOND_PANNEAU);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Palette.OR_FONCE, 2),
                BorderFactory.createEmptyBorder(16, 20, 16, 20)));
        panel.setLayout(new BorderLayout(0, 12));

        JLabel titre = new JLabel("Statistiques du regne".toUpperCase());
        titre.setFont(Polices.SECTION.deriveFont(16f));
        titre.setForeground(Palette.OR);
        panel.add(titre, BorderLayout.NORTH);

        JPanel lignes = new JPanel(new GridLayout(0, 1, 0, 8));
        lignes.setOpaque(false);

        Royaume j = this.partie.joueur();
        lignes.add(ligneStat("Tours regnes", String.valueOf(this.partie.numeroTour())));
        lignes.add(ligneStat("Population finale",
                j.population().total() + " habitants"));
        lignes.add(ligneStat("Or amasse",
                j.tresor().quantite(Ressource.OR) + ""));
        lignes.add(ligneStat("Force militaire",
                j.armee().effectifTotal() + " soldats"));
        lignes.add(ligneStat("Moral final",
                j.moral().valeur() + " / 100"));
        int batAmeliores = 0;
        for (var b : j.batiments()) {
            if (b.niveau() > 1) batAmeliores++;
        }
        lignes.add(ligneStat("Batiments ameliores",
                batAmeliores + " / " + j.batiments().size()));

        panel.add(lignes, BorderLayout.CENTER);
        return panel;
    }

    private JPanel ligneStat(String libelle, String valeur) {
        JPanel l = new JPanel(new BorderLayout());
        l.setOpaque(true);
        l.setBackground(Palette.FOND_PANNEAU_CLAIR);
        l.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Palette.BORDURE_FONCEE, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        JLabel nom = new JLabel(libelle);
        nom.setFont(Polices.LABEL);
        nom.setForeground(Palette.TEXTE_SECONDAIRE);

        JLabel val = new JLabel(valeur, SwingConstants.RIGHT);
        val.setFont(Polices.VALEUR);
        val.setForeground(Palette.OR_CLAIR);

        l.add(nom, BorderLayout.WEST);
        l.add(val, BorderLayout.EAST);
        return l;
    }

    // panneau classement (joueur + bots tries par or)
    private JPanel creerClassement() {
        JPanel panel = new JPanel();
        panel.setOpaque(true);
        panel.setBackground(Palette.FOND_PANNEAU);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Palette.OR_FONCE, 2),
                BorderFactory.createEmptyBorder(16, 20, 16, 20)));
        panel.setLayout(new BorderLayout(0, 12));

        JLabel titre = new JLabel("Classement final".toUpperCase());
        titre.setFont(Polices.SECTION.deriveFont(16f));
        titre.setForeground(Palette.OR);
        panel.add(titre, BorderLayout.NORTH);

        JPanel lignes = new JPanel(new GridLayout(0, 1, 0, 8));
        lignes.setOpaque(false);

        List<Royaume> tous = new java.util.ArrayList<>(this.partie.tousLesRoyaumes());
        tous.sort((a, b) -> Integer.compare(
                b.tresor().quantite(Ressource.OR),
                a.tresor().quantite(Ressource.OR)));

        int rang = 1;
        for (Royaume r : tous) {
            boolean estJoueur = r == this.partie.joueur();
            lignes.add(ligneClassement(rang, r, estJoueur));
            rang++;
        }

        panel.add(lignes, BorderLayout.CENTER);
        return panel;
    }

    private JPanel ligneClassement(int rang, Royaume r, boolean estJoueur) {
        JPanel l = new JPanel(new BorderLayout(8, 0));
        l.setOpaque(true);
        l.setBackground(estJoueur ? new Color(42, 30, 8) : Palette.FOND_PANNEAU_CLAIR);
        l.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        estJoueur ? Palette.OR : Palette.BORDURE_FONCEE,
                        estJoueur ? 2 : 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        JLabel rangLabel = new JLabel("#" + rang, SwingConstants.CENTER);
        rangLabel.setFont(Polices.VALEUR);
        rangLabel.setForeground(rang == 1 ? Palette.OR_CLAIR
                : rang == 2 ? new Color(192, 192, 192)
                : rang == 3 ? new Color(192, 128, 64)
                : Palette.TEXTE_TERTIAIRE);
        rangLabel.setPreferredSize(new Dimension(36, 22));

        JLabel nom = new JLabel(r.nom());
        nom.setFont(Polices.LABEL.deriveFont(14f));
        nom.setForeground(estJoueur ? Palette.OR_CLAIR : Palette.TEXTE_PRIMAIRE);

        JLabel or = new JLabel(r.tresor().quantite(Ressource.OR) + " "
                + Ressource.OR.libelle().toLowerCase(),
                SwingConstants.RIGHT);
        or.setFont(Polices.VALEUR);
        or.setForeground(Palette.OR_RESSOURCE);

        l.add(rangLabel, BorderLayout.WEST);
        l.add(nom, BorderLayout.CENTER);
        l.add(or, BorderLayout.EAST);
        return l;
    }

    public BoutonMedieval boutonRejouer() {
        return this.boutonRejouer;
    }

    public BoutonMedieval boutonMenuPrincipal() {
        return this.boutonMenuPrincipal;
    }
}
