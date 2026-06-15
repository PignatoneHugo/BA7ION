package Vue.onglets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.EnumMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;

import Modele.economie.Ressource;
import Modele.infrastructure.Marche;
import Modele.infrastructure.TypeBatiment;
import Modele.notification.Notification;
import Modele.royaume.Royaume;
import Vue.theme.BoutonMedieval;
import Vue.theme.ChampsMedievaux;
import Vue.theme.Palette;
import Vue.theme.Polices;
import Vue.theme.ToggleMedieval;

/** Onglet Marche : echange une ressource contre une autre selon le taux du marche. */
public class OngletMarche extends JPanel implements Observer {

    private static final long serialVersionUID = 1L;

    private final Royaume royaume;

    private final JLabel labelTitre;
    private final Map<Ressource, ToggleMedieval> togglesSource;
    private final Map<Ressource, ToggleMedieval> togglesCible;
    private final JSpinner spinnerMontant;
    private final JLabel labelResultat;
    private final BoutonMedieval boutonEchanger;

    public OngletMarche(Royaume royaume) {
        this.royaume = royaume;
        this.togglesSource = new EnumMap<>(Ressource.class);
        this.togglesCible = new EnumMap<>(Ressource.class);

        setOpaque(true);
        setBackground(Palette.FOND_PANNEAU);
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        // titre + niveau du marche
        JPanel tete = new JPanel(new BorderLayout());
        tete.setOpaque(false);

        JLabel titre = new JLabel("Marche".toUpperCase(),
                SwingConstants.LEFT);
        titre.setFont(Polices.SECTION.deriveFont(16f));
        titre.setForeground(Palette.OR);
        tete.add(titre, BorderLayout.WEST);

        this.labelTitre = new JLabel("", SwingConstants.RIGHT);
        this.labelTitre.setFont(Polices.VALEUR.deriveFont(13f));
        this.labelTitre.setForeground(Palette.OR_CLAIR);
        tete.add(this.labelTitre, BorderLayout.EAST);

        tete.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Palette.OR_FONCE),
                BorderFactory.createEmptyBorder(0, 0, 6, 0)));
        add(tete, BorderLayout.NORTH);

        // 2 selecteurs + resultat + bouton
        JPanel centre = new JPanel();
        centre.setOpaque(false);
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));

        // ce qu'on donne
        centre.add(creerSousBloc("Donner", this.togglesSource));

        // le montant
        centre.add(Box.createVerticalStrut(10));
        JPanel ligneMontant = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        ligneMontant.setOpaque(false);
        ligneMontant.setAlignmentX(LEFT_ALIGNMENT);
        JLabel labelMontant = new JLabel("Montant a donner" + " :");
        labelMontant.setFont(Polices.LABEL.deriveFont(13f));
        labelMontant.setForeground(Palette.TEXTE_PRIMAIRE);
        this.spinnerMontant = new JSpinner(new SpinnerNumberModel(10, 1, 9999, 10));
        this.spinnerMontant.setPreferredSize(new Dimension(110, 32));
        this.spinnerMontant.setFont(Polices.VALEUR.deriveFont(14f));
        ChampsMedievaux.stylerSpinner(this.spinnerMontant);
        ligneMontant.add(labelMontant);
        ligneMontant.add(this.spinnerMontant);
        ligneMontant.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        centre.add(ligneMontant);

        centre.add(Box.createVerticalStrut(10));

        // ce qu'on recoit
        centre.add(creerSousBloc("Recevoir", this.togglesCible));

        // resultat + bouton
        centre.add(Box.createVerticalStrut(12));
        this.labelResultat = new JLabel("", SwingConstants.CENTER);
        this.labelResultat.setFont(Polices.VALEUR.deriveFont(18f));
        this.labelResultat.setForeground(Palette.OR_CLAIR);
        JPanel wrapperResultat = new JPanel(new BorderLayout());
        wrapperResultat.setOpaque(false);
        wrapperResultat.setAlignmentX(LEFT_ALIGNMENT);
        wrapperResultat.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        wrapperResultat.add(this.labelResultat, BorderLayout.CENTER);
        centre.add(wrapperResultat);

        centre.add(Box.createVerticalStrut(8));
        this.boutonEchanger = new BoutonMedieval(
                "Echanger".toUpperCase(),
                BoutonMedieval.Style.PRIMAIRE);
        this.boutonEchanger.setPreferredSize(new Dimension(220, 40));
        JPanel wrapperBouton = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapperBouton.setOpaque(false);
        wrapperBouton.setAlignmentX(LEFT_ALIGNMENT);
        wrapperBouton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        wrapperBouton.add(this.boutonEchanger);
        centre.add(wrapperBouton);

        centre.add(Box.createVerticalGlue());
        add(centre, BorderLayout.CENTER);

        // on rafraichit le resultat a chaque changement
        ChangeListener cl = e -> rafraichirResultat();
        this.spinnerMontant.addChangeListener(cl);
        for (ToggleMedieval t : this.togglesSource.values()) {
            t.addActionListener(e -> rafraichirResultat());
        }
        for (ToggleMedieval t : this.togglesCible.values()) {
            t.addActionListener(e -> rafraichirResultat());
        }

        // par defaut : donner OR, recevoir NOURRITURE
        this.togglesSource.get(Ressource.OR).setSelected(true);
        this.togglesCible.get(Ressource.NOURRITURE).setSelected(true);

        rafraichir();
        this.royaume.addObserver(this);
    }

    // un sous-bloc : un titre + une ligne de boutons ressource
    private JPanel creerSousBloc(String titre, Map<Ressource, ToggleMedieval> map) {
        JPanel bloc = new JPanel();
        bloc.setOpaque(true);
        bloc.setBackground(Palette.FOND_PANNEAU_CLAIR);
        bloc.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Palette.OR_FONCE, 1),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        bloc.setLayout(new BoxLayout(bloc, BoxLayout.Y_AXIS));
        bloc.setAlignmentX(LEFT_ALIGNMENT);

        JLabel sousTitre = new JLabel(titre.toUpperCase());
        sousTitre.setFont(Polices.SECTION.deriveFont(13f));
        sousTitre.setForeground(Palette.OR);
        sousTitre.setAlignmentX(LEFT_ALIGNMENT);
        bloc.add(sousTitre);
        bloc.add(Box.createVerticalStrut(6));

        JPanel ligne = new JPanel(new GridLayout(1, Ressource.values().length, 6, 0));
        ligne.setOpaque(false);
        ligne.setAlignmentX(LEFT_ALIGNMENT);
        ligne.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        ButtonGroup grp = new ButtonGroup();
        for (Ressource r : Ressource.values()) {
            ToggleMedieval t = new ToggleMedieval(r.libelle());
            t.setPreferredSize(new Dimension(110, 32));
            grp.add(t);
            map.put(r, t);
            ligne.add(t);
        }
        bloc.add(ligne);
        return bloc;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(arg instanceof Notification)) {
            return;
        }
        rafraichir();
    }

    private void rafraichir() {
        Marche marche = (Marche) this.royaume.batiment(TypeBatiment.MARCHE);
        if (marche != null) {
            this.labelTitre.setText(
                    "Niveau" + " " + marche.niveau()
                            + "  •  " + "Taux" + " : "
                            + String.format("%.1f", marche.tauxEchange()) + " "
                            + "pour 1");
        }
        rafraichirResultat();
    }

    // met a jour le resultat et active/desactive le bouton
    private void rafraichirResultat() {
        Ressource src = ressourceSelectionnee(this.togglesSource);
        Ressource cible = ressourceSelectionnee(this.togglesCible);
        int montant = (Integer) this.spinnerMontant.getValue();

        Marche marche = (Marche) this.royaume.batiment(TypeBatiment.MARCHE);
        if (src == null || cible == null || marche == null) {
            this.labelResultat.setText("--");
            this.boutonEchanger.setEnabled(false);
            return;
        }
        if (src == cible) {
            this.labelResultat.setText("Choisir 2 ressources differentes");
            this.labelResultat.setForeground(Palette.TEXTE_TERTIAIRE);
            this.boutonEchanger.setEnabled(false);
            return;
        }
        int recu = marche.quantiteRecue(montant);
        this.labelResultat.setText("→ " + recu + " " + cible.libelle());
        this.labelResultat.setForeground(couleurRessource(cible));

        boolean assez = this.royaume.tresor().contient(src, montant);
        this.boutonEchanger.setEnabled(assez && recu > 0);
    }

    private Ressource ressourceSelectionnee(Map<Ressource, ToggleMedieval> map) {
        for (Map.Entry<Ressource, ToggleMedieval> e : map.entrySet()) {
            if (e.getValue().isSelected()) {
                return e.getKey();
            }
        }
        return null;
    }

    private Color couleurRessource(Ressource r) {
        switch (r) {
            case OR: return Palette.OR_RESSOURCE;
            case NOURRITURE: return Palette.NOURRITURE_RESSOURCE;
            case BOIS: return Palette.BOIS_RESSOURCE;
            case PIERRE: return Palette.PIERRE_RESSOURCE;
            case SAVOIR: return Palette.SAVOIR_RESSOURCE;
            default: return Palette.TEXTE_PRIMAIRE;
        }
    }

    public BoutonMedieval boutonEchanger() {
        return this.boutonEchanger;
    }

    public Ressource ressourceSource() {
        return ressourceSelectionnee(this.togglesSource);
    }

    public Ressource ressourceCible() {
        return ressourceSelectionnee(this.togglesCible);
    }

    public int montantSource() {
        return (Integer) this.spinnerMontant.getValue();
    }
}
