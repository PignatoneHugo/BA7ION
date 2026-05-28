package Modele.population;

import java.util.EnumMap;
import java.util.Map;

import config.Equilibrage;

/**
 * Population d'un royaume, modelisee comme une repartition d'habitants par
 * {@link Role}. Tous les habitants sont comptabilises (y compris les
 * {@link Role#INACTIF}) et le total ne peut pas depasser la capacite de
 * logement courante.
 *
 * Cette classe ne notifie pas elle-meme les Observers : c'est le Royaume qui
 * la contient qui emet une notification {@code POPULATION_CHANGEE}.
 */
public class Population {

    private final Map<Role, Integer> effectifs = new EnumMap<>(Role.class);
    private int capaciteLogement;

    /**
     * Construit une population initialisee avec les valeurs par defaut de
     * la table d'equilibrage.
     */
    public Population() {
        this(Equilibrage.POPULATION_INITIALE, Equilibrage.CAPACITE_LOGEMENT_INITIALE);
    }

    /**
     * @param totalInitial nombre d'habitants au tour 1, tous places en {@link Role#INACTIF}
     * @param capaciteLogement plafond impose au total des effectifs
     */
    public Population(int totalInitial, int capaciteLogement) {
        for (Role r : Role.values()) {
            this.effectifs.put(r, 0);
        }
        this.effectifs.put(Role.INACTIF, totalInitial);
        this.capaciteLogement = capaciteLogement;
    }

    /**
     * @return somme des effectifs sur tous les roles
     */
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

    /**
     * @param nouvelle nouvelle capacite, doit etre positive ou nulle
     * @throws IllegalArgumentException si {@code nouvelle} est negative
     */
    public void definirCapaciteLogement(int nouvelle) {
        if (nouvelle < 0) {
            throw new IllegalArgumentException("La capacite de logement doit etre positive.");
        }
        this.capaciteLogement = nouvelle;
    }

    /**
     * Deplace un certain nombre d'habitants d'un role vers un autre.
     * L'operation est atomique : elle echoue sans rien modifier si le role
     * source ne contient pas assez d'habitants.
     *
     * @param source role d'origine
     * @param cible role d'arrivee
     * @param montant nombre d'habitants a deplacer (doit etre strictement positif)
     * @return {@code true} si la reaffectation a ete appliquee
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
     * Tente d'ajouter de nouveaux inactifs (croissance demographique). Le
     * surplus au-dela de la capacite de logement est rejete.
     *
     * @param montant nombre d'habitants demande
     * @return nombre d'habitants effectivement ajoutes
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
     * Retire des habitants suite a une famine, une epidemie ou tout autre
     * evenement letal. Les inactifs sont prioritaires ; si cela ne suffit
     * pas, les autres roles sont decrementes dans l'ordre de declaration de
     * l'enum (deterministe).
     *
     * @param montant nombre d'habitants a retirer
     * @return nombre d'habitants effectivement retires (peut etre inferieur
     *         a {@code montant} si la population totale n'est pas suffisante)
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
