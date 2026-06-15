package Modele.combat;

import Modele.militaire.PostureCombat;
import Modele.royaume.Royaume;

// Un combat prevu entre deux royaumes, pas encore resolu.
public class Bataille {

    private final Royaume attaquant;
    private final Royaume defenseur;
    private final PostureCombat posture;

    public Bataille(Royaume attaquant, Royaume defenseur, PostureCombat posture) {
        if (attaquant == null || defenseur == null) {
            throw new IllegalArgumentException("Attaquant et defenseur requis.");
        }
        this.attaquant = attaquant;
        this.defenseur = defenseur;
        this.posture = posture != null ? posture : PostureCombat.ATTAQUE;
    }

    public Royaume attaquant() {
        return this.attaquant;
    }

    public Royaume defenseur() {
        return this.defenseur;
    }

    public PostureCombat posture() {
        return this.posture;
    }
}
