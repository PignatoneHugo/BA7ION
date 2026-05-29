package Modele.action;

import Modele.royaume.Royaume;

/**
 * Action que le joueur planifie pendant son tour et qui sera executee
 * plus tard pendant la phase de resolution. Pattern Command.
 */
public interface Action {

    /** Verifie si l'action peut etre executee (ressources dispo, etc.). */
    boolean estExecutable(Royaume royaume);

    /** Applique l'effet de l'action sur le royaume. */
    void executer(Royaume royaume);

    /** Cle i18n pour l'affichage dans le journal et l'UI. */
    String description();
}
