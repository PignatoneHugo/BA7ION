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

        // On verifie qu'on a de quoi payer.
        for (Map.Entry<Ressource, Integer> e : cout.entrySet()) {
            if (!this.royaumeJoueur.tresor().contient(e.getKey(), e.getValue())) {
                return;
            }
        }

        // On paye et on empile l'action.
        for (Map.Entry<Ressource, Integer> e : cout.entrySet()) {
            this.royaumeJoueur.tresor().retirer(e.getKey(), e.getValue());
        }
        this.royaumeJoueur.fileActions().ajouter(new ActionAmeliorer(type));
        this.royaumeJoueur.notifierTresorChange();
        this.royaumeJoueur.notifierFileActionsChangee();

        String nomBatiment = type.libelle();
        this.fenetre.statusBar().setMessage(
                "Amelioration planifiee" + " : " + nomBatiment);
    }

    private void deplanifier(TypeBatiment type) {
        for (Action a : new ArrayList<>(this.royaumeJoueur.fileActions().contenu())) {
            if (a instanceof ActionAmeliorer && ((ActionAmeliorer) a).type() == type) {
                this.royaumeJoueur.fileActions().retirer(a);

                // On rembourse le cout.
                Batiment b = this.royaumeJoueur.batiment(type);
                Map<Ressource, Integer> cout = Equilibrage.coutAmelioration(type, b.niveau() + 1);
                for (Map.Entry<Ressource, Integer> e : cout.entrySet()) {
                    this.royaumeJoueur.tresor().ajouter(e.getKey(), e.getValue());
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
