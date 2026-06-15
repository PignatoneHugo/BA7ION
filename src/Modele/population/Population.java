package Modele.population;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

import config.Equilibrage;

// Habitants d'un royaume, repartis par role et limites par le logement.
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

    // Somme de tous les roles.
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

    // Force l'effectif d'un role (chargement de sauvegarde).
    public void definirEffectif(Role role, int effectif) {
        if (effectif < 0) {
            throw new IllegalArgumentException("Un effectif ne peut pas etre negatif.");
        }
        this.effectifs.put(role, effectif);
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

    // Deplace des habitants d'un role vers un autre. False si pas assez de monde.
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

    // Ajoute des inactifs (croissance), dans la limite du logement.
    public int ajouterInactifs(int montant) {
        if (montant <= 0) {
            return 0;
        }
        int marge = this.capaciteLogement - this.total();
        int ajoute = Math.max(0, Math.min(montant, marge));
        this.effectifs.merge(Role.INACTIF, ajoute, Integer::sum);
        return ajoute;
    }

    // Retire des habitants (famine...) tires au hasard parmi tous les roles.
    // Renvoie le nombre reellement retire.
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

    // Retire des inactifs (recrutement de soldats). Renvoie le nombre retire.
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
        for (Role r : Role.values()) {
            cumul += this.effectifs.get(r);
            if (tirage < cumul) {
                return r;
            }
        }
        return null;
    }
}
