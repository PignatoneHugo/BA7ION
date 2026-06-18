package Modele.partie;

import Modele.partie.etat.EtatPlanification;
import Modele.partie.etat.EtatTour;

// Compteur de tour et enchainement des phases.
public class Tour {

    private int numero;
    private EtatTour etat;

    /**
     * Cree un tour au numero 1 en phase de planification.
     */
    public Tour() {
        this.numero = 1;
        this.etat = new EtatPlanification();
    }

    /**
     * Renvoie le numero du tour.
     *
     * @return le numero du tour
     */
    public int numero() {
        return this.numero;
    }

    /**
     * Renvoie la phase courante du tour.
     *
     * @return l'etat courant du tour
     */
    public EtatTour etat() {
        return this.etat;
    }

    /**
     * Incremente le numero du tour de un.
     */
    public void incrementer() {
        this.numero++;
    }

    /**
     * Force le numero du tour, par exemple au chargement d'une sauvegarde.
     *
     * @param numero le numero de tour a appliquer
     * @throws IllegalArgumentException si le numero est inferieur a 1
     */
    public void definirNumero(int numero) {
        if (numero < 1) {
            throw new IllegalArgumentException("Le numero de tour doit etre >= 1.");
        }
        this.numero = numero;
    }

    /**
     * Execute la phase courante puis passe a la phase suivante.
     *
     * @param partie la partie sur laquelle agir
     */
    public void executerPhaseCourante(Partie partie) {
        EtatTour courant = this.etat;
        courant.executer(partie);
        this.etat = courant.suivant();
    }

    /**
     * Indique si le tour attend que le joueur joue.
     *
     * @return true si on est en phase de planification
     */
    public boolean enAttenteJoueur() {
        return this.etat instanceof EtatPlanification;
    }
}
