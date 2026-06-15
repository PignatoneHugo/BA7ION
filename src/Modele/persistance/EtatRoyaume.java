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

    /** Capture l'etat courant d'un royaume. */
    public EtatRoyaume(Royaume r) {
        this.nom = r.nom();
        this.estBot = r.estBot();
        this.moral = r.moral().valeur();
        this.capaciteLogement = r.population().capaciteLogement();
        this.niveauTaxes = r.niveauTaxes().name();
        this.posture = r.armee().posture().name();

        this.ressources = new LinkedHashMap<>();
        for (Ressource res : Ressource.values()) {
            this.ressources.put(res, r.tresor().quantite(res));
        }
        this.populationParRole = new LinkedHashMap<>();
        for (Role role : Role.values()) {
            this.populationParRole.put(role, r.population().effectif(role));
        }
        this.niveauxBatiments = new LinkedHashMap<>();
        this.chantiersBatiments = new LinkedHashMap<>();
        for (Batiment b : r.batiments()) {
            this.niveauxBatiments.put(b.type(), b.niveau());
            this.chantiersBatiments.put(b.type(), b.toursRestants());
        }
        this.armee = new LinkedHashMap<>();
        for (TypeUnite tu : TypeUnite.values()) {
            int n = r.armee().effectifParType(tu);
            if (n > 0) {
                this.armee.put(tu, n);
            }
        }
    }

    /** Recharge un royaume neuf avec les valeurs sauvegardees. */
    public void appliquerA(Royaume r) {
        Map<Ressource, Integer> res = orVide(ressources);
        for (Ressource x : Ressource.values()) {
            r.tresor().definirQuantite(x, res.getOrDefault(x, 0));
        }
        Map<Role, Integer> pop = orVide(populationParRole);
        for (Role role : Role.values()) {
            r.population().definirEffectif(role, pop.getOrDefault(role, 0));
        }
        if (capaciteLogement > 0) {
            r.population().definirCapaciteLogement(capaciteLogement);
        }
        if (moral > 0) {
            r.moral().definir(moral);
        }
        if (niveauTaxes != null) {
            try {
                r.definirNiveauTaxes(NiveauTaxes.valueOf(niveauTaxes));
            } catch (IllegalArgumentException ignore) {
                // Niveau inconnu : on garde la valeur par defaut.
            }
        }
        Map<TypeBatiment, Integer> niv = orVide(niveauxBatiments);
        Map<TypeBatiment, Integer> chan = orVide(chantiersBatiments);
        for (Batiment b : r.batiments()) {
            b.restaurer(niv.getOrDefault(b.type(), b.niveau()),
                    chan.getOrDefault(b.type(), 0));
        }
        if (posture != null) {
            try {
                r.armee().definirPosture(PostureCombat.valueOf(posture));
            } catch (IllegalArgumentException ignore) {
                // Posture inconnue : on garde la valeur par defaut.
            }
        }
        Map<TypeUnite, Integer> arm = orVide(armee);
        for (TypeUnite tu : TypeUnite.values()) {
            int n = arm.getOrDefault(tu, 0);
            if (n > 0) {
                r.armee().recruter(tu, n);
            }
        }
    }

    private static <K> Map<K, Integer> orVide(Map<K, Integer> m) {
        return m != null ? m : Collections.emptyMap();
    }
}
