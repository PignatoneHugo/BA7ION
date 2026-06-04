package Controleur;

import java.util.ArrayList;
import java.util.Map;

import Modele.action.Action;
import Modele.action.ActionAmeliorer;
import Modele.economie.Ressource;
import Modele.infrastructure.Batiment;
import Modele.infrastructure.TypeBatiment;
import Modele.partie.Partie;
import Vue.FenetreJeu;
import Vue.i18n.Traducteur;
import Vue.onglets.OngletInfrastructures;

import config.Equilibrage;

/**
 * Controleur de l'onglet Infrastructures. Attache les listeners sur les
 * boutons "Ameliorer" / "Annuler".
 *
 * Le cout d'amelioration est retire au moment de la planification (et
 * rembourse a l'annulation), pour eviter que le joueur planifie plus
 * d'ameliorations qu'il ne peut payer.
 */
public class ControleurInfrastructures extends ControleurOnglet {

    private final OngletInfrastructures onglet;
    private final FenetreJeu fenetre;

    public ControleurInfrastructures(Partie partie, FenetreJeu fenetre) {
        super(partie);
        this.fenetre = fenetre;
        this.onglet = fenetre.dashboard().ongletInfrastructures();
        miseEnPlaceEvenements();
    }

    private void miseEnPlaceEvenements() {
        for (TypeBatiment t : TypeBatiment.values()) {
            final TypeBatiment type = t;
            this.onglet.boutonAmeliorer(type).addActionListener(e -> cliquer(type));
        }
    }

    private void cliquer(TypeBatiment type) {
        if (this.onglet.estPlanifie(type)) {
            deplanifier(type);
        } else {
            planifier(type);
        }
    }

    private void planifier(TypeBatiment type) {
        Batiment b = this.royaumeJoueur.batiment(type);
        if (b == null || !b.peutEtreAmeliore()) {
            return;
        }
        Map<Ressource, Integer> cout = Equilibrage.coutAmelioration(type, b.niveau() + 1);

        // Verifier que toutes les ressources sont disponibles (compte tenu
        // des deductions deja faites pour les ameliorations precedemment planifiees).
        for (Map.Entry<Ressource, Integer> e : cout.entrySet()) {
            if (!this.royaumeJoueur.tresor().contient(e.getKey(), e.getValue())) {
                return;
            }
        }

        // Retirer le cout et empiler l'action.
        for (Map.Entry<Ressource, Integer> e : cout.entrySet()) {
            this.royaumeJoueur.tresor().retirer(e.getKey(), e.getValue());
        }
        this.royaumeJoueur.fileActions().ajouter(new ActionAmeliorer(type));
        this.royaumeJoueur.notifierTresorChange();
        this.royaumeJoueur.notifierFileActionsChangee();

        String nomBatiment = Traducteur.t(type.cleI18n());
        this.fenetre.statusBar().setMessage(
                Traducteur.t("status.amelioration_planifiee") + " : " + nomBatiment);
    }

    private void deplanifier(TypeBatiment type) {
        for (Action a : new ArrayList<>(this.royaumeJoueur.fileActions().contenu())) {
            if (a instanceof ActionAmeliorer && ((ActionAmeliorer) a).type() == type) {
                this.royaumeJoueur.fileActions().retirer(a);

                // Rembourser le cout (le batiment est toujours au niveau d'avant
                // puisque le chantier n'a pas demarre).
                Batiment b = this.royaumeJoueur.batiment(type);
                Map<Ressource, Integer> cout = Equilibrage.coutAmelioration(type, b.niveau() + 1);
                for (Map.Entry<Ressource, Integer> e : cout.entrySet()) {
                    this.royaumeJoueur.tresor().ajouter(e.getKey(), e.getValue());
                }
                this.royaumeJoueur.notifierTresorChange();
                this.royaumeJoueur.notifierFileActionsChangee();

                String nomBatiment = Traducteur.t(type.cleI18n());
                this.fenetre.statusBar().setMessage(
                        Traducteur.t("status.amelioration_annulee") + " : " + nomBatiment);
                return;
            }
        }
    }
}
