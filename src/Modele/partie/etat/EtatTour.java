package Modele.partie.etat;

import Modele.partie.Partie;

/**
 * Pattern State pour le cycle de tour (9 phases ordonnees).
 *
 * Chaque sous-classe represente une phase. Le Tour delegue {@code passerEtape()}
 * a son etat courant qui :
 *   1. execute son traitement metier ({@link #executer(Partie)}),
 *   2. retourne l'etat suivant ({@link #suivant()}) pour la transition.
 *
 * Note importante (cf. risque 3 du plan d'architecture) :
 * {@link #executer(Partie)} peut etre asynchrone. Si une phase bloque sur une
 * interaction utilisateur (cas de EtatEvenement), elle stocke l'attente dans
 * la Partie, et le ControleurPartie reprend la chaine quand le joueur a repondu.
 */
public interface EtatTour {

    /**
     * Execute la phase. Modifie le modele et notifie les observers
     * (au moins une fois, avec une Notification de type PHASE_CHANGEE).
     */
    void executer(Partie partie);

    /**
     * Retourne l'etat suivant dans le cycle (jamais null).
     * Apres {@code EtatFinTour}, on revient a {@code EtatPlanification}.
     */
    EtatTour suivant();

    /**
     * Cle i18n pour l'affichage du nom de la phase dans le HUD/journal.
     */
    String nomCle();
}
