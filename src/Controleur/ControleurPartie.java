package Controleur;

import javax.swing.SwingUtilities;

import Modele.partie.Partie;
import Vue.FenetreJeu;
import Vue.VueHUD;

/**
 * Chef d'orchestre de la couche Controleur. Possede la reference vers la
 * Partie et expose les actions de haut niveau (terminer le tour, sauvegarder,
 * charger). Au Sprint 1, on n'a que terminer le tour.
 *
 * Ce controleur s'attache au bouton "Fin de tour" du VueHUD via un
 * ActionListener (cf. plan d'architecture section 10 : "Tout listener Swing
 * est attache par un controleur").
 *
 * Implementation de la fin de tour : on appelle {@code partie.passerEtape()}
 * en boucle jusqu'a retourner en EtatPlanification. Au Sprint 1 il n'y a pas
 * de dialogue d'evenement bloquant ; ce sera ajoute Sprint 2+ (cf. risque 3
 * du plan d'architecture).
 */
public class ControleurPartie {

    private final Partie partie;
    private final FenetreJeu fenetre;

    public ControleurPartie(Partie partie, FenetreJeu fenetre) {
        this.partie = partie;
        this.fenetre = fenetre;
        cablerEvenements();
    }

    private void cablerEvenements() {
        VueHUD hud = this.fenetre.hud();
        hud.boutonFinTour().addActionListener(e -> terminerTour());
    }

    /**
     * Lance la phase de resolution : on enchaine les phases jusqu'a retour
     * en EtatPlanification.
     *
     * On execute sur l'EDT Swing (le clic vient deja de l'EDT). Au Sprint 1
     * la resolution est synchrone et rapide ; quand on ajoutera le
     * DialogueEvenement (Sprint 2+), il faudra rendre la main a l'EDT entre
     * EtatEvenement et la suite (cf. risque 3 du plan d'architecture).
     */
    public void terminerTour() {
        SwingUtilities.invokeLater(() -> {
            // Boucle de resolution : on quitte des qu'on est de nouveau en attente joueur.
            int garde = 0;
            do {
                this.partie.passerEtape();
                garde++;
                if (garde > 50) {
                    // Garde-fou contre les boucles infinies (transition d'etat
                    // mal definie). 50 phases largement au-dessus des 9 prevues.
                    throw new IllegalStateException(
                            "Boucle de resolution > 50 phases : transition incorrecte ?");
                }
            } while (!this.partie.enAttenteJoueur());
            this.partie.notifierTourDemarre();
        });
    }
}
