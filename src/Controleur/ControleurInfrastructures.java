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
import Vue.onglets.OngletInfrastructures;

import config.Equilibrage;

// Controleur de l'onglet Infrastructures (boutons Ameliorer / Annuler).
// Le cout est paye a la planification et rembourse a l'annulation.
public class ControleurInfrastructures extends ControleurOnglet {

    private final OngletInfrastructures onglet;
    private final FenetreJeu fenetre;

    /**
     * Construit le controleur de l'onglet Infrastructures et branche ses boutons.
     *
     * @param partie la partie en cours
     * @param fenetre la fenetre de jeu
     */
    public ControleurInfrastructures(Partie partie, FenetreJeu fenetre) {
        super(partie);
        this.fenetre = fenetre;
        this.onglet = fenetre.dashboard().ongletInfrastructures();
        miseEnPlaceEvenements();
    }

    private void miseEnPlaceEvenements() {
        for (TypeBatiment typeCourant : TypeBatiment.values()) {
            final TypeBatiment type = typeCourant;
            this.onglet.boutonAmeliorer(type).addActionListener(evenement -> cliquer(type));
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
        Batiment batiment = this.royaumeJoueur.batiment(type);
        if (batiment == null || !batiment.peutEtreAmeliore()) {
            return;
        }
        Map<Ressource, Integer> cout = Equilibrage.coutAmelioration(type, batiment.niveau() + 1);

        // On verifie qu'on a de quoi payer.
        for (Map.Entry<Ressource, Integer> entree : cout.entrySet()) {
            if (!this.royaumeJoueur.tresor().contient(entree.getKey(), entree.getValue())) {
                return;
            }
        }

        // On paye et on empile l'action.
        for (Map.Entry<Ressource, Integer> entree : cout.entrySet()) {
            this.royaumeJoueur.tresor().retirer(entree.getKey(), entree.getValue());
        }
        this.royaumeJoueur.fileActions().ajouter(new ActionAmeliorer(type));
        this.royaumeJoueur.notifierTresorChange();
        this.royaumeJoueur.notifierFileActionsChangee();

        String nomBatiment = type.libelle();
        this.fenetre.statusBar().setMessage(
                "Amelioration planifiee" + " : " + nomBatiment);
    }

    private void deplanifier(TypeBatiment type) {
        for (Action action : new ArrayList<>(this.royaumeJoueur.fileActions().contenu())) {
            if (action instanceof ActionAmeliorer && ((ActionAmeliorer) action).type() == type) {
                this.royaumeJoueur.fileActions().retirer(action);

                // On rembourse le cout.
                Batiment batiment = this.royaumeJoueur.batiment(type);
                Map<Ressource, Integer> cout = Equilibrage.coutAmelioration(type, batiment.niveau() + 1);
                for (Map.Entry<Ressource, Integer> entree : cout.entrySet()) {
                    this.royaumeJoueur.tresor().ajouter(entree.getKey(), entree.getValue());
                }
                this.royaumeJoueur.notifierTresorChange();
                this.royaumeJoueur.notifierFileActionsChangee();

                String nomBatiment = type.libelle();
                this.fenetre.statusBar().setMessage(
                        "Amelioration annulee" + " : " + nomBatiment);
                return;
            }
        }
    }
}
