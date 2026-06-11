package Vue.onglets;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.SwingConstants;

import Modele.economie.Ressource;
import Modele.militaire.PostureCombat;
import Modele.militaire.TypeUnite;
import Modele.notification.Notification;
import Modele.partie.Partie;
import Modele.population.Role;
import Modele.royaume.Royaume;
import Vue.i18n.Traducteur;
import Vue.theme.BoutonMedieval;
import Vue.theme.Palette;
import Vue.theme.Polices;
import Vue.theme.ToggleMedieval;

import config.Equilibrage;

/**
 * Onglet militaire : recrute/demobilise les unites, choisit la posture.
 * Au sprint 3 seule l'INFANTERIE_LEGERE est recrutable. Les autres types
 * sont affiches mais marques "Bientot disponible".
 */
public class OngletMilitaire extends JPanel implements Observer {

    private static final long serialVersionUID = 1L;

    private final Royaume royaume;
    private final Partie partie;

    private final JLabel labelEffectifTotal;
    private final Map<TypeUnite, CarteUnite> cartes;
    private final Map<PostureCombat, ToggleMedieval> togglesPosture;
    private BoutonMedieval boutonAttaquer;
    private JLabel labelAttaquesPlanifiees;

    public OngletMilitaire(Partie partie) {
        this.partie = partie;
        this.royaume = partie.joueur();
        this.cartes = new EnumMap<>(TypeUnite.class);
        this.togglesPosture = new EnumMap<>(PostureCombat.class);

        setOpaque(true);
        setBackground(Palette.FOND_PANNEAU);
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        // === TITRE + effectif total ===
        JPanel tete = new JPanel(new BorderLayout());
        tete.setOpaque(false);

        JLabel titre = new JLabel(Traducteur.t("onglet.militaire").toUpperCase(),
                SwingConstants.LEFT);
        titre.setFont(Polices.SECTION.deriveFont(16f));
        titre.setForeground(Palette.OR);
        tete.add(titre, BorderLayout.WEST);

        this.labelEffectifTotal = new JLabel("", SwingConstants.RIGHT);
        this.labelEffectifTotal.setFont(Polices.VALEUR.deriveFont(14f));
        this.labelEffectifTotal.setForeground(Palette.ROUGE_BANNIERE);
        tete.add(this.labelEffectifTotal, BorderLayout.EAST);

        tete.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Palette.OR_FONCE),
                BorderFactory.createEmptyBorder(0, 0, 6, 0)));
        add(tete, BorderLayout.NORTH);

        // === Centre : grille des types + bloc posture ===
        JPanel centre = new JPanel();
        centre.setOpaque(false);
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));

        centre.add(creerGrilleUnites());
        centre.add(Box.createVerticalStrut(12));
        centre.add(creerBlocCombat());
        centre.add(Box.createVerticalGlue());

        add(centre, BorderLayout.CENTER);

        rafraichir();
        this.royaume.addObserver(this);
        // L'onglet rafraichit aussi quand un bot change (effectif, or, mort).
        for (Royaume bot : this.partie.bots()) {
            bot.addObserver(this);
        }
    }

    private JPanel creerGrilleUnites() {
        // Une carte par type d'unite, en 2 colonnes.
        JPanel grille = new JPanel(new GridLayout(2, 2, 10, 10));
        grille.setOpaque(false);
        for (TypeUnite t : TypeUnite.values()) {
            CarteUnite carte = new CarteUnite(t);
            this.cartes.put(t, carte);
            grille.add(carte);
        }
        return grille;
    }

    /**
     * Bloc Combat compact : posture (3 toggles) + bouton Attaquer
     * sur la meme ligne. Le compteur d'attaques planifiees est en
     * dessous, en petit, italique.
     */
    private JPanel creerBlocCombat() {
        JPanel bloc = new JPanel(new BorderLayout(0, 6));
        bloc.setOpaque(true);
        bloc.setBackground(Palette.FOND_PANNEAU_CLAIR);
        bloc.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Palette.OR_FONCE, 1),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)));

        // Titre du bloc
        JLabel sousTitre = new JLabel(Traducteur.t("militaire.combat").toUpperCase());
        sousTitre.setFont(Polices.SECTION.deriveFont(13f));
        sousTitre.setForeground(Palette.OR);
        bloc.add(sousTitre, BorderLayout.NORTH);

        // Ligne unique : Posture (3 toggles) + Attaquer
        JPanel ligne = new JPanel(new BorderLayout(16, 0));
        ligne.setOpaque(false);

        // Gauche : posture
        JPanel groupePosture = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        groupePosture.setOpaque(false);
        ButtonGroup grp = new ButtonGroup();
        for (PostureCombat p : PostureCombat.values()) {
            ToggleMedieval t = new ToggleMedieval(Traducteur.t(p.cleI18n()));
            t.setPreferredSize(new java.awt.Dimension(120, 32));
            grp.add(t);
            this.togglesPosture.put(p, t);
            groupePosture.add(t);
        }
        ligne.add(groupePosture, BorderLayout.WEST);

        // Droite : bouton Attaquer (seulement si bots presents)
        if (!this.partie.bots().isEmpty()) {
            this.boutonAttaquer = new BoutonMedieval(
                    Traducteur.t("militaire.attaquer").toUpperCase(),
                    BoutonMedieval.Style.DANGER);
            this.boutonAttaquer.setPreferredSize(new java.awt.Dimension(180, 38));
            ligne.add(this.boutonAttaquer, BorderLayout.EAST);
        }
        bloc.add(ligne, BorderLayout.CENTER);

        // Pied : compteur d'attaques planifiees (si bots)
        if (!this.partie.bots().isEmpty()) {
            this.labelAttaquesPlanifiees = new JLabel("", SwingConstants.CENTER);
            this.labelAttaquesPlanifiees.setFont(Polices.LABEL.deriveFont(
                    java.awt.Font.ITALIC, 11f));
            this.labelAttaquesPlanifiees.setForeground(Palette.TEXTE_SECONDAIRE);
            bloc.add(this.labelAttaquesPlanifiees, BorderLayout.SOUTH);
        }
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
        // En-tete : effectif total + recrues disponibles
        int recrues = this.royaume.population().effectif(Role.SOLDAT);
        this.labelEffectifTotal.setText(
                Traducteur.t("militaire.effectif_total") + " : "
                        + this.royaume.armee().effectifTotal()
                        + "    |    "
                        + Traducteur.t("militaire.recrues") + " : " + recrues);

        // Chaque carte
        for (TypeUnite t : TypeUnite.values()) {
            this.cartes.get(t).rafraichir();
        }

        // Posture courante
        PostureCombat courant = this.royaume.armee().posture();
        for (PostureCombat p : PostureCombat.values()) {
            this.togglesPosture.get(p).setSelected(p == courant);
        }

        // Bouton Attaquer : actif si on a une armee et au moins un bot
        // vivant non deja attaque.
        if (this.boutonAttaquer != null) {
            boolean armeeOk = !this.royaume.armee().estVide();
            boolean cibleDispo = false;
            int planifiees = 0;
            for (Royaume bot : this.partie.bots()) {
                if (this.royaume.aAttaquePlanifieeContre(bot)) {
                    planifiees++;
                } else if (bot.population().total() > 0) {
                    cibleDispo = true;
                }
            }
            this.boutonAttaquer.setEnabled(armeeOk && cibleDispo);
            if (this.labelAttaquesPlanifiees != null) {
                if (planifiees == 0) {
                    this.labelAttaquesPlanifiees.setText(
                            Traducteur.t("militaire.aucune_attaque"));
                } else {
                    this.labelAttaquesPlanifiees.setText(
                            planifiees + " "
                                    + Traducteur.t("militaire.attaques_planifiees"));
                }
            }
        }
    }

    /** Accesseur du gros bouton Attaquer (pour le controleur). */
    public BoutonMedieval boutonAttaquer() {
        return this.boutonAttaquer;
    }

    // ============================================================
    // Accesseurs pour le controleur
    // ============================================================
    public BoutonMedieval boutonRecruter(TypeUnite type) {
        return this.cartes.get(type).boutonRecruter;
    }

    public BoutonMedieval boutonDemobiliser(TypeUnite type) {
        return this.cartes.get(type).boutonDemobiliser;
    }

    public ToggleMedieval togglePosture(PostureCombat posture) {
        return this.togglesPosture.get(posture);
    }

    // ============================================================
    // Une carte = un type d'unite
    // ============================================================
    private class CarteUnite extends JPanel {
        private static final long serialVersionUID = 1L;

        private final TypeUnite type;
        final BoutonMedieval boutonRecruter;
        final BoutonMedieval boutonDemobiliser;
        private final JLabel valeurEffectif;
        private final JLabel valeurStats;
        private final JLabel valeurCout;

        CarteUnite(TypeUnite type) {
            this.type = type;
            setOpaque(true);
            setBackground(Palette.FOND_PANNEAU_CLAIR);
            setLayout(new BorderLayout(0, 4));
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Palette.OR_FONCE, 1),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)));

            // === Nom du type ===
            JLabel nom = new JLabel(Traducteur.t(type.cleI18n()).toUpperCase(),
                    SwingConstants.CENTER);
            nom.setFont(Polices.SECTION.deriveFont(13f));
            nom.setForeground(Palette.OR);
            nom.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Palette.OR_FONCE),
                    BorderFactory.createEmptyBorder(0, 0, 4, 0)));
            add(nom, BorderLayout.NORTH);

            // === 3 colonnes : Effectif / Stats / Cout ===
            JPanel corps = new JPanel(new GridLayout(1, 3, 4, 0));
            corps.setOpaque(false);

            this.valeurEffectif = new JLabel("", SwingConstants.CENTER);
            this.valeurEffectif.setFont(Polices.VALEUR.deriveFont(15f));
            this.valeurEffectif.setForeground(Palette.TEXTE_PRIMAIRE);
            corps.add(creerColonne("militaire.effectif", this.valeurEffectif));

            this.valeurStats = new JLabel("", SwingConstants.CENTER);
            this.valeurStats.setFont(Polices.LABEL.deriveFont(11f));
            this.valeurStats.setForeground(Palette.TEXTE_PRIMAIRE);
            corps.add(creerColonne("militaire.stats", this.valeurStats));

            this.valeurCout = new JLabel("", SwingConstants.CENTER);
            this.valeurCout.setFont(Polices.LABEL.deriveFont(11f));
            this.valeurCout.setForeground(Palette.OR_RESSOURCE);
            corps.add(creerColonne("militaire.cout_unitaire", this.valeurCout));

            add(corps, BorderLayout.CENTER);

            // === Footer : 2 boutons cote a cote ===
            JPanel pied = new JPanel(new GridLayout(1, 2, 6, 0));
            pied.setOpaque(false);
            pied.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

            this.boutonDemobiliser = new BoutonMedieval(
                    Traducteur.t("militaire.demobiliser"),
                    BoutonMedieval.Style.DANGER);
            this.boutonRecruter = new BoutonMedieval(
                    Traducteur.t("militaire.recruter"),
                    BoutonMedieval.Style.PRIMAIRE);
            pied.add(this.boutonDemobiliser);
            pied.add(this.boutonRecruter);
            add(pied, BorderLayout.SOUTH);
        }

        private JPanel creerColonne(String cleI18n, JLabel valeur) {
            JPanel col = new JPanel(new GridLayout(2, 1));
            col.setOpaque(true);
            col.setBackground(new Color(8, 6, 4));
            col.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Palette.BORDURE_FONCEE, 1),
                    BorderFactory.createEmptyBorder(2, 4, 2, 4)));

            JLabel titre = new JLabel(Traducteur.t(cleI18n).toUpperCase(),
                    SwingConstants.CENTER);
            titre.setFont(Polices.PETIT_LABEL);
            titre.setForeground(Palette.TEXTE_TERTIAIRE);
            titre.setVerticalAlignment(SwingConstants.BOTTOM);
            col.add(titre);
            col.add(valeur);
            return col;
        }

        void rafraichir() {
            // Effectif
            this.valeurEffectif.setText(String.valueOf(
                    royaume.armee().effectifParType(this.type)));

            // Stats Att / Def
            this.valeurStats.setText("A " + this.type.attaqueBase()
                    + " / D " + this.type.defenseBase());

            // Deblocage : la Caserne doit avoir le niveau requis pour ce type.
            Modele.infrastructure.Batiment caserne =
                    royaume.batiment(Modele.infrastructure.TypeBatiment.CASERNE);
            int niveauCaserne = (caserne != null) ? caserne.niveau() : 0;
            boolean debloque = niveauCaserne >= this.type.niveauCaserneRequis();

            // Cout / condition
            if (debloque) {
                this.valeurCout.setText(
                        Equilibrage.COUT_OR_PAR_SOLDAT + " Or + 1 recrue");
                this.valeurCout.setForeground(Palette.OR_RESSOURCE);
            } else {
                this.valeurCout.setText(
                        Traducteur.t("militaire.caserne_requise")
                                + " " + this.type.niveauCaserneRequis());
                this.valeurCout.setForeground(Palette.TEXTE_TERTIAIRE);
            }

            // Boutons : recruter requiert debloque + recrue (Role.SOLDAT) + or.
            int effectifType = royaume.armee().effectifParType(this.type);
            int recrues = royaume.population().effectif(Role.SOLDAT);
            boolean peutPayer = royaume.tresor().contient(Ressource.OR,
                    Equilibrage.COUT_OR_PAR_SOLDAT);

            this.boutonRecruter.setEnabled(debloque && peutPayer
                    && recrues >= Equilibrage.HABITANTS_PAR_SOLDAT);
            this.boutonDemobiliser.setEnabled(effectifType > 0);
        }
    }

}
