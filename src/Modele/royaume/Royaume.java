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
 * Agregat principal du modele cote royaume. Une instance par participant
 * (joueur + bots). Cette classe est le seul Observable cote "royaume" :
 * ses sous-composants (Tresor, Population) ne notifient pas eux-memes.
 *
 * Convention (cf. plan d'architecture section 10) :
 *   - Toute methode publique modificatrice se termine par {@code setChanged()}
 *     + {@code notifyObservers(new Notification(...))}.
 *   - L'argument de notifyObservers est TOUJOURS une Notification.
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

        // Tous les batiments sont presents au niveau 1 des le tour 1
        // (cf. Epic 3 note preliminaire). Au Sprint 1 on n'instancie que
        // la Ferme ; les 8 autres viendront avec leurs sous-classes.
        this.batiments.add(new Ferme());
    }

    // ----- Accesseurs lecture seule -----

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

    // ----- Operations metier qui notifient -----

    /**
     * Reaffecte des habitants d'un role vers un autre.
     * Notifie POPULATION_CHANGEE si l'operation a reussi.
     */
    public boolean reaffecter(Role source, Role cible, int montant) {
        boolean ok = this.population.reaffecter(source, cible, montant);
        if (ok) {
            setChanged();
            notifyObservers(new Notification(TypeNotification.POPULATION_CHANGEE));
        }
        return ok;
    }

    /**
     * Applique la consommation de nourriture des habitants civils.
     * Si le stock est insuffisant, retire des habitants (famine simplifiee).
     * Notifie TRESOR_CHANGE puis eventuellement POPULATION_CHANGEE.
     */
    public void appliquerConsommationCivile() {
        int total = this.population.total();
        int besoin = total * Equilibrage.CONSOMMATION_NOURRITURE_PAR_HABITANT;
        int retire = this.tresor.retirer(Ressource.NOURRITURE, besoin);
        setChanged();
        notifyObservers(new Notification(TypeNotification.TRESOR_CHANGE));

        int deficit = besoin - retire;
        if (deficit > 0) {
            // Famine simplifiee : 1 habitant meurt par tranche de 5 unites
            // de nourriture manquante (a reequilibrer Sprint 2+).
            int morts = Math.max(1, deficit / 5);
            int retireReellement = this.population.retirerHabitants(morts);
            if (retireReellement > 0) {
                setChanged();
                notifyObservers(new Notification(TypeNotification.POPULATION_CHANGEE));
            }
        }
    }

    /**
     * Notifie un changement non specifie sur la file d'actions.
     * A appeler par les controleurs apres ajout/retrait/vidage.
     */
    public void notifierFileActionsChangee() {
        setChanged();
        notifyObservers(new Notification(TypeNotification.FILE_ACTIONS_CHANGEE));
    }

    /**
     * Notification utilitaire apres production (utilisee par EtatProduction).
     */
    public void notifierProduction() {
        setChanged();
        notifyObservers(new Notification(TypeNotification.TRESOR_CHANGE));
    }
}
