package Modele.population;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

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
     * Retire des habitants (famine, evenements, etc.) en tirant chaque
     * victime au hasard parmi la population totale. Chaque habitant a la
     * meme probabilite d'etre choisi, quelle que soit son role : un fermier
     * a autant de chances qu'un inactif.
     *
     * @param montant nombre d'habitants a retirer
     * @param aleatoire generateur aleatoire (typiquement celui de la Partie,
     *                  seedable pour reproductibilite)
     * @return le nombre d'habitants effectivement retires
     */
    public int retirerHabitants(int montant, Random aleatoire) {
        if (montant <= 0 || aleatoire == null) {
            return 0;
        }
        int total = this.total();
        if (total == 0) {
            return 0;
        }
        int aRetirer = Math.min(montant, total);
        for (int i = 0; i < aRetirer; i++) {
            Role victime = tirerRoleAleatoire(aleatoire);
            if (victime != null) {
                this.effectifs.merge(victime, -1, Integer::sum);
            }
        }
        return aRetirer;
    }

    /**
     * Retire un nombre d'inactifs (utilise pour le recrutement : les soldats
     * sont preleves dans les inactifs, pas au hasard).
     *
     * @return le nombre d'inactifs effectivement retires
     */
    public int retirerInactifs(int montant) {
        if (montant <= 0) {
            return 0;
        }
        int dispo = this.effectifs.get(Role.INACTIF);
        int retire = Math.min(montant, dispo);
        this.effectifs.merge(Role.INACTIF, -retire, Integer::sum);
        return retire;
    }

    /**
     * Tire un role au hasard, avec une probabilite proportionnelle a son
     * effectif dans la population. Retourne null si la population est vide.
     */
    private Role tirerRoleAleatoire(Random aleatoire) {
        int total = this.total();
        if (total == 0) {
            return null;
        }
        int tirage = aleatoire.nextInt(total);
        int cumul = 0;
        for (Role r : Role.values()) {
            cumul += this.effectifs.get(r);
            if (tirage < cumul) {
                return r;
            }
        }
        return null;
    }
}
