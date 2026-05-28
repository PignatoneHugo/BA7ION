package Modele.economie;

import java.util.EnumMap;
import java.util.Map;

import config.Equilibrage;

/**
 * Tresor d'un royaume : regroupe un {@link Stock} par {@link Ressource} et
 * expose des operations agregees qui delegent au stock concerne. Les
 * quantites et capacites initiales sont lues dans {@link Equilibrage}.
 *
 * Cette classe ne notifie pas elle-meme les Observers : c'est le Royaume qui
 * la contient qui emet une notification {@code TRESOR_CHANGE} apres chaque
 * operation cumulee coherente.
 */
public class Tresor {

    private final Map<Ressource, Stock> stocks;

    /**
     * Construit un tresor initialise avec les valeurs par defaut definies
     * dans la table d'equilibrage.
     */
    public Tresor() {
        this.stocks = new EnumMap<>(Ressource.class);
        for (Ressource r : Ressource.values()) {
            int capacite = Equilibrage.capaciteInitiale(r);
            int initial = Equilibrage.stockInitial(r);
            this.stocks.put(r, new Stock(r, initial, capacite));
        }
    }

    /**
     * @param r ressource recherchee
     * @return le stock associe (jamais null pour une ressource du catalogue)
     */
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
     * Ajoute des unites a la ressource demandee, ecretees a sa capacite.
     *
     * @return quantite effectivement ajoutee
     * @see Stock#ajouter(int)
     */
    public int ajouter(Ressource r, int montant) {
        return this.stocks.get(r).ajouter(montant);
    }

    /**
     * Retire des unites de la ressource demandee, sans descendre sous zero.
     *
     * @return quantite effectivement retiree
     * @see Stock#retirer(int)
     */
    public int retirer(Ressource r, int montant) {
        return this.stocks.get(r).retirer(montant);
    }

    /**
     * Teste si la ressource possede au moins la quantite demandee.
     */
    public boolean contient(Ressource r, int montant) {
        return this.stocks.get(r).contient(montant);
    }
}
