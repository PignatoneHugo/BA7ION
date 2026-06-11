package Controleur;

import Modele.action.ActionEchanger;
import Modele.economie.Ressource;
import Modele.partie.Partie;
import Modele.royaume.Royaume;
import Vue.FenetreJeu;
import Vue.i18n.Traducteur;
import Vue.onglets.OngletMarche;

/**
 * Branche le bouton Echanger de l'onglet Marche sur le modele.
 * L'action est executee immediatement (pas dans la file d'attente)
 * pour un feedback direct.
 */
public class ControleurMarche {

    private final Partie partie;
    private final FenetreJeu fenetre;

    public ControleurMarche(Partie partie, FenetreJeu fenetre) {
        this.partie = partie;
        this.fenetre = fenetre;
        brancher();
    }

    private void brancher() {
        OngletMarche onglet = this.fenetre.dashboard().ongletMarche();
        Royaume joueur = this.partie.joueur();

        onglet.boutonEchanger().addActionListener(e -> {
            Ressource src = onglet.ressourceSource();
            Ressource cible = onglet.ressourceCible();
            int montant = onglet.montantSource();
            if (src == null || cible == null || src == cible || montant <= 0) {
                return;
            }
            ActionEchanger action = new ActionEchanger(src, cible, montant);
            if (!action.estExecutable(joueur)) {
                return;
            }
            action.executer(joueur);
            joueur.notifierTresorChange();
            this.fenetre.statusBar().setMessage(
                    Traducteur.t("status.echange_effectue") + " : "
                            + montant + " " + Traducteur.t(src.cleI18n())
                            + " → " + Traducteur.t(cible.cleI18n()));
        });
    }
}
