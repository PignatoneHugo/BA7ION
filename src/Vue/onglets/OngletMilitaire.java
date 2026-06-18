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
import Vue.theme.BoutonMedieval;
import Vue.theme.Palette;
import Vue.theme.Polices;
import Vue.theme.ToggleMedieval;

import config.Equilibrage;

/** Onglet Militaire : recrute/demobilise les unites, choisit la posture, attaque. */
public class OngletMilitaire extends JPanel implements Observer {

    private static final long serialVersionUID = 1L;

    private final Royaume royaume;
    private final Partie partie;

    private final JLabel labelEffectifTotal;
    private final Map<TypeUnite, CarteUnite> cartes;
    private final Map<PostureCombat, ToggleMedieval> togglesPosture;
    private BoutonMedieval boutonAttaquer;
    private JLabel labelAttaquesPlanifiees;

    /**
     * Cree l'onglet Militaire et s'abonne au joueur et aux bots.
     *
     * @param partie la partie en cours
     */
    public OngletMilitaire(Partie partie) {
        this.partie = partie;
        this.royaume = partie.joueur();
        this.cartes = new EnumMap<>(TypeUnite.class);
        this.togglesPosture = new EnumMap<>(PostureCombat.class);

        setOpaque(true);
        setBackground(Palette.FOND_PANNEAU);
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        // titre + effectif total
        JPanel tete = new JPanel(new BorderLayout());
        tete.setOpaque(false);

        JLabel titre = new JLabel("Militaire".toUpperCase(),
                SwingConstants.LEFT);
        titre.setFont(Polices.SECTION.deriveFont(16f));
        titre.setForeground(Palette.OR);
        tete.add(titre, BorderLayout.WEST);

        this.labelEffectifTotal = new JLabel("", SwingConstants.RIGHT);
        this.labelEffectifTotal.setFont(Polices.VALEUR.deriveFont(14f));
        this.labelEffectifTotal.setForeground(Palette.ROUGE_DANGER);
        tete.add(this.labelEffectifTotal, BorderLayout.EAST);

        tete.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Palette.OR_FONCE),
                BorderFactory.createEmptyBorder(0, 0, 6, 0)));
        add(tete, BorderLayout.NORTH);

        // grille des unites + bloc combat
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
        // on observe aussi les bots (effectif, or...)
        for (Royaume bot : this.partie.bots()) {
            bot.addObserver(this);
        }
    }

    private JPanel creerGrilleUnites() {
        // une carte par type d'unite
        JPanel grille = new JPanel(new GridLayout(2, 2, 10, 10));
        grille.setOpaque(false);
        for (TypeUnite type : TypeUnite.values()) {
            CarteUnite carte = new CarteUnite(type);
            this.cartes.put(type, carte);
            grille.add(carte);
        }
        return grille;
    }

    // bloc combat : posture + bouton Attaquer + compteur d'attaques
    private JPanel creerBlocCombat() {
        JPanel bloc = new JPanel(new BorderLayout(0, 6));
        bloc.setOpaque(true);
        bloc.setBackground(Palette.FOND_PANNEAU_CLAIR);
        bloc.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Palette.OR_FONCE, 1),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)));

        // titre
        JLabel sousTitre = new JLabel("Combat".toUpperCase());
        sousTitre.setFont(Polices.SECTION.deriveFont(13f));
        sousTitre.setForeground(Palette.OR);
        bloc.add(sousTitre, BorderLayout.NORTH);

        // posture + bouton Attaquer sur la meme ligne
        JPanel ligne = new JPanel(new BorderLayout(16, 0));
        ligne.setOpaque(false);

        // la posture
        JPanel groupePosture = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        groupePosture.setOpaque(false);
        ButtonGroup grp = new ButtonGroup();
        for (PostureCombat posture : PostureCombat.values()) {
            ToggleMedieval toggle = new ToggleMedieval(posture.libelle());
            toggle.setPreferredSize(new java.awt.Dimension(120, 32));
            grp.add(toggle);
            this.togglesPosture.put(posture, toggle);
            groupePosture.add(toggle);
        }
        ligne.add(groupePosture, BorderLayout.WEST);

        // bouton Attaquer (seulement s'il y a des bots)
        if (!this.partie.bots().isEmpty()) {
            this.boutonAttaquer = new BoutonMedieval(
                    "Attaquer".toUpperCase(),
                    BoutonMedieval.Style.DANGER);
            this.boutonAttaquer.setPreferredSize(new java.awt.Dimension(180, 38));
            ligne.add(this.boutonAttaquer, BorderLayout.EAST);
        }
        bloc.add(ligne, BorderLayout.CENTER);

        // compteur d'attaques planifiees
        if (!this.partie.bots().isEmpty()) {
            this.labelAttaquesPlanifiees = new JLabel("", SwingConstants.CENTER);
            this.labelAttaquesPlanifiees.setFont(Polices.LABEL.deriveFont(
                    java.awt.Font.ITALIC, 11f));
            this.labelAttaquesPlanifiees.setForeground(Palette.TEXTE_SECONDAIRE);
            bloc.add(this.labelAttaquesPlanifiees, BorderLayout.SOUTH);
        }
        return bloc;
    }

    /**
     * Met a jour l'affichage quand un royaume change.
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
        // effectif total + recrues dispo
        int recrues = this.royaume.population().effectif(Role.SOLDAT);
        this.labelEffectifTotal.setText(
                "Effectif total" + " : "
                        + this.royaume.armee().effectifTotal()
                        + "    |    "
                        + "Recrues dispo." + " : " + recrues);

        // les cartes
        for (TypeUnite type : TypeUnite.values()) {
            this.cartes.get(type).rafraichir();
        }

        // posture courante
        PostureCombat courant = this.royaume.armee().posture();
        for (PostureCombat posture : PostureCombat.values()) {
            this.togglesPosture.get(posture).setSelected(posture == courant);
        }

        // bouton Attaquer : actif si on a une armee et une cible dispo
        if (this.boutonAttaquer != null) {
            boolean combatsOuverts = this.partie.combatsAutorises();
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
            this.boutonAttaquer.setEnabled(combatsOuverts && armeeOk && cibleDispo);
            if (this.labelAttaquesPlanifiees != null) {
                if (!combatsOuverts) {
                    this.labelAttaquesPlanifiees.setText(
                            "Combats verrouilles jusqu'au tour "
                                    + Equilibrage.TOUR_DEBUT_COMBATS);
                } else if (planifiees == 0) {
                    this.labelAttaquesPlanifiees.setText(
                            "Aucune attaque planifiee ce tour");
                } else {
                    this.labelAttaquesPlanifiees.setText(
                            planifiees + " "
                                    + "attaque(s) planifiee(s) ce tour");
                }
            }
        }
    }

    /**
     * Renvoie le bouton Attaquer.
     *
     * @return le bouton Attaquer, ou null s'il n'y a pas de bots
     */
    public BoutonMedieval boutonAttaquer() {
        return this.boutonAttaquer;
    }

    /**
     * Renvoie le bouton de recrutement d'un type d'unite.
     *
     * @param type le type d'unite concerne
     * @return le bouton de recrutement de ce type
     */
    public BoutonMedieval boutonRecruter(TypeUnite type) {
        return this.cartes.get(type).boutonRecruter;
    }

    /**
     * Renvoie le bouton de demobilisation d'un type d'unite.
     *
     * @param type le type d'unite concerne
     * @return le bouton de demobilisation de ce type
     */
    public BoutonMedieval boutonDemobiliser(TypeUnite type) {
        return this.cartes.get(type).boutonDemobiliser;
    }

    /**
     * Renvoie le bouton bascule d'une posture de combat.
     *
     * @param posture la posture concernee
     * @return le bouton bascule de cette posture
     */
    public ToggleMedieval togglePosture(PostureCombat posture) {
        return this.togglesPosture.get(posture);
    }

    // une carte = un type d'unite
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

            // nom du type
            JLabel nom = new JLabel(type.libelle().toUpperCase(),
                    SwingConstants.CENTER);
            nom.setFont(Polices.SECTION.deriveFont(13f));
            nom.setForeground(Palette.OR);
            nom.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Palette.OR_FONCE),
                    BorderFactory.createEmptyBorder(0, 0, 4, 0)));
            add(nom, BorderLayout.NORTH);

            // 3 colonnes : effectif / stats / cout
            JPanel corps = new JPanel(new GridLayout(1, 3, 4, 0));
            corps.setOpaque(false);

            this.valeurEffectif = new JLabel("", SwingConstants.CENTER);
            this.valeurEffectif.setFont(Polices.VALEUR.deriveFont(15f));
            this.valeurEffectif.setForeground(Palette.TEXTE_PRIMAIRE);
            corps.add(creerColonne("Effectif", this.valeurEffectif));

            this.valeurStats = new JLabel("", SwingConstants.CENTER);
            this.valeurStats.setFont(Polices.LABEL.deriveFont(11f));
            this.valeurStats.setForeground(Palette.TEXTE_PRIMAIRE);
            corps.add(creerColonne("Att / Def", this.valeurStats));

            this.valeurCout = new JLabel("", SwingConstants.CENTER);
            this.valeurCout.setFont(Polices.LABEL.deriveFont(11f));
            this.valeurCout.setForeground(Palette.OR_RESSOURCE);
            corps.add(creerColonne("Cout / unite", this.valeurCout));

            add(corps, BorderLayout.CENTER);

            // 2 boutons en bas
            JPanel pied = new JPanel(new GridLayout(1, 2, 6, 0));
            pied.setOpaque(false);
            pied.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

            this.boutonDemobiliser = new BoutonMedieval(
                    "-",
                    BoutonMedieval.Style.DANGER);
            this.boutonRecruter = new BoutonMedieval(
                    "+",
                    BoutonMedieval.Style.PRIMAIRE);
            pied.add(this.boutonDemobiliser);
            pied.add(this.boutonRecruter);
            add(pied, BorderLayout.SOUTH);
        }

        private JPanel creerColonne(String libelle, JLabel valeur) {
            JPanel col = new JPanel(new GridLayout(2, 1));
            col.setOpaque(true);
            col.setBackground(new Color(8, 6, 4));
            col.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Palette.BORDURE_FONCEE, 1),
                    BorderFactory.createEmptyBorder(2, 4, 2, 4)));

            JLabel titre = new JLabel(libelle.toUpperCase(),
                    SwingConstants.CENTER);
            titre.setFont(Polices.PETIT_LABEL);
            titre.setForeground(Palette.TEXTE_TERTIAIRE);
            titre.setVerticalAlignment(SwingConstants.BOTTOM);
            col.add(titre);
            col.add(valeur);
            return col;
        }

        void rafraichir() {
            // effectif
            this.valeurEffectif.setText(String.valueOf(
                    royaume.armee().effectifParType(this.type)));

            // stats att / def
            this.valeurStats.setText("A " + this.type.attaqueBase()
                    + " / D " + this.type.defenseBase());

            // debloque si la caserne est au bon niveau
            Modele.infrastructure.Batiment caserne =
                    royaume.batiment(Modele.infrastructure.TypeBatiment.CASERNE);
            int niveauCaserne = (caserne != null) ? caserne.niveau() : 0;
            boolean debloque = niveauCaserne >= this.type.niveauCaserneRequis();

            // cout ou condition de deblocage
            if (debloque) {
                this.valeurCout.setText(
                        Equilibrage.COUT_OR_PAR_SOLDAT + " Or + 1 recrue");
                this.valeurCout.setForeground(Palette.OR_RESSOURCE);
            } else {
                this.valeurCout.setText(
                        "Caserne niv."
                                + " " + this.type.niveauCaserneRequis());
                this.valeurCout.setForeground(Palette.TEXTE_TERTIAIRE);
            }

            // recruter demande : debloque + une recrue + assez d'or
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
