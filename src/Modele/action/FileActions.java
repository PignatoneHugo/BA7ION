package Modele.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Modele.royaume.Royaume;

/**
 * File FIFO des {@link Action} planifiees par un royaume durant la phase de
 * planification. Videe et appliquee lors de la phase de resolution.
 *
 * Cette classe ne notifie pas elle-meme les Observers : la mise a jour des
 * vues est de la responsabilite du Royaume qui detient la file, via
 * {@link Royaume#notifierFileActionsChangee()}.
 */
public class FileActions {

    private final List<Action> actions = new ArrayList<>();

    /**
     * @param action action a empiler en fin de file, non null
     * @throws IllegalArgumentException si {@code action} est null
     */
    public void ajouter(Action action) {
        if (action == null) {
            throw new IllegalArgumentException("Une Action ne peut pas etre null.");
        }
        this.actions.add(action);
    }

    /**
     * Retire la premiere occurrence d'une action egale a celle passee.
     *
     * @return {@code true} si une action a effectivement ete retiree
     */
    public boolean retirer(Action action) {
        return this.actions.remove(action);
    }

    public void vider() {
        this.actions.clear();
    }

    public int taille() {
        return this.actions.size();
    }

    public boolean estVide() {
        return this.actions.isEmpty();
    }

    /**
     * @return vue non modifiable du contenu de la file, dans l'ordre FIFO
     */
    public List<Action> contenu() {
        return Collections.unmodifiableList(this.actions);
    }

    /**
     * Execute dans l'ordre toutes les actions executables puis vide la file.
     * Les actions dont {@link Action#estExecutable(Royaume)} retourne
     * {@code false} sont ignorees sans interrompre le traitement.
     *
     * @param royaume royaume sur lequel les actions s'appliquent
     * @return nombre d'actions effectivement executees
     */
    public int executerToutes(Royaume royaume) {
        int compte = 0;
        for (Action action : this.actions) {
            if (action.estExecutable(royaume)) {
                action.executer(royaume);
                compte++;
            }
        }
        this.actions.clear();
        return compte;
    }
}
