package Vue.onglets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.EnumMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Modele.action.Action;
import Modele.action.ActionAmeliorer;
import Modele.economie.Ressource;
import Modele.infrastructure.Batiment;
import Modele.infrastructure.TypeBatiment;
import Modele.notification.Notification;
import Modele.royaume.Royaume;
import Vue.theme.BoutonMedieval;
import Vue.theme.Palette;
import Vue.theme.Polices;

import config.Equilibrage;

/** Onglet Infrastructures : une carte par batiment (niveau, statut, cout, bouton). */
public class OngletInfrastructures extends JPanel implements Observer {

    private static final long serialVersionUID = 1L;

    private final Royaume royaume;
    private final Map<TypeBatiment, CarteBatiment> cartes;

    /**
     * Cree l'onglet Infrastructures et s'abonne aux changements du royaume.
     *
     * @param royaume le royaume du joueur
     */
    public OngletInfrastructures(Royaume royaume) {
        this.royaume = royaume;
        this.cartes = new EnumMap<>(TypeBatiment.class);

        setOpaque(true);
        setBackground(Palette.FOND_PANNEAU);
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        // titre
        JLabel titre = new JLabel("Infrastructures".toUpperCase(),
                SwingConstants.LEFT);
        titre.setFont(Polices.SECTION.deriveFont(16f));
        titre.setForeground(Palette.OR);
        titre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Palette.OR_FONCE),
                BorderFactory.createEmptyBorder(0, 0, 6, 0)));
        add(titre, BorderLayout.NORTH);

        // grille 3 x 3
        JPanel grille = new JPanel(new GridLayout(3, 3, 8, 8));
        grille.setOpaque(false);
        for (TypeBatiment type : TypeBatiment.values()) {
            CarteBatiment carte = new CarteBatiment(type);
            this.cartes.put(type, carte);
            grille.add(carte);
        }
        add(grille, BorderLayout.CENTER);

        rafraichir();
        this.royaume.addObserver(this);
    }

    /**
     * Renvoie le bouton Ameliorer d'un type de batiment.
     *
     * @param type le type de batiment concerne
     * @return le bouton de ce batiment
     */
    public BoutonMedieval boutonAmeliorer(TypeBatiment type) {
        return this.cartes.get(type).bouton;
    }

    /**
     * Dit si une amelioration est deja planifiee pour ce batiment.
     *
     * @param type le type de batiment concerne
     * @return vrai si une amelioration est en file
     */
    public boolean estPlanifie(TypeBatiment type) {
        return ameliorationEnFile(type);
    }

    private boolean ameliorationEnFile(TypeBatiment type) {
        for (Action action : this.royaume.fileActions().contenu()) {
            if (action instanceof ActionAmeliorer && ((ActionAmeliorer) action).type() == type) {
                return true;
            }
        }
        return false;
    }

    private boolean coutPayable(TypeBatiment type, int niveauCible) {
        Map<Ressource, Integer> cout = Equilibrage.coutAmelioration(type, niveauCible);
        for (Map.Entry<Ressource, Integer> entree : cout.entrySet()) {
            if (!this.royaume.tresor().contient(entree.getKey(), entree.getValue())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Met a jour les cartes quand le royaume change.
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
        for (TypeBatiment type : TypeBatiment.values()) {
            Batiment batiment = this.royaume.batiment(type);
            this.cartes.get(type).rafraichir(batiment, ameliorationEnFile(type),
                    coutPayable(type, batiment.niveau() + 1));
        }
    }

    // la carte d'un batiment : nom, niveau, statut, cout, bouton
    private class CarteBatiment extends JPanel {
        private static final long serialVersionUID = 1L;

        private final TypeBatiment type;
        final BoutonMedieval bouton;

        private final JLabel valeurNiveau;
        private final JLabel valeurStatut;
        private final JPanel valeurCout;

        CarteBatiment(TypeBatiment type) {
            this.type = type;
            setOpaque(true);
            setBackground(Palette.FOND_PANNEAU_CLAIR);
            setLayout(new BorderLayout(0, 4));
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Palette.OR_FONCE, 1),
                    BorderFactory.createEmptyBorder(6, 6, 6, 6)));

            // nom du batiment
            JLabel nom = new JLabel(type.libelle().toUpperCase(),
                    SwingConstants.CENTER);
            nom.setFont(Polices.SECTION.deriveFont(13f));
            nom.setForeground(Palette.OR);
            nom.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Palette.OR_FONCE),
                    BorderFactory.createEmptyBorder(0, 0, 3, 0)));
            add(nom, BorderLayout.NORTH);

            // 3 colonnes : niveau / statut / cout
            JPanel corps = new JPanel(new GridLayout(1, 3, 4, 0));
            corps.setOpaque(false);

            // niveau
            this.valeurNiveau = new JLabel("", SwingConstants.CENTER);
            this.valeurNiveau.setFont(Polices.VALEUR.deriveFont(15f));
            this.valeurNiveau.setForeground(Palette.TEXTE_PRIMAIRE);
            this.valeurNiveau.setVerticalAlignment(SwingConstants.CENTER);
            corps.add(creerColonne("Niveau", this.valeurNiveau));

            // statut
            this.valeurStatut = new JLabel("", SwingConstants.CENTER);
            this.valeurStatut.setFont(Polices.LABEL.deriveFont(11f));
            this.valeurStatut.setVerticalAlignment(SwingConstants.CENTER);
            corps.add(creerColonne("Statut", this.valeurStatut));

            // cout (rempli au rafraichissement)
            this.valeurCout = new JPanel(new GridLayout(0, 1, 0, 1));
            this.valeurCout.setOpaque(false);
            corps.add(creerColonne("Cout", this.valeurCout));

            add(corps, BorderLayout.CENTER);

            // bouton en bas
            JPanel piedBouton = new JPanel(new BorderLayout());
            piedBouton.setOpaque(false);
            this.bouton = new BoutonMedieval("Ameliorer",
                    BoutonMedieval.Style.PRIMAIRE);
            piedBouton.add(this.bouton, BorderLayout.CENTER);
            piedBouton.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
            add(piedBouton, BorderLayout.SOUTH);
        }

        // une colonne titre (haut) + valeur (bas)
        private JPanel creerColonne(String libelle, JComponent valeur) {
            JPanel col = new JPanel(new GridLayout(2, 1, 0, 0));
            col.setOpaque(true);
            col.setBackground(new Color(8, 6, 4));
            col.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Palette.BORDURE_FONCEE, 1),
                    BorderFactory.createEmptyBorder(2, 2, 2, 2)));

            // titre
            JLabel titre = new JLabel(libelle.toUpperCase(),
                    SwingConstants.CENTER);
            titre.setFont(Polices.PETIT_LABEL);
            titre.setForeground(Palette.TEXTE_TERTIAIRE);
            titre.setVerticalAlignment(SwingConstants.BOTTOM);
            col.add(titre);

            // valeur
            col.add(valeur);
            return col;
        }

        void rafraichir(Batiment batiment, boolean planifie, boolean payable) {
            // niveau
            this.valeurNiveau.setText(batiment.niveau() + " / "
                    + Equilibrage.NIVEAU_MAX_BATIMENT);

            // statut
            if (planifie) {
                this.valeurStatut.setText("Planifie");
                this.valeurStatut.setForeground(Palette.OR_CLAIR);
            } else if (batiment.enChantier()) {
                this.valeurStatut.setText("Chantier"
                        + " (" + batiment.toursRestants() + ")");
                this.valeurStatut.setForeground(Palette.OR);
            } else if (batiment.estEndommage()) {
                this.valeurStatut.setText("Endommage");
                this.valeurStatut.setForeground(Palette.ROUGE_CLAIR);
            } else {
                this.valeurStatut.setText("Normal");
                this.valeurStatut.setForeground(Palette.VERT_POSITIF);
            }

            // cout
            this.valeurCout.removeAll();
            if (planifie) {
                ajouterLigneCout("--", Palette.TEXTE_TERTIAIRE);
            } else if (batiment.peutEtreAmeliore()) {
                Map<Ressource, Integer> cout = Equilibrage.coutAmelioration(
                        this.type, batiment.niveau() + 1);
                for (Map.Entry<Ressource, Integer> entree : cout.entrySet()) {
                    ajouterLigneCout(
                            entree.getValue() + " " + entree.getKey().libelle(),
                            couleurRessource(entree.getKey()));
                }
            } else {
                ajouterLigneCout("Niveau max atteint",
                        Palette.TEXTE_TERTIAIRE);
            }
            this.valeurCout.revalidate();
            this.valeurCout.repaint();

            // bouton
            if (planifie) {
                this.bouton.setText("Annuler");
                this.bouton.setEnabled(true);
            } else if (batiment.peutEtreAmeliore()) {
                this.bouton.setText("Ameliorer");
                this.bouton.setEnabled(payable);
            } else {
                this.bouton.setText("Ameliorer");
                this.bouton.setEnabled(false);
            }
        }

        private void ajouterLigneCout(String texte, Color couleur) {
            JLabel label = new JLabel(texte, SwingConstants.CENTER);
            label.setFont(Polices.PETITE_VALEUR.deriveFont(11f));
            label.setForeground(couleur);
            label.setAlignmentX(CENTER_ALIGNMENT);
            this.valeurCout.add(label);
        }

        private Color couleurRessource(Ressource ressource) {
            switch (ressource) {
                case OR: return Palette.OR_RESSOURCE;
                case NOURRITURE: return Palette.NOURRITURE_RESSOURCE;
                case BOIS: return Palette.BOIS_RESSOURCE;
                case PIERRE: return Palette.PIERRE_RESSOURCE;
                case SAVOIR: return Palette.SAVOIR_RESSOURCE;
                default: return Palette.TEXTE_PRIMAIRE;
            }
        }
    }
}
