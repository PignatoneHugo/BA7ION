package Vue;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Modele.partie.Partie;
import Vue.i18n.Traducteur;
import Vue.menu.VueMenuPrincipal;
import Vue.menu.VueNouvellePartie;

/**
 * Fenetre principale du jeu. Utilise un CardLayout pour swapper entre les
 * differents ecrans : menu principal, ecran nouvelle partie, ecran de jeu.
 *
 * Les ecrans dependant de la Partie (HUD, Dashboard) sont crees uniquement
 * apres que le joueur a clique "Demarrer".
 */
public class FenetreJeu extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final String CARTE_MENU = "menu";
    private static final String CARTE_NOUVELLE_PARTIE = "nouvelle_partie";
    private static final String CARTE_JEU = "jeu";

    private final JPanel conteneur;
    private final CardLayout cards;

    private final VueMenuPrincipal vueMenu;
    private final VueNouvellePartie vueNouvellePartie;

    private VueHUD hud;
    private VueDashboard dashboard;
    private VueStatusBar statusBar;

    public FenetreJeu() {
        super(Traducteur.t("app.titre"));

        this.cards = new CardLayout();
        this.conteneur = new JPanel(this.cards);
        setContentPane(this.conteneur);

        this.vueMenu = new VueMenuPrincipal();
        this.vueNouvellePartie = new VueNouvellePartie();
        this.conteneur.add(this.vueMenu, CARTE_MENU);
        this.conteneur.add(this.vueNouvellePartie, CARTE_NOUVELLE_PARTIE);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1280, 720));
        pack();
        setLocationRelativeTo(null);
    }

    public void afficherMenu() {
        this.cards.show(this.conteneur, CARTE_MENU);
    }

    public void afficherNouvellePartie() {
        this.cards.show(this.conteneur, CARTE_NOUVELLE_PARTIE);
    }

    /**
     * Construit l'ecran de jeu pour la partie donnee et l'affiche.
     * Les composants HUD et Dashboard sont crees ici, observent la Partie
     * et restent en place pour toute la duree de la partie.
     */
    public void afficherJeu(Partie partie) {
        this.hud = new VueHUD(partie);
        this.dashboard = new VueDashboard(partie);
        this.statusBar = new VueStatusBar();

        JPanel ecran = new JPanel(new BorderLayout());
        ecran.add(this.hud, BorderLayout.NORTH);
        ecran.add(this.dashboard, BorderLayout.CENTER);
        ecran.add(this.statusBar, BorderLayout.SOUTH);

        this.conteneur.add(ecran, CARTE_JEU);
        this.cards.show(this.conteneur, CARTE_JEU);
    }

    public VueMenuPrincipal vueMenu() {
        return this.vueMenu;
    }

    public VueNouvellePartie vueNouvellePartie() {
        return this.vueNouvellePartie;
    }

    public VueHUD hud() {
        return this.hud;
    }

    public VueDashboard dashboard() {
        return this.dashboard;
    }

    public VueStatusBar statusBar() {
        return this.statusBar;
    }
}
