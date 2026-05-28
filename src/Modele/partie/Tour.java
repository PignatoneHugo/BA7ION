package Modele.partie;

import Modele.partie.etat.EtatPlanification;
import Modele.partie.etat.EtatTour;

/**
 * Compteur de tour et machine a etats des phases de resolution.
 *
 * Le Tour delegue l'execution de la phase courante a son {@link EtatTour}
 * puis transite vers l'etat suivant retourne par {@link EtatTour#suivant()}.
 * Cette classe n'est pas Observable : les notifications associees au cycle
 * de tour sont emises par les etats eux-memes via la {@link Partie}.
 */
public class Tour {

    private int numero;
    private EtatTour etat;

    /**
     * Initialise un nouveau cycle de jeu au tour 1, en phase de planification.
     */
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

    /**
     * Execute la phase courante puis transite vers la phase suivante.
     *
     * @param partie partie sur laquelle s'applique la phase
     */
    public void executerPhaseCourante(Partie partie) {
        EtatTour courant = this.etat;
        courant.executer(partie);
        this.etat = courant.suivant();
    }

    /**
     * @return {@code true} si le tour attend une action du joueur (phase de
     *         planification), {@code false} pendant la resolution
     */
    public boolean enAttenteJoueur() {
        return this.etat instanceof EtatPlanification;
    }
}
