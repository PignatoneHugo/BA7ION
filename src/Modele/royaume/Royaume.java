package Modele.royaume;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

import Modele.action.FileActions;
import Modele.combat.Bataille;
import Modele.economie.NiveauTaxes;
import Modele.economie.Ressource;
import Modele.economie.Tresor;
import Modele.ia.StrategieIA;
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
import Modele.militaire.Armee;
import Modele.notification.Notification;
import Modele.notification.TypeNotification;
import Modele.population.Moral;
import Modele.population.Population;
import Modele.population.Role;

import config.Equilibrage;

// Royaume du joueur ou d'un bot : tresor, population, batiments, armee.
public class Royaume extends Observable {

    private final String nom;
    private final Tresor tresor;
    private final Population population;
    private final Moral moral;
    private final List<Batiment> batiments;
    private final FileActions fileActions;
    private final Armee armee;
    private final List<Bataille> bataillesOffensives;
    private NiveauTaxes niveauTaxes;
    private StrategieIA strategieIA;

    /**
     * Cree un royaume avec son nom et tous ses batiments au niveau 1.
     *
     * @param nom le nom du royaume
     */
    public Royaume(String nom) {
        this.nom = nom;
        this.tresor = new Tresor();
        this.population = new Population();
        this.moral = new Moral();
        this.batiments = new ArrayList<>();
        this.fileActions = new FileActions();
        this.armee = new Armee();
        this.bataillesOffensives = new ArrayList<>();
        this.niveauTaxes = NiveauTaxes.NORMAL;
        this.strategieIA = null;

        // Tous les batiments au niveau 1 des le depart.
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

    /**
     * Renvoie le nom du royaume.
     *
     * @return le nom du royaume
     */
    public String nom() {
        return this.nom;
    }

    /**
     * Renvoie le tresor du royaume.
     *
     * @return le tresor du royaume
     */
    public Tresor tresor() {
        return this.tresor;
    }

    /**
     * Renvoie la population du royaume.
     *
     * @return la population du royaume
     */
    public Population population() {
        return this.population;
    }

    /**
     * Renvoie le moral du royaume.
     *
     * @return le moral du royaume
     */
    public Moral moral() {
        return this.moral;
    }

    /**
     * Renvoie le niveau de taxes actuel.
     *
     * @return le niveau de taxes du royaume
     */
    public NiveauTaxes niveauTaxes() {
        return this.niveauTaxes;
    }

    /**
     * Change le niveau de taxes du royaume et previent les vues.
     *
     * @param niveau le nouveau niveau de taxes
     */
    public void definirNiveauTaxes(NiveauTaxes niveau) {
        if (niveau == null || niveau == this.niveauTaxes) {
            return;
        }
        this.niveauTaxes = niveau;
        setChanged();
        notifyObservers(new Notification(TypeNotification.TRESOR_CHANGE));
    }

    /**
     * Renvoie la liste des batiments du royaume.
     *
     * @return la liste des batiments
     */
    public List<Batiment> batiments() {
        return this.batiments;
    }

    /**
     * Cherche le batiment correspondant a un type donne.
     *
     * @param type le type de batiment recherche
     * @return le batiment de ce type, ou null s'il n'existe pas
     */
    public Batiment batiment(TypeBatiment type) {
        for (Batiment batiment : this.batiments) {
            if (batiment.type() == type) {
                return batiment;
            }
        }
        return null;
    }

    /**
     * Renvoie la file des actions planifiees du royaume.
     *
     * @return la file des actions
     */
    public FileActions fileActions() {
        return this.fileActions;
    }

    /**
     * Renvoie l'armee du royaume.
     *
     * @return l'armee du royaume
     */
    public Armee armee() {
        return this.armee;
    }

    /**
     * Renvoie la strategie IA du royaume.
     *
     * @return la strategie IA, ou null si ce n'est pas un bot
     */
    public StrategieIA strategieIA() {
        return this.strategieIA;
    }

    /**
     * Definit la strategie IA du royaume.
     *
     * @param strategie la strategie IA a appliquer
     */
    public void definirStrategieIA(StrategieIA strategie) {
        this.strategieIA = strategie;
    }

    /**
     * Indique si ce royaume est controle par l'IA.
     *
     * @return true si c'est un bot, false sinon
     */
    public boolean estBot() {
        return this.strategieIA != null;
    }

    /**
     * Previent les vues que l'armee a change.
     */
    public void notifierArmeeChangee() {
        setChanged();
        notifyObservers(new Notification(TypeNotification.BATIMENTS_CHANGES));
    }

    /**
     * Renvoie la liste des batailles offensives prevues par ce royaume.
     *
     * @return la liste des batailles offensives
     */
    public List<Bataille> bataillesOffensives() {
        return this.bataillesOffensives;
    }

    /**
     * Ajoute une bataille offensive a la liste si elle n'est pas null.
     *
     * @param bataille la bataille a ajouter
     */
    public void ajouterBatailleOffensive(Bataille bataille) {
        if (bataille != null) {
            this.bataillesOffensives.add(bataille);
        }
    }

    /**
     * Indique si une attaque contre une cible donnee est deja prevue.
     *
     * @param cible le royaume vise
     * @return true si une attaque contre cette cible existe deja
     */
    public boolean aAttaquePlanifieeContre(Royaume cible) {
        for (Bataille bataille : this.bataillesOffensives) {
            if (bataille.defenseur() == cible) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vide la liste des batailles offensives prevues.
     */
    public void viderBataillesOffensives() {
        this.bataillesOffensives.clear();
    }

    /**
     * Deplace des habitants d'un role vers un autre et previent les vues.
     *
     * @param source le role d'origine des habitants
     * @param cible le role de destination
     * @param montant le nombre d'habitants a deplacer
     * @return true si la reaffectation a reussi
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
     * Retire la nourriture mangee par les habitants. En cas de manque, declenche
     * une famine qui tue des habitants et baisse le moral.
     *
     * @param aleatoire le generateur aleatoire utilise pour choisir les morts
     */
    public void appliquerConsommationCivile(Random aleatoire) {
        int total = this.population.total();
        int besoin = total * Equilibrage.CONSOMMATION_NOURRITURE_PAR_HABITANT;
        int retire = this.tresor.retirer(Ressource.NOURRITURE, besoin);
        setChanged();
        notifyObservers(new Notification(TypeNotification.TRESOR_CHANGE));

        int deficit = besoin - retire;
        if (deficit > 0) {
            int morts = Math.max(1, deficit / 5);
            int retireReellement = this.population.retirerHabitants(morts, aleatoire);
            if (retireReellement > 0) {
                setChanged();
                notifyObservers(new Notification(TypeNotification.POPULATION_CHANGEE));

                // La famine baisse le moral.
                this.moral.ajuster(Equilibrage.IMPACT_MORAL_PAR_FAMINE * retireReellement);
                setChanged();
                notifyObservers(new Notification(TypeNotification.MORAL_CHANGE));
            }
        }
    }

    /**
     * Collecte l'or des taxes et applique leur effet sur le moral.
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

    // Petits helpers pour prevenir les vues quand quelque chose change.

    /**
     * Previent les vues que la file des actions a change.
     */
    public void notifierFileActionsChangee() {
        setChanged();
        notifyObservers(new Notification(TypeNotification.FILE_ACTIONS_CHANGEE));
    }

    /**
     * Previent les vues qu'une production a eu lieu.
     */
    public void notifierProduction() {
        setChanged();
        notifyObservers(new Notification(TypeNotification.TRESOR_CHANGE));
    }

    /**
     * Previent les vues que le tresor a change.
     */
    public void notifierTresorChange() {
        setChanged();
        notifyObservers(new Notification(TypeNotification.TRESOR_CHANGE));
    }

    /**
     * Previent les vues que les batiments ont change.
     */
    public void notifierBatimentsChanges() {
        setChanged();
        notifyObservers(new Notification(TypeNotification.BATIMENTS_CHANGES));
    }

    /**
     * Previent les vues que la population a change.
     */
    public void notifierPopulationChangee() {
        setChanged();
        notifyObservers(new Notification(TypeNotification.POPULATION_CHANGEE));
    }

    /**
     * Previent les vues que le moral a change.
     */
    public void notifierMoralChange() {
        setChanged();
        notifyObservers(new Notification(TypeNotification.MORAL_CHANGE));
    }
}
