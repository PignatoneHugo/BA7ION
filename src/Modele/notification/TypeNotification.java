package Modele.notification;

/** Types de notifications que le modele envoie aux vues. */
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

    // Evenements
    EVENEMENT_EN_ATTENTE,
    EVENEMENT_RESOLU,

    // Fin de partie
    PARTIE_GAGNEE,
    PARTIE_PERDUE
}
