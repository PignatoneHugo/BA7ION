package Modele.combat;

import Modele.militaire.PostureCombat;
import Modele.royaume.Royaume;

// Un combat prevu entre deux royaumes, pas encore resolu.
public class Bataille {

    private final Royaume attaquant;
    private final Royaume defenseur;
    private final PostureCombat posture;

    /**
     * Cree une bataille prevue entre deux royaumes.
     *
     * @param attaquant le royaume qui attaque
     * @param defenseur le royaume qui defend
     * @param posture la posture de l'attaquant
     * @throws IllegalArgumentException si l'attaquant ou le defenseur est null
     */
    public Bataille(Royaume attaquant, Royaume defenseur, PostureCombat posture) {
        if (attaquant == null || defenseur == null) {
            throw new IllegalArgumentException("Attaquant et defenseur requis.");
        }
        this.attaquant = attaquant;
        this.defenseur = defenseur;
        this.posture = posture != null ? posture : PostureCombat.ATTAQUE;
    }

    /**
     * Renvoie le royaume attaquant.
     *
     * @return le royaume attaquant
     */
    public Royaume attaquant() {
        return this.attaquant;
    }

    /**
     * Renvoie le royaume defenseur.
     *
     * @return le royaume defenseur
     */
    public Royaume defenseur() {
        return this.defenseur;
    }

    /**
     * Renvoie la posture de l'attaquant.
     *
     * @return la posture de combat
     */
    public PostureCombat posture() {
        return this.posture;
    }
}
