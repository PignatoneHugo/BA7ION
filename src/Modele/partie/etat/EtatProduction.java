package Modele.partie.etat;

import Modele.infrastructure.Batiment;
import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.partie.Partie;
import Modele.royaume.Royaume;

// Phase de production : chaque batiment produit ses ressources.
public class EtatProduction implements EtatTour {

    /**
     * Fait produire chaque batiment et applique les taxes de tous les royaumes.
     *
     * @param partie la partie sur laquelle agir
     */
    @Override
    public void executer(Partie partie) {
        for (Royaume royaume : partie.tousLesRoyaumes()) {
            for (Batiment batiment : royaume.batiments()) {
                batiment.produire(royaume);
            }
            royaume.appliquerTaxes();
            royaume.notifierProduction();
            royaume.notifierBatimentsChanges();
        }
        partie.notifier(new Notification(TypeNotification.PHASE_CHANGEE, this.nomCle()));
    }

    /**
     * Renvoie la phase suivante, la consommation.
     *
     * @return la phase de consommation
     */
    @Override
    public EtatTour suivant() {
        return new EtatConsommation();
    }

    /**
     * Renvoie la cle de traduction du nom de cette phase.
     *
     * @return la cle du nom de la phase
     */
    @Override
    public String nomCle() {
        return "phase.production";
    }
}
