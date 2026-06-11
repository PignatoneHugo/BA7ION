package Modele.combat;

import Modele.militaire.PostureCombat;
import Modele.royaume.Royaume;

/**
 * Combat planifie entre deux royaumes, en attente de resolution.
 * Stocke par la Partie entre la phase ou il est declenche (ActionAttaquer
 * ou IA des bots) et la phase de resolution (EtatCombatsSubis / Offensifs).
 */
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
