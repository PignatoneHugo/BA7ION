package Vue;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Modele.economie.Ressource;
import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.partie.Partie;
import Modele.royaume.Royaume;
import Vue.i18n.Traducteur;

/**
 * Bandeau permanent en haut de l'ecran de jeu. Affiche :
 *   - le nom du royaume joueur,
 *   - les 5 ressources avec leur quantite,
 *   - la population et le numero de tour,
 *   - le bouton "Fin de tour".
 *
 * Cette vue est purement passive : elle ne modifie jamais le modele. Le
 * bouton "Fin de tour" expose un getter {@link #boutonFinTour()} pour que le
 * ControleurPartie y attache son ActionListener.
 *
 * Observe a la fois la Partie (pour TOUR_TERMINE, PHASE_CHANGEE) et le
 * Royaume du joueur (pour TRESOR_CHANGE, POPULATION_CHANGEE).
 */
public class VueHUD extends JPanel implements Observer {

    private static final long serialVersionUID = 1L;

    private final Partie partie;
    private final Royaume royaumeJoueur;

    private final JLabel labelNom;
    private final JLabel labelTour;
    private final JLabel labelPopulation;
    private final JLabel[] labelsRessources;
    private final JButton boutonFinTour;

    public VueHUD(Partie partie) {
        this.partie = partie;
        this.royaumeJoueur = partie.joueur();

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, java.awt.Color.DARK_GRAY),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        // --- Partie gauche : nom + ressources ---
        JPanel gauche = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));

        this.labelNom = new JLabel(this.royaumeJoueur.nom());
        this.labelNom.setFont(this.labelNom.getFont().deriveFont(Font.BOLD, 16f));
        gauche.add(this.labelNom);

        Ressource[] ressources = Ressource.values();
        this.labelsRessources = new JLabel[ressources.length];
        for (int i = 0; i < ressources.length; i++) {
            this.labelsRessources[i] = new JLabel();
            this.labelsRessources[i].setHorizontalAlignment(SwingConstants.LEFT);
            gauche.add(this.labelsRessources[i]);
        }

        add(gauche, BorderLayout.WEST);

        // --- Partie centrale : tour + population ---
        JPanel centre = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        this.labelTour = new JLabel();
        this.labelPopulation = new JLabel();
        centre.add(this.labelTour);
        centre.add(this.labelPopulation);
        add(centre, BorderLayout.CENTER);

        // --- Partie droite : bouton fin de tour ---
        JPanel droite = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        this.boutonFinTour = new JButton(Traducteur.t("app.fin_tour"));
        this.boutonFinTour.setPreferredSize(new Dimension(140, 32));
        droite.add(this.boutonFinTour);
        add(droite, BorderLayout.EAST);

        // Affichage initial
        rafraichir();

        // Cablage Observer
        this.partie.addObserver(this);
        this.royaumeJoueur.addObserver(this);
    }

    public JButton boutonFinTour() {
        return this.boutonFinTour;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(arg instanceof Notification)) {
            return;
        }
        Notification n = (Notification) arg;
        switch (n.type()) {
            case TRESOR_CHANGE:
            case POPULATION_CHANGEE:
            case TOUR_TERMINE:
            case TOUR_DEMARRE:
            case PHASE_CHANGEE:
                rafraichir();
                break;
            default:
                // Notifications non gerees par le HUD (ignorees silencieusement).
                break;
        }
    }

    /** Recalcule tous les labels depuis l'etat courant du modele. */
    private void rafraichir() {
        Ressource[] ressources = Ressource.values();
        for (int i = 0; i < ressources.length; i++) {
            Ressource r = ressources[i];
            int q = this.royaumeJoueur.tresor().quantite(r);
            int max = this.royaumeJoueur.tresor().capaciteMax(r);
            this.labelsRessources[i].setText(Traducteur.t(r.cleI18n()) + ": " + q + "/" + max);
        }
        this.labelTour.setText(Traducteur.t("app.tour") + " " + this.partie.numeroTour());
        int total = this.royaumeJoueur.population().total();
        int cap = this.royaumeJoueur.population().capaciteLogement();
        this.labelPopulation.setText(Traducteur.t("population.total") + ": " + total + "/" + cap);
    }
}
