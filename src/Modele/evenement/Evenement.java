package Modele.evenement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe abstraite parente des evenements aleatoires. Un evenement a un
 * titre, une description et plusieurs choix possibles. Le joueur doit en
 * selectionner un pour que la partie continue.
 */
public abstract class Evenement {

    private final String cleTitre;
    private final String cleDescription;
    private final List<Choix> choix;

    protected Evenement(String cleTitre, String cleDescription) {
        this.cleTitre = cleTitre;
        this.cleDescription = cleDescription;
        this.choix = new ArrayList<>();
    }

    /** A appeler dans le constructeur des sous-classes pour ajouter les choix. */
    protected void ajouterChoix(Choix c) {
        if (c != null) {
            this.choix.add(c);
        }
    }

    public String cleTitre() {
        return this.cleTitre;
    }

    public String cleDescription() {
        return this.cleDescription;
    }

    public List<Choix> choix() {
        return Collections.unmodifiableList(this.choix);
    }
}
