import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import Modele.action.ActionAmeliorer;
import Modele.action.ActionAttaquer;
import Modele.action.ActionEchanger;
import Modele.action.ActionMobiliser;
import Modele.action.ActionRecruterVillageois;
import Modele.economie.Ressource;
import Modele.infrastructure.TypeBatiment;
import Modele.militaire.TypeUnite;
import Modele.population.Role;
import Modele.royaume.Royaume;

import config.Equilibrage;

// Tests des Actions (pattern Command) : estExecutable et executer.
public class ActionTest {

    /**
     * Verifie qu'ameliorer une ferme de niveau 1 demarre un chantier.
     */
    @Test
    public void ameliorer() {
        Royaume royaume = new Royaume("Test");
        ActionAmeliorer action = new ActionAmeliorer(TypeBatiment.FERME);
        assertTrue("ferme niveau 1 ameliorable", action.estExecutable(royaume));
        action.executer(royaume);
        assertTrue("chantier demarre", royaume.batiment(TypeBatiment.FERME).enChantier());
    }

    /**
     * Verifie les conditions de mobilisation d'unites et le refus d'un effectif nul.
     */
    @Test
    public void mobiliserConditions() {
        Royaume royaume = new Royaume("Test");
        ActionMobiliser action = new ActionMobiliser(TypeUnite.INFANTERIE_LEGERE, 2);
        assertFalse("refuse sans recrues (Role.SOLDAT)", action.estExecutable(royaume));
        royaume.reaffecter(Role.INACTIF, Role.SOLDAT, 2);
        assertTrue("accepte avec or + recrues + caserne niveau 1", action.estExecutable(royaume));
        assertThrows("effectif 0 refuse", IllegalArgumentException.class,
                () -> new ActionMobiliser(TypeUnite.INFANTERIE_LEGERE, 0));
    }

    /**
     * Verifie qu'une mobilisation paye l'or et equipe les unites.
     */
    @Test
    public void mobiliserEffet() {
        Royaume royaume = new Royaume("Test");
        royaume.reaffecter(Role.INACTIF, Role.SOLDAT, 2);
        int orAvant = royaume.tresor().quantite(Ressource.OR);
        new ActionMobiliser(TypeUnite.INFANTERIE_LEGERE, 2).executer(royaume);
        assertEquals("or paye", orAvant - 2 * Equilibrage.COUT_OR_PAR_SOLDAT,
                royaume.tresor().quantite(Ressource.OR));
        assertEquals("2 unites equipees", 2,
                royaume.armee().effectifParType(TypeUnite.INFANTERIE_LEGERE));
    }

    /**
     * Verifie qu'un archer est refuse si la caserne est de niveau trop bas.
     */
    @Test
    public void mobiliserCaserne() {
        Royaume royaume = new Royaume("Test");
        royaume.reaffecter(Role.INACTIF, Role.SOLDAT, 1);
        ActionMobiliser action = new ActionMobiliser(TypeUnite.ARCHER, 1);
        assertFalse("archer refuse si caserne trop basse", action.estExecutable(royaume));
    }

    /**
     * Verifie les conditions d'attaque et qu'on ne peut pas s'attaquer soi-meme.
     */
    @Test
    public void attaquerConditions() {
        Royaume attaquant = new Royaume("A");
        Royaume cible = new Royaume("B");
        ActionAttaquer sansArmee = new ActionAttaquer(cible);
        assertFalse("refuse sans armee", sansArmee.estExecutable(attaquant));
        attaquant.armee().recruter(TypeUnite.INFANTERIE_LEGERE, 5);
        assertTrue("accepte avec une armee", sansArmee.estExecutable(attaquant));
        assertFalse("on ne s'attaque pas soi-meme",
                new ActionAttaquer(attaquant).estExecutable(attaquant));
    }

    /**
     * Verifie qu'executer une attaque planifie une bataille offensive.
     */
    @Test
    public void attaquerEffet() {
        Royaume attaquant = new Royaume("A");
        Royaume cible = new Royaume("B");
        attaquant.armee().recruter(TypeUnite.INFANTERIE_LEGERE, 5);
        new ActionAttaquer(cible).executer(attaquant);
        assertEquals("une bataille offensive planifiee", 1, attaquant.bataillesOffensives().size());
    }

    /**
     * Verifie qu'un echange au marche donne de l'or et recoit du bois.
     */
    @Test
    public void echanger() {
        Royaume royaume = new Royaume("Test");
        int orAvant = royaume.tresor().quantite(Ressource.OR);
        int boisAvant = royaume.tresor().quantite(Ressource.BOIS);
        ActionEchanger action = new ActionEchanger(Ressource.OR, Ressource.BOIS, 9);
        assertTrue("echange possible au marche niveau 1", action.estExecutable(royaume));
        action.executer(royaume);
        assertEquals("or donne", orAvant - 9, royaume.tresor().quantite(Ressource.OR));
        assertTrue("bois recu", royaume.tresor().quantite(Ressource.BOIS) > boisAvant);
    }

    /**
     * Verifie qu'un echange avec source et cible identiques est refuse.
     */
    @Test
    public void echangerInvalide() {
        assertThrows("source et cible identiques refusees", IllegalArgumentException.class,
                () -> new ActionEchanger(Ressource.OR, Ressource.OR, 10));
    }

    /**
     * Verifie le recrutement d'un villageois selon la nourriture et le logement.
     */
    @Test
    public void recruter() {
        Royaume royaume = new Royaume("Test");
        ActionRecruterVillageois action = new ActionRecruterVillageois();
        assertTrue("recrutement possible (nourriture + place)", action.estExecutable(royaume));

        Royaume sansVivres = new Royaume("Famine");
        sansVivres.tresor().definirQuantite(Ressource.NOURRITURE,
                Equilibrage.COUT_NOURRITURE_PAR_VILLAGEOIS - 1);
        assertFalse("refuse sans assez de nourriture", action.estExecutable(sansVivres));

        Royaume plein = new Royaume("Plein");
        plein.population().definirEffectif(Role.INACTIF, plein.population().capaciteLogement());
        assertFalse("refuse si logement plein", action.estExecutable(plein));
    }
}
