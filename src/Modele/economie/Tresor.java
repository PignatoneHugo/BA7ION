package Modele.economie;

import java.util.EnumMap;
import java.util.Map;

import config.Equilibrage;

/**
 * Agregat des 5 Stock d'un royaume. Expose des methodes haut-niveau qui
 * delegent au Stock correspondant.
 *
 * Encore une fois : pas d'Observable ici, c'est Royaume qui notifie.
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

    /**
     * Ajoute du montant a la ressource, ecrete a la capacite.
     * @return le montant reellement ajoute
     */
    public int ajouter(Ressource r, int montant) {
        return this.stocks.get(r).ajouter(montant);
    }

    /**
     * Retire du montant a la ressource, s'arrete a 0.
     * @return le montant reellement retire
     */
    public int retirer(Ressource r, int montant) {
        return this.stocks.get(r).retirer(montant);
    }

    public boolean contient(Ressource r, int montant) {
        return this.stocks.get(r).contient(montant);
    }
}
