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

    /**
     * Definit le nom du joueur, sauf si le nom est null ou vide.
     *
     * @param nom le nom du joueur souhaite
     * @return le builder pour chainer les appels
     */
    public PartieBuilder nomJoueur(String nom) {
        if (nom != null && !nom.isBlank()) {
            this.nomJoueur = nom;
        }
        return this;
    }

    /**
     * Definit le nombre de bots de la partie.
     *
     * @param nombre le nombre de bots souhaite
     * @return le builder pour chainer les appels
     * @throws IllegalArgumentException si le nombre n'est pas entre 1 et 4
     */
    public PartieBuilder nombreBots(int nombre) {
        if (nombre < 1 || nombre > 4) {
            throw new IllegalArgumentException("Le nombre de bots doit etre entre 1 et 4.");
        }
        this.nombreBots = nombre;
        return this;
    }

    /**
     * Definit la graine aleatoire de la partie.
     *
     * @param graine la graine a utiliser
     * @return le builder pour chainer les appels
     */
    public PartieBuilder graineAleatoire(long graine) {
        this.graineAleatoire = graine;
        return this;
    }

    /**
     * Renvoie la graine aleatoire configuree.
     *
     * @return la graine aleatoire
     */
    public long graineAleatoire() {
        return this.graineAleatoire;
    }

    /**
     * Definit la difficulte de la partie, sauf si elle est null.
     *
     * @param difficulte la difficulte souhaitee
     * @return le builder pour chainer les appels
     */
    public PartieBuilder difficulte(Difficulte difficulte) {
        if (difficulte != null) {
            this.difficulte = difficulte;
        }
        return this;
    }

    /**
     * Renvoie la difficulte configuree.
     *
     * @return la difficulte de la partie
     */
    public Difficulte difficulte() {
        return this.difficulte;
    }

    /**
     * Construit la partie a partir des parametres configures.
     *
     * @return la nouvelle partie creee
     */
    public Partie build() {
        Royaume joueur = new Royaume(this.nomJoueur);
        appliquerDifficulte(joueur);

        List<Royaume> bots = new ArrayList<>();
        for (int indice = 0; indice < this.nombreBots; indice++) {
            Royaume bot = new Royaume("Bot " + (indice + 1));
            bot.definirStrategieIA(FabriqueIA.creerEquilibree());
            bots.add(bot);
        }
        Partie partie = new Partie(joueur, bots);
        partie.definirGraineAleatoire(this.graineAleatoire);
        return partie;
    }

    /**
     * Recree une partie complete a partir d'une sauvegarde.
     *
     * @param sauvegarde la sauvegarde a charger
     * @return la partie reconstruite
     * @throws IllegalArgumentException si la sauvegarde est null
     */
    public static Partie depuisSauvegarde(Sauvegarde sauvegarde) {
        if (sauvegarde == null) {
            throw new IllegalArgumentException("Sauvegarde null.");
        }

        Royaume joueur = new Royaume(sauvegarde.joueur.nom);
        sauvegarde.joueur.appliquerA(joueur);

        List<Royaume> bots = new ArrayList<>();
        for (EtatRoyaume etatBot : sauvegarde.bots) {
            Royaume bot = new Royaume(etatBot.nom);
            if (etatBot.estBot) {
                bot.definirStrategieIA(FabriqueIA.creerEquilibree());
            }
            etatBot.appliquerA(bot);
            bots.add(bot);
        }

        Partie partie = new Partie(joueur, bots);
        partie.definirGraineAleatoire(sauvegarde.graineAleatoire);
        partie.tour().definirNumero(sauvegarde.numeroTour);
        if (sauvegarde.grenouilleEmpoisonneeDeclenchee) {
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
