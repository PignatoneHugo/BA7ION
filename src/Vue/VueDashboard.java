package Vue;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import Modele.partie.Partie;
import Vue.i18n.Traducteur;
import Vue.onglets.OngletEconomie;

/**
 * Panneau central du jeu : JTabbedPane qui contient tous les onglets metier.
 *
 * Au Sprint 1, on n'a qu'un seul onglet (Economie). Les 7 autres viendront
 * Sprint 2+ (Population dediee, Infrastructures, Recherche, Militaire,
 * Espionnage, Diplomatie, Journal).
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
