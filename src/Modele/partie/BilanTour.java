package Modele.partie;

import java.util.EnumMap;
import java.util.Map;

import Modele.economie.Ressource;
import Modele.infrastructure.Batiment;
import Modele.infrastructure.TypeBatiment;
import Modele.royaume.Royaume;

/**
 * Photo des valeurs cles du royaume joueur a un instant donne.
 * Utilise pour calculer le bilan d'un tour (variations entre debut
 * et fin de tour) et l'afficher dans le DialogueFinTour.
 *
 * Le snapshot doit etre pris AU DEBUT du tour, avant que le joueur
 * ait planifie quoi que ce soit -- sinon les couts des ameliorations
 * (deduits a la planification) seraient invisibles dans le bilan.
 */
public class BilanTour {

    private final int numeroTour;
    private final Map<Ressource, Integer> ressources;
    private final int populationTotale;
    private final int effectifArmee;
    private final int moral;
    private final Map<TypeBatiment, Integer> niveauxBatiments;
    private final Map<TypeBatiment, Boolean> chantiersActifs;

    public BilanTour(int numeroTour, Royaume joueur) {
        this.numeroTour = numeroTour;
        this.ressources = new EnumMap<>(Ressource.class);
        for (Ressource r : Ressource.values()) {
            this.ressources.put(r, joueur.tresor().quantite(r));
        }
        this.populationTotale = joueur.population().total();
        this.effectifArmee = joueur.armee().effectifTotal();
        this.moral = joueur.moral().valeur();

        this.niveauxBatiments = new EnumMap<>(TypeBatiment.class);
        this.chantiersActifs = new EnumMap<>(TypeBatiment.class);
        for (Batiment b : joueur.batiments()) {
            this.niveauxBatiments.put(b.type(), b.niveau());
            this.chantiersActifs.put(b.type(), b.enChantier());
        }
    }

    public int numeroTour() {
        return this.numeroTour;
    }

    public int ressource(Ressource r) {
        return this.ressources.getOrDefault(r, 0);
    }

    public int populationTotale() {
        return this.populationTotale;
    }

    public int effectifArmee() {
        return this.effectifArmee;
    }

    public int moral() {
        return this.moral;
    }

    public int niveau(TypeBatiment t) {
        return this.niveauxBatiments.getOrDefault(t, 0);
    }

    public boolean enChantier(TypeBatiment t) {
        return this.chantiersActifs.getOrDefault(t, false);
    }
}
