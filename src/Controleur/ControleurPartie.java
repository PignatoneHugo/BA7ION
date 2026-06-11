package Controleur;

import javax.swing.SwingUtilities;

import Modele.evenement.Choix;
import Modele.partie.BilanTour;
import Modele.partie.ConditionsFin;
import Modele.partie.Partie;
import Vue.FenetreJeu;
import Vue.VueHUD;
import Vue.dialogue.DialogueEvenement;
import Vue.dialogue.DialogueFinTour;
import Vue.dialogue.DialogueRapportCombat;
import Vue.i18n.Traducteur;

/**
 * Controleur principal. Ecoute les actions du joueur (clic sur "Fin de tour")
 * et fait avancer le modele en consequence.
 *
 * Apres chaque tour, verifie l'etat de fin de partie : si VICTOIRE ou
 * DEFAITE, bascule l'ecran sur VueFinPartie.
 */
public class ControleurPartie {

    private final Partie partie;
    private final FenetreJeu fenetre;

    // Snapshot pris au DEBUT du tour, AVANT que le joueur ait planifie quoi
    // que ce soit. Sinon le cout des ameliorations (deduit a la planification)
    // serait invisible dans le bilan.
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

        // Controleurs des onglets metier.
        new ControleurEconomie(this.partie, this.fenetre);
        new ControleurInfrastructures(this.partie, this.fenetre);
        new ControleurMilitaire(this.partie, this.fenetre);
        new ControleurMarche(this.partie, this.fenetre);
    }

    public void terminerTour() {
        SwingUtilities.invokeLater(() -> {
            // Snapshot pris au debut du tour (avant que le joueur ait paye
            // le cout de ses planifications).
            int numeroAvant = this.partie.numeroTour();
            BilanTour bilanAvant = this.bilanDebutTour;

            // On vide les batailles du tour precedent.
            this.partie.viderBatraillesDuTour();

            // === Phase 1 : sortir de la planification et avancer jusqu'a
            // un evenement en attente OU la fin du tour s'il n'y en a pas. ===
            avancerDepuisPlanification();

            // === Phase 2 : rapports avant l'evenement du nouveau tour ===
            if (!this.partie.batraillesDuTour().isEmpty()) {
                DialogueRapportCombat rc = new DialogueRapportCombat(
                        this.fenetre, this.partie, numeroAvant);
                rc.setVisible(true);
            }
            DialogueFinTour recap = new DialogueFinTour(
                    this.fenetre, bilanAvant, this.partie.joueur(),
                    numeroAvant, this.partie);
            recap.setVisible(true);

            // === Phase 3 : evenement (s'il y en a un en attente) ===
            if (this.partie.enAttenteEvenement()) {
                String titre = Traducteur.t(
                        this.partie.evenementEnAttente().cleTitre());
                Choix choix = DialogueEvenement.afficher(this.fenetre,
                        this.partie.evenementEnAttente(),
                        this.partie.joueur());
                this.partie.resoudreEvenement(choix);
                this.fenetre.statusBar().setMessage(
                        Traducteur.t("status.evenement_resolu") + " : " + titre
                                + " - " + Traducteur.t(choix.cleI18n()));
            }

            // === Phase 4 : achever les phases restantes UNIQUEMENT si on
            // n'est pas deja revenu en planification (= cas ou un evenement
            // a interrompu la phase 1). Sinon on entamerait un second tour. ===
            avancerJusquaJoueur();

            this.partie.notifierTourDemarre();
            this.fenetre.statusBar().setMessage(
                    Traducteur.t("status.tour_demarre") + " " + this.partie.numeroTour());

            // === Phase 5 : verifier fin de partie ===
            ConditionsFin.Etat etat = ConditionsFin.evaluer(this.partie);
            if (etat != ConditionsFin.Etat.EN_COURS) {
                this.fenetre.afficherFinPartie(this.partie, etat);
                new ControleurFinPartie(this.fenetre);
                return;
            }

            // Snapshot pour le nouveau tour.
            this.bilanDebutTour = new BilanTour(this.partie.numeroTour(),
                    this.partie.joueur());
        });
    }

    /**
     * Phase 1 : sortir de EtatPlanification (do-while pour forcer au moins
     * un passerEtape) et continuer jusqu'a un evenement en attente OU
     * le retour en planification s'il n'y a aucun evenement ce tour.
     */
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

    /**
     * Phase 4 : avance jusqu'a etre en attente du joueur (= retour en
     * planification). NE FAIT RIEN si on y est deja, ce qui evite
     * d'entamer un second tour quand la phase 1 a deja tout fait.
     */
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
