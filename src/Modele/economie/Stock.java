package Modele.economie;

/**
 * Quantite stockee pour une ressource, bornee par une capacite maximale.
 *
 * Cette classe n'est volontairement pas un Observable : c'est le Royaume qui
 * contient ses Stock qui notifie l'unique evenement TRESOR_CHANGE apres chaque
 * action atomique (cf. regle d'or "un seul Observable par agregat metier").
 *
 * Toute production excedentaire est ecretee a la capacite ; l'appelant peut
 * recuperer la quantite reellement ajoutee via la valeur de retour pour
 * journaliser les pertes (cf. US-ECO-15 et US-ECO-16).
 */
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

    /**
     * Ajoute la quantite demandee, en ecretant a la capacite max.
     * @return la quantite reellement ajoutee (peut etre inferieure a {@code montant})
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
     * Retire la quantite demandee, en s'arretant a 0.
     * @return la quantite reellement retiree (peut etre inferieure a {@code montant})
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
     * Verifie si le stock contient au moins {@code montant} unites.
     * Utilise par {@link Modele.action.Action#estExecutable} pour valider
     * les couts avant execution.
     */
    public boolean contient(int montant) {
        return this.quantite >= montant;
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
