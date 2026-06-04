package Modele.partie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Random;

import Modele.evenement.Choix;
import Modele.evenement.Evenement;
import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.royaume.Royaume;

/**
 * Classe racine du modele. Contient le royaume du joueur, les bots et le tour courant.
 * Observable : notifie les vues des changements globaux (phase, fin de tour, fin de partie).
 */
public class Partie extends Observable {

    private final Royaume joueur;
    private final List<Royaume> bots;
    private final Tour tour;
    private Evenement evenementEnAttente;
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
    }

    public Royaume joueur() {
        return this.joueur;
    }

    public List<Royaume> bots() {
        return Collections.unmodifiableList(this.bots);
    }

    /** Liste du joueur suivi des bots. */
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

    public boolean enAttenteJoueur() {
        return this.tour.enAttenteJoueur();
    }

    /** Avance d'une phase. Appele en boucle apres "Fin de tour". */
    public void passerEtape() {
        this.tour.executerPhaseCourante(this);
    }

    public void notifierTourDemarre() {
        notifier(new Notification(TypeNotification.TOUR_DEMARRE, this.numeroTour()));
    }

    /**
     * Helper public pour notifier depuis les classes des sous-packages
     * (les EtatXxx ne peuvent pas appeler setChanged() directement car protected).
     */
    public void notifier(Notification n) {
        setChanged();
        notifyObservers(n);
    }

    // ----- Aleatoire seedable -----

    /** Generateur aleatoire partage par la partie (combats, evenements...). */
    public Random aleatoire() {
        return this.aleatoire;
    }

    /** Reinitialise le generateur avec la graine donnee (pour tests reproductibles). */
    public void definirGraineAleatoire(long graine) {
        this.aleatoire = new Random(graine);
    }

    // ----- Gestion des evenements aleatoires -----

    public boolean enAttenteEvenement() {
        return this.evenementEnAttente != null;
    }

    public Evenement evenementEnAttente() {
        return this.evenementEnAttente;
    }

    /** A appeler par EtatEvenement quand un evenement est tire. */
    public void declencherEvenement(Evenement e) {
        this.evenementEnAttente = e;
        notifier(new Notification(TypeNotification.EVENEMENT_EN_ATTENTE, e));
    }

    /**
     * Applique le choix retenu par le joueur sur le royaume du joueur,
     * puis notifie la fin de l'evenement.
     */
    public void resoudreEvenement(Choix choix) {
        if (this.evenementEnAttente == null || choix == null) {
            return;
        }
        choix.effet().appliquer(this.joueur);
        this.joueur.notifierTresorChange();
        this.joueur.notifierPopulationChangee();
        this.joueur.notifierMoralChange();
        this.evenementEnAttente = null;
        notifier(new Notification(TypeNotification.EVENEMENT_RESOLU));
    }
}
