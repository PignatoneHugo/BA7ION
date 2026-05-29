package Modele.partie.etat;

import Modele.partie.Partie;

/**
 * Interface des phases du tour. Pattern State.
 * Chaque sous-classe represente une phase et sait vers quelle phase aller ensuite.
 */
public interface EtatTour {

    /** Execute le traitement de la phase et notifie les observers. */
    void executer(Partie partie);

    /** Retourne la phase suivante (jamais null). */
    EtatTour suivant();

    /** Cle i18n du nom de la phase. */
    String nomCle();
}
