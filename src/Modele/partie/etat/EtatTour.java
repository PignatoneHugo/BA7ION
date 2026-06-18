package Modele.partie.etat;

import Modele.partie.Partie;

// Une phase du tour. Chaque phase fait son traitement et donne la suivante.
public interface EtatTour {

    /**
     * Execute le traitement de cette phase sur la partie.
     *
     * @param partie la partie sur laquelle agir
     */
    void executer(Partie partie);

    /**
     * Renvoie la phase qui suit celle-ci.
     *
     * @return la phase suivante
     */
    EtatTour suivant();

    /**
     * Renvoie la cle de traduction du nom de la phase.
     *
     * @return la cle du nom de la phase
     */
    String nomCle();
}
