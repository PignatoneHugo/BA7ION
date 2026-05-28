package Modele.notification;

/**
 * Categories d'evenements qu'un Observable du modele peut emettre vers ses
 * Observers. Permet aux vues de filtrer rapidement les notifications a traiter
 * sans avoir a inspecter le contenu de la donnee transportee.
 *
 * @see Notification
 */
public enum TypeNotification {

    // Cycle de tour
    TOUR_DEMARRE,
    PHASE_CHANGEE,
    TOUR_TERMINE,

    // Etat d'un royaume
    TRESOR_CHANGE,
    POPULATION_CHANGEE,
    MORAL_CHANGE,
    BATIMENTS_CHANGES,
    FILE_ACTIONS_CHANGEE,

    // Evenements aleatoires
    EVENEMENT_EN_ATTENTE,
    EVENEMENT_RESOLU,

    // Conditions de fin
    PARTIE_GAGNEE,
    PARTIE_PERDUE
}
