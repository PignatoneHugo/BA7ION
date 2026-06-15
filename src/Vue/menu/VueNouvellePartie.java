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

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);

        // titre
        JLabel titre = new JLabel("Nouvelle partie", SwingConstants.CENTER);
        titre.setFont(Polices.TITRE.deriveFont(36f));
        titre.setForeground(Palette.OR);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 8, 0);
        panneau.add(titre, c);

        // separateur
        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setForeground(Palette.OR);
        sep.setBackground(Palette.OR);
        sep.setPreferredSize(new Dimension(320, 2));
        c.gridy = 1;
        c.insets = new Insets(0, 80, 28, 80);
        panneau.add(sep, c);

        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(12, 8, 12, 8);

        // nom du royaume
        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.LINE_END;
        panneau.add(labelChamp("Nom du royaume" + " :"), c);

        this.champNom = new JTextField("Royaume du Joueur", 20);
        this.champNom.setEditable(true);
        this.champNom.setEnabled(true);
        this.champNom.setFocusable(true);
        stylerChamp(this.champNom);
        this.champNom.setPreferredSize(new Dimension(280, 36));
        c.gridx = 1;
        c.anchor = GridBagConstraints.LINE_START;
        panneau.add(this.champNom, c);

        // nombre de bots
        c.gridx = 0;
        c.gridy = 3;
        c.anchor = GridBagConstraints.LINE_END;
        panneau.add(labelChamp("Nombre d'adversaires" + " :"), c);

        this.spinnerBots = new JSpinner(new SpinnerNumberModel(1, 1, 4, 1));
        this.spinnerBots.setPreferredSize(new Dimension(80, 36));
        this.spinnerBots.setFont(Polices.VALEUR);
        ChampsMedievaux.stylerSpinner(this.spinnerBots);
        c.gridx = 1;
        c.anchor = GridBagConstraints.LINE_START;
        panneau.add(this.spinnerBots, c);

        // difficulte
        c.gridx = 0;
        c.gridy = 4;
        c.anchor = GridBagConstraints.LINE_END;
        panneau.add(labelChamp("Difficulte" + " :"), c);

        this.comboDifficulte = new JComboBox<>(Difficulte.values());
        this.comboDifficulte.setSelectedItem(Difficulte.NORMAL);
        this.comboDifficulte.setPreferredSize(new Dimension(200, 36));
        this.comboDifficulte.setRenderer(new RendererDifficulte());
        this.comboDifficulte.setFont(Polices.LABEL);
        ChampsMedievaux.stylerCombo(this.comboDifficulte);
        c.gridx = 1;
        c.anchor = GridBagConstraints.LINE_START;
        panneau.add(this.comboDifficulte, c);

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

        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(28, 0, 0, 0);
        panneau.add(actions, c);

        // on centre le panneau
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.add(panneau);
        add(wrapper, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // fond degrade
        GradientPaint grad = new GradientPaint(0, 0, Palette.FOND_HAUT, 0, h, Palette.FOND_BAS);
        g2.setPaint(grad);
        g2.fillRect(0, 0, w, h);

        // cadre
        g2.setColor(Palette.OR_FONCE);
        g2.setStroke(new java.awt.BasicStroke(2));
        g2.drawRect(8, 8, w - 16, h - 16);

        g2.dispose();
    }

    private JLabel labelChamp(String texte) {
        JLabel l = new JLabel(texte);
        l.setFont(Polices.LABEL.deriveFont(15f));
        l.setForeground(Palette.TEXTE_PRIMAIRE);
        return l;
    }

    private void stylerChamp(JTextField champ) {
        ChampsMedievaux.stylerChamp(champ);
        champ.setFont(Polices.LABEL.deriveFont(14f));
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

    public BoutonMedieval boutonDemarrer() {
        return this.boutonDemarrer;
    }

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
