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

    /**
     * Cree une partie avec le royaume du joueur et aucun bot.
     *
     * @param joueur le royaume du joueur
     */
    public Partie(Royaume joueur) {
        this(joueur, new ArrayList<>());
    }

    /**
     * Cree une partie avec le royaume du joueur et une liste de bots.
     *
     * @param joueur le royaume du joueur
     * @param bots la liste des royaumes controles par l'IA
     * @throws IllegalArgumentException si le royaume joueur est null
     */
    public Partie(Royaume joueur, List<Royaume> bots) {
        if (joueur == null) {
            throw new IllegalArgumentException("Le royaume joueur ne peut pas etre null.");
        }
        this.joueur = joueur;
        this.bots = new ArrayList<>(bots);
        this.tour = new Tour();
        this.batraillesDuTour = new ArrayList<>();
    }

    /**
     * Renvoie les batailles resolues durant ce tour, pour le recapitulatif.
     *
     * @return la liste non modifiable des batailles du tour
     */
    public List<BatailleResolue> batraillesDuTour() {
        return Collections.unmodifiableList(this.batraillesDuTour);
    }

    /**
     * Enregistre une bataille resolue dans le tour si elle n'est pas null.
     *
     * @param resolue la bataille resolue a enregistrer
     */
    public void enregistrerBataille(BatailleResolue resolue) {
        if (resolue != null) {
            this.batraillesDuTour.add(resolue);
        }
    }

    /**
     * Vide la liste des batailles resolues du tour.
     */
    public void viderBatraillesDuTour() {
        this.batraillesDuTour.clear();
    }

    /**
     * Renvoie le royaume du joueur.
     *
     * @return le royaume du joueur
     */
    public Royaume joueur() {
        return this.joueur;
    }

    /**
     * Renvoie la liste des royaumes controles par l'IA.
     *
     * @return la liste non modifiable des bots
     */
    public List<Royaume> bots() {
        return Collections.unmodifiableList(this.bots);
    }

    /**
     * Renvoie tous les royaumes : le joueur d'abord, puis les bots.
     *
     * @return la liste de tous les royaumes de la partie
     */
    public List<Royaume> tousLesRoyaumes() {
        List<Royaume> tous = new ArrayList<>(this.bots.size() + 1);
        tous.add(this.joueur);
        tous.addAll(this.bots);
        return tous;
    }

    /**
     * Renvoie le numero du tour courant.
     *
     * @return le numero du tour
     */
    public int numeroTour() {
        return this.tour.numero();
    }

    /**
     * Renvoie le tour courant.
     *
     * @return le tour courant
     */
    public Tour tour() {
        return this.tour;
    }

    /**
     * Passe au tour suivant en incrementant le compteur.
     */
    public void incrementerTour() {
        this.tour.incrementer();
    }

    /**
     * Indique si on a atteint le tour a partir duquel les combats sont permis.
     *
     * @return true si les combats sont autorises
     */
    public boolean combatsAutorises() {
        return numeroTour() >= Equilibrage.TOUR_DEBUT_COMBATS;
    }

    /**
     * Indique si la partie attend que le joueur joue.
     *
     * @return true si on attend l'action du joueur
     */
    public boolean enAttenteJoueur() {
        return this.tour.enAttenteJoueur();
    }

    /**
     * Avance le tour d'une phase en executant la phase courante.
     */
    public void passerEtape() {
        this.tour.executerPhaseCourante(this);
    }

    /**
     * Previent les vues qu'un nouveau tour a demarre.
     */
    public void notifierTourDemarre() {
        notifier(new Notification(TypeNotification.TOUR_DEMARRE, this.numeroTour()));
    }

    /**
     * Notifie les vues d'un changement (les phases passent par ici).
     *
     * @param notification la notification a transmettre aux vues
     */
    public void notifier(Notification notification) {
        setChanged();
        notifyObservers(notification);
    }

    /**
     * Renvoie le generateur aleatoire partage par la partie.
     *
     * @return le generateur aleatoire
     */
    public Random aleatoire() {
        return this.aleatoire;
    }

    /**
     * Reinitialise le generateur aleatoire avec une graine fixe.
     *
     * @param graine la graine a utiliser
     */
    public void definirGraineAleatoire(long graine) {
        this.aleatoire = new Random(graine);
    }

    /**
     * Indique si un evenement est en attente de resolution.
     *
     * @return true si un evenement attend une reponse du joueur
     */
    public boolean enAttenteEvenement() {
        return this.evenementEnAttente != null;
    }

    /**
     * Indique si la grenouille empoisonnee a deja ete declenchee.
     *
     * @return true si l'evenement scripte a deja eu lieu
     */
    public boolean grenouilleEmpoisonneeDeclenchee() {
        return this.grenouilleEmpoisonneeDeclenchee;
    }

    /**
     * Marque la grenouille empoisonnee comme deja declenchee.
     */
    public void marquerGrenouilleEmpoisonneeDeclenchee() {
        this.grenouilleEmpoisonneeDeclenchee = true;
    }

    /**
     * Renvoie l'evenement actuellement en attente.
     *
     * @return l'evenement en attente, ou null s'il n'y en a pas
     */
    public Evenement evenementEnAttente() {
        return this.evenementEnAttente;
    }

    /**
     * Declenche un evenement et previent les vues qu'il est en attente.
     *
     * @param evenement l'evenement a declencher
     */
    public void declencherEvenement(Evenement evenement) {
        this.evenementEnAttente = evenement;
        notifier(new Notification(TypeNotification.EVENEMENT_EN_ATTENTE, evenement));
    }

    /**
     * Applique le choix du joueur sur l'evenement en attente puis le termine.
     *
     * @param choix le choix fait par le joueur
     */
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
