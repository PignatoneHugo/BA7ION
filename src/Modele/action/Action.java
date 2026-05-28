package Modele.action;

import Modele.royaume.Royaume;

/**
 * Decision du joueur (ou d'un bot) en attente d'execution. Implementation
 * du pattern Command : chaque sous-classe encapsule une intention validee
 * en phase de planification puis appliquee lors de la phase de resolution
 * du tour.
 *
 * Cycle de vie :
 * <ol>
 *   <li>Un controleur cree l'action et l'empile dans une {@link FileActions}.</li>
 *   <li>A la resolution du tour, chaque action de la file est testee via
 *       {@link #estExecutable(Royaume)} puis appliquee via
 *       {@link #executer(Royaume)}.</li>
 *   <li>Une action non executable est ignoree silencieusement, jamais propagee
 *       en exception.</li>
 * </ol>
 */
public interface Action {

    /**
     * Verifie que les prerequis sont toujours reunis au moment ou l'action
     * va etre executee (ressources suffisantes, batiment existant, etc.).
     *
     * @param royaume royaume sur lequel l'action s'applique
     * @return {@code true} si l'action peut etre executee dans l'etat courant
     */
    boolean estExecutable(Royaume royaume);

    /**
     * Applique l'effet de l'action sur le royaume cible. Doit etre appele
     * apres {@link #estExecutable(Royaume)} positif. Aucun contrat
     * d'idempotence : appliquer deux fois la meme action consomme deux fois
     * son cout.
     *
     * @param royaume royaume sur lequel l'action s'applique
     */
    void executer(Royaume royaume);

    /**
     * @return cle de traduction d'un libelle court pour l'affichage et le
     *         journal de partie
     */
    String description();
}
