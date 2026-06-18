package Modele.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Modele.royaume.Royaume;

// File des actions planifiees par un royaume, jouees a la resolution.
public class FileActions {

    private final List<Action> actions = new ArrayList<>();

    /**
     * Ajoute une action a la file.
     *
     * @param action l'action a ajouter
     * @throws IllegalArgumentException si l'action est null
     */
    public void ajouter(Action action) {
        if (action == null) {
            throw new IllegalArgumentException("Une Action ne peut pas etre null.");
        }
        this.actions.add(action);
    }

    /**
     * Retire une action de la file.
     *
     * @param action l'action a retirer
     * @return true si l'action etait dans la file
     */
    public boolean retirer(Action action) {
        return this.actions.remove(action);
    }

    /**
     * Vide la file de toutes ses actions.
     */
    public void vider() {
        this.actions.clear();
    }

    /**
     * Renvoie le nombre d'actions dans la file.
     *
     * @return la taille de la file
     */
    public int taille() {
        return this.actions.size();
    }

    /**
     * Indique si la file ne contient aucune action.
     *
     * @return true si la file est vide
     */
    public boolean estVide() {
        return this.actions.isEmpty();
    }

    /**
     * Renvoie le contenu de la file pour l'affichage.
     *
     * @return la liste non modifiable des actions
     */
    public List<Action> contenu() {
        return Collections.unmodifiableList(this.actions);
    }

    /**
     * Execute dans l'ordre toutes les actions executables puis vide la file.
     *
     * @param royaume le royaume concerne
     * @return le nombre d'actions reellement executees
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
