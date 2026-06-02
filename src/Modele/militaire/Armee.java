package Modele.militaire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Armee d'un royaume : liste d'Unite avec une posture de combat.
 */
public class Armee {

    private final List<Unite> unites;
    private PostureCombat posture;

    public Armee() {
        this(PostureCombat.DEFENSE);
    }

    public Armee(PostureCombat posture) {
        this.unites = new ArrayList<>();
        this.posture = posture != null ? posture : PostureCombat.DEFENSE;
    }

    public void ajouterUnite(Unite u) {
        if (u == null) {
            throw new IllegalArgumentException("Une Unite ne peut pas etre null.");
        }
        this.unites.add(u);
    }

    public List<Unite> unites() {
        return Collections.unmodifiableList(this.unites);
    }

    public PostureCombat posture() {
        return this.posture;
    }

    public void definirPosture(PostureCombat posture) {
        if (posture != null) {
            this.posture = posture;
        }
    }

    /** Somme des effectifs de toutes les unites. */
    public int effectifTotal() {
        int total = 0;
        for (Unite u : this.unites) {
            total += u.effectif();
        }
        return total;
    }

    public boolean estVide() {
        return effectifTotal() == 0;
    }
}
