package Modele.population;

import java.util.EnumMap;
import java.util.Map;

import config.Equilibrage;

/**
 * Population d'un royaume : repartition d'habitants par role,
 * limitee par une capacite de logement.
 */
public class Population {

    private final Map<Role, Integer> effectifs = new EnumMap<>(Role.class);
    private int capaciteLogement;

    public Population() {
        this(Equilibrage.POPULATION_INITIALE, Equilibrage.CAPACITE_LOGEMENT_INITIALE);
    }

    public Population(int totalInitial, int capaciteLogement) {
        for (Role r : Role.values()) {
            this.effectifs.put(r, 0);
        }
        this.effectifs.put(Role.INACTIF, totalInitial);
        this.capaciteLogement = capaciteLogement;
    }

    /** Somme des effectifs de tous les roles. */
    public int total() {
        int t = 0;
        for (int n : this.effectifs.values()) {
            t += n;
        }
        return t;
    }

    public int effectif(Role role) {
        return this.effectifs.get(role);
    }

    public int capaciteLogement() {
        return this.capaciteLogement;
    }

    public void definirCapaciteLogement(int nouvelle) {
        if (nouvelle < 0) {
            throw new IllegalArgumentException("La capacite de logement doit etre positive.");
        }
        this.capaciteLogement = nouvelle;
    }

    /**
     * Deplace des habitants d'un role vers un autre.
     * Retourne false si le role source n'a pas assez d'habitants.
     */
    public boolean reaffecter(Role source, Role cible, int montant) {
        if (montant <= 0) {
            return false;
        }
        if (this.effectifs.get(source) < montant) {
            return false;
        }
        this.effectifs.merge(source, -montant, Integer::sum);
        this.effectifs.merge(cible, montant, Integer::sum);
        return true;
    }

    /**
     * Ajoute des inactifs (croissance demographique).
     * Le surplus au-dela de la capacite est ignore.
     */
    public int ajouterInactifs(int montant) {
        if (montant <= 0) {
            return 0;
        }
        int marge = this.capaciteLogement - this.total();
        int ajoute = Math.max(0, Math.min(montant, marge));
        this.effectifs.merge(Role.INACTIF, ajoute, Integer::sum);
        return ajoute;
    }

    /**
     * Retire des habitants (famine, etc.). On retire d'abord les inactifs,
     * puis les autres roles dans l'ordre de l'enum.
     */
    public int retirerHabitants(int montant) {
        if (montant <= 0) {
            return 0;
        }
        int total = this.total();
        if (total == 0) {
            return 0;
        }
        int aRetirer = Math.min(montant, total);
        int restant = aRetirer;

        int inactifs = this.effectifs.get(Role.INACTIF);
        int retraitInactifs = Math.min(restant, inactifs);
        this.effectifs.merge(Role.INACTIF, -retraitInactifs, Integer::sum);
        restant -= retraitInactifs;

        if (restant > 0) {
            for (Role r : Role.values()) {
                if (r == Role.INACTIF) {
                    continue;
                }
                if (restant == 0) {
                    break;
                }
                int n = this.effectifs.get(r);
                int retrait = Math.min(restant, n);
                this.effectifs.merge(r, -retrait, Integer::sum);
                restant -= retrait;
            }
        }
        return aRetirer - restant;
    }
}
