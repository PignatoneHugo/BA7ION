package Controleur;

import java.io.IOException;

import javax.swing.SwingUtilities;

import Modele.evenement.Choix;
import Modele.partie.BilanTour;
import Modele.partie.ConditionsFin;
import Modele.partie.Partie;
import Modele.persistance.GestionnaireSauvegardes;
import Vue.FenetreJeu;
import Vue.VueHUD;
import Vue.dialogue.DialogueEvenement;
import Vue.dialogue.DialogueFinTour;
import Vue.dialogue.DialogueRapportCombat;
// Controleur principal : gere le bouton "Fin de tour" et l'avancement du jeu.
public class ControleurPartie {

    private final Partie partie;
    private final FenetreJeu fenetre;

    // Etat pris au debut du tour, pour le bilan de fin de tour.
    private BilanTour bilanDebutTour;

    public ControleurPartie(Partie partie, FenetreJeu fenetre) {
        this.partie = partie;
        this.fenetre = fenetre;
        this.bilanDebutTour = new BilanTour(partie.numeroTour(), partie.joueur());
        miseEnPlaceEvenements();
    }

    private void miseEnPlaceEvenements() {
        VueHUD hud = this.fenetre.hud();
        hud.boutonFinTour().addActionListener(e -> terminerTour());

        // Les controleurs des onglets.
        new ControleurEconomie(this.partie, this.fenetre);
        new ControleurInfrastructures(this.partie, this.fenetre);
        new ControleurMilitaire(this.partie, this.fenetre);
        new ControleurMarche(this.partie, this.fenetre);
    }

    // Sauvegarde auto de fin de tour. On n'embete pas le joueur si ca rate.
    private void sauvegardeAutomatique() {
        try {
            GestionnaireSauvegardes.sauvegarderAuto(this.partie);
            this.fenetre.statusBar().setMessage(
                    "Tour " + this.partie.numeroTour()
                            + " - sauvegarde automatique : "
                            + GestionnaireSauvegardes.fichierAuto(this.partie).getName());
        } catch (IOException ex) {
            this.fenetre.statusBar().setMessage(
                    "Echec de la sauvegarde automatique : " + ex.getMessage());
        }
    }

    public void terminerTour() {
        SwingUtilities.invokeLater(() -> {
            int numeroAvant = this.partie.numeroTour();
            BilanTour bilanAvant = this.bilanDebutTour;

            this.partie.viderBatraillesDuTour();

            // On avance jusqu'a un evenement ou la fin du tour.
            avancerDepuisPlanification();

            // Rapports de combat puis recap du tour.
            if (!this.partie.batraillesDuTour().isEmpty()) {
                DialogueRapportCombat rc = new DialogueRapportCombat(
                        this.fenetre, this.partie, numeroAvant);
                rc.setVisible(true);
            }
            DialogueFinTour recap = new DialogueFinTour(
                    this.fenetre, bilanAvant, this.partie.joueur(),
                    numeroAvant, this.partie);
            recap.setVisible(true);

            // L'evenement du tour, s'il y en a un.
            if (this.partie.enAttenteEvenement()) {
                String titre = this.partie.evenementEnAttente().titre();
                Choix choix = DialogueEvenement.afficher(this.fenetre,
                        this.partie.evenementEnAttente(),
                        this.partie.joueur());
                this.partie.resoudreEvenement(choix);
                this.fenetre.statusBar().setMessage(
                        "Evenement resolu" + " : " + titre
                                + " - " + choix.libelle());
            }

            // On termine les phases restantes du tour.
            avancerJusquaJoueur();

            this.partie.notifierTourDemarre();
            this.fenetre.statusBar().setMessage(
                    "Nouveau tour :" + " " + this.partie.numeroTour());

            // Fin de partie ?
            ConditionsFin.Etat etat = ConditionsFin.evaluer(this.partie);
            if (etat != ConditionsFin.Etat.EN_COURS) {
                this.fenetre.afficherFinPartie(this.partie, etat);
                new ControleurFinPartie(this.fenetre);
                return;
            }

            sauvegardeAutomatique();

            this.bilanDebutTour = new BilanTour(this.partie.numeroTour(),
                    this.partie.joueur());
        });
    }

    // Sort de la planification et avance jusqu'a un evenement ou la fin du tour.
    private void avancerDepuisPlanification() {
        int garde = 0;
        do {
            this.partie.passerEtape();
            garde++;
            if (garde > 50) {
                throw new IllegalStateException(
                        "Boucle de resolution > 50 phases : transition incorrecte ?");
            }
            if (this.partie.enAttenteEvenement()) {
                return;
            }
        } while (!this.partie.enAttenteJoueur());
    }

    // Avance jusqu'au retour en planification (ne fait rien si on y est deja).
    private void avancerJusquaJoueur() {
        int garde = 0;
        while (!this.partie.enAttenteJoueur()) {
            this.partie.passerEtape();
            garde++;
            if (garde > 50) {
                throw new IllegalStateException(
                        "Boucle de resolution > 50 phases : transition incorrecte ?");
            }
        }
    }
}
