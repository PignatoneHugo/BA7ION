package Modele.economie;

/**
 * Reserve d'une ressource, bornee entre zero et une capacite maximale.
 *
 * Toutes les operations sont saturantes : ajouter au-dela de la capacite
 * ecrete au plafond, retirer plus que disponible s'arrete a zero. Les methodes
 * {@link #ajouter(int)} et {@link #retirer(int)} renvoient toujours la
 * quantite reellement appliquee, ce qui permet a l'appelant de detecter une
 * saturation ou une penurie sans avoir a comparer avant/apres lui-meme.
 */
public class Stock {

    private final Ressource ressource;
    private int quantite;
    private int capaciteMax;

    /**
     * @param ressource ressource representee par ce stock, non null
     * @param quantiteInitiale quantite de depart (sera tronquee a {@code [0, capaciteMax]})
     * @param capaciteMax capacite maximale, doit etre positive ou nulle
     * @throws IllegalArgumentException si {@code ressource} est null ou si
     *                                  {@code capaciteMax} est negative
     */
    public Stock(Ressource ressource, int quantiteInitiale, int capaciteMax) {
        if (ressource == null) {
            throw new IllegalArgumentException("La ressource ne peut pas etre null.");
        }
        if (capaciteMax < 0) {
            throw new IllegalArgumentException("La capacite max doit etre positive.");
        }
        this.ressource = ressource;
        this.capaciteMax = capaciteMax;
        this.quantite = Math.max(0, Math.min(quantiteInitiale, capaciteMax));
    }

    public Ressource ressource() {
        return this.ressource;
    }

    public int quantite() {
        return this.quantite;
    }

    public int capaciteMax() {
        return this.capaciteMax;
    }

    /**
     * Ajoute des unites au stock en ecretant au plafond.
     *
     * @param montant quantite a ajouter, doit etre positive ou nulle
     * @return quantite effectivement ajoutee (peut etre inferieure a {@code montant}
     *         si la capacite est atteinte)
     * @throws IllegalArgumentException si {@code montant} est negatif
     */
    public int ajouter(int montant) {
        if (montant < 0) {
            throw new IllegalArgumentException("Utiliser retirer() pour soustraire.");
        }
        int avant = this.quantite;
        this.quantite = Math.min(this.capaciteMax, this.quantite + montant);
        return this.quantite - avant;
    }

    /**
     * Retire des unites du stock sans descendre en dessous de zero.
     *
     * @param montant quantite a retirer, doit etre positive ou nulle
     * @return quantite effectivement retiree (peut etre inferieure a {@code montant}
     *         en cas de penurie)
     * @throws IllegalArgumentException si {@code montant} est negatif
     */
    public int retirer(int montant) {
        if (montant < 0) {
            throw new IllegalArgumentException("Utiliser ajouter() pour ajouter.");
        }
        int avant = this.quantite;
        this.quantite = Math.max(0, this.quantite - montant);
        return avant - this.quantite;
    }

    /**
     * Teste la disponibilite d'une quantite sans modifier le stock.
     *
     * @param montant quantite a verifier
     * @return {@code true} si le stock contient au moins {@code montant} unites
     */
    public boolean contient(int montant) {
        return this.quantite >= montant;
    }

    /**
     * Modifie la capacite maximale du stock. La quantite courante est tronquee
     * si elle depasse la nouvelle capacite.
     *
     * @param nouvelleCapacite nouveau plafond, doit etre positif ou nul
     * @throws IllegalArgumentException si {@code nouvelleCapacite} est negative
     */
    public void redimensionner(int nouvelleCapacite) {
        if (nouvelleCapacite < 0) {
            throw new IllegalArgumentException("La capacite max doit etre positive.");
        }
        this.capaciteMax = nouvelleCapacite;
        if (this.quantite > nouvelleCapacite) {
            this.quantite = nouvelleCapacite;
        }
    }

    @Override
    public String toString() {
        return this.ressource + ": " + this.quantite + "/" + this.capaciteMax;
    }
}
