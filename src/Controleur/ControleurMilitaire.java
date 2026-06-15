package Controleur;

import Modele.action.ActionAttaquer;
import Modele.action.ActionDemobiliser;
import Modele.action.ActionMobiliser;
import Modele.militaire.PostureCombat;
import Modele.militaire.TypeUnite;
import Modele.partie.Partie;
import Modele.royaume.Royaume;
import Vue.FenetreJeu;
import Vue.dialogue.DialogueChoixCible;
import Vue.onglets.OngletMilitaire;

// Controleur de l'onglet Militaire : recruter, demobiliser, posture, attaque.
public class ControleurMilitaire {

    private final Partie partie;
    private final FenetreJeu fenetre;

    public ControleurMilitaire(Partie partie, FenetreJeu fenetre) {
        this.partie = partie;
        this.fenetre = fenetre;
        brancher();
    }

    private void brancher() {
        OngletMilitaire onglet = this.fenetre.dashboard().ongletMilitaire();
        Royaume joueur = this.partie.joueur();

        // Recruter / Demobiliser pour chaque type
        for (TypeUnite type : TypeUnite.values()) {
            onglet.boutonRecruter(type).addActionListener(e -> {
                ActionMobiliser action = new ActionMobiliser(type, 1);
                if (action.estExecutable(joueur)) {
                    action.executer(joueur);
                    joueur.notifierArmeeChangee();
                    joueur.notifierTresorChange();
                    joueur.notifierPopulationChangee();
                    this.fenetre.statusBar().setMessage(
                            "Soldat recrute :"
                                    + " " + type.libelle());
                }
            });
            onglet.boutonDemobiliser(type).addActionListener(e -> {
                ActionDemobiliser action = new ActionDemobiliser(type, 1);
                if (action.estExecutable(joueur)) {
                    action.executer(joueur);
                    joueur.notifierArmeeChangee();
                    joueur.notifierPopulationChangee();
                    this.fenetre.statusBar().setMessage(
                            "Soldat demobilise :"
                                    + " " + type.libelle());
                }
            });
        }

        // Selecteur de posture
        for (PostureCombat posture : PostureCombat.values()) {
            onglet.togglePosture(posture).addActionListener(e -> {
                joueur.armee().definirPosture(posture);
                joueur.notifierArmeeChangee();
                this.fenetre.statusBar().setMessage(
                        "Posture changee"
                                + " : " + posture.libelle());
            });
        }

        // Bouton Attaquer : ouvre une popup pour choisir la cible.
        if (onglet.boutonAttaquer() != null) {
            onglet.boutonAttaquer().addActionListener(e -> ouvrirPopupAttaque());
        }
    }

    private void ouvrirPopupAttaque() {
        if (!this.partie.combatsAutorises()) {
            this.fenetre.statusBar().setMessage(
                    "Les combats ne sont pas encore ouverts (tour "
                            + config.Equilibrage.TOUR_DEBUT_COMBATS + ").");
            return;
        }
        Royaume joueur = this.partie.joueur();
        DialogueChoixCible popup = new DialogueChoixCible(
                this.fenetre, joueur, this.partie.bots(),
                cible -> {
                    ActionAttaquer action = new ActionAttaquer(
                            cible, joueur.armee().posture());
                    if (!action.estExecutable(joueur)) {
                        return;
                    }
                    action.executer(joueur);
                    joueur.notifierFileActionsChangee();
                    this.fenetre.statusBar().setMessage(
                            "Attaque planifiee"
                                    + " : " + cible.nom());
                });
        popup.setVisible(true);
    }
}
