package Modele.evenement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Parent des evenements aleatoires : un titre, une description et des choix. */
public abstract class Evenement {

    private final String titre;
    private final String description;
    private final List<Choix> choix;

    protected Evenement(String titre, String description) {
        this.titre = titre;
        this.description = description;
        this.choix = new ArrayList<>();
    }

    // a appeler dans le constructeur des sous-classes
    protected void ajouterChoix(Choix nouveauChoix) {
        if (nouveauChoix != null) {
            this.choix.add(nouveauChoix);
        }
    }

    /**
     * Donne le titre de l'evenement.
     *
     * @return le titre
     */
    public String titre() {
        return this.titre;
    }

    /**
     * Donne la description de l'evenement.
     *
     * @return la description
     */
    public String description() {
        return this.description;
    }

    /**
     * Donne la liste des choix proposes au joueur.
     *
     * @return la liste des choix (non modifiable)
     */
    public List<Choix> choix() {
        return Collections.unmodifiableList(this.choix);
    }
}
