package Modele.persistance;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import Modele.economie.NiveauTaxes;
import Modele.economie.Ressource;
import Modele.infrastructure.Batiment;
import Modele.infrastructure.TypeBatiment;
import Modele.militaire.PostureCombat;
import Modele.militaire.TypeUnite;
import Modele.population.Role;
import Modele.royaume.Royaume;

/**
 * Etat sauvegarde d'un royaume (ressources, population, batiments, armee...).
 * Simple objet de donnees, serialise par Gson.
 */
public class EtatRoyaume {

    public final String nom;
    public final boolean estBot;
    public final int moral;
    public final int capaciteLogement;
    public final String niveauTaxes;
    public final String posture;
    public final Map<Ressource, Integer> ressources;
    public final Map<Role, Integer> populationParRole;
    public final Map<TypeBatiment, Integer> niveauxBatiments;
    public final Map<TypeBatiment, Integer> chantiersBatiments;
    public final Map<TypeUnite, Integer> armee;

    /**
     * Capture l'etat courant d'un royaume.
     *
     * @param royaume le royaume a sauvegarder
     */
    public EtatRoyaume(Royaume royaume) {
        this.nom = royaume.nom();
        this.estBot = royaume.estBot();
        this.moral = royaume.moral().valeur();
        this.capaciteLogement = royaume.population().capaciteLogement();
        this.niveauTaxes = royaume.niveauTaxes().name();
        this.posture = royaume.armee().posture().name();

        this.ressources = new LinkedHashMap<>();
        for (Ressource ressource : Ressource.values()) {
            this.ressources.put(ressource, royaume.tresor().quantite(ressource));
        }
        this.populationParRole = new LinkedHashMap<>();
        for (Role role : Role.values()) {
            this.populationParRole.put(role, royaume.population().effectif(role));
        }
        this.niveauxBatiments = new LinkedHashMap<>();
        this.chantiersBatiments = new LinkedHashMap<>();
        for (Batiment batiment : royaume.batiments()) {
            this.niveauxBatiments.put(batiment.type(), batiment.niveau());
            this.chantiersBatiments.put(batiment.type(), batiment.toursRestants());
        }
        this.armee = new LinkedHashMap<>();
        for (TypeUnite typeUnite : TypeUnite.values()) {
            int effectif = royaume.armee().effectifParType(typeUnite);
            if (effectif > 0) {
                this.armee.put(typeUnite, effectif);
            }
        }
    }

    /**
     * Recharge un royaume neuf avec les valeurs sauvegardees.
     *
     * @param royaume le royaume a restaurer
     */
    public void appliquerA(Royaume royaume) {
        Map<Ressource, Integer> res = orVide(ressources);
        for (Ressource ressource : Ressource.values()) {
            royaume.tresor().definirQuantite(ressource, res.getOrDefault(ressource, 0));
        }
        Map<Role, Integer> pop = orVide(populationParRole);
        for (Role role : Role.values()) {
            royaume.population().definirEffectif(role, pop.getOrDefault(role, 0));
        }
        if (capaciteLogement > 0) {
            royaume.population().definirCapaciteLogement(capaciteLogement);
        }
        if (moral > 0) {
            royaume.moral().definir(moral);
        }
        if (niveauTaxes != null) {
            try {
                royaume.definirNiveauTaxes(NiveauTaxes.valueOf(niveauTaxes));
            } catch (IllegalArgumentException ignore) {
                // Niveau inconnu : on garde la valeur par defaut.
            }
        }
        Map<TypeBatiment, Integer> niv = orVide(niveauxBatiments);
        Map<TypeBatiment, Integer> chan = orVide(chantiersBatiments);
        for (Batiment batiment : royaume.batiments()) {
            batiment.restaurer(niv.getOrDefault(batiment.type(), batiment.niveau()),
                    chan.getOrDefault(batiment.type(), 0));
        }
        if (posture != null) {
            try {
                royaume.armee().definirPosture(PostureCombat.valueOf(posture));
            } catch (IllegalArgumentException ignore) {
                // Posture inconnue : on garde la valeur par defaut.
            }
        }
        Map<TypeUnite, Integer> arm = orVide(armee);
        for (TypeUnite typeUnite : TypeUnite.values()) {
            int effectif = arm.getOrDefault(typeUnite, 0);
            if (effectif > 0) {
                royaume.armee().recruter(typeUnite, effectif);
            }
        }
    }

    private static <K> Map<K, Integer> orVide(Map<K, Integer> map) {
        return map != null ? map : Collections.emptyMap();
    }
}
