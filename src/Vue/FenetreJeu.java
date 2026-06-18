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

/** Fenetre principale : change d'ecran avec un CardLayout (menu, jeu, fin...). */
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

    /**
     * Cree la fenetre principale avec le menu et l'ecran de nouvelle partie.
     */
    public FenetreJeu() {
        super("BA7ION - Simulation de royaume medieval");

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

    /**
     * Affiche l'ecran du menu principal.
     */
    public void afficherMenu() {
        this.cards.show(this.conteneur, CARTE_MENU);
    }

    /**
     * Affiche l'ecran de configuration d'une nouvelle partie.
     */
    public void afficherNouvellePartie() {
        this.cards.show(this.conteneur, CARTE_NOUVELLE_PARTIE);
    }

    /**
     * Construit et affiche l'ecran de jeu pour la partie donnee.
     *
     * @param partie la partie en cours
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

    /**
     * Construit et affiche l'ecran de fin de partie.
     *
     * @param partie la partie terminee
     * @param etat l'etat final (victoire ou defaite)
     */
    public void afficherFinPartie(Partie partie, ConditionsFin.Etat etat) {
        this.vueFinPartie = new VueFinPartie(partie, etat);
        this.conteneur.add(this.vueFinPartie, CARTE_FIN_PARTIE);
        this.cards.show(this.conteneur, CARTE_FIN_PARTIE);
    }

    /**
     * Renvoie la vue du menu principal.
     *
     * @return la vue du menu principal
     */
    public VueMenuPrincipal vueMenu() {
        return this.vueMenu;
    }

    /**
     * Renvoie la vue de nouvelle partie.
     *
     * @return la vue de nouvelle partie
     */
    public VueNouvellePartie vueNouvellePartie() {
        return this.vueNouvellePartie;
    }

    /**
     * Renvoie le bandeau du haut (HUD).
     *
     * @return le HUD, ou null si le jeu n'est pas lance
     */
    public VueHUD hud() {
        return this.hud;
    }

    /**
     * Renvoie le panneau central (dashboard).
     *
     * @return le dashboard, ou null si le jeu n'est pas lance
     */
    public VueDashboard dashboard() {
        return this.dashboard;
    }

    /**
     * Renvoie la barre de statut du bas.
     *
     * @return la barre de statut, ou null si le jeu n'est pas lance
     */
    public VueStatusBar statusBar() {
        return this.statusBar;
    }

    /**
     * Renvoie la vue de fin de partie.
     *
     * @return la vue de fin de partie, ou null si la partie n'est pas finie
     */
    public VueFinPartie vueFinPartie() {
        return this.vueFinPartie;
    }
}
