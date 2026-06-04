package Vue;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import Modele.partie.Partie;
import Vue.i18n.Traducteur;
import Vue.onglets.OngletEconomie;
import Vue.onglets.OngletInfrastructures;

/**
 * Panneau central de la fenetre. JTabbedPane qui contient les onglets du jeu
 * (Economie, Infrastructures pour l'instant).
 *
 * Expose chaque onglet via un getter pour permettre aux controleurs
 * d'attacher leurs listeners.
 */
public class VueDashboard extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JTabbedPane onglets;
    private final OngletEconomie ongletEconomie;
    private final OngletInfrastructures ongletInfrastructures;

    public VueDashboard(Partie partie) {
        setLayout(new BorderLayout());
        this.onglets = new JTabbedPane();

        this.ongletEconomie = new OngletEconomie(partie.joueur());
        this.onglets.addTab(Traducteur.t("onglet.economie"), this.ongletEconomie);

        this.ongletInfrastructures = new OngletInfrastructures(partie.joueur());
        this.onglets.addTab(Traducteur.t("onglet.infrastructures"), this.ongletInfrastructures);

        add(this.onglets, BorderLayout.CENTER);
    }

    public OngletEconomie ongletEconomie() {
        return this.ongletEconomie;
    }

    public OngletInfrastructures ongletInfrastructures() {
        return this.ongletInfrastructures;
    }
}
