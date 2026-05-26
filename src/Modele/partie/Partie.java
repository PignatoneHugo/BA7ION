package Modele.partie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.royaume.Royaume;

/**
 * Racine du modele. Une seule instance vivante a la fois (sauf transition de
 * chargement). Contient le royaume joueur, la liste des bots, le tour courant.
 *
 * Cette classe est Observable : elle relaie les notifications de cycle de tour
 * (PHASE_CHANGEE, TOUR_TERMINE) et les conditions de fin (PARTIE_GAGNEE,
 * PARTIE_PERDUE). Les changements locaux d'un royaume (TRESOR_CHANGE, etc.)
 * sont emis par le Royaume concerne lui-meme.
 *
 * Pour faire avancer le tour, on appelle {@link #passerEtape()} en boucle
 * jusqu'a ce que {@link #enAttenteJoueur()} retourne true (retour en
 * EtatPlanification).
 */
public class Partie extends Observable {

    private final Royaume joueur;
    private final List<Royaume> bots;
    private final Tour tour;

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

    /** Liste {joueur} U bots, dans l'ordre. */
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

    /**
     * Avance d'une phase. Appele par le ControleurPartie en boucle apres
     * "Fin de tour" jusqu'a retour en EtatPlanification.
     */
    public void passerEtape() {
        this.tour.executerPhaseCourante(this);
    }

    /**
     * Notification utilitaire pour signaler le demarrage d'un nouveau tour
     * (utile pour le HUD).
     */
    public void notifierTourDemarre() {
        notifier(new Notification(TypeNotification.TOUR_DEMARRE, this.numeroTour()));
    }

    /**
     * Helper public expose aux EtatTour : encapsule l'appel a {@code setChanged()}
     * (protected dans Observable) + {@code notifyObservers(arg)}. C'est le
     * seul point d'entree pour qu'une classe d'un autre package puisse emettre
     * une notification depuis cet Observable.
     */
    public void notifier(Notification n) {
        setChanged();
        notifyObservers(n);
    }
}
