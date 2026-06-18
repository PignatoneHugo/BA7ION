package Vue.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import Vue.theme.BoutonMedieval;
import Vue.theme.ChampsMedievaux;
import Vue.theme.Palette;
import Vue.theme.Polices;

import config.Difficulte;

/** Ecran de config d'une nouvelle partie : nom, nombre de bots, difficulte. */
public class VueNouvellePartie extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JTextField champNom;
    private final JSpinner spinnerBots;
    private final JComboBox<Difficulte> comboDifficulte;
    private final BoutonMedieval boutonDemarrer;
    private final BoutonMedieval boutonRetour;

    /**
     * Cree l'ecran de configuration d'une nouvelle partie.
     */
    public VueNouvellePartie() {
        setOpaque(true);
        setBackground(Palette.FOND_BAS);
        setLayout(new BorderLayout());

        // panneau du formulaire
        JPanel panneau = new JPanel(new GridBagLayout());
        panneau.setOpaque(true);
        panneau.setBackground(Palette.FOND_PANNEAU);
        panneau.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Palette.OR, 2),
                BorderFactory.createEmptyBorder(36, 56, 36, 56)));

        GridBagConstraints contraintes = new GridBagConstraints();
        contraintes.insets = new Insets(8, 8, 8, 8);

        // titre
        JLabel titre = new JLabel("Nouvelle partie", SwingConstants.CENTER);
        titre.setFont(Polices.TITRE.deriveFont(36f));
        titre.setForeground(Palette.OR);
        contraintes.gridx = 0;
        contraintes.gridy = 0;
        contraintes.gridwidth = 2;
        contraintes.fill = GridBagConstraints.HORIZONTAL;
        contraintes.insets = new Insets(0, 0, 8, 0);
        panneau.add(titre, contraintes);

        // separateur
        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setForeground(Palette.OR);
        sep.setBackground(Palette.OR);
        sep.setPreferredSize(new Dimension(320, 2));
        contraintes.gridy = 1;
        contraintes.insets = new Insets(0, 80, 28, 80);
        panneau.add(sep, contraintes);

        contraintes.gridwidth = 1;
        contraintes.fill = GridBagConstraints.NONE;
        contraintes.insets = new Insets(12, 8, 12, 8);

        // nom du royaume
        contraintes.gridx = 0;
        contraintes.gridy = 2;
        contraintes.anchor = GridBagConstraints.LINE_END;
        panneau.add(labelChamp("Nom du royaume" + " :"), contraintes);

        this.champNom = new JTextField("Royaume du Joueur", 20);
        this.champNom.setEditable(true);
        this.champNom.setEnabled(true);
        this.champNom.setFocusable(true);
        stylerChamp(this.champNom);
        this.champNom.setPreferredSize(new Dimension(280, 36));
        contraintes.gridx = 1;
        contraintes.anchor = GridBagConstraints.LINE_START;
        panneau.add(this.champNom, contraintes);

        // nombre de bots
        contraintes.gridx = 0;
        contraintes.gridy = 3;
        contraintes.anchor = GridBagConstraints.LINE_END;
        panneau.add(labelChamp("Nombre d'adversaires" + " :"), contraintes);

        this.spinnerBots = new JSpinner(new SpinnerNumberModel(1, 1, 4, 1));
        this.spinnerBots.setPreferredSize(new Dimension(80, 36));
        this.spinnerBots.setFont(Polices.VALEUR);
        ChampsMedievaux.stylerSpinner(this.spinnerBots);
        contraintes.gridx = 1;
        contraintes.anchor = GridBagConstraints.LINE_START;
        panneau.add(this.spinnerBots, contraintes);

        // difficulte
        contraintes.gridx = 0;
        contraintes.gridy = 4;
        contraintes.anchor = GridBagConstraints.LINE_END;
        panneau.add(labelChamp("Difficulte" + " :"), contraintes);

        this.comboDifficulte = new JComboBox<>(Difficulte.values());
        this.comboDifficulte.setSelectedItem(Difficulte.NORMAL);
        this.comboDifficulte.setPreferredSize(new Dimension(200, 36));
        this.comboDifficulte.setRenderer(new RendererDifficulte());
        this.comboDifficulte.setFont(Polices.LABEL);
        ChampsMedievaux.stylerCombo(this.comboDifficulte);
        contraintes.gridx = 1;
        contraintes.anchor = GridBagConstraints.LINE_START;
        panneau.add(this.comboDifficulte, contraintes);

        // boutons
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 0));
        actions.setOpaque(false);
        this.boutonRetour = new BoutonMedieval(
                "Retour", BoutonMedieval.Style.SECONDAIRE);
        this.boutonRetour.setPreferredSize(new Dimension(160, 44));
        this.boutonDemarrer = new BoutonMedieval(
                "Demarrer", BoutonMedieval.Style.PRIMAIRE);
        this.boutonDemarrer.setPreferredSize(new Dimension(220, 50));
        actions.add(this.boutonRetour);
        actions.add(this.boutonDemarrer);

        contraintes.gridx = 0;
        contraintes.gridy = 5;
        contraintes.gridwidth = 2;
        contraintes.fill = GridBagConstraints.HORIZONTAL;
        contraintes.anchor = GridBagConstraints.CENTER;
        contraintes.insets = new Insets(28, 0, 0, 0);
        panneau.add(actions, contraintes);

        // on centre le panneau
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.add(panneau);
        add(wrapper, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int largeur = getWidth();
        int hauteur = getHeight();

        // fond degrade
        GradientPaint grad = new GradientPaint(0, 0, Palette.FOND_HAUT, 0, hauteur, Palette.FOND_BAS);
        g2.setPaint(grad);
        g2.fillRect(0, 0, largeur, hauteur);

        // cadre
        g2.setColor(Palette.OR_FONCE);
        g2.setStroke(new java.awt.BasicStroke(2));
        g2.drawRect(8, 8, largeur - 16, hauteur - 16);

        g2.dispose();
    }

    private JLabel labelChamp(String texte) {
        JLabel label = new JLabel(texte);
        label.setFont(Polices.LABEL.deriveFont(15f));
        label.setForeground(Palette.TEXTE_PRIMAIRE);
        return label;
    }

    private void stylerChamp(JTextField champ) {
        ChampsMedievaux.stylerChamp(champ);
        champ.setFont(Polices.LABEL.deriveFont(14f));
    }

    /**
     * Renvoie le nom du royaume saisi par le joueur.
     *
     * @return le nom du royaume
     */
    public String nomJoueur() {
        return this.champNom.getText();
    }

    /**
     * Renvoie le nombre d'adversaires choisi.
     *
     * @return le nombre de bots
     */
    public int nombreBots() {
        return (Integer) this.spinnerBots.getValue();
    }

    /**
     * Renvoie la difficulte choisie dans la liste.
     *
     * @return la difficulte selectionnee
     */
    public Difficulte difficulteSelectionnee() {
        return (Difficulte) this.comboDifficulte.getSelectedItem();
    }

    /**
     * Renvoie le bouton Demarrer.
     *
     * @return le bouton Demarrer
     */
    public BoutonMedieval boutonDemarrer() {
        return this.boutonDemarrer;
    }

    /**
     * Renvoie le bouton Retour.
     *
     * @return le bouton Retour
     */
    public BoutonMedieval boutonRetour() {
        return this.boutonRetour;
    }

    // affiche le libelle de chaque difficulte dans la combo
    private static class RendererDifficulte extends DefaultListCellRenderer {
        private static final long serialVersionUID = 1L;

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Difficulte) {
                setText(((Difficulte) value).libelle());
            }
            setBackground(isSelected ? Palette.BOUTON_FOND_SURVOL : Palette.CHAMP_FOND);
            setForeground(Palette.TEXTE_PRIMAIRE);
            return this;
        }
    }
}
