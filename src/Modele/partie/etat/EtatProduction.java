package Modele.partie.etat;

import Modele.infrastructure.Batiment;
import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.partie.Partie;
import Modele.royaume.Royaume;

/**
 * Phase de production : chaque batiment de chaque royaume produit ses ressources.
 */
public class EtatProduction implements EtatTour {

    @Override
    public void executer(Partie partie) {
        for (Royaume r : partie.tousLesRoyaumes()) {
            for (Batiment b : r.batiments()) {
                b.produire(r);
            }
            r.notifierProduction();
        }
        partie.notifier(new Notification(TypeNotification.PHASE_CHANGEE, this.nomCle()));
    }

    @Override
    public EtatTour suivant() {
        return new EtatConsommation();
    }

    @Override
    public String nomCle() {
        return "phase.production";
    }
}
