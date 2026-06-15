package Modele.militaire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Armee d'un royaume : ses unites et sa posture.
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

    // Effectif total de l'armee.
    public int effectifTotal() {
        int total = 0;
        for (Unite u : this.unites) {
            total += u.effectif();
        }
        return total;
    }

    // Effectif d'un type donne.
    public int effectifParType(TypeUnite type) {
        int total = 0;
        for (Unite u : this.unites) {
            if (u.type() == type) {
                total += u.effectif();
            }
        }
        return total;
    }

    // Ajoute des soldats du type donne (fusionne avec l'unite existante si elle existe).
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

    // Retire jusqu'a nombre soldats du type donne. Renvoie combien ont ete retires.
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
        // on enleve les unites vides
        this.unites.removeIf(u -> u.effectif() == 0);
        return nombre - aRetirer;
    }

    public boolean estVide() {
        return effectifTotal() == 0;
    }
}
