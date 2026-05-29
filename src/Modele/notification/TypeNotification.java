package Modele.notification;

/**
 * Liste des differents types d'evenements que le modele peut envoyer aux vues.
 * Les vues utilisent ce type pour savoir si elles doivent se rafraichir ou non.
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

    // Evenements
    EVENEMENT_EN_ATTENTE,
    EVENEMENT_RESOLU,

    // Fin de partie
    PARTIE_GAGNEE,
    PARTIE_PERDUE
}
