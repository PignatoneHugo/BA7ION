package Modele.royaume;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import Modele.action.FileActions;
import Modele.economie.NiveauTaxes;
import Modele.economie.Ressource;
import Modele.economie.Tresor;
import Modele.infrastructure.Batiment;
import Modele.infrastructure.Bibliotheque;
import Modele.infrastructure.Caserne;
import Modele.infrastructure.Ferme;
import Modele.infrastructure.Habitations;
import Modele.infrastructure.Marche;
import Modele.infrastructure.Mine;
import Modele.infrastructure.Remparts;
import Modele.infrastructure.Scierie;
import Modele.infrastructure.TourDeGuet;
import Modele.infrastructure.TypeBatiment;
import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.population.Moral;
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
    private final Moral moral;
    private final List<Batiment> batiments;
    private final FileActions fileActions;
    private NiveauTaxes niveauTaxes;

    public Royaume(String nom) {
        this.nom = nom;
        this.tresor = new Tresor();
        this.population = new Population();
        this.moral = new Moral();
        this.batiments = new ArrayList<>();
        this.fileActions = new FileActions();
        this.niveauTaxes = NiveauTaxes.NORMAL;

        // Tous les batiments sont presents au niveau 1 des le tour 1.
        this.batiments.add(new Ferme());
        this.batiments.add(new Mine());
        this.batiments.add(new Scierie());
        this.batiments.add(new Habitations());
        this.batiments.add(new Caserne());
        this.batiments.add(new Remparts());
        this.batiments.add(new Marche());
        this.batiments.add(new Bibliotheque());
        this.batiments.add(new TourDeGuet());
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

    public Moral moral() {
        return this.moral;
    }

    public NiveauTaxes niveauTaxes() {
        return this.niveauTaxes;
    }

    /** Change le niveau de taxes et notifie. */
    public void definirNiveauTaxes(NiveauTaxes niveau) {
        if (niveau == null || niveau == this.niveauTaxes) {
            return;
        }
        this.niveauTaxes = niveau;
        setChanged();
        notifyObservers(new Notification(TypeNotification.TRESOR_CHANGE));
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

                // La famine fait baisser le moral.
                this.moral.ajuster(Equilibrage.IMPACT_MORAL_PAR_FAMINE * retireReellement);
                setChanged();
                notifyObservers(new Notification(TypeNotification.MORAL_CHANGE));
            }
        }
    }

    /**
     * Collecte les taxes et applique leur effet sur le moral.
     * Appele a chaque tour, generalement pendant la phase de production.
     */
    public void appliquerTaxes() {
        int habitants = this.population.total();
        int revenu = habitants * this.niveauTaxes.orParHabitant();
        if (revenu > 0) {
            this.tresor.ajouter(Ressource.OR, revenu);
            setChanged();
            notifyObservers(new Notification(TypeNotification.TRESOR_CHANGE));
        }

        int delta = this.niveauTaxes.impactMoralParTour();
        if (delta != 0) {
            this.moral.ajuster(delta);
            setChanged();
            notifyObservers(new Notification(TypeNotification.MORAL_CHANGE));
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

    /** A appeler apres une modification directe du tresor (ex : cout d'une action). */
    public void notifierTresorChange() {
        setChanged();
        notifyObservers(new Notification(TypeNotification.TRESOR_CHANGE));
    }

    /** A appeler apres un changement sur les batiments (chantier avance/termine). */
    public void notifierBatimentsChanges() {
        setChanged();
        notifyObservers(new Notification(TypeNotification.BATIMENTS_CHANGES));
    }

    /** A appeler apres une modification directe de la population. */
    public void notifierPopulationChangee() {
        setChanged();
        notifyObservers(new Notification(TypeNotification.POPULATION_CHANGEE));
    }

    /** A appeler apres une modification directe du moral. */
    public void notifierMoralChange() {
        setChanged();
        notifyObservers(new Notification(TypeNotification.MORAL_CHANGE));
    }
}
