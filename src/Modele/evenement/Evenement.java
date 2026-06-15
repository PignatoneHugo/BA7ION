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
    protected void ajouterChoix(Choix c) {
        if (c != null) {
            this.choix.add(c);
        }
    }

    public String titre() {
        return this.titre;
    }

    public String description() {
        return this.description;
    }

    public List<Choix> choix() {
        return Collections.unmodifiableList(this.choix);
    }
}
