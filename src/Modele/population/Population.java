package Modele.population;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

import config.Equilibrage;

// Habitants d'un royaume, repartis par role et limites par le logement.
public class Population {

    private final Map<Role, Integer> effectifs = new EnumMap<>(Role.class);
    private int capaciteLogement;

    /**
     * Cree une population aux valeurs initiales par defaut.
     */
    public Population() {
        this(Equilibrage.POPULATION_INITIALE, Equilibrage.CAPACITE_LOGEMENT_INITIALE);
    }

    /**
     * Cree une population avec un total initial d'inactifs et une capacite de logement.
     *
     * @param totalInitial le nombre d'habitants au depart
     * @param capaciteLogement la capacite de logement de depart
     */
    public Population(int totalInitial, int capaciteLogement) {
        for (Role role : Role.values()) {
            this.effectifs.put(role, 0);
        }
        this.effectifs.put(Role.INACTIF, totalInitial);
        this.capaciteLogement = capaciteLogement;
    }

    /**
     * Donne le nombre total d'habitants, tous roles confondus.
     *
     * @return la somme des effectifs de tous les roles
     */
    public int total() {
        int totalHabitants = 0;
        for (int effectifRole : this.effectifs.values()) {
            totalHabitants += effectifRole;
        }
        return totalHabitants;
    }

    /**
     * Donne l'effectif d'un role donne.
     *
     * @param role le role voulu
     * @return le nombre d'habitants de ce role
     */
    public int effectif(Role role) {
        return this.effectifs.get(role);
    }

    /**
     * Force l'effectif d'un role, utilise au chargement d'une sauvegarde.
     *
     * @param role le role concerne
     * @param effectif l'effectif a fixer
     * @throws IllegalArgumentException si l'effectif est negatif
     */
    public void definirEffectif(Role role, int effectif) {
        if (effectif < 0) {
            throw new IllegalArgumentException("Un effectif ne peut pas etre negatif.");
        }
        this.effectifs.put(role, effectif);
    }

    /**
     * Donne la capacite de logement actuelle.
     *
     * @return la capacite de logement
     */
    public int capaciteLogement() {
        return this.capaciteLogement;
    }

    /**
     * Change la capacite de logement.
     *
     * @param nouvelle la nouvelle capacite de logement
     * @throws IllegalArgumentException si la capacite est negative
     */
    public void definirCapaciteLogement(int nouvelle) {
        if (nouvelle < 0) {
            throw new IllegalArgumentException("La capacite de logement doit etre positive.");
        }
        this.capaciteLogement = nouvelle;
    }

    /**
     * Deplace des habitants d'un role vers un autre.
     *
     * @param source le role de depart
     * @param cible le role d'arrivee
     * @param montant le nombre d'habitants a deplacer
     * @return true si le deplacement a reussi, false si pas assez de monde
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
     * Ajoute des inactifs lors de la croissance, dans la limite du logement.
     *
     * @param montant le nombre d'inactifs a ajouter
     * @return le nombre d'inactifs reellement ajoutes
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
     * Retire des habitants tires au hasard parmi tous les roles, par exemple lors d'une famine.
     *
     * @param montant le nombre d'habitants a retirer
     * @param aleatoire le generateur aleatoire utilise pour le tirage
     * @return le nombre d'habitants reellement retires
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
        for (int indice = 0; indice < aRetirer; indice++) {
            Role victime = tirerRoleAleatoire(aleatoire);
            if (victime != null) {
                this.effectifs.merge(victime, -1, Integer::sum);
            }
        }
        return aRetirer;
    }

    /**
     * Retire des inactifs, par exemple lors du recrutement de soldats.
     *
     * @param montant le nombre d'inactifs a retirer
     * @return le nombre d'inactifs reellement retires
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

    // Tire un role au hasard, proportionnellement a son effectif.
    private Role tirerRoleAleatoire(Random aleatoire) {
        int total = this.total();
        if (total == 0) {
            return null;
        }
        int tirage = aleatoire.nextInt(total);
        int cumul = 0;
        for (Role role : Role.values()) {
            cumul += this.effectifs.get(role);
            if (tirage < cumul) {
                return role;
            }
        }
        return null;
    }
}
