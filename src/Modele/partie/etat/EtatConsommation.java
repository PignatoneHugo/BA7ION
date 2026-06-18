package Modele.partie.etat;

import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.partie.Partie;
import Modele.royaume.Royaume;

// Phase de consommation : on mange la nourriture, famine si pas assez.
public class EtatConsommation implements EtatTour {

    /**
     * Fait consommer la nourriture a chaque royaume, avec famine si manque.
     *
     * @param partie la partie sur laquelle agir
     */
    @Override
    public void executer(Partie partie) {
        for (Royaume royaume : partie.tousLesRoyaumes()) {
            royaume.appliquerConsommationCivile(partie.aleatoire());
        }
        partie.notifier(new Notification(TypeNotification.PHASE_CHANGEE, this.nomCle()));
    }

    /**
     * Renvoie la phase suivante, les actions differees.
     *
     * @return la phase des actions differees
     */
    @Override
    public EtatTour suivant() {
        return new EtatActionsDifferees();
    }

    /**
     * Renvoie la cle de traduction du nom de cette phase.
     *
     * @return la cle du nom de la phase
     */
    @Override
    public String nomCle() {
        return "phase.consommation";
    }
}
