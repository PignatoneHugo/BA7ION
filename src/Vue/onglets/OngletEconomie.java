package Vue.onglets;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.EnumMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import Modele.economie.NiveauTaxes;
import Modele.notification.Notification;
import Modele.population.Role;
import Modele.royaume.Royaume;
import Vue.i18n.Traducteur;

/**
 * Onglet Economie : affiche le nombre d'habitants par role avec des boutons
 * +/- pour deplacer un habitant entre INACTIF et le role correspondant.
 * Affiche aussi un selecteur de niveau de taxes.
 *
 * Les boutons et toggles sont crees par la vue mais les listeners sont
 * attaches par le ControleurEconomie.
 */
public class OngletEconomie extends JPanel implements Observer {

    private static final long serialVersionUID = 1L;

    private final Royaume royaume;
    private final Map<Role, JLabel> labelsEffectif;
    private final Map<Role, JButton> boutonsPlus;
    private final Map<Role, JButton> boutonsMoins;
    private final Map<NiveauTaxes, JToggleButton> togglesTaxes;

    public OngletEconomie(Royaume royaume) {
        this.royaume = royaume;
        this.labelsEffectif = new EnumMap<>(Role.class);
        this.boutonsPlus = new EnumMap<>(Role.class);
        this.boutonsMoins = new EnumMap<>(Role.class);
        this.togglesTaxes = new EnumMap<>(NiveauTaxes.class);

        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel titre = new JLabel(Traducteur.t("onglet.population"));
        titre.setFont(titre.getFont().deriveFont(Font.BOLD, 18f));
        add(titre, BorderLayout.NORTH);

        JPanel centre = new JPanel(new BorderLayout(0, 16));
        centre.add(creerBlocRoles(), BorderLayout.NORTH);
        centre.add(creerBlocTaxes(), BorderLayout.CENTER);
        add(centre, BorderLayout.CENTER);

        rafraichir();
        this.royaume.addObserver(this);
    }

    private JPanel creerBlocRoles() {
        JPanel contenu = new JPanel(new GridLayout(0, 3, 16, 8));
        for (Role r : Role.values()) {
            JLabel lblNom = new JLabel(Traducteur.t(r.cleI18n()) + " :", SwingConstants.LEFT);

            JLabel lblValeur = new JLabel("0", SwingConstants.RIGHT);
            lblValeur.setFont(lblValeur.getFont().deriveFont(Font.BOLD));
            this.labelsEffectif.put(r, lblValeur);

            JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
            if (r != Role.INACTIF) {
                JButton bMoins = creerBouton("-");
                JButton bPlus = creerBouton("+");
                this.boutonsMoins.put(r, bMoins);
                this.boutonsPlus.put(r, bPlus);
                actions.add(bMoins);
                actions.add(bPlus);
            }

            contenu.add(lblNom);
            contenu.add(lblValeur);
            contenu.add(actions);
        }
        return contenu;
    }

    private JPanel creerBlocTaxes() {
        JPanel bloc = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        bloc.add(new JLabel(Traducteur.t("taxes.titre") + " :"));

        ButtonGroup groupe = new ButtonGroup();
        for (NiveauTaxes n : NiveauTaxes.values()) {
            JToggleButton t = new JToggleButton(Traducteur.t(n.cleI18n()));
            this.togglesTaxes.put(n, t);
            groupe.add(t);
            bloc.add(t);
        }
        return bloc;
    }

    private JButton creerBouton(String texte) {
        JButton b = new JButton(texte);
        b.setPreferredSize(new Dimension(40, 24));
        b.setMargin(new java.awt.Insets(0, 0, 0, 0));
        return b;
    }

    /** Exposes pour que le controleur attache les ActionListener. */
    public JButton boutonPlus(Role role) {
        return this.boutonsPlus.get(role);
    }

    public JButton boutonMoins(Role role) {
        return this.boutonsMoins.get(role);
    }

    public JToggleButton toggleTaxes(NiveauTaxes niveau) {
        return this.togglesTaxes.get(niveau);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(arg instanceof Notification)) {
            return;
        }
        rafraichir();
    }

    /**
     * Met a jour les compteurs, l'etat actif/inactif des boutons et la
     * selection du toggle de taxes selon l'etat courant du royaume.
     */
    private void rafraichir() {
        int inactifs = this.royaume.population().effectif(Role.INACTIF);
        for (Role r : Role.values()) {
            int effectif = this.royaume.population().effectif(r);
            this.labelsEffectif.get(r).setText(String.valueOf(effectif));
            if (r != Role.INACTIF) {
                this.boutonsPlus.get(r).setEnabled(inactifs > 0);
                this.boutonsMoins.get(r).setEnabled(effectif > 0);
            }
        }
        NiveauTaxes courant = this.royaume.niveauTaxes();
        for (NiveauTaxes n : NiveauTaxes.values()) {
            this.togglesTaxes.get(n).setSelected(n == courant);
        }
    }
}
