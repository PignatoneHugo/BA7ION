package Modele.militaire;

/**
 * Groupe d'unites d'un meme type, caracterise par un effectif.
 */
public class Unite {

    private final TypeUnite type;
    private int effectif;

    public Unite(TypeUnite type, int effectif) {
        if (type == null) {
            throw new IllegalArgumentException("Le type d'unite ne peut pas etre null.");
        }
        if (effectif < 0) {
            throw new IllegalArgumentException("L'effectif doit etre positif ou nul.");
        }
        this.type = type;
        this.effectif = effectif;
    }

    public TypeUnite type() {
        return this.type;
    }

    public int effectif() {
        return this.effectif;
    }

    /** Retire un nombre d'unites, s'arrete a 0. Retourne ce qui a ete retire. */
    public int subirPertes(int pertes) {
        if (pertes < 0) {
            throw new IllegalArgumentException("Les pertes doivent etre positives.");
        }
        int avant = this.effectif;
        this.effectif = Math.max(0, this.effectif - pertes);
        return avant - this.effectif;
    }

    /** Ajoute des soldats du meme type a cette unite. */
    public void renforcer(int nombre) {
        if (nombre < 0) {
            throw new IllegalArgumentException("Le renfort doit etre positif ou nul.");
        }
        this.effectif += nombre;
    }
}
