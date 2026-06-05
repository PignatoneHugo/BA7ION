package Controleur;

import javax.swing.SwingUtilities;

import Modele.evenement.Choix;
import Modele.partie.Partie;
import Vue.FenetreJeu;
import Vue.VueHUD;
import Vue.dialogue.DialogueEvenement;
import Vue.i18n.Traducteur;

/**
 * Controleur principal. Ecoute les actions du joueur (clic sur "Fin de tour")
 * et fait avancer le modele en consequence.
 *
 * Instancie aussi les controleurs des onglets (ControleurEconomie, ...) au
 * moment de la mise en place des evenements.
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

        // Controleurs des onglets metier.
        new ControleurEconomie(this.partie, this.fenetre);
        new ControleurInfrastructures(this.partie, this.fenetre);
    }

    /**
     * Enchaine les phases jusqu'a revenir en planification. Si un evenement
     * est en attente apres une phase, ouvre le dialogue modal pour recuperer
     * le choix du joueur puis applique l'effet avant de continuer.
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

                if (this.partie.enAttenteEvenement()) {
                    String titre = Traducteur.t(
                            this.partie.evenementEnAttente().cleTitre());
                    Choix choix = DialogueEvenement.afficher(this.fenetre,
                            this.partie.evenementEnAttente());
                    this.partie.resoudreEvenement(choix);
                    this.fenetre.statusBar().setMessage(
                            Traducteur.t("status.evenement_resolu") + " : " + titre
                                    + " - " + Traducteur.t(choix.cleI18n()));
                }
            } while (!this.partie.enAttenteJoueur());
            this.partie.notifierTourDemarre();
            this.fenetre.statusBar().setMessage(
                    Traducteur.t("status.tour_demarre") + " " + this.partie.numeroTour());
        });
    }
}
