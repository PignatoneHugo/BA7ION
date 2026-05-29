package Vue;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import Modele.partie.Partie;
import Vue.i18n.Traducteur;
import Vue.onglets.OngletEconomie;

/**
 * Panneau central de la fenetre. JTabbedPane qui contiendra
 * tous les onglets du jeu (au Sprint 1 : juste Economie).
 */
public class VueDashboard extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JTabbedPane onglets;

    public VueDashboard(Partie partie) {
        setLayout(new BorderLayout());
        this.onglets = new JTabbedPane();
        this.onglets.addTab(Traducteur.t("onglet.economie"),
                new OngletEconomie(partie.joueur()));
        add(this.onglets, BorderLayout.CENTER);
    }
}
