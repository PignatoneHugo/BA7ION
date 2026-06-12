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

/**
 * Onglet Economie style medieval : repartition des habitants par role,
 * boutons +/- pour chaque role (sur INACTIF, le + recrute un villageois
 * pour 100 nourriture). Selecteur de taxes en bas.
 */
public class OngletEconomie extends JPanel implements Observer {

    private static final long serialVersionUID = 1L;

    private final Royaume royaume;
    private final Map<Role, JLabel> labelsEffectif;
    private final Map<Role, BoutonMedieval> boutonsPlus;
    private final Map<Role, BoutonMedieval> boutonsMoins;
    private final Map<NiveauTaxes, JToggleButton> togglesTaxes;
    private final BoutonMedieval boutonRecruter;

    public OngletEconomie(Royaume royaume) {
        this.royaume = royaume;
        this.labelsEffectif = new EnumMap<>(Role.class);
        this.boutonsPlus = new EnumMap<>(Role.class);
        this.boutonsMoins = new EnumMap<>(Role.class);
        this.togglesTaxes = new EnumMap<>(NiveauTaxes.class);

        // Bouton + sur la ligne INACTIF = recruter un villageois.
        // On le cree en amont pour avoir une reference utilisable par
        // ControleurEconomie via boutonRecruterVillageois().
        this.boutonRecruter = new BoutonMedieval("+", BoutonMedieval.Style.SECONDAIRE);
        this.boutonRecruter.setPreferredSize(new Dimension(44, 30));

        setOpaque(true);
        setBackground(Palette.FOND_PANNEAU);
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        // Titre section (meme style que Infrastructures et Militaire)
        add(creerTitreSection("Economie"), BorderLayout.NORTH);

        // Contenu central : bloc roles + bloc taxes
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
        JLabel l = new JLabel(texte.toUpperCase(), SwingConstants.LEFT);
        l.setFont(Polices.SECTION.deriveFont(16f));
        l.setForeground(Palette.OR);
        l.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Palette.OR_FONCE),
                BorderFactory.createEmptyBorder(0, 0, 6, 0)));
        return l;
    }

    private JLabel creerSousTitre(String texte) {
        JLabel l = new JLabel(texte.toUpperCase());
        l.setFont(Polices.SECTION.deriveFont(13f));
        l.setForeground(Palette.OR);
        return l;
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

        for (Role r : Role.values()) {
            bloc.add(creerLigneRole(r));
            bloc.add(Box.createVerticalStrut(6));
        }

        return bloc;
    }

    /** Une ligne pour un role : pastille + nom + cout (si INACTIF) + effectif + boutons. */
    private JPanel creerLigneRole(Role r) {
        JPanel ligne = new JPanel(new BorderLayout(12, 0));
        ligne.setOpaque(false);
        ligne.setAlignmentX(LEFT_ALIGNMENT);
        ligne.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        ligne.setPreferredSize(new Dimension(1, 36));

        // Gauche : pastille + nom + (libelle de cout si INACTIF)
        JPanel gauche = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        gauche.setOpaque(false);
        gauche.add(creerPastilleRole(r));
        JLabel nom = new JLabel(r.libelle());
        nom.setFont(Polices.LABEL.deriveFont(15f));
        nom.setForeground(couleurDe(r));
        gauche.add(nom);
        if (r == Role.INACTIF) {
            JLabel cout = new JLabel("(-100 nourriture pour recruter)");
            cout.setFont(Polices.LABEL.deriveFont(java.awt.Font.ITALIC, 12f));
            cout.setForeground(Palette.NOURRITURE_RESSOURCE);
            gauche.add(cout);
        }
        ligne.add(gauche, BorderLayout.WEST);

        // Centre vide (pour pousser les boutons a droite)
        ligne.add(new JLabel(""), BorderLayout.CENTER);

        // Droite : effectif + boutons +/-
        JPanel droite = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        droite.setOpaque(false);

        JLabel valeur = new JLabel("0", SwingConstants.RIGHT);
        valeur.setFont(Polices.VALEUR.deriveFont(18f));
        valeur.setForeground(couleurDe(r));
        valeur.setPreferredSize(new Dimension(54, 28));
        this.labelsEffectif.put(r, valeur);
        droite.add(valeur);

        if (r == Role.INACTIF) {
            // Sur INACTIF : un bouton - desactive (on ne peut pas "retirer" un
            // habitant sans raison) et le bouton + = recruter villageois.
            BoutonMedieval bMoins = new BoutonMedieval("−", BoutonMedieval.Style.SECONDAIRE);
            bMoins.setPreferredSize(new Dimension(44, 30));
            bMoins.setEnabled(false);
            this.boutonsMoins.put(r, bMoins);
            droite.add(bMoins);
            droite.add(this.boutonRecruter);
            this.boutonsPlus.put(r, this.boutonRecruter);
        } else {
            BoutonMedieval bMoins = new BoutonMedieval("−", BoutonMedieval.Style.SECONDAIRE);
            bMoins.setPreferredSize(new Dimension(44, 30));
            BoutonMedieval bPlus = new BoutonMedieval("+", BoutonMedieval.Style.SECONDAIRE);
            bPlus.setPreferredSize(new Dimension(44, 30));
            this.boutonsMoins.put(r, bMoins);
            this.boutonsPlus.put(r, bPlus);
            droite.add(bMoins);
            droite.add(bPlus);
        }
        ligne.add(droite, BorderLayout.EAST);
        return ligne;
    }

    /** Bloc taxes : 3 toggle. */
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
        for (NiveauTaxes n : NiveauTaxes.values()) {
            ToggleMedieval t = new ToggleMedieval(n.libelle());
            t.setPreferredSize(new Dimension(140, 36));
            grp.add(t);
            this.togglesTaxes.put(n, t);
            boutons.add(t);
        }
        bloc.add(boutons);
        return bloc;
    }

    private JPanel creerPastilleRole(Role r) {
        JPanel pastille = new JPanel() {
            private static final long serialVersionUID = 1L;
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                int d = Math.min(getWidth(), getHeight()) - 2;
                int cx = (getWidth() - d) / 2;
                int cy = (getHeight() - d) / 2;
                g2.setColor(couleurDe(r).darker().darker());
                g2.fillOval(cx, cy, d, d);
                g2.setColor(couleurDe(r));
                g2.fillOval(cx + 3, cy + 3, d - 6, d - 6);
                g2.dispose();
            }
        };
        pastille.setOpaque(false);
        pastille.setPreferredSize(new Dimension(20, 20));
        return pastille;
    }

    private Color couleurDe(Role r) {
        switch (r) {
            case INACTIF: return Palette.OR;
            case FERMIER: return Palette.NOURRITURE_RESSOURCE;
            case MINEUR: return Palette.PIERRE_RESSOURCE;
            case BUCHERON: return Palette.BOIS_RESSOURCE;
            case ERUDIT: return Palette.SAVOIR_RESSOURCE;
            case SOLDAT: return Palette.ROUGE_BANNIERE;
            default: return Palette.TEXTE_PRIMAIRE;
        }
    }

    public BoutonMedieval boutonPlus(Role role) {
        return this.boutonsPlus.get(role);
    }

    public BoutonMedieval boutonMoins(Role role) {
        return this.boutonsMoins.get(role);
    }

    public JToggleButton toggleTaxes(NiveauTaxes niveau) {
        return this.togglesTaxes.get(niveau);
    }

    public BoutonMedieval boutonRecruterVillageois() {
        return this.boutonRecruter;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(arg instanceof Notification)) {
            return;
        }
        rafraichir();
    }

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
        // Bouton + sur INACTIF (= recruter villageois)
        boolean peutRecruter = this.royaume.tresor().contient(
                Modele.economie.Ressource.NOURRITURE,
                config.Equilibrage.COUT_NOURRITURE_PAR_VILLAGEOIS)
                && this.royaume.population().total()
                        < this.royaume.population().capaciteLogement();
        this.boutonRecruter.setEnabled(peutRecruter);
    }
}
