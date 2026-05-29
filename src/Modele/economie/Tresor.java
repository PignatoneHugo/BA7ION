package Modele.economie;

import java.util.EnumMap;
import java.util.Map;

import config.Equilibrage;

/**
 * Regroupe les 5 Stock d'un royaume.
 * Les valeurs initiales viennent de la table d'equilibrage.
 */
public class Tresor {

    private final Map<Ressource, Stock> stocks;

    public Tresor() {
        this.stocks = new EnumMap<>(Ressource.class);
        for (Ressource r : Ressource.values()) {
            int capacite = Equilibrage.capaciteInitiale(r);
            int initial = Equilibrage.stockInitial(r);
            this.stocks.put(r, new Stock(r, initial, capacite));
        }
    }

    public Stock stock(Ressource r) {
        return this.stocks.get(r);
    }

    public int quantite(Ressource r) {
        return this.stocks.get(r).quantite();
    }

    public int capaciteMax(Ressource r) {
        return this.stocks.get(r).capaciteMax();
    }

    public int ajouter(Ressource r, int montant) {
        return this.stocks.get(r).ajouter(montant);
    }

    public int retirer(Ressource r, int montant) {
        return this.stocks.get(r).retirer(montant);
    }

    public boolean contient(Ressource r, int montant) {
        return this.stocks.get(r).contient(montant);
    }
}
