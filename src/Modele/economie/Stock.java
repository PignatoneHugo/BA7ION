package Modele.economie;

// Quantite d'une ressource, bornee entre 0 et une capacite max.
public class Stock {

    private final Ressource ressource;
    private int quantite;
    private int capaciteMax;

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

    // Ajoute des unites (limite par la capacite), renvoie ce qui a ete ajoute.
    public int ajouter(int montant) {
        if (montant < 0) {
            throw new IllegalArgumentException("Utiliser retirer() pour soustraire.");
        }
        int avant = this.quantite;
        this.quantite = Math.min(this.capaciteMax, this.quantite + montant);
        return this.quantite - avant;
    }

    // Retire des unites (sans aller sous 0), renvoie ce qui a ete retire.
    public int retirer(int montant) {
        if (montant < 0) {
            throw new IllegalArgumentException("Utiliser ajouter() pour ajouter.");
        }
        int avant = this.quantite;
        this.quantite = Math.max(0, this.quantite - montant);
        return avant - this.quantite;
    }

    public boolean contient(int montant) {
        return this.quantite >= montant;
    }

    // Force la quantite (utilise au chargement d'une sauvegarde).
    public void definirQuantite(int nouvelleQuantite) {
        this.quantite = Math.max(0, Math.min(nouvelleQuantite, this.capaciteMax));
    }

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
