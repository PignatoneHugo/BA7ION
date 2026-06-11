package Vue.dialogue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.Window;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import Modele.evenement.Choix;
import Modele.evenement.Evenement;
import Modele.royaume.Royaume;
import Vue.i18n.Traducteur;
import Vue.theme.BoutonMedieval;
import Vue.theme.Palette;
import Vue.theme.Polices;

/**
 * Popup modale d'un evenement style medieval. Affiche titre + description,
 * et un bouton par choix. Les boutons des choix dont le royaume n'a pas
 * les ressources sont desactives, avec un tooltip "Ressources insuffisantes".
 *
 * Vue passive : retourne juste le Choix selectionne, c'est au controleur
 * d'appeler partie.resoudreEvenement(choix).
 */
public final class DialogueEvenement {

    private DialogueEvenement() {
        // Classe utilitaire.
    }

    /**
     * Affiche le dialogue modal et bloque jusqu'a la reponse du joueur.
     *
     * @param parent fenetre parente (pour le positionnement)
     * @param evenement evenement a presenter
     * @param royaume royaume du joueur (pour verifier les ressources requises)
     */
    public static Choix afficher(java.awt.Component parent,
                                 Evenement evenement,
                                 Royaume royaume) {
        Frame frame = trouverFrame(parent);
        DialogueImpl dialog = new DialogueImpl(frame, evenement, royaume);
        dialog.setVisible(true);
        Choix selection = dialog.choixSelectionne();
        if (selection == null) {
            // Securite : si fermeture sans choisir, premier dispo (ou 1er).
            for (Choix c : evenement.choix()) {
                if (c.peutEtreChoisi(royaume)) {
                    return c;
                }
            }
            return evenement.choix().get(0);
        }
        return selection;
    }

    private static Frame trouverFrame(java.awt.Component c) {
        Window w = SwingUtilities.getWindowAncestor(c);
        if (w instanceof Frame) {
            return (Frame) w;
        }
        return null;
    }

    /** Implementation interne du JDialog. */
    private static class DialogueImpl extends JDialog {
        private static final long serialVersionUID = 1L;

        private Choix selection;

        DialogueImpl(Frame parent, Evenement evenement, Royaume royaume) {
            super(parent, Traducteur.t(evenement.cleTitre()), true);
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

            // En-tete : "EVENEMENT" + titre
            JPanel tete = new JPanel(new BorderLayout());
            tete.setOpaque(false);

            JLabel surTitre = new JLabel(
                    Traducteur.t("evenement.sur_titre").toUpperCase(),
                    SwingConstants.CENTER);
            surTitre.setFont(Polices.PETIT_LABEL.deriveFont(11f));
            surTitre.setForeground(new Color(140, 88, 32));
            tete.add(surTitre, BorderLayout.NORTH);

            JLabel titre = new JLabel(Traducteur.t(evenement.cleTitre()),
                    SwingConstants.CENTER);
            titre.setFont(Polices.SECTION.deriveFont(22f));
            titre.setForeground(Palette.OR_CLAIR);
            titre.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Palette.OR_FONCE),
                    BorderFactory.createEmptyBorder(4, 0, 12, 0)));
            tete.add(titre, BorderLayout.CENTER);
            panneau.add(tete, BorderLayout.NORTH);

            // Description
            JLabel description = new JLabel(
                    "<html><div style='text-align:center; width:480px;'>"
                            + Traducteur.t(evenement.cleDescription())
                            + "</div></html>",
                    SwingConstants.CENTER);
            description.setFont(Polices.LABEL.deriveFont(14f));
            description.setForeground(Palette.TEXTE_PRIMAIRE);
            description.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            panneau.add(description, BorderLayout.CENTER);

            // Choix : un bouton par option
            List<Choix> choix = evenement.choix();
            JPanel piedChoix = new JPanel(new GridLayout(0, 1, 0, 6));
            piedChoix.setOpaque(false);
            piedChoix.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            for (Choix c : choix) {
                BoutonMedieval bouton = new BoutonMedieval(
                        Traducteur.t(c.cleI18n()),
                        BoutonMedieval.Style.PRIMAIRE);
                bouton.setPreferredSize(new Dimension(520, 40));
                if (c.peutEtreChoisi(royaume)) {
                    bouton.addActionListener(e -> {
                        this.selection = c;
                        dispose();
                    });
                } else {
                    bouton.setEnabled(false);
                    bouton.setToolTipText(Traducteur.t("evenement.ressources_insuffisantes"));
                }
                piedChoix.add(bouton);
            }
            panneau.add(piedChoix, BorderLayout.SOUTH);

            setContentPane(panneau);
            pack();
            if (getWidth() < 560) {
                setSize(560, getHeight());
            }
            setLocationRelativeTo(parent);
        }

        Choix choixSelectionne() {
            return this.selection;
        }
    }
}
