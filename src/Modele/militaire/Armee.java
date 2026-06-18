package Modele.militaire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Armee d'un royaume : ses unites et sa posture.
public class Armee {

    private final List<Unite> unites;
    private PostureCombat posture;

    /**
     * Cree une armee vide en posture de defense.
     */
    public Armee() {
        this(PostureCombat.DEFENSE);
    }

    /**
     * Cree une armee vide avec la posture donnee.
     *
     * @param posture la posture de depart de l'armee
     */
    public Armee(PostureCombat posture) {
        this.unites = new ArrayList<>();
        this.posture = posture != null ? posture : PostureCombat.DEFENSE;
    }

    /**
     * Ajoute une unite a l'armee.
     *
     * @param unite l'unite a ajouter
     * @throws IllegalArgumentException si l'unite est null
     */
    public void ajouterUnite(Unite unite) {
        if (unite == null) {
            throw new IllegalArgumentException("Une Unite ne peut pas etre null.");
        }
        this.unites.add(unite);
    }

    /**
     * Renvoie la liste des unites de l'armee.
     *
     * @return la liste non modifiable des unites
     */
    public List<Unite> unites() {
        return Collections.unmodifiableList(this.unites);
    }

    /**
     * Renvoie la posture actuelle de l'armee.
     *
     * @return la posture de combat
     */
    public PostureCombat posture() {
        return this.posture;
    }

    /**
     * Change la posture de l'armee.
     *
     * @param posture la nouvelle posture
     */
    public void definirPosture(PostureCombat posture) {
        if (posture != null) {
            this.posture = posture;
        }
    }

    /**
     * Calcule l'effectif total de l'armee.
     *
     * @return le nombre total de soldats
     */
    public int effectifTotal() {
        int total = 0;
        for (Unite unite : this.unites) {
            total += unite.effectif();
        }
        return total;
    }

    /**
     * Calcule l'effectif d'un type d'unite donne.
     *
     * @param type le type d'unite a compter
     * @return le nombre de soldats de ce type
     */
    public int effectifParType(TypeUnite type) {
        int total = 0;
        for (Unite unite : this.unites) {
            if (unite.type() == type) {
                total += unite.effectif();
            }
        }
        return total;
    }

    /**
     * Ajoute des soldats du type donne, en fusionnant avec l'unite existante si elle existe.
     *
     * @param type le type de soldats a ajouter
     * @param nombre le nombre de soldats a ajouter
     */
    public void recruter(TypeUnite type, int nombre) {
        if (nombre <= 0) {
            return;
        }
        for (Unite unite : this.unites) {
            if (unite.type() == type) {
                unite.renforcer(nombre);
                return;
            }
        }
        this.unites.add(new Unite(type, nombre));
    }

    /**
     * Retire jusqu'a un certain nombre de soldats du type donne.
     *
     * @param type le type de soldats a retirer
     * @param nombre le nombre de soldats a retirer
     * @return le nombre de soldats reellement retires
     */
    public int retirer(TypeUnite type, int nombre) {
        if (nombre <= 0) {
            return 0;
        }
        int aRetirer = nombre;
        for (Unite unite : this.unites) {
            if (aRetirer <= 0) break;
            if (unite.type() == type) {
                aRetirer -= unite.subirPertes(aRetirer);
            }
        }
        // on enleve les unites vides
        this.unites.removeIf(unite -> unite.effectif() == 0);
        return nombre - aRetirer;
    }

    /**
     * Indique si l'armee n'a plus aucun soldat.
     *
     * @return true si l'armee est vide
     */
    public boolean estVide() {
        return effectifTotal() == 0;
    }
}
