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

/**
 * Onglet Infrastructures style medieval : grille de cartes pour les 9
 * batiments. Chaque carte affiche clairement :
 *   - le nom du batiment en or,
 *   - le niveau actuel et le niveau max en gros,
 *   - le statut (Normal / Endommage / Chantier / Planifie) colore,
 *   - le cout d'amelioration detaille par ressource,
 *   - un bouton Ameliorer / Annuler.
 *
 * Pas d'icones dessinees : la lisibilite des chiffres et statuts prime.
 */
public class OngletInfrastructures extends JPanel implements Observer {

    private static final long serialVersionUID = 1L;

    private final Royaume royaume;
    private final Map<TypeBatiment, CarteBatiment> cartes;

    public OngletInfrastructures(Royaume royaume) {
        this.royaume = royaume;
        this.cartes = new EnumMap<>(TypeBatiment.class);

        setOpaque(true);
        setBackground(Palette.FOND_PANNEAU);
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        // Titre compact
        JLabel titre = new JLabel("Infrastructures".toUpperCase(),
                SwingConstants.LEFT);
        titre.setFont(Polices.SECTION.deriveFont(16f));
        titre.setForeground(Palette.OR);
        titre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Palette.OR_FONCE),
                BorderFactory.createEmptyBorder(0, 0, 6, 0)));
        add(titre, BorderLayout.NORTH);

        // Grille 3 x 3 (gaps reduits pour donner plus de hauteur aux cartes)
        JPanel grille = new JPanel(new GridLayout(3, 3, 8, 8));
        grille.setOpaque(false);
        for (TypeBatiment t : TypeBatiment.values()) {
            CarteBatiment carte = new CarteBatiment(t);
            this.cartes.put(t, carte);
            grille.add(carte);
        }
        add(grille, BorderLayout.CENTER);

        rafraichir();
        this.royaume.addObserver(this);
    }

    public BoutonMedieval boutonAmeliorer(TypeBatiment type) {
        return this.cartes.get(type).bouton;
    }

    public boolean estPlanifie(TypeBatiment type) {
        return ameliorationEnFile(type);
    }

    private boolean ameliorationEnFile(TypeBatiment type) {
        for (Action a : this.royaume.fileActions().contenu()) {
            if (a instanceof ActionAmeliorer && ((ActionAmeliorer) a).type() == type) {
                return true;
            }
        }
        return false;
    }

    private boolean coutPayable(TypeBatiment type, int niveauCible) {
        Map<Ressource, Integer> cout = Equilibrage.coutAmelioration(type, niveauCible);
        for (Map.Entry<Ressource, Integer> e : cout.entrySet()) {
            if (!this.royaume.tresor().contient(e.getKey(), e.getValue())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(arg instanceof Notification)) {
            return;
        }
        rafraichir();
    }

    private void rafraichir() {
        for (TypeBatiment t : TypeBatiment.values()) {
            Batiment b = this.royaume.batiment(t);
            this.cartes.get(t).rafraichir(b, ameliorationEnFile(t),
                    coutPayable(t, b.niveau() + 1));
        }
    }

    // ============================================================
    // Carte d'un batiment :
    //   - Header : nom du batiment (en or)
    //   - Corps : 3 colonnes cote a cote -- NIVEAU / ETAT / COUT
    //   - Footer : bouton Ameliorer / Annuler
    // ============================================================
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

            // === HEADER : nom du batiment ===
            JLabel nom = new JLabel(type.libelle().toUpperCase(),
                    SwingConstants.CENTER);
            nom.setFont(Polices.SECTION.deriveFont(13f));
            nom.setForeground(Palette.OR);
            nom.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Palette.OR_FONCE),
                    BorderFactory.createEmptyBorder(0, 0, 3, 0)));
            add(nom, BorderLayout.NORTH);

            // === CORPS : 3 colonnes (NIVEAU / ETAT / COUT) ===
            JPanel corps = new JPanel(new GridLayout(1, 3, 4, 0));
            corps.setOpaque(false);

            // Colonne 1 : NIVEAU
            this.valeurNiveau = new JLabel("", SwingConstants.CENTER);
            this.valeurNiveau.setFont(Polices.VALEUR.deriveFont(15f));
            this.valeurNiveau.setForeground(Palette.TEXTE_PRIMAIRE);
            this.valeurNiveau.setVerticalAlignment(SwingConstants.CENTER);
            corps.add(creerColonne("Niveau", this.valeurNiveau));

            // Colonne 2 : STATUT
            this.valeurStatut = new JLabel("", SwingConstants.CENTER);
            this.valeurStatut.setFont(Polices.LABEL.deriveFont(11f));
            this.valeurStatut.setVerticalAlignment(SwingConstants.CENTER);
            corps.add(creerColonne("Statut", this.valeurStatut));

            // Colonne 3 : COUT (panel rempli dynamiquement)
            this.valeurCout = new JPanel(new GridLayout(0, 1, 0, 1));
            this.valeurCout.setOpaque(false);
            corps.add(creerColonne("Cout", this.valeurCout));

            add(corps, BorderLayout.CENTER);

            // === FOOTER : bouton (s'etire pour remplir la largeur de la
            // carte grace au BorderLayout, hauteur calculee depuis sa police) ===
            JPanel piedBouton = new JPanel(new BorderLayout());
            piedBouton.setOpaque(false);
            this.bouton = new BoutonMedieval("Ameliorer",
                    BoutonMedieval.Style.PRIMAIRE);
            piedBouton.add(this.bouton, BorderLayout.CENTER);
            piedBouton.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
            add(piedBouton, BorderLayout.SOUTH);
        }

        /**
         * Une colonne titre (en haut) + valeur (en bas) avec GridLayout(2,1)
         * pour que les deux soient toujours visibles a part egales, peu
         * importe la taille du contenu.
         */
        private JPanel creerColonne(String libelle, JComponent valeur) {
            JPanel col = new JPanel(new GridLayout(2, 1, 0, 0));
            col.setOpaque(true);
            col.setBackground(new Color(8, 6, 4));
            col.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Palette.BORDURE_FONCEE, 1),
                    BorderFactory.createEmptyBorder(2, 2, 2, 2)));

            // Cellule du haut : titre, ancre en bas pour qu'il colle a la valeur
            JLabel titre = new JLabel(libelle.toUpperCase(),
                    SwingConstants.CENTER);
            titre.setFont(Polices.PETIT_LABEL);
            titre.setForeground(Palette.TEXTE_TERTIAIRE);
            titre.setVerticalAlignment(SwingConstants.BOTTOM);
            col.add(titre);

            // Cellule du bas : valeur (label ou panel)
            col.add(valeur);
            return col;
        }

        void rafraichir(Batiment b, boolean planifie, boolean payable) {
            // === NIVEAU ===
            this.valeurNiveau.setText(b.niveau() + " / "
                    + Equilibrage.NIVEAU_MAX_BATIMENT);

            // === STATUT ===
            if (planifie) {
                this.valeurStatut.setText("Planifie");
                this.valeurStatut.setForeground(Palette.OR_CLAIR);
            } else if (b.enChantier()) {
                this.valeurStatut.setText("Chantier"
                        + " (" + b.toursRestants() + ")");
                this.valeurStatut.setForeground(Palette.OR);
            } else if (b.estEndommage()) {
                this.valeurStatut.setText("Endommage");
                this.valeurStatut.setForeground(Palette.ROUGE_CLAIR);
            } else {
                this.valeurStatut.setText("Normal");
                this.valeurStatut.setForeground(Palette.VERT_POSITIF);
            }

            // === COUT ===
            this.valeurCout.removeAll();
            if (planifie) {
                ajouterLigneCout("--", Palette.TEXTE_TERTIAIRE);
            } else if (b.peutEtreAmeliore()) {
                Map<Ressource, Integer> cout = Equilibrage.coutAmelioration(
                        this.type, b.niveau() + 1);
                for (Map.Entry<Ressource, Integer> e : cout.entrySet()) {
                    ajouterLigneCout(
                            e.getValue() + " " + e.getKey().libelle(),
                            couleurRessource(e.getKey()));
                }
            } else {
                ajouterLigneCout("Niveau max atteint",
                        Palette.TEXTE_TERTIAIRE);
            }
            this.valeurCout.revalidate();
            this.valeurCout.repaint();

            // === BOUTON ===
            if (planifie) {
                this.bouton.setText("Annuler");
                this.bouton.setEnabled(true);
            } else if (b.peutEtreAmeliore()) {
                this.bouton.setText("Ameliorer");
                this.bouton.setEnabled(payable);
            } else {
                this.bouton.setText("Ameliorer");
                this.bouton.setEnabled(false);
            }
        }

        private void ajouterLigneCout(String texte, Color couleur) {
            JLabel l = new JLabel(texte, SwingConstants.CENTER);
            l.setFont(Polices.PETITE_VALEUR.deriveFont(11f));
            l.setForeground(couleur);
            l.setAlignmentX(CENTER_ALIGNMENT);
            this.valeurCout.add(l);
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
    }
}
