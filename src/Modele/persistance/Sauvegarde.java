package Modele.persistance;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import Modele.economie.Ressource;
import Modele.infrastructure.Batiment;
import Modele.infrastructure.TypeBatiment;
import Modele.partie.Partie;
import Modele.population.Role;
import Modele.royaume.Royaume;

/**
 * DTO serialisable representant l'etat d'une partie a un instant T.
 * On ne serialise pas le graphe d'objets vivants (qui contient des
 * Observable et des Observer Swing) mais une representation plate.
 *
 * Au Sprint 3, on ne sauvegarde que les donnees du joueur ; le restaurer
 * dans une nouvelle Partie suffit pour reprendre une partie solo.
 */
public class Sauvegarde implements Serializable {

    private static final long serialVersionUID = 1L;

    public final String nomJoueur;
    public final int numeroTour;
    public final Map<Ressource, Integer> ressources;
    public final Map<Role, Integer> populationParRole;
    public final int moral;
    public final int capaciteLogement;
    public final String niveauTaxes;
    public final Map<TypeBatiment, Integer> niveauxBatiments;
    public final Map<TypeBatiment, Integer> chantiersBatiments;
    public final long graineAleatoire;

    public Sauvegarde(Partie partie) {
        Royaume j = partie.joueur();
        this.nomJoueur = j.nom();
        this.numeroTour = partie.numeroTour();
        this.moral = j.moral().valeur();
        this.capaciteLogement = j.population().capaciteLogement();
        this.niveauTaxes = j.niveauTaxes().name();
        this.graineAleatoire = partie.aleatoire().nextLong();

        this.ressources = new HashMap<>();
        for (Ressource r : Ressource.values()) {
            this.ressources.put(r, j.tresor().quantite(r));
        }

        this.populationParRole = new HashMap<>();
        for (Role r : Role.values()) {
            this.populationParRole.put(r, j.population().effectif(r));
        }

        this.niveauxBatiments = new HashMap<>();
        this.chantiersBatiments = new HashMap<>();
        for (Batiment b : j.batiments()) {
            this.niveauxBatiments.put(b.type(), b.niveau());
            this.chantiersBatiments.put(b.type(), b.toursRestants());
        }
    }
}
