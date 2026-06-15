package Modele.partie;

import Modele.partie.etat.EtatPlanification;
import Modele.partie.etat.EtatTour;

// Compteur de tour et enchainement des phases.
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

    // Force le numero de tour (au chargement d'une sauvegarde).
    public void definirNumero(int numero) {
        if (numero < 1) {
            throw new IllegalArgumentException("Le numero de tour doit etre >= 1.");
        }
        this.numero = numero;
    }

    public void executerPhaseCourante(Partie partie) {
        EtatTour courant = this.etat;
        courant.executer(partie);
        this.etat = courant.suivant();
    }

    // True quand on attend que le joueur joue.
    public boolean enAttenteJoueur() {
        return this.etat instanceof EtatPlanification;
    }
}
