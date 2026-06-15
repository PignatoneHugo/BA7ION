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

    // Cherche le batiment du type donne, null si absent.
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

    public Armee armee() {
        return this.armee;
    }

    public StrategieIA strategieIA() {
        return this.strategieIA;
    }

    public void definirStrategieIA(StrategieIA strategie) {
        this.strategieIA = strategie;
    }

    // True si c'est un bot.
    public boolean estBot() {
        return this.strategieIA != null;
    }

    public void notifierArmeeChangee() {
        setChanged();
        notifyObservers(new Notification(TypeNotification.BATIMENTS_CHANGES));
    }

    public List<Bataille> bataillesOffensives() {
        return this.bataillesOffensives;
    }

    public void ajouterBatailleOffensive(Bataille bataille) {
        if (bataille != null) {
            this.bataillesOffensives.add(bataille);
        }
    }

    // True si une attaque contre cette cible est deja prevue.
    public boolean aAttaquePlanifieeContre(Royaume cible) {
        for (Bataille b : this.bataillesOffensives) {
            if (b.defenseur() == cible) {
                return true;
            }
        }
        return false;
    }

    public void viderBataillesOffensives() {
        this.bataillesOffensives.clear();
    }

    // Deplace des habitants d'un role a un autre.
    public boolean reaffecter(Role source, Role cible, int montant) {
        boolean ok = this.population.reaffecter(source, cible, montant);
        if (ok) {
            setChanged();
            notifyObservers(new Notification(TypeNotification.POPULATION_CHANGEE));
        }
        return ok;
    }

    // Retire la nourriture des habitants. Si manque, famine (1 mort / 5 manquants).
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

    // Collecte les taxes et applique leur effet sur le moral.
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
    public void notifierFileActionsChangee() {
        setChanged();
        notifyObservers(new Notification(TypeNotification.FILE_ACTIONS_CHANGEE));
    }

    public void notifierProduction() {
        setChanged();
        notifyObservers(new Notification(TypeNotification.TRESOR_CHANGE));
    }

    public void notifierTresorChange() {
        setChanged();
        notifyObservers(new Notification(TypeNotification.TRESOR_CHANGE));
    }

    public void notifierBatimentsChanges() {
        setChanged();
        notifyObservers(new Notification(TypeNotification.BATIMENTS_CHANGES));
    }

    public void notifierPopulationChangee() {
        setChanged();
        notifyObservers(new Notification(TypeNotification.POPULATION_CHANGEE));
    }

    public void notifierMoralChange() {
        setChanged();
        notifyObservers(new Notification(TypeNotification.MORAL_CHANGE));
    }
}
