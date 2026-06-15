package Modele.partie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Random;

import Modele.combat.BatailleResolue;
import Modele.evenement.Choix;
import Modele.evenement.Evenement;
import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.royaume.Royaume;

import config.Equilibrage;

// Racine du modele : royaume du joueur, bots et tour courant.
public class Partie extends Observable {

    private final Royaume joueur;
    private final List<Royaume> bots;
    private final Tour tour;
    private final List<BatailleResolue> batraillesDuTour;
    private Evenement evenementEnAttente;
    private boolean grenouilleEmpoisonneeDeclenchee;
    private Random aleatoire = new Random();

    public Partie(Royaume joueur) {
        this(joueur, new ArrayList<>());
    }

    public Partie(Royaume joueur, List<Royaume> bots) {
        if (joueur == null) {
            throw new IllegalArgumentException("Le royaume joueur ne peut pas etre null.");
        }
        this.joueur = joueur;
        this.bots = new ArrayList<>(bots);
        this.tour = new Tour();
        this.batraillesDuTour = new ArrayList<>();
    }

    // Batailles resolues ce tour (pour le recap).
    public List<BatailleResolue> batraillesDuTour() {
        return Collections.unmodifiableList(this.batraillesDuTour);
    }

    public void enregistrerBataille(BatailleResolue resolue) {
        if (resolue != null) {
            this.batraillesDuTour.add(resolue);
        }
    }

    public void viderBatraillesDuTour() {
        this.batraillesDuTour.clear();
    }

    public Royaume joueur() {
        return this.joueur;
    }

    public List<Royaume> bots() {
        return Collections.unmodifiableList(this.bots);
    }

    // Le joueur puis tous les bots.
    public List<Royaume> tousLesRoyaumes() {
        List<Royaume> tous = new ArrayList<>(this.bots.size() + 1);
        tous.add(this.joueur);
        tous.addAll(this.bots);
        return tous;
    }

    public int numeroTour() {
        return this.tour.numero();
    }

    public Tour tour() {
        return this.tour;
    }

    public void incrementerTour() {
        this.tour.incrementer();
    }

    // True si on a atteint le tour ou les combats sont permis.
    public boolean combatsAutorises() {
        return numeroTour() >= Equilibrage.TOUR_DEBUT_COMBATS;
    }

    public boolean enAttenteJoueur() {
        return this.tour.enAttenteJoueur();
    }

    // Avance d'une phase.
    public void passerEtape() {
        this.tour.executerPhaseCourante(this);
    }

    public void notifierTourDemarre() {
        notifier(new Notification(TypeNotification.TOUR_DEMARRE, this.numeroTour()));
    }

    // Notifie les vues (les EtatXxx passent par ici car setChanged est protege).
    public void notifier(Notification n) {
        setChanged();
        notifyObservers(n);
    }

    // Generateur aleatoire partage (combats, evenements...).
    public Random aleatoire() {
        return this.aleatoire;
    }

    // Remet le generateur avec une graine fixe (utile pour les tests).
    public void definirGraineAleatoire(long graine) {
        this.aleatoire = new Random(graine);
    }

    public boolean enAttenteEvenement() {
        return this.evenementEnAttente != null;
    }

    // True si la grenouille empoisonnee a deja eu lieu.
    public boolean grenouilleEmpoisonneeDeclenchee() {
        return this.grenouilleEmpoisonneeDeclenchee;
    }

    public void marquerGrenouilleEmpoisonneeDeclenchee() {
        this.grenouilleEmpoisonneeDeclenchee = true;
    }

    public Evenement evenementEnAttente() {
        return this.evenementEnAttente;
    }

    public void declencherEvenement(Evenement e) {
        this.evenementEnAttente = e;
        notifier(new Notification(TypeNotification.EVENEMENT_EN_ATTENTE, e));
    }

    // Applique le choix du joueur puis termine l'evenement.
    public void resoudreEvenement(Choix choix) {
        if (this.evenementEnAttente == null || choix == null) {
            return;
        }
        choix.effet().appliquer(this.joueur, this.aleatoire);
        this.joueur.notifierTresorChange();
        this.joueur.notifierPopulationChangee();
        this.joueur.notifierMoralChange();
        this.evenementEnAttente = null;
        notifier(new Notification(TypeNotification.EVENEMENT_RESOLU));
    }
}
