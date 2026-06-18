package Modele.economie;

import java.util.EnumMap;
import java.util.Map;

import config.Equilibrage;

// Regroupe les 5 stocks d'un royaume.
public class Tresor {

    private final Map<Ressource, Stock> stocks;

    /**
     * Cree un tresor avec un stock par ressource, aux valeurs initiales.
     */
    public Tresor() {
        this.stocks = new EnumMap<>(Ressource.class);
        for (Ressource ressource : Ressource.values()) {
            int capacite = Equilibrage.capaciteInitiale(ressource);
            int initial = Equilibrage.stockInitial(ressource);
            this.stocks.put(ressource, new Stock(ressource, initial, capacite));
        }
    }

    /**
     * Donne le stock d'une ressource.
     *
     * @param ressource la ressource voulue
     * @return le stock correspondant
     */
    public Stock stock(Ressource ressource) {
        return this.stocks.get(ressource);
    }

    /**
     * Donne la quantite stockee d'une ressource.
     *
     * @param ressource la ressource voulue
     * @return la quantite courante
     */
    public int quantite(Ressource ressource) {
        return this.stocks.get(ressource).quantite();
    }

    /**
     * Donne la capacite max du stock d'une ressource.
     *
     * @param ressource la ressource voulue
     * @return la capacite max
     */
    public int capaciteMax(Ressource ressource) {
        return this.stocks.get(ressource).capaciteMax();
    }

    /**
     * Ajoute des unites au stock d'une ressource.
     *
     * @param ressource la ressource voulue
     * @param montant le montant a ajouter
     * @return le nombre d'unites reellement ajoutees
     */
    public int ajouter(Ressource ressource, int montant) {
        return this.stocks.get(ressource).ajouter(montant);
    }

    /**
     * Force la quantite d'une ressource, utilise au chargement d'une sauvegarde.
     *
     * @param ressource la ressource voulue
     * @param quantite la quantite a fixer
     */
    public void definirQuantite(Ressource ressource, int quantite) {
        this.stocks.get(ressource).definirQuantite(quantite);
    }

    /**
     * Retire des unites du stock d'une ressource.
     *
     * @param ressource la ressource voulue
     * @param montant le montant a retirer
     * @return le nombre d'unites reellement retirees
     */
    public int retirer(Ressource ressource, int montant) {
        return this.stocks.get(ressource).retirer(montant);
    }

    /**
     * Indique si le stock d'une ressource contient au moins le montant demande.
     *
     * @param ressource la ressource voulue
     * @param montant le montant a verifier
     * @return true si le stock contient assez d'unites
     */
    public boolean contient(Ressource ressource, int montant) {
        return this.stocks.get(ressource).contient(montant);
    }
}
