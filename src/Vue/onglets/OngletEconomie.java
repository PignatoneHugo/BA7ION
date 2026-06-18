package Vue.onglets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import Modele.economie.NiveauTaxes;
import Modele.notification.Notification;
import Modele.population.Role;
import Modele.royaume.Royaume;
import Vue.theme.BoutonMedieval;
import Vue.theme.Palette;
import Vue.theme.Polices;
import Vue.theme.ToggleMedieval;

/** Onglet Economie : repartition des habitants par role + niveau de taxes. */
public class OngletEconomie extends JPanel implements Observer {

    private static final long serialVersionUID = 1L;

    private final Royaume royaume;
    private final Map<Role, JLabel> labelsEffectif;
    private final Map<Role, BoutonMedieval> boutonsPlus;
    private final Map<Role, BoutonMedieval> boutonsMoins;
    private final Map<NiveauTaxes, JToggleButton> togglesTaxes;
    private final BoutonMedieval boutonRecruter;

    /**
     * Cree l'onglet Economie et s'abonne aux changements du royaume.
     *
     * @param royaume le royaume du joueur
     */
    public OngletEconomie(Royaume royaume) {
        this.royaume = royaume;
        this.labelsEffectif = new EnumMap<>(Role.class);
        this.boutonsPlus = new EnumMap<>(Role.class);
        this.boutonsMoins = new EnumMap<>(Role.class);
        this.togglesTaxes = new EnumMap<>(NiveauTaxes.class);

        // le + sur la ligne INACTIF sert a recruter un villageois
        this.boutonRecruter = new BoutonMedieval("+", BoutonMedieval.Style.SECONDAIRE);
        this.boutonRecruter.setPreferredSize(new Dimension(44, 30));

        setOpaque(true);
        setBackground(Palette.FOND_PANNEAU);
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        add(creerTitreSection("Economie"), BorderLayout.NORTH);

        // roles + taxes
        JPanel centre = new JPanel();
        centre.setOpaque(false);
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));

        centre.add(creerBlocRoles());
        centre.add(Box.createVerticalStrut(16));
        centre.add(creerBlocTaxes());
        centre.add(Box.createVerticalGlue());

        add(centre, BorderLayout.CENTER);

        rafraichir();
        this.royaume.addObserver(this);
    }

    private JLabel creerTitreSection(String texte) {
        JLabel label = new JLabel(texte.toUpperCase(), SwingConstants.LEFT);
        label.setFont(Polices.SECTION.deriveFont(16f));
        label.setForeground(Palette.OR);
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Palette.OR_FONCE),
                BorderFactory.createEmptyBorder(0, 0, 6, 0)));
        return label;
    }

    private JLabel creerSousTitre(String texte) {
        JLabel label = new JLabel(texte.toUpperCase());
        label.setFont(Polices.SECTION.deriveFont(13f));
        label.setForeground(Palette.OR);
        return label;
    }

    private JPanel creerBlocRoles() {
        JPanel bloc = new JPanel();
        bloc.setLayout(new BoxLayout(bloc, BoxLayout.Y_AXIS));
        bloc.setOpaque(true);
        bloc.setBackground(Palette.FOND_PANNEAU_CLAIR);
        bloc.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Palette.OR_FONCE, 1),
                BorderFactory.createEmptyBorder(14, 18, 14, 18)));

        JLabel sousTitre = creerSousTitre("Repartition des habitants");
        sousTitre.setAlignmentX(LEFT_ALIGNMENT);
        bloc.add(sousTitre);
        bloc.add(Box.createVerticalStrut(10));

        for (Role role : Role.values()) {
            bloc.add(creerLigneRole(role));
            bloc.add(Box.createVerticalStrut(6));
        }

        return bloc;
    }

    // une ligne de role : pastille + nom + effectif + boutons +/-
    private JPanel creerLigneRole(Role role) {
        JPanel ligne = new JPanel(new BorderLayout(12, 0));
        ligne.setOpaque(false);
        ligne.setAlignmentX(LEFT_ALIGNMENT);
        ligne.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        ligne.setPreferredSize(new Dimension(1, 36));

        // gauche : pastille + nom
        JPanel gauche = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        gauche.setOpaque(false);
        gauche.add(creerPastilleRole(role));
        JLabel nom = new JLabel(role.libelle());
        nom.setFont(Polices.LABEL.deriveFont(15f));
        nom.setForeground(couleurDe(role));
        gauche.add(nom);
        if (role == Role.INACTIF) {
            JLabel cout = new JLabel("(-100 nourriture pour recruter)");
            cout.setFont(Polices.LABEL.deriveFont(java.awt.Font.ITALIC, 12f));
            cout.setForeground(Palette.NOURRITURE_RESSOURCE);
            gauche.add(cout);
        }
        ligne.add(gauche, BorderLayout.WEST);

        // vide au centre pour pousser les boutons a droite
        ligne.add(new JLabel(""), BorderLayout.CENTER);

        // droite : effectif + boutons
        JPanel droite = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        droite.setOpaque(false);

        JLabel valeur = new JLabel("0", SwingConstants.RIGHT);
        valeur.setFont(Polices.VALEUR.deriveFont(18f));
        valeur.setForeground(couleurDe(role));
        valeur.setPreferredSize(new Dimension(54, 28));
        this.labelsEffectif.put(role, valeur);
        droite.add(valeur);

        if (role == Role.INACTIF) {
            // pour INACTIF : le - est desactive, le + recrute
            BoutonMedieval bMoins = new BoutonMedieval("−", BoutonMedieval.Style.SECONDAIRE);
            bMoins.setPreferredSize(new Dimension(44, 30));
            bMoins.setEnabled(false);
            this.boutonsMoins.put(role, bMoins);
            droite.add(bMoins);
            droite.add(this.boutonRecruter);
            this.boutonsPlus.put(role, this.boutonRecruter);
        } else {
            BoutonMedieval bMoins = new BoutonMedieval("−", BoutonMedieval.Style.SECONDAIRE);
            bMoins.setPreferredSize(new Dimension(44, 30));
            BoutonMedieval bPlus = new BoutonMedieval("+", BoutonMedieval.Style.SECONDAIRE);
            bPlus.setPreferredSize(new Dimension(44, 30));
            this.boutonsMoins.put(role, bMoins);
            this.boutonsPlus.put(role, bPlus);
            droite.add(bMoins);
            droite.add(bPlus);
        }
        ligne.add(droite, BorderLayout.EAST);
        return ligne;
    }

    // bloc taxes : 3 boutons bascule
    private JPanel creerBlocTaxes() {
        JPanel bloc = new JPanel();
        bloc.setLayout(new BoxLayout(bloc, BoxLayout.Y_AXIS));
        bloc.setOpaque(true);
        bloc.setBackground(Palette.FOND_PANNEAU_CLAIR);
        bloc.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Palette.OR_FONCE, 1),
                BorderFactory.createEmptyBorder(14, 18, 14, 18)));

        JLabel sousTitre = creerSousTitre("Taxes");
        sousTitre.setAlignmentX(LEFT_ALIGNMENT);
        bloc.add(sousTitre);
        bloc.add(Box.createVerticalStrut(10));

        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        boutons.setOpaque(false);
        boutons.setAlignmentX(LEFT_ALIGNMENT);

        ButtonGroup grp = new ButtonGroup();
        for (NiveauTaxes niveau : NiveauTaxes.values()) {
            ToggleMedieval toggle = new ToggleMedieval(niveau.libelle());
            toggle.setPreferredSize(new Dimension(140, 36));
            grp.add(toggle);
            this.togglesTaxes.put(niveau, toggle);
            boutons.add(toggle);
        }
        bloc.add(boutons);
        return bloc;
    }

    private JPanel creerPastilleRole(Role role) {
        JPanel pastille = new JPanel() {
            private static final long serialVersionUID = 1L;
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                Graphics2D g2 = (Graphics2D) graphics.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                int diametre = Math.min(getWidth(), getHeight()) - 2;
                int centreX = (getWidth() - diametre) / 2;
                int centreY = (getHeight() - diametre) / 2;
                g2.setColor(couleurDe(role).darker().darker());
                g2.fillOval(centreX, centreY, diametre, diametre);
                g2.setColor(couleurDe(role));
                g2.fillOval(centreX + 3, centreY + 3, diametre - 6, diametre - 6);
                g2.dispose();
            }
        };
        pastille.setOpaque(false);
        pastille.setPreferredSize(new Dimension(20, 20));
        return pastille;
    }

    private Color couleurDe(Role role) {
        switch (role) {
            case INACTIF: return Palette.OR;
            case FERMIER: return Palette.NOURRITURE_RESSOURCE;
            case MINEUR: return Palette.PIERRE_RESSOURCE;
            case BUCHERON: return Palette.BOIS_RESSOURCE;
            case ERUDIT: return Palette.SAVOIR_RESSOURCE;
            case SOLDAT: return Palette.ROUGE_DANGER;
            default: return Palette.TEXTE_PRIMAIRE;
        }
    }

    /**
     * Renvoie le bouton + d'un role donne.
     *
     * @param role le role concerne
     * @return le bouton + de ce role
     */
    public BoutonMedieval boutonPlus(Role role) {
        return this.boutonsPlus.get(role);
    }

    /**
     * Renvoie le bouton - d'un role donne.
     *
     * @param role le role concerne
     * @return le bouton - de ce role
     */
    public BoutonMedieval boutonMoins(Role role) {
        return this.boutonsMoins.get(role);
    }

    /**
     * Renvoie le bouton bascule d'un niveau de taxes.
     *
     * @param niveau le niveau de taxes concerne
     * @return le bouton bascule de ce niveau
     */
    public JToggleButton toggleTaxes(NiveauTaxes niveau) {
        return this.togglesTaxes.get(niveau);
    }

    /**
     * Renvoie le bouton pour recruter un villageois.
     *
     * @return le bouton de recrutement
     */
    public BoutonMedieval boutonRecruterVillageois() {
        return this.boutonRecruter;
    }

    /**
     * Met a jour l'affichage quand le royaume change.
     *
     * @param observable l'objet observe
     * @param arg la notification recue
     */
    @Override
    public void update(Observable observable, Object arg) {
        if (!(arg instanceof Notification)) {
            return;
        }
        rafraichir();
    }

    private void rafraichir() {
        int inactifs = this.royaume.population().effectif(Role.INACTIF);
        for (Role role : Role.values()) {
            int effectif = this.royaume.population().effectif(role);
            this.labelsEffectif.get(role).setText(String.valueOf(effectif));
            if (role != Role.INACTIF) {
                this.boutonsPlus.get(role).setEnabled(inactifs > 0);
                this.boutonsMoins.get(role).setEnabled(effectif > 0);
            }
        }
        NiveauTaxes courant = this.royaume.niveauTaxes();
        for (NiveauTaxes niveau : NiveauTaxes.values()) {
            this.togglesTaxes.get(niveau).setSelected(niveau == courant);
        }
        // peut-on recruter un villageois ?
        boolean peutRecruter = this.royaume.tresor().contient(
                Modele.economie.Ressource.NOURRITURE,
                config.Equilibrage.COUT_NOURRITURE_PAR_VILLAGEOIS)
                && this.royaume.population().total()
                        < this.royaume.population().capaciteLogement();
        this.boutonRecruter.setEnabled(peutRecruter);
    }
}
