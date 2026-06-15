package Modele.action;

import Modele.royaume.Royaume;

// Une action planifiee pendant le tour et executee a la resolution.
public interface Action {

    // Verifie si l'action peut etre executee.
    boolean estExecutable(Royaume royaume);

    // Applique l'action sur le royaume.
    void executer(Royaume royaume);

    // Identifiant texte pour l'affichage.
    String description();
}
