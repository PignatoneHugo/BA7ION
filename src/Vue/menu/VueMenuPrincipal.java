package Vue.menu;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Vue.i18n.Traducteur;

/**
 * Ecran d'accueil du jeu. Affiche le titre et trois boutons :
 * Nouvelle partie, Options, Quitter.
 *
 * Vue passive : les boutons sont exposes via getters pour que le
 * ControleurMenu y attache ses listeners.
 */
public class VueMenuPrincipal extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JButton boutonNouvellePartie;
    private final JButton boutonOptions;
    private final JButton boutonQuitter;

    public VueMenuPrincipal() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(8, 0, 8, 0);

        JLabel titre = new JLabel(Traducteur.t("menu.titre"), SwingConstants.CENTER);
        titre.setFont(titre.getFont().deriveFont(Font.BOLD, 48f));
        c.gridy = 0;
        add(titre, c);

        JLabel sousTitre = new JLabel(Traducteur.t("menu.sous_titre"), SwingConstants.CENTER);
        sousTitre.setFont(sousTitre.getFont().deriveFont(Font.ITALIC, 16f));
        c.gridy = 1;
        c.insets = new Insets(0, 0, 40, 0);
        add(sousTitre, c);

        c.insets = new Insets(8, 0, 8, 0);

        this.boutonNouvellePartie = creerBouton(Traducteur.t("menu.nouvelle_partie"));
        c.gridy = 2;
        add(this.boutonNouvellePartie, c);

        this.boutonOptions = creerBouton(Traducteur.t("menu.options"));
        c.gridy = 3;
        add(this.boutonOptions, c);

        this.boutonQuitter = creerBouton(Traducteur.t("menu.quitter"));
        c.gridy = 4;
        add(this.boutonQuitter, c);

        c.gridy = 5;
        add(Box.createVerticalStrut(60), c);
    }

    private JButton creerBouton(String texte) {
        JButton b = new JButton(texte);
        b.setPreferredSize(new Dimension(280, 48));
        b.setFont(b.getFont().deriveFont(Font.BOLD, 16f));
        return b;
    }

    public JButton boutonNouvellePartie() {
        return this.boutonNouvellePartie;
    }

    public JButton boutonOptions() {
        return this.boutonOptions;
    }

    public JButton boutonQuitter() {
        return this.boutonQuitter;
    }
}
