package Modele.action;

import Modele.royaume.Royaume;

/**
 * Pattern Command applique aux actions du joueur (et des bots) : une Action
 * encapsule une intention validee par le controleur durant la phase de
 * planification, et executee plus tard lors de la phase de resolution du tour.
 *
 * Inspire de l'interface Commande du TP10 (Menu/Entree/Commande).
 *
 * Cycle de vie typique :
 *   1. Le controleur cree une Action (ex : ActionConstruire).
 *   2. Il l'empile dans la FileActions du royaume joueur.
 *   3. Pendant EtatActionsDifferees#executer(Partie), chaque Action de la file
 *      est executee dans l'ordre via {@link #executer(Royaume)}.
 *   4. {@link #estExecutable(Royaume)} permet d'ignorer silencieusement les
 *      actions dont les prerequis ne sont plus reunis au moment de l'execution
 *      (ressources insuffisantes, batiment detruit, etc.).
 */
public interface Action {

    /**
     * Verifie si l'action peut etre executee dans l'etat courant du royaume.
     * Appelee juste avant {@link #executer(Royaume)}. Une action non executable
     * doit etre ignoree, pas lever d'exception.
     */
    boolean estExecutable(Royaume royaume);

    /**
     * Applique l'effet de l'action sur le royaume cible.
     * Le contrat n'impose pas l'idempotence : appeler executer deux fois sur
     * la meme Action n'est pas necessairement sur (cf. cout des ressources).
     */
    void executer(Royaume royaume);

    /**
     * Description courte pour le journal et l'IHM. Doit etre internationalisable
     * via le Traducteur ; ici on retourne par defaut une cle i18n.
     */
    String description();
}
