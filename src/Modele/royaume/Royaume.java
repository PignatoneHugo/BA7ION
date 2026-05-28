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
 * Royaume controle par un joueur ou un bot. C'est l'agregat central du modele
 * metier : il regroupe le tresor, la population, les batiments et la file
 * d'actions planifiees, et il est le seul {@link Observable} de cette grappe.
 *
 * Convention de notification : chaque methode publique modificatrice se
 * termine par {@code setChanged()} + {@code notifyObservers(new Notification(...))}
 * pour permettre aux vues de se rafraichir. Les sous-composants n'emettent
 * jamais eux-memes : c'est le royaume qui agrege et notifie.
 */
public class Royaume extends Observable {

    private final String nom;
    private final Tresor tresor;
    private final Population population;
    private final List<Batiment> batiments;
    private final FileActions fileActions;

    /**
     * @param nom nom affiche du royaume
     */
    public Royaume(String nom) {
        this.nom = nom;
        this.tresor = new Tresor();
        this.population = new Population();
        this.batiments = new ArrayList<>();
        this.fileActions = new FileActions();

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

    /**
     * Recherche le batiment du type demande dans ce royaume.
     *
     * @return le batiment trouve, ou {@code null} si aucun n'existe
     */
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

    /**
     * Deplace des habitants d'un role vers un autre. Notifie
     * {@code POPULATION_CHANGEE} si l'operation est appliquee.
     *
     * @return {@code true} si la reaffectation a eu lieu
     * @see Population#reaffecter(Role, Role, int)
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
     * Applique la consommation de nourriture des habitants pour un tour. Si
     * le stock est insuffisant, declenche une famine qui retire des habitants
     * proportionnellement au deficit.
     *
     * Notifie systematiquement {@code TRESOR_CHANGE}, puis
     * {@code POPULATION_CHANGEE} si une famine a eu lieu.
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

    /**
     * A appeler par les controleurs apres une modification de la file
     * d'actions (ajout, retrait, vidage) pour declencher le rafraichissement
     * des vues qui l'affichent.
     */
    public void notifierFileActionsChangee() {
        setChanged();
        notifyObservers(new Notification(TypeNotification.FILE_ACTIONS_CHANGEE));
    }

    /**
     * A appeler une fois la production de tous les batiments appliquee, pour
     * que les vues qui affichent le tresor se rafraichissent.
     */
    public void notifierProduction() {
        setChanged();
        notifyObservers(new Notification(TypeNotification.TRESOR_CHANGE));
    }
}
