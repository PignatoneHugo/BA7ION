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

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Modele.economie.Ressource;
import Modele.royaume.Royaume;
import Vue.theme.BoutonMedieval;
import Vue.theme.Palette;
import Vue.theme.Polices;

/**
 * Popup qui s'ouvre depuis le bouton "Attaquer" de l'onglet militaire.
 * Liste les bots avec leur etat (effectif, or, eliminé, attaque deja
 * planifiee) et permet d'en choisir un comme cible.
 *
 * Le bouton Attaquer dans la ligne est desactive si la cible n'est pas
 * valide. La selection se fait via le bouton de chaque ligne -- la
 * popup se ferme et notifie le controleur via le callback.
 */
public class DialogueChoixCible extends JDialog {

    private static final long serialVersionUID = 1L;

    /** Callback appele quand le joueur a choisi une cible (null si annule). */
    public interface CibleChoisie {
        void choisir(Royaume cible);
    }

    public DialogueChoixCible(Frame parent,
                              Royaume joueur,
                              List<Royaume> bots,
                              CibleChoisie callback) {
        super(parent, "Choisir une cible", true);
        setUndecorated(true);
        setLayout(new BorderLayout());

        JPanel panneau = new JPanel(new BorderLayout(0, 10)) {
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
                BorderFactory.createEmptyBorder(20, 24, 18, 24)));

        // En-tete
        JPanel tete = new JPanel(new BorderLayout());
        tete.setOpaque(false);
        JLabel surTitre = new JLabel(
                "Adversaires".toUpperCase(),
                SwingConstants.CENTER);
        surTitre.setFont(Polices.PETIT_LABEL.deriveFont(11f));
        surTitre.setForeground(new Color(106, 72, 32));
        tete.add(surTitre, BorderLayout.NORTH);
        JLabel titre = new JLabel("Choisir une cible",
                SwingConstants.CENTER);
        titre.setFont(Polices.SECTION.deriveFont(22f));
        titre.setForeground(Palette.OR_CLAIR);
        titre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Palette.OR_FONCE),
                BorderFactory.createEmptyBorder(2, 0, 10, 0)));
        tete.add(titre, BorderLayout.CENTER);
        panneau.add(tete, BorderLayout.NORTH);

        // Centre : liste des bots
        JPanel liste = new JPanel(new GridLayout(0, 1, 0, 6));
        liste.setOpaque(false);
        for (Royaume bot : bots) {
            liste.add(creerLigneBot(joueur, bot, callback));
        }
        panneau.add(liste, BorderLayout.CENTER);

        // Pied : bouton Annuler
        JPanel pied = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pied.setOpaque(false);
        pied.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        BoutonMedieval annuler = new BoutonMedieval(
                "Annuler",
                BoutonMedieval.Style.SECONDAIRE);
        annuler.setPreferredSize(new Dimension(140, 36));
        annuler.addActionListener(e -> dispose());
        pied.add(annuler);
        panneau.add(pied, BorderLayout.SOUTH);

        setContentPane(panneau);
        pack();
        if (getWidth() < 540) {
            setSize(540, getHeight());
        }
        setLocationRelativeTo(parent);
    }

    private JPanel creerLigneBot(Royaume joueur, Royaume bot, CibleChoisie cb) {
        JPanel ligne = new JPanel(new BorderLayout(10, 0));
        ligne.setOpaque(true);
        ligne.setBackground(new Color(10, 8, 4));
        ligne.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Palette.OR_FONCE, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        // Gauche : nom
        JLabel nom = new JLabel(bot.nom());
        nom.setFont(Polices.LABEL.deriveFont(14f));
        nom.setForeground(Palette.OR_CLAIR);
        ligne.add(nom, BorderLayout.WEST);

        // Centre : effectif + or + etat
        JPanel infos = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        infos.setOpaque(false);
        int eff = bot.armee().effectifTotal();
        int or = bot.tresor().quantite(Ressource.OR);
        int pop = bot.population().total();

        JLabel labelEff = new JLabel("Effectif" + " : " + eff);
        labelEff.setFont(Polices.LABEL.deriveFont(12f));
        labelEff.setForeground(Palette.TEXTE_PRIMAIRE);
        infos.add(labelEff);

        JLabel labelOr = new JLabel(or + " Or");
        labelOr.setFont(Polices.LABEL.deriveFont(12f));
        labelOr.setForeground(Palette.OR_RESSOURCE);
        infos.add(labelOr);

        JLabel labelEtat = new JLabel();
        labelEtat.setFont(Polices.LABEL.deriveFont(12f));
        boolean dejaAttaque = joueur.aAttaquePlanifieeContre(bot);
        if (pop <= 0) {
            labelEtat.setText("ELIMINE");
            labelEtat.setForeground(Palette.ROUGE_DANGER);
        } else if (dejaAttaque) {
            labelEtat.setText("Deja attaque ce tour");
            labelEtat.setForeground(Palette.OR_CLAIR);
        }
        infos.add(labelEtat);
        ligne.add(infos, BorderLayout.CENTER);

        // Droite : bouton choisir
        BoutonMedieval choisir = new BoutonMedieval(
                "Attaquer",
                BoutonMedieval.Style.DANGER);
        choisir.setPreferredSize(new Dimension(120, 30));
        boolean cibleValide = pop > 0 && !dejaAttaque && !joueur.armee().estVide();
        choisir.setEnabled(cibleValide);
        choisir.addActionListener(e -> {
            cb.choisir(bot);
            dispose();
        });
        ligne.add(choisir, BorderLayout.EAST);

        return ligne;
    }
}
