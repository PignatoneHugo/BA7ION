package Modele.partie;

import java.util.ArrayList;
import java.util.List;

import Modele.royaume.Royaume;

/**
 * Pattern Builder pour creer une Partie. Permet de chainer les parametres
 * optionnels avant de construire l'objet final.
 *
 * Exemple : new PartieBuilder().nomJoueur("X").nombreBots(2).build();
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
            bots.add(new Royaume("Bot " + (i + 1)));
        }
        return new Partie(joueur, bots);
    }
}
