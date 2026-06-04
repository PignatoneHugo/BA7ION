package Vue.onglets;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.EnumMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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
import Vue.i18n.Traducteur;

import config.Equilibrage;

/**
 * Onglet Infrastructures : affiche les 9 batiments du royaume avec leur
 * niveau, leur statut (normal/endommage/en chantier/planifie), le cout
 * d'amelioration et un bouton pour lancer le chantier.
 *
 * Affiche aussi en bas le nombre d'actions actuellement dans la file
 * d'attente du royaume.
 *
 * Vue passive : les boutons sont crees ici mais les listeners sont attaches
 * par le ControleurInfrastructures.
 */
public class OngletInfrastructures extends JPanel implements Observer {

    private static final long serialVersionUID = 1L;

    private final Royaume royaume;
    private final Map<TypeBatiment, JLabel> labelsNiveau;
    private final Map<TypeBatiment, JLabel> labelsStatut;
    private final Map<TypeBatiment, JLabel> labelsCout;
    private final Map<TypeBatiment, JButton> boutonsAmeliorer;
    private final JLabel labelFileActions;

    public OngletInfrastructures(Royaume royaume) {
        this.royaume = royaume;
        this.labelsNiveau = new EnumMap<>(TypeBatiment.class);
        this.labelsStatut = new EnumMap<>(TypeBatiment.class);
        this.labelsCout = new EnumMap<>(TypeBatiment.class);
        this.boutonsAmeliorer = new EnumMap<>(TypeBatiment.class);

        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel titre = new JLabel(Traducteur.t("onglet.infrastructures"));
        titre.setFont(titre.getFont().deriveFont(Font.BOLD, 18f));
        add(titre, BorderLayout.NORTH);

        // 5 colonnes : Batiment | Niveau | Statut | Cout | Bouton
        JPanel contenu = new JPanel(new GridLayout(0, 5, 16, 8));

        contenu.add(enTete("infra.batiment"));
        contenu.add(enTete("infra.niveau"));
        contenu.add(enTete("infra.statut"));
        contenu.add(enTete("infra.cout"));
        contenu.add(new JLabel(""));

        for (TypeBatiment t : TypeBatiment.values()) {
            contenu.add(new JLabel(Traducteur.t(t.cleI18n())));

            JLabel lblNiveau = new JLabel("", SwingConstants.CENTER);
            this.labelsNiveau.put(t, lblNiveau);
            contenu.add(lblNiveau);

            JLabel lblStatut = new JLabel("", SwingConstants.LEFT);
            this.labelsStatut.put(t, lblStatut);
            contenu.add(lblStatut);

            JLabel lblCout = new JLabel("", SwingConstants.LEFT);
            this.labelsCout.put(t, lblCout);
            contenu.add(lblCout);

            JButton btn = new JButton(Traducteur.t("infra.ameliorer"));
            this.boutonsAmeliorer.put(t, btn);
            contenu.add(btn);
        }

        JPanel centre = new JPanel(new BorderLayout());
        centre.add(contenu, BorderLayout.NORTH);

        // Pied : compteur d'actions en file
        this.labelFileActions = new JLabel();
        this.labelFileActions.setFont(this.labelFileActions.getFont().deriveFont(Font.ITALIC));
        this.labelFileActions.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));
        centre.add(this.labelFileActions, BorderLayout.SOUTH);

        add(centre, BorderLayout.CENTER);

        rafraichir();
        this.royaume.addObserver(this);
    }

    private JLabel enTete(String cle) {
        JLabel l = new JLabel(Traducteur.t(cle));
        l.setFont(l.getFont().deriveFont(Font.BOLD));
        return l;
    }

    public JButton boutonAmeliorer(TypeBatiment type) {
        return this.boutonsAmeliorer.get(type);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(arg instanceof Notification)) {
            return;
        }
        rafraichir();
    }

    /**
     * Met a jour niveaux, statuts, couts, texte/etat des boutons et compteur
     * de file. Si une amelioration est deja planifiee pour ce batiment, le
     * bouton passe en mode "Annuler" pour permettre au joueur de la retirer.
     */
    private void rafraichir() {
        for (TypeBatiment t : TypeBatiment.values()) {
            Batiment b = this.royaume.batiment(t);
            this.labelsNiveau.get(t).setText(b.niveau() + " / " + Equilibrage.NIVEAU_MAX_BATIMENT);
            boolean planifie = ameliorationEnFile(t);
            this.labelsStatut.get(t).setText(statut(b, planifie));

            JButton btn = this.boutonsAmeliorer.get(t);
            if (planifie) {
                this.labelsCout.get(t).setText("-");
                btn.setText(Traducteur.t("infra.annuler"));
                btn.setEnabled(true);
            } else if (b.peutEtreAmeliore()) {
                this.labelsCout.get(t).setText(formaterCout(t, b.niveau() + 1));
                btn.setText(Traducteur.t("infra.ameliorer"));
                btn.setEnabled(coutPayable(t, b.niveau() + 1));
            } else {
                this.labelsCout.get(t).setText("-");
                btn.setText(Traducteur.t("infra.ameliorer"));
                btn.setEnabled(false);
            }
        }

        int nbActions = this.royaume.fileActions().taille();
        if (nbActions == 0) {
            this.labelFileActions.setText(Traducteur.t("infra.file.vide"));
        } else {
            this.labelFileActions.setText(
                    Traducteur.t("infra.file.compteur") + " : " + nbActions);
        }
    }

    /** True si une amelioration pour ce batiment est deja dans la file. */
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

    /** True si le tresor courant peut couvrir le cout d'amelioration vise. */
    private boolean coutPayable(TypeBatiment type, int niveauCible) {
        Map<Ressource, Integer> cout = Equilibrage.coutAmelioration(type, niveauCible);
        for (Map.Entry<Ressource, Integer> e : cout.entrySet()) {
            if (!this.royaume.tresor().contient(e.getKey(), e.getValue())) {
                return false;
            }
        }
        return true;
    }

    private String statut(Batiment b, boolean planifie) {
        if (planifie) {
            return Traducteur.t("infra.statut.planifie");
        }
        if (b.enChantier()) {
            return Traducteur.t("infra.statut.chantier") + " (" + b.toursRestants() + ")";
        }
        if (b.estEndommage()) {
            return Traducteur.t("infra.statut.endommage");
        }
        return Traducteur.t("infra.statut.normal");
    }

    private String formaterCout(TypeBatiment type, int niveauCible) {
        Map<Ressource, Integer> cout = Equilibrage.coutAmelioration(type, niveauCible);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Ressource, Integer> e : cout.entrySet()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(e.getValue()).append(" ").append(Traducteur.t(e.getKey().cleI18n()));
        }
        return sb.toString();
    }
}
