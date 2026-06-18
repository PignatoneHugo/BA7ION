package Modele.partie;

import java.util.EnumMap;
import java.util.Map;

import Modele.economie.Ressource;
import Modele.infrastructure.Batiment;
import Modele.infrastructure.TypeBatiment;
import Modele.royaume.Royaume;

// Photo du royaume joueur a un instant, pour comparer debut et fin de tour.
// A prendre en debut de tour, avant que le joueur planifie quoi que ce soit.
public class BilanTour {

    private final int numeroTour;
    private final Map<Ressource, Integer> ressources;
    private final int populationTotale;
    private final int effectifArmee;
    private final int moral;
    private final Map<TypeBatiment, Integer> niveauxBatiments;
    private final Map<TypeBatiment, Boolean> chantiersActifs;

    /**
     * Prend une photo de l'etat du royaume du joueur au debut d'un tour.
     *
     * @param numeroTour le numero du tour photographie
     * @param joueur le royaume du joueur a photographier
     */
    public BilanTour(int numeroTour, Royaume joueur) {
        this.numeroTour = numeroTour;
        this.ressources = new EnumMap<>(Ressource.class);
        for (Ressource ressource : Ressource.values()) {
            this.ressources.put(ressource, joueur.tresor().quantite(ressource));
        }
        this.populationTotale = joueur.population().total();
        this.effectifArmee = joueur.armee().effectifTotal();
        this.moral = joueur.moral().valeur();

        this.niveauxBatiments = new EnumMap<>(TypeBatiment.class);
        this.chantiersActifs = new EnumMap<>(TypeBatiment.class);
        for (Batiment batiment : joueur.batiments()) {
            this.niveauxBatiments.put(batiment.type(), batiment.niveau());
            this.chantiersActifs.put(batiment.type(), batiment.enChantier());
        }
    }

    /**
     * Renvoie le numero du tour photographie.
     *
     * @return le numero du tour
     */
    public int numeroTour() {
        return this.numeroTour;
    }

    /**
     * Renvoie la quantite d'une ressource au moment de la photo.
     *
     * @param ressource la ressource concernee
     * @return la quantite stockee de cette ressource
     */
    public int ressource(Ressource ressource) {
        return this.ressources.getOrDefault(ressource, 0);
    }

    /**
     * Renvoie la population totale au moment de la photo.
     *
     * @return la population totale
     */
    public int populationTotale() {
        return this.populationTotale;
    }

    /**
     * Renvoie l'effectif de l'armee au moment de la photo.
     *
     * @return l'effectif total de l'armee
     */
    public int effectifArmee() {
        return this.effectifArmee;
    }

    /**
     * Renvoie le moral au moment de la photo.
     *
     * @return la valeur du moral
     */
    public int moral() {
        return this.moral;
    }

    /**
     * Renvoie le niveau d'un type de batiment au moment de la photo.
     *
     * @param type le type de batiment concerne
     * @return le niveau du batiment, ou 0 s'il est absent
     */
    public int niveau(TypeBatiment type) {
        return this.niveauxBatiments.getOrDefault(type, 0);
    }

    /**
     * Indique si un type de batiment etait en chantier au moment de la photo.
     *
     * @param type le type de batiment concerne
     * @return true si ce batiment etait en chantier
     */
    public boolean enChantier(TypeBatiment type) {
        return this.chantiersActifs.getOrDefault(type, false);
    }
}
