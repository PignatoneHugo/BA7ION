package Modele.militaire;

// Un groupe de soldats du meme type avec son effectif.
public class Unite {

    private final TypeUnite type;
    private int effectif;

    /**
     * Cree une unite d'un type et d'un effectif donnes.
     *
     * @param type le type de l'unite
     * @param effectif le nombre de soldats au depart
     * @throws IllegalArgumentException si le type est null ou si l'effectif est negatif
     */
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

    /**
     * Renvoie le type de l'unite.
     *
     * @return le type de l'unite
     */
    public TypeUnite type() {
        return this.type;
    }

    /**
     * Renvoie l'effectif actuel de l'unite.
     *
     * @return le nombre de soldats
     */
    public int effectif() {
        return this.effectif;
    }

    /**
     * Enleve des soldats a l'unite sans descendre sous zero.
     *
     * @param pertes le nombre de soldats a enlever
     * @return le nombre de soldats reellement enleves
     * @throws IllegalArgumentException si les pertes sont negatives
     */
    public int subirPertes(int pertes) {
        if (pertes < 0) {
            throw new IllegalArgumentException("Les pertes doivent etre positives.");
        }
        int avant = this.effectif;
        this.effectif = Math.max(0, this.effectif - pertes);
        return avant - this.effectif;
    }

    /**
     * Ajoute des soldats a l'unite.
     *
     * @param nombre le nombre de soldats a ajouter
     * @throws IllegalArgumentException si le nombre est negatif
     */
    public void renforcer(int nombre) {
        if (nombre < 0) {
            throw new IllegalArgumentException("Le renfort doit etre positif ou nul.");
        }
        this.effectif += nombre;
    }
}
