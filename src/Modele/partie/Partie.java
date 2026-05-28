package Modele.partie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.royaume.Royaume;

/**
 * Racine du modele de jeu. Une instance represente une partie en cours :
 * elle agrege le royaume du joueur, ses adversaires et le compteur de tour.
 *
 * La Partie est Observable et relaie les notifications globales du cycle
 * (PHASE_CHANGEE, TOUR_TERMINE) ainsi que les conditions de fin
 * (PARTIE_GAGNEE, PARTIE_PERDUE). Les notifications locales (changement de
 * tresor, de population, etc.) sont emises directement par le {@link Royaume}
 * concerne.
 *
 * Avancement d'un tour : appeler {@link #passerEtape()} en boucle jusqu'a
 * ce que {@link #enAttenteJoueur()} renvoie {@code true}.
 */
public class Partie extends Observable {

    private final Royaume joueur;
    private final List<Royaume> bots;
    private final Tour tour;

    /**
     * Partie en solo sans adversaire.
     */
    public Partie(Royaume joueur) {
        this(joueur, new ArrayList<>());
    }

    /**
     * @param joueur royaume controle par l'utilisateur, non null
     * @param bots adversaires geres par une IA (peut etre vide)
     * @throws IllegalArgumentException si {@code joueur} est null
     */
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

    /**
     * @return liste ordonnee contenant le joueur suivi des bots
     */
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
     * Avance d'une phase dans le cycle de tour.
     */
    public void passerEtape() {
        this.tour.executerPhaseCourante(this);
    }

    /**
     * Notifie le demarrage d'un nouveau tour. A appeler par le controleur
     * une fois la phase de resolution terminee, pour que le HUD se rafraichisse.
     */
    public void notifierTourDemarre() {
        notifier(new Notification(TypeNotification.TOUR_DEMARRE, this.numeroTour()));
    }

    /**
     * Point d'entree unique permettant aux classes des sous-packages
     * (notamment les {@link Modele.partie.etat.EtatTour}) d'emettre une
     * notification depuis cet Observable. Encapsule l'appel a {@code setChanged()}
     * + {@code notifyObservers(arg)}, qui ne peuvent pas etre invoques
     * directement car {@code setChanged()} est protected.
     *
     * @param n notification a transmettre aux observers, non null
     */
    public void notifier(Notification n) {
        setChanged();
        notifyObservers(n);
    }
}
