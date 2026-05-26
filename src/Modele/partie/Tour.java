package Modele.partie;

import Modele.partie.etat.EtatPlanification;
import Modele.partie.etat.EtatTour;

/**
 * Compteur de tour + machine a etats des 9 phases (pattern State).
 *
 * Le Tour delegue {@code executerPhaseCourante(Partie)} a son {@link #etat}
 * courant, puis transite vers l'etat suivant via {@link EtatTour#suivant()}.
 *
 * Pas d'Observable ici : c'est la Partie qui notifie PHASE_CHANGEE et
 * TOUR_TERMINE via ses etats.
 */
public class Tour {

    private int numero;
    private EtatTour etat;

    public Tour() {
        this.numero = 1;
        this.etat = new EtatPlanification();
    }

    public int numero() {
        return this.numero;
    }

    public EtatTour etat() {
        return this.etat;
    }

    public void incrementer() {
        this.numero++;
    }

    public void executerPhaseCourante(Partie partie) {
        EtatTour courant = this.etat;
        courant.executer(partie);
        this.etat = courant.suivant();
    }

    /**
     * True si on est en attente de l'action du joueur (phase Planification),
     * false si on est en train de derouler la resolution.
     */
    public boolean enAttenteJoueur() {
        return this.etat instanceof EtatPlanification;
    }
}
