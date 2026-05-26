package Modele.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Modele.royaume.Royaume;

/**
 * File d'attente FIFO des Actions planifiees par le joueur durant la phase de
 * planification. Videe (et executee) durant EtatActionsDifferees.
 *
 * Cette classe ne notifie pas elle-meme les Observers ; c'est le Royaume qui
 * contient cette file qui emet la notification FILE_ACTIONS_CHANGEE apres
 * chaque modification.
 */
public class FileActions {

    private final List<Action> actions = new ArrayList<>();

    public void ajouter(Action action) {
        if (action == null) {
            throw new IllegalArgumentException("Une Action ne peut pas etre null.");
        }
        this.actions.add(action);
    }

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
     * Vue non-modifiable sur la file (pour l'affichage).
     */
    public List<Action> contenu() {
        return Collections.unmodifiableList(this.actions);
    }

    /**
     * Execute toutes les actions executables dans l'ordre puis vide la file.
     * Les actions non executables sont silencieusement ignorees.
     *
     * @return le nombre d'actions effectivement executees
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
