package Modele.royaume;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import Modele.action.FileActions;
import Modele.economie.Ressource;
import Modele.economie.Tresor;
import Modele.infrastructure.Batiment;
import Modele.infrastructure.Ferme;
import Modele.infrastructure.TypeBatiment;
import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.population.Population;
import Modele.population.Role;

import config.Equilibrage;

/**
 * Royaume du joueur ou d'un bot. Contient le tresor, la population,
 * les batiments et la file d'actions planifiees.
 *
 * Seul Observable cote royaume : Tresor et Population n'envoient pas
 * de notifications eux-memes, c'est le Royaume qui notifie pour eux.
 */
public class Royaume extends Observable {

    private final String nom;
    private final Tresor tresor;
    private final Population population;
    private final List<Batiment> batiments;
    private final FileActions fileActions;

    public Royaume(String nom) {
        this.nom = nom;
        this.tresor = new Tresor();
        this.population = new Population();
        this.batiments = new ArrayList<>();
        this.fileActions = new FileActions();

        // Au Sprint 1, seule la Ferme est instanciee.
        this.batiments.add(new Ferme());
    }

    public String nom() {
        return this.nom;
    }

    public Tresor tresor() {
        return this.tresor;
    }

    public Population population() {
        return this.population;
    }

    public List<Batiment> batiments() {
        return this.batiments;
    }

    /** Retourne le batiment du type demande, ou null si pas trouve. */
    public Batiment batiment(TypeBatiment type) {
        for (Batiment b : this.batiments) {
            if (b.type() == type) {
                return b;
            }
        }
        return null;
    }

    public FileActions fileActions() {
        return this.fileActions;
    }

    /** Deplace des habitants d'un role vers un autre. Notifie si OK. */
    public boolean reaffecter(Role source, Role cible, int montant) {
        boolean ok = this.population.reaffecter(source, cible, montant);
        if (ok) {
            setChanged();
            notifyObservers(new Notification(TypeNotification.POPULATION_CHANGEE));
        }
        return ok;
    }

    /**
     * Retire la nourriture necessaire aux habitants. En cas de penurie,
     * declenche une famine (1 mort par 5 unites manquantes).
     */
    public void appliquerConsommationCivile() {
        int total = this.population.total();
        int besoin = total * Equilibrage.CONSOMMATION_NOURRITURE_PAR_HABITANT;
        int retire = this.tresor.retirer(Ressource.NOURRITURE, besoin);
        setChanged();
        notifyObservers(new Notification(TypeNotification.TRESOR_CHANGE));

        int deficit = besoin - retire;
        if (deficit > 0) {
            int morts = Math.max(1, deficit / 5);
            int retireReellement = this.population.retirerHabitants(morts);
            if (retireReellement > 0) {
                setChanged();
                notifyObservers(new Notification(TypeNotification.POPULATION_CHANGEE));
            }
        }
    }

    /** A appeler apres modification de la file d'actions (ajout/retrait). */
    public void notifierFileActionsChangee() {
        setChanged();
        notifyObservers(new Notification(TypeNotification.FILE_ACTIONS_CHANGEE));
    }

    /** A appeler apres la production de tous les batiments du royaume. */
    public void notifierProduction() {
        setChanged();
        notifyObservers(new Notification(TypeNotification.TRESOR_CHANGE));
    }
}
