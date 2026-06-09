package Modele.militaire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Armee d'un royaume : liste d'Unite avec une posture de combat.
 */
public class Armee {

    private final List<Unite> unites;
    private PostureCombat posture;

    public Armee() {
        this(PostureCombat.DEFENSE);
    }

    public Armee(PostureCombat posture) {
        this.unites = new ArrayList<>();
        this.posture = posture != null ? posture : PostureCombat.DEFENSE;
    }

    public void ajouterUnite(Unite u) {
        if (u == null) {
            throw new IllegalArgumentException("Une Unite ne peut pas etre null.");
        }
        this.unites.add(u);
    }

    public List<Unite> unites() {
        return Collections.unmodifiableList(this.unites);
    }

    public PostureCombat posture() {
        return this.posture;
    }

    public void definirPosture(PostureCombat posture) {
        if (posture != null) {
            this.posture = posture;
        }
    }

    /** Somme des effectifs de toutes les unites. */
    public int effectifTotal() {
        int total = 0;
        for (Unite u : this.unites) {
            total += u.effectif();
        }
        return total;
    }

    /** Effectif d'un type donne (somme si plusieurs entrees du meme type). */
    public int effectifParType(TypeUnite type) {
        int total = 0;
        for (Unite u : this.unites) {
            if (u.type() == type) {
                total += u.effectif();
            }
        }
        return total;
    }

    /**
     * Recrute n soldats du type donne en fusionnant avec une unite
     * existante si possible -- evite de creer plusieurs entrees du
     * meme type dans la liste.
     */
    public void recruter(TypeUnite type, int nombre) {
        if (nombre <= 0) {
            return;
        }
        for (Unite u : this.unites) {
            if (u.type() == type) {
                u.renforcer(nombre);
                return;
            }
        }
        this.unites.add(new Unite(type, nombre));
    }

    /**
     * Retire jusqu'a {@code nombre} soldats du type donne. Si plusieurs
     * entrees du meme type existent, vide les plus petites d'abord.
     * Retourne le nombre reellement retire.
     */
    public int retirer(TypeUnite type, int nombre) {
        if (nombre <= 0) {
            return 0;
        }
        int aRetirer = nombre;
        for (Unite u : this.unites) {
            if (aRetirer <= 0) break;
            if (u.type() == type) {
                aRetirer -= u.subirPertes(aRetirer);
            }
        }
        // Nettoyer les unites vides
        this.unites.removeIf(u -> u.effectif() == 0);
        return nombre - aRetirer;
    }

    public boolean estVide() {
        return effectifTotal() == 0;
    }
}
