package Modele.partie.etat;

import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.partie.Partie;
import Modele.royaume.Royaume;

// Phase de consommation : on mange la nourriture, famine si pas assez.
public class EtatConsommation implements EtatTour {

    @Override
    public void executer(Partie partie) {
        for (Royaume r : partie.tousLesRoyaumes()) {
            r.appliquerConsommationCivile(partie.aleatoire());
        }
        partie.notifier(new Notification(TypeNotification.PHASE_CHANGEE, this.nomCle()));
    }

    @Override
    public EtatTour suivant() {
        return new EtatActionsDifferees();
    }

    @Override
    public String nomCle() {
        return "phase.consommation";
    }
}
