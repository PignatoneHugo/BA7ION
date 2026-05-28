package Modele.partie.etat;

import Modele.partie.Partie;

/**
 * Phase du cycle de tour. Implementation du pattern State : le {@link Modele.partie.Tour}
 * delegue le traitement de la phase courante a l'instance d'EtatTour qu'il
 * detient, puis transite vers l'etat suivant retourne par {@link #suivant()}.
 *
 * Une phase peut etre purement synchrone (production, consommation) ou
 * suspendue en attente d'une interaction utilisateur. Dans ce dernier cas,
 * la phase enregistre son attente dans la {@link Partie} et rend la main au
 * controleur, qui relance la chaine apres la reponse du joueur.
 */
public interface EtatTour {

    /**
     * Execute le traitement metier de la phase. L'implementation doit
     * notifier au moins une fois les Observers avec une notification de type
     * {@code PHASE_CHANGEE}, ainsi que toutes les notifications metier
     * propres aux modifications effectuees.
     *
     * @param partie partie sur laquelle la phase s'applique
     */
    void executer(Partie partie);

    /**
     * @return phase qui doit etre executee apres celle-ci, jamais null
     */
    EtatTour suivant();

    /**
     * @return cle de traduction du nom de la phase (ex. {@code "phase.production"})
     */
    String nomCle();
}
