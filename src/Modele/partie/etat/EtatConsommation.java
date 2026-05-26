package Modele.partie.etat;

import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.partie.Partie;
import Modele.royaume.Royaume;

/**
 * Phase 2 du tour : chaque royaume consomme la nourriture necessaire a sa
 * population civile. En cas de penurie, des habitants meurent (famine
 * simplifiee, sera affinee Sprint 2+).
 *
 * En sortie : EtatFinTour (au Sprint 1 on saute les phases combat/IA/evenement
 * et on enchaine directement la fin du tour ; ces phases viendront Sprint 2+).
 */
public class EtatConsommation implements EtatTour {

    @Override
    public void executer(Partie partie) {
        for (Royaume r : partie.tousLesRoyaumes()) {
            r.appliquerConsommationCivile();
        }
        partie.notifier(new Notification(TypeNotification.PHASE_CHANGEE, this.nomCle()));
    }

    @Override
    public EtatTour suivant() {
        return new EtatFinTour();
    }

    @Override
    public String nomCle() {
        return "phase.consommation";
    }
}
