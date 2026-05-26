package Modele.notification;

/**
 * Categorise les notifications emises par les Observable du modele
 * (Partie, Royaume) vers les vues.
 *
 * Le filtrage dans Vue#update(Observable, Object) se fait via :
 *     if (arg instanceof Notification n {@literal &&} n.type() == TypeNotification.X) { ... }
 *
 * Convention : un seul enum centralise tous les types de notifications
 * pour faciliter le suivi des messages traverses dans l'application.
 */
public enum TypeNotification {

    // --- Cycle de tour ---
    TOUR_DEMARRE,
    PHASE_CHANGEE,
    TOUR_TERMINE,

    // --- Royaume ---
    TRESOR_CHANGE,
    POPULATION_CHANGEE,
    MORAL_CHANGE,
    BATIMENTS_CHANGES,
    FILE_ACTIONS_CHANGEE,

    // --- Evenements ---
    EVENEMENT_EN_ATTENTE,
    EVENEMENT_RESOLU,

    // --- Fin de partie ---
    PARTIE_GAGNEE,
    PARTIE_PERDUE
}
