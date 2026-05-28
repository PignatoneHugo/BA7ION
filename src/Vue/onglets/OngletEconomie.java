package Vue.onglets;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import Modele.notification.Notification;
import Modele.population.Role;
import Modele.royaume.Royaume;
import Vue.i18n.Traducteur;

/**
 * Onglet affichant la repartition des habitants du royaume joueur par
 * {@link Role}. Vue purement passive observe du {@link Royaume} : tout
 * changement (POPULATION_CHANGEE, TRESOR_CHANGE) declenche un rafraichissement
 * complet des libelles.
 */
public class OngletEconomie extends JPanel implements Observer {

    private static final long serialVersionUID = 1L;

    private final Royaume royaume;
    private final JLabel[] labelsRoles;

    /**
     * @param royaume royaume du joueur observe par l'onglet
     */
    public OngletEconomie(Royaume royaume) {
        this.royaume = royaume;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel titre = new JLabel(Traducteur.t("onglet.population"));
        titre.setFont(titre.getFont().deriveFont(Font.BOLD, 18f));
        add(titre, BorderLayout.NORTH);

        JPanel contenu = new JPanel(new GridLayout(0, 2, 16, 8));
        Role[] roles = Role.values();
        this.labelsRoles = new JLabel[roles.length];
        for (int i = 0; i < roles.length; i++) {
            JLabel lblNom = new JLabel(Traducteur.t(roles[i].cleI18n()) + " :",
                    SwingConstants.LEFT);
            JLabel lblValeur = new JLabel("0", SwingConstants.RIGHT);
            lblValeur.setFont(lblValeur.getFont().deriveFont(Font.BOLD));
            contenu.add(lblNom);
            contenu.add(lblValeur);
            this.labelsRoles[i] = lblValeur;
        }

        JPanel centre = new JPanel(new BorderLayout());
        centre.add(contenu, BorderLayout.NORTH);
        centre.add(new JSeparator(), BorderLayout.SOUTH);
        add(centre, BorderLayout.CENTER);

        rafraichir();
        this.royaume.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(arg instanceof Notification)) {
            return;
        }
        rafraichir();
    }

    /**
     * Recalcule les compteurs d'effectif affiches a partir de l'etat courant
     * de la population.
     */
    private void rafraichir() {
        Role[] roles = Role.values();
        for (int i = 0; i < roles.length; i++) {
            this.labelsRoles[i].setText(String.valueOf(this.royaume.population().effectif(roles[i])));
        }
    }
}
