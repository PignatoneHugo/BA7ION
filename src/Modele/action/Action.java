package Modele.action;

import Modele.royaume.Royaume;

// Une action planifiee pendant le tour et executee a la resolution.
public interface Action {

    /**
     * Verifie si l'action peut etre executee sur le royaume.
     *
     * @param royaume le royaume concerne
     * @return true si l'action est executable
     */
    boolean estExecutable(Royaume royaume);

    /**
     * Applique l'action sur le royaume.
     *
     * @param royaume le royaume concerne
     */
    void executer(Royaume royaume);

    /**
     * Renvoie l'identifiant texte de l'action pour l'affichage.
     *
     * @return la description de l'action
     */
    String description();
}
