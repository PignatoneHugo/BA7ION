package Modele.partie;

import java.util.ArrayList;
import java.util.List;

import Modele.royaume.Royaume;

/**
 * Pattern Builder (cf. TP14) pour la creation d'une Partie.
 *
 * Permet une construction progressive avec un nombre variable de bots, une
 * difficulte, une graine aleatoire pour les tests, etc. Au Sprint 1 on ne
 * gere encore que le nom du joueur ; les autres parametres viendront avec
 * les Epics correspondants (10 = IA, 12 = sauvegarde, 13 = options).
 *
 * Usage typique :
 *     Partie p = new PartieBuilder()
 *         .nomJoueur("Mon Royaume")
 *         .nombreBots(0)
 *         .build();
 */
public class PartieBuilder {

    private String nomJoueur = "Royaume du Joueur";
    private int nombreBots = 0;
    private long graineAleatoire = System.nanoTime();

    public PartieBuilder nomJoueur(String nom) {
        if (nom != null && !nom.isBlank()) {
            this.nomJoueur = nom;
        }
        return this;
    }

    public PartieBuilder nombreBots(int n) {
        if (n < 0 || n > 4) {
            throw new IllegalArgumentException("Le nombre de bots doit etre entre 0 et 4.");
        }
        this.nombreBots = n;
        return this;
    }

    public PartieBuilder graineAleatoire(long graine) {
        this.graineAleatoire = graine;
        return this;
    }

    public long graineAleatoire() {
        return this.graineAleatoire;
    }

    public Partie build() {
        Royaume joueur = new Royaume(this.nomJoueur);
        List<Royaume> bots = new ArrayList<>();
        for (int i = 0; i < this.nombreBots; i++) {
            // Au Sprint 1 les bots sont des royaumes "vides", sans IA branchee.
            // La StrategieIA sera ajoutee Sprint 2+ via FabriqueIA.
            bots.add(new Royaume("Bot " + (i + 1)));
        }
        return new Partie(joueur, bots);
    }
}
