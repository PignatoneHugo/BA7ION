package Modele.partie.etat;

import Modele.partie.Partie;

// Une phase du tour. Chaque phase fait son traitement et donne la suivante.
public interface EtatTour {

    void executer(Partie partie);

    // La phase d'apres.
    EtatTour suivant();

    String nomCle();
}
