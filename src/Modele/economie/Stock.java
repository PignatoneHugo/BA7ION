package Modele.economie;

// Quantite d'une ressource, bornee entre 0 et une capacite max.
public class Stock {

    private final Ressource ressource;
    private int quantite;
    private int capaciteMax;

    /**
     * Cree un stock pour une ressource, avec une quantite et une capacite max.
     *
     * @param ressource la ressource concernee
     * @param quantiteInitiale la quantite de depart
     * @param capaciteMax la capacite maximale du stock
     * @throws IllegalArgumentException si la ressource est null ou la capacite negative
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

    /**
     * Donne la ressource de ce stock.
     *
     * @return la ressource
     */
    public Ressource ressource() {
        return this.ressource;
    }

    /**
     * Donne la quantite actuellement stockee.
     *
     * @return la quantite courante
     */
    public int quantite() {
        return this.quantite;
    }

    /**
     * Donne la capacite maximale du stock.
     *
     * @return la capacite max
     */
    public int capaciteMax() {
        return this.capaciteMax;
    }

    /**
     * Ajoute des unites au stock dans la limite de la capacite.
     *
     * @param montant le nombre d'unites a ajouter
     * @return le nombre d'unites reellement ajoutees
     * @throws IllegalArgumentException si le montant est negatif
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
     * Retire des unites du stock sans descendre sous zero.
     *
     * @param montant le nombre d'unites a retirer
     * @return le nombre d'unites reellement retirees
     * @throws IllegalArgumentException si le montant est negatif
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
     * Indique si le stock contient au moins le montant demande.
     *
     * @param montant le montant a verifier
     * @return true si le stock contient assez d'unites
     */
    public boolean contient(int montant) {
        return this.quantite >= montant;
    }

    /**
     * Force la quantite du stock, utilise au chargement d'une sauvegarde.
     *
     * @param nouvelleQuantite la quantite a fixer
     */
    public void definirQuantite(int nouvelleQuantite) {
        this.quantite = Math.max(0, Math.min(nouvelleQuantite, this.capaciteMax));
    }

    /**
     * Change la capacite maximale du stock et ajuste la quantite si besoin.
     *
     * @param nouvelleCapacite la nouvelle capacite max
     * @throws IllegalArgumentException si la capacite est negative
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

    /**
     * Donne une representation texte du stock.
     *
     * @return le texte ressource: quantite/capacite
     */
    @Override
    public String toString() {
        return this.ressource + ": " + this.quantite + "/" + this.capaciteMax;
    }
}
