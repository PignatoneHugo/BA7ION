package Vue;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import Modele.partie.Partie;
import Vue.i18n.Traducteur;
import Vue.onglets.OngletEconomie;

/**
 * Panneau central de la fenetre de jeu, organise en {@link JTabbedPane}.
 * Chaque onglet correspond a un domaine fonctionnel du royaume (economie,
 * population, infrastructures, ...).
 *
 * Cette classe n'effectue que le cablage des onglets ; chaque onglet est
 * responsable de son propre rafraichissement via Observer.
 */
public class VueDashboard extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JTabbedPane onglets;

    /**
     * @param partie modele racine, dont le royaume joueur sera observe par
     *               les onglets
     */
    public VueDashboard(Partie partie) {
        setLayout(new BorderLayout());
        this.onglets = new JTabbedPane();
        this.onglets.addTab(Traducteur.t("onglet.economie"),
                new OngletEconomie(partie.joueur()));
        add(this.onglets, BorderLayout.CENTER);
    }
}
