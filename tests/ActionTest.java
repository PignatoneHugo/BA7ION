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

    @Test
    public void ameliorer() {
        Royaume r = new Royaume("Test");
        ActionAmeliorer a = new ActionAmeliorer(TypeBatiment.FERME);
        assertTrue("ferme niveau 1 ameliorable", a.estExecutable(r));
        a.executer(r);
        assertTrue("chantier demarre", r.batiment(TypeBatiment.FERME).enChantier());
    }

    @Test
    public void mobiliserConditions() {
        Royaume r = new Royaume("Test");
        ActionMobiliser a = new ActionMobiliser(TypeUnite.INFANTERIE_LEGERE, 2);
        assertFalse("refuse sans recrues (Role.SOLDAT)", a.estExecutable(r));
        r.reaffecter(Role.INACTIF, Role.SOLDAT, 2);
        assertTrue("accepte avec or + recrues + caserne niveau 1", a.estExecutable(r));
        assertThrows("effectif 0 refuse", IllegalArgumentException.class,
                () -> new ActionMobiliser(TypeUnite.INFANTERIE_LEGERE, 0));
    }

    @Test
    public void mobiliserEffet() {
        Royaume r = new Royaume("Test");
        r.reaffecter(Role.INACTIF, Role.SOLDAT, 2);
        int orAvant = r.tresor().quantite(Ressource.OR);
        new ActionMobiliser(TypeUnite.INFANTERIE_LEGERE, 2).executer(r);
        assertEquals("or paye", orAvant - 2 * Equilibrage.COUT_OR_PAR_SOLDAT,
                r.tresor().quantite(Ressource.OR));
        assertEquals("2 unites equipees", 2,
                r.armee().effectifParType(TypeUnite.INFANTERIE_LEGERE));
    }

    @Test
    public void mobiliserCaserne() {
        Royaume r = new Royaume("Test");
        r.reaffecter(Role.INACTIF, Role.SOLDAT, 1);
        ActionMobiliser a = new ActionMobiliser(TypeUnite.ARCHER, 1);
        assertFalse("archer refuse si caserne trop basse", a.estExecutable(r));
    }

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

    @Test
    public void attaquerEffet() {
        Royaume attaquant = new Royaume("A");
        Royaume cible = new Royaume("B");
        attaquant.armee().recruter(TypeUnite.INFANTERIE_LEGERE, 5);
        new ActionAttaquer(cible).executer(attaquant);
        assertEquals("une bataille offensive planifiee", 1, attaquant.bataillesOffensives().size());
    }

    @Test
    public void echanger() {
        Royaume r = new Royaume("Test");
        int orAvant = r.tresor().quantite(Ressource.OR);
        int boisAvant = r.tresor().quantite(Ressource.BOIS);
        ActionEchanger a = new ActionEchanger(Ressource.OR, Ressource.BOIS, 9);
        assertTrue("echange possible au marche niveau 1", a.estExecutable(r));
        a.executer(r);
        assertEquals("or donne", orAvant - 9, r.tresor().quantite(Ressource.OR));
        assertTrue("bois recu", r.tresor().quantite(Ressource.BOIS) > boisAvant);
    }

    @Test
    public void echangerInvalide() {
        assertThrows("source et cible identiques refusees", IllegalArgumentException.class,
                () -> new ActionEchanger(Ressource.OR, Ressource.OR, 10));
    }

    @Test
    public void recruter() {
        Royaume r = new Royaume("Test");
        ActionRecruterVillageois a = new ActionRecruterVillageois();
        assertTrue("recrutement possible (nourriture + place)", a.estExecutable(r));

        Royaume sansVivres = new Royaume("Famine");
        sansVivres.tresor().definirQuantite(Ressource.NOURRITURE,
                Equilibrage.COUT_NOURRITURE_PAR_VILLAGEOIS - 1);
        assertFalse("refuse sans assez de nourriture", a.estExecutable(sansVivres));

        Royaume plein = new Royaume("Plein");
        plein.population().definirEffectif(Role.INACTIF, plein.population().capaciteLogement());
        assertFalse("refuse si logement plein", a.estExecutable(plein));
    }
}
