package Modele.partie;

import java.util.ArrayList;
import java.util.List;

import Modele.economie.Ressource;
import Modele.ia.FabriqueIA;
import Modele.persistance.EtatRoyaume;
import Modele.persistance.Sauvegarde;
import Modele.royaume.Royaume;

import config.Difficulte;

// Sert a creer une Partie en chainant les parametres.
// Ex : new PartieBuilder().nomJoueur("X").nombreBots(2).build();
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

    // Recree une Partie a partir d'une sauvegarde.
    public static Partie depuisSauvegarde(Sauvegarde s) {
        if (s == null) {
            throw new IllegalArgumentException("Sauvegarde null.");
        }

        Royaume joueur = new Royaume(s.joueur.nom);
        s.joueur.appliquerA(joueur);

        List<Royaume> bots = new ArrayList<>();
        for (EtatRoyaume etatBot : s.bots) {
            Royaume bot = new Royaume(etatBot.nom);
            if (etatBot.estBot) {
                bot.definirStrategieIA(FabriqueIA.creerEquilibree());
            }
            etatBot.appliquerA(bot);
            bots.add(bot);
        }

        Partie partie = new Partie(joueur, bots);
        partie.definirGraineAleatoire(s.graineAleatoire);
        partie.tour().definirNumero(s.numeroTour);
        if (s.grenouilleEmpoisonneeDeclenchee) {
            partie.marquerGrenouilleEmpoisonneeDeclenchee();
        }
        return partie;
    }

    // Applique le bonus ou malus d'or de la difficulte au joueur.
    private void appliquerDifficulte(Royaume joueur) {
        int bonus = this.difficulte.bonusOrInitial();
        if (bonus > 0) {
            joueur.tresor().ajouter(Ressource.OR, bonus);
        } else if (bonus < 0) {
            joueur.tresor().retirer(Ressource.OR, -bonus);
        }
    }
}
