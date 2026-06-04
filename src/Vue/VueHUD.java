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
import Modele.partie.Partie;
import Modele.royaume.Royaume;
import Vue.i18n.Traducteur;

/**
 * Bandeau permanent en haut de la fenetre.
 * Affiche : nom du royaume, ressources, population, tour, bouton "Fin de tour".
 *
 * Observer de Partie (pour le tour) et de Royaume (pour ressources/population).
 * Le bouton est expose via boutonFinTour() pour que le controleur l'ecoute.
 */
public class VueHUD extends JPanel implements Observer {

    private static final long serialVersionUID = 1L;

    private final Partie partie;
    private final Royaume royaumeJoueur;

    private final JLabel labelNom;
    private final JLabel labelTour;
    private final JLabel labelPopulation;
    private final JLabel labelMoral;
    private final JLabel[] labelsRessources;
    private final JButton boutonFinTour;

    public VueHUD(Partie partie) {
        this.partie = partie;
        this.royaumeJoueur = partie.joueur();

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, java.awt.Color.DARK_GRAY),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        // Gauche : nom + ressources
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

        // Centre : tour + population + moral
        JPanel centre = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        this.labelTour = new JLabel();
        this.labelPopulation = new JLabel();
        this.labelMoral = new JLabel();
        centre.add(this.labelTour);
        centre.add(this.labelPopulation);
        centre.add(this.labelMoral);
        add(centre, BorderLayout.CENTER);

        // Droite : bouton fin de tour
        JPanel droite = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        this.boutonFinTour = new JButton(Traducteur.t("app.fin_tour"));
        this.boutonFinTour.setPreferredSize(new Dimension(140, 32));
        droite.add(this.boutonFinTour);
        add(droite, BorderLayout.EAST);

        rafraichir();

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
            case MORAL_CHANGE:
            case TOUR_TERMINE:
            case TOUR_DEMARRE:
            case PHASE_CHANGEE:
                rafraichir();
                break;
            default:
                break;
        }
    }

    /** Met a jour tous les labels depuis l'etat courant du modele. */
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
        int moral = this.royaumeJoueur.moral().valeur();
        this.labelMoral.setText(Traducteur.t("moral.titre") + ": " + moral + "/100");
    }
}
