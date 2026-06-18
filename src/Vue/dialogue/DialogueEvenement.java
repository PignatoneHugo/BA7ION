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
import Vue.theme.BoutonMedieval;
import Vue.theme.Palette;
import Vue.theme.Polices;

/** Popup d'un evenement : titre, description et un bouton par choix possible. */
public final class DialogueEvenement {

    private DialogueEvenement() {
        // classe utilitaire
    }

    /**
     * Affiche la popup de l'evenement et renvoie le choix du joueur.
     *
     * @param parent le composant parent pour centrer la popup
     * @param evenement l'evenement a afficher
     * @param royaume le royaume du joueur
     * @return le choix selectionne par le joueur
     */
    public static Choix afficher(java.awt.Component parent,
                                 Evenement evenement,
                                 Royaume royaume) {
        Frame frame = trouverFrame(parent);
        DialogueImpl dialog = new DialogueImpl(frame, evenement, royaume);
        dialog.setVisible(true);
        Choix selection = dialog.choixSelectionne();
        if (selection == null) {
            // si on ferme sans choisir : premier choix possible
            for (Choix choix : evenement.choix()) {
                if (choix.peutEtreChoisi(royaume)) {
                    return choix;
                }
            }
            return evenement.choix().get(0);
        }
        return selection;
    }

    private static Frame trouverFrame(java.awt.Component composant) {
        Window fenetre = SwingUtilities.getWindowAncestor(composant);
        if (fenetre instanceof Frame) {
            return (Frame) fenetre;
        }
        return null;
    }

    // le JDialog interne
    private static class DialogueImpl extends JDialog {
        private static final long serialVersionUID = 1L;

        private Choix selection;

        DialogueImpl(Frame parent, Evenement evenement, Royaume royaume) {
            super(parent, evenement.titre(), true);
            setUndecorated(true);
            setLayout(new BorderLayout());

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

            // en-tete
            JPanel tete = new JPanel(new BorderLayout());
            tete.setOpaque(false);

            JLabel surTitre = new JLabel(
                    "Evenement".toUpperCase(),
                    SwingConstants.CENTER);
            surTitre.setFont(Polices.PETIT_LABEL.deriveFont(11f));
            surTitre.setForeground(new Color(140, 88, 32));
            tete.add(surTitre, BorderLayout.NORTH);

            JLabel titre = new JLabel(evenement.titre(),
                    SwingConstants.CENTER);
            titre.setFont(Polices.SECTION.deriveFont(22f));
            titre.setForeground(Palette.OR_CLAIR);
            titre.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Palette.OR_FONCE),
                    BorderFactory.createEmptyBorder(4, 0, 12, 0)));
            tete.add(titre, BorderLayout.CENTER);
            panneau.add(tete, BorderLayout.NORTH);

            // description
            JLabel description = new JLabel(
                    "<html><div style='text-align:center; width:480px;'>"
                            + evenement.description()
                            + "</div></html>",
                    SwingConstants.CENTER);
            description.setFont(Polices.LABEL.deriveFont(14f));
            description.setForeground(Palette.TEXTE_PRIMAIRE);
            description.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            panneau.add(description, BorderLayout.CENTER);

            // un bouton par choix
            List<Choix> choix = evenement.choix();
            JPanel piedChoix = new JPanel(new GridLayout(0, 1, 0, 6));
            piedChoix.setOpaque(false);
            piedChoix.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            for (Choix choixCourant : choix) {
                BoutonMedieval bouton = new BoutonMedieval(
                        choixCourant.libelle(),
                        BoutonMedieval.Style.PRIMAIRE);
                bouton.setPreferredSize(new Dimension(520, 40));
                if (choixCourant.peutEtreChoisi(royaume)) {
                    bouton.addActionListener(actionEvenement -> {
                        this.selection = choixCourant;
                        dispose();
                    });
                } else {
                    bouton.setEnabled(false);
                    bouton.setToolTipText("Ressources insuffisantes");
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
