package Modele.partie;

import java.util.ArrayList;
import java.util.List;

import Modele.economie.Ressource;
import Modele.ia.FabriqueIA;
import Modele.royaume.Royaume;

import config.Difficulte;

/**
 * Pattern Builder pour creer une Partie. Permet de chainer les parametres
 * optionnels avant de construire l'objet final.
 *
 * Exemple : new PartieBuilder().nomJoueur("X").nombreBots(2).build();
 */
public class PartieBuilder {

    private String nomJoueur = "Royaume du Joueur";
    private int nombreBots = 1;
    private long graineAleatoire = System.nanoTime();
    private Difficulte difficulte = Difficulte.NORMAL;

    public PartieBuilder nomJoueur(String nom) {
        if (nom != null && !nom.isBlank()) {
            this.nomJoueur = nom;
        }
        return this;
    }

    public PartieBuilder nombreBots(int n) {
        if (n < 1 || n > 4) {
            throw new IllegalArgumentException("Le nombre de bots doit etre entre 1 et 4.");
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

    public PartieBuilder difficulte(Difficulte d) {
        if (d != null) {
            this.difficulte = d;
        }
        return this;
    }

    public Difficulte difficulte() {
        return this.difficulte;
    }

    public Partie build() {
        Royaume joueur = new Royaume(this.nomJoueur);
        appliquerDifficulte(joueur);

        List<Royaume> bots = new ArrayList<>();
        for (int i = 0; i < this.nombreBots; i++) {
            Royaume bot = new Royaume("Bot " + (i + 1));
            bot.definirStrategieIA(FabriqueIA.creerEquilibree());
            bots.add(bot);
        }
        Partie partie = new Partie(joueur, bots);
        partie.definirGraineAleatoire(this.graineAleatoire);
        return partie;
    }

    /**
     * Applique le bonus / malus d'or de la difficulte sur le royaume joueur.
     * Les bots restent en parametres standards pour Sprint 2.
     */
    private void appliquerDifficulte(Royaume joueur) {
        int bonus = this.difficulte.bonusOrInitial();
        if (bonus > 0) {
            joueur.tresor().ajouter(Ressource.OR, bonus);
        } else if (bonus < 0) {
            joueur.tresor().retirer(Ressource.OR, -bonus);
        }
    }
}
