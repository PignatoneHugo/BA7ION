package Vue;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import Modele.partie.Partie;
import Vue.i18n.Traducteur;

/**
 * Fenetre principale de l'application. Heberge en {@link BorderLayout#NORTH}
 * le bandeau d'informations ({@link VueHUD}) et en {@link BorderLayout#CENTER}
 * le panneau central a onglets ({@link VueDashboard}).
 *
 * Cette classe ne fait que le cablage visuel : aucune logique metier, aucun
 * listener attache. Les listeners sont la responsabilite des controleurs,
 * qui recuperent les composants via les accesseurs publics.
 */
public class FenetreJeu extends JFrame {

    private static final long serialVersionUID = 1L;

    private final VueHUD hud;
    private final VueDashboard dashboard;

    /**
     * @param partie modele racine observe par les composants de la fenetre
     */
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

    /**
     * @return bandeau d'informations, expose pour permettre au controleur d'y
     *         attacher ses listeners
     */
    public VueHUD hud() {
        return this.hud;
    }
}
