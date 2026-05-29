package Modele.partie;

import Modele.partie.etat.EtatPlanification;
import Modele.partie.etat.EtatTour;

/**
 * Compteur de tour + machine a etats des phases.
 * Le Tour delegue l'execution a sa phase courante, puis passe a la suivante.
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

    /** True si on attend une action du joueur, false pendant la resolution. */
    public boolean enAttenteJoueur() {
        return this.etat instanceof EtatPlanification;
    }
}
