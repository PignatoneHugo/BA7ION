package Vue;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Modele.partie.ConditionsFin;
import Modele.partie.Partie;
import Vue.menu.VueMenuPrincipal;
import Vue.menu.VueNouvellePartie;

/**
 * Fenetre principale du jeu. Utilise un CardLayout pour swapper entre :
 * menu principal, nouvelle partie, ecran de jeu, ecran de fin de partie.
 */
public class FenetreJeu extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final String CARTE_MENU = "menu";
    private static final String CARTE_NOUVELLE_PARTIE = "nouvelle_partie";
    private static final String CARTE_JEU = "jeu";
    private static final String CARTE_FIN_PARTIE = "fin_partie";

    private final JPanel conteneur;
    private final CardLayout cards;

    private final VueMenuPrincipal vueMenu;
    private final VueNouvellePartie vueNouvellePartie;

    private VueHUD hud;
    private VueDashboard dashboard;
    private VueStatusBar statusBar;
    private VueFinPartie vueFinPartie;

    public FenetreJeu() {
        super("BAS7ION - Simulation de royaume medieval");

        this.cards = new CardLayout();
        this.conteneur = new JPanel(this.cards);
        setContentPane(this.conteneur);

        this.vueMenu = new VueMenuPrincipal();
        this.vueNouvellePartie = new VueNouvellePartie();
        this.conteneur.add(this.vueMenu, CARTE_MENU);
        this.conteneur.add(this.vueNouvellePartie, CARTE_NOUVELLE_PARTIE);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1280, 800));
        pack();
        setLocationRelativeTo(null);
    }

    public void afficherMenu() {
        this.cards.show(this.conteneur, CARTE_MENU);
    }

    public void afficherNouvellePartie() {
        this.cards.show(this.conteneur, CARTE_NOUVELLE_PARTIE);
    }

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

    /** Affiche l'ecran de fin de partie (victoire ou defaite). */
    public void afficherFinPartie(Partie partie, ConditionsFin.Etat etat) {
        this.vueFinPartie = new VueFinPartie(partie, etat);
        this.conteneur.add(this.vueFinPartie, CARTE_FIN_PARTIE);
        this.cards.show(this.conteneur, CARTE_FIN_PARTIE);
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

    public VueFinPartie vueFinPartie() {
        return this.vueFinPartie;
    }
}
