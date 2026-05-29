package Controleur;

import javax.swing.SwingUtilities;

import Modele.partie.Partie;
import Vue.FenetreJeu;
import Vue.VueHUD;

/**
 * Controleur principal. Ecoute les actions du joueur (clic sur "Fin de tour")
 * et fait avancer le modele en consequence.
 *
 * Le controleur attache l'ActionListener du bouton (pas la vue elle-meme).
 */
public class ControleurPartie {

    private final Partie partie;
    private final FenetreJeu fenetre;

    public ControleurPartie(Partie partie, FenetreJeu fenetre) {
        this.partie = partie;
        this.fenetre = fenetre;
        miseEnPlaceEvenements();
    }

    private void miseEnPlaceEvenements() {
        VueHUD hud = this.fenetre.hud();
        hud.boutonFinTour().addActionListener(e -> terminerTour());
    }

    /**
     * Enchaine les phases jusqu'a revenir en planification.
     * Garde-fou a 50 phases pour eviter une boucle infinie en cas de bug.
     */
    public void terminerTour() {
        SwingUtilities.invokeLater(() -> {
            int garde = 0;
            do {
                this.partie.passerEtape();
                garde++;
                if (garde > 50) {
                    throw new IllegalStateException(
                            "Boucle de resolution > 50 phases : transition incorrecte ?");
                }
            } while (!this.partie.enAttenteJoueur());
            this.partie.notifierTourDemarre();
        });
    }
}
