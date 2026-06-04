package Vue.menu;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import Vue.i18n.Traducteur;

import config.Difficulte;

/**
 * Ecran de configuration d'une nouvelle partie. Permet au joueur de saisir
 * le nom de son royaume, le nombre d'adversaires (bots) et la difficulte,
 * puis de lancer la partie.
 *
 * Vue passive : les champs et boutons sont exposes via getters pour que le
 * ControleurMenu les lise et y attache ses listeners.
 */
public class VueNouvellePartie extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JTextField champNom;
    private final JSpinner spinnerBots;
    private final JComboBox<Difficulte> comboDifficulte;
    private final JButton boutonDemarrer;
    private final JButton boutonRetour;

    public VueNouvellePartie() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.anchor = GridBagConstraints.LINE_START;

        JLabel titre = new JLabel(Traducteur.t("nouvelle_partie.titre"), SwingConstants.CENTER);
        titre.setFont(titre.getFont().deriveFont(Font.BOLD, 28f));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(8, 8, 32, 8);
        add(titre, c);

        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(8, 8, 8, 8);

        // Nom du royaume
        c.gridx = 0;
        c.gridy = 1;
        add(new JLabel(Traducteur.t("nouvelle_partie.nom_joueur") + " :"), c);

        this.champNom = new JTextField("Royaume du Joueur", 20);
        this.champNom.setPreferredSize(new Dimension(220, 28));
        c.gridx = 1;
        add(this.champNom, c);

        // Nombre de bots
        c.gridx = 0;
        c.gridy = 2;
        add(new JLabel(Traducteur.t("nouvelle_partie.nb_bots") + " :"), c);

        this.spinnerBots = new JSpinner(new SpinnerNumberModel(0, 0, 4, 1));
        this.spinnerBots.setPreferredSize(new Dimension(60, 28));
        c.gridx = 1;
        add(this.spinnerBots, c);

        // Difficulte
        c.gridx = 0;
        c.gridy = 3;
        add(new JLabel(Traducteur.t("difficulte.titre") + " :"), c);

        this.comboDifficulte = new JComboBox<>(Difficulte.values());
        this.comboDifficulte.setSelectedItem(Difficulte.NORMAL);
        this.comboDifficulte.setPreferredSize(new Dimension(160, 28));
        this.comboDifficulte.setRenderer(new RendererDifficulte());
        c.gridx = 1;
        add(this.comboDifficulte, c);

        // Boutons
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        this.boutonRetour = new JButton(Traducteur.t("nouvelle_partie.retour"));
        this.boutonDemarrer = new JButton(Traducteur.t("nouvelle_partie.demarrer"));
        this.boutonDemarrer.setFont(this.boutonDemarrer.getFont().deriveFont(Font.BOLD));
        actions.add(this.boutonRetour);
        actions.add(this.boutonDemarrer);

        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(32, 8, 8, 8);
        add(actions, c);
    }

    public String nomJoueur() {
        return this.champNom.getText();
    }

    public int nombreBots() {
        return (Integer) this.spinnerBots.getValue();
    }

    public Difficulte difficulteSelectionnee() {
        return (Difficulte) this.comboDifficulte.getSelectedItem();
    }

    public JButton boutonDemarrer() {
        return this.boutonDemarrer;
    }

    public JButton boutonRetour() {
        return this.boutonRetour;
    }

    /** Renderer custom pour afficher le libelle traduit de chaque Difficulte. */
    private static class RendererDifficulte extends DefaultListCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Difficulte) {
                setText(Traducteur.t(((Difficulte) value).cleI18n()));
            }
            return this;
        }
    }
}
