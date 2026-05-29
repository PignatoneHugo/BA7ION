package Vue;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import Modele.partie.Partie;
import Vue.i18n.Traducteur;

/**
 * Fenetre principale du jeu (JFrame).
 * Contient le VueHUD en haut et le VueDashboard au centre.
 */
public class FenetreJeu extends JFrame {

    private static final long serialVersionUID = 1L;

    private final VueHUD hud;
    private final VueDashboard dashboard;

    public FenetreJeu(Partie partie) {
        super(Traducteur.t("app.titre"));

        setLayout(new BorderLayout());

        this.hud = new VueHUD(partie);
        this.dashboard = new VueDashboard(partie);

        add(this.hud, BorderLayout.NORTH);
        add(this.dashboard, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1280, 720));
        pack();
        setLocationRelativeTo(null);
    }

    public VueHUD hud() {
        return this.hud;
    }
}
