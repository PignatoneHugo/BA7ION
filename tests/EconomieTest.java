import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

import Modele.economie.NiveauTaxes;
import Modele.economie.Ressource;
import Modele.economie.Stock;
import Modele.economie.Tresor;
import Modele.royaume.Royaume;

import config.Equilibrage;

// Tests de l'economie : stocks, tresor, taxes, consommation et famine.
public class EconomieTest {

    @Test
    public void stockAjoutPlafonne() {
        Stock s = new Stock(Ressource.OR, 50, 100);
        int ajoute = s.ajouter(80);
        assertEquals("ajout reellement applique (plafonne)", 50, ajoute);
        assertEquals("quantite plafonnee a la capacite", 100, s.quantite());
    }

    @Test
    public void stockRetraitPlancher() {
        Stock s = new Stock(Ressource.BOIS, 30, 100);
        int retire = s.retirer(200);
        assertEquals("retrait reellement applique", 30, retire);
        assertEquals("quantite plancher a 0", 0, s.quantite());
    }

    @Test
    public void stockDefinirBorne() {
        Stock s = new Stock(Ressource.PIERRE, 10, 100);
        s.definirQuantite(999);
        assertEquals("definirQuantite plafonne a la capacite", 100, s.quantite());
        s.definirQuantite(-5);
        assertEquals("definirQuantite plancher a 0", 0, s.quantite());
    }

    @Test
    public void tresorInitial() {
        Tresor t = new Tresor();
        assertEquals("or initial", Equilibrage.stockInitial(Ressource.OR), t.quantite(Ressource.OR));
        assertEquals("nourriture initiale", Equilibrage.stockInitial(Ressource.NOURRITURE),
                t.quantite(Ressource.NOURRITURE));
    }

    @Test
    public void tresorContient() {
        Tresor t = new Tresor();
        int or = t.quantite(Ressource.OR);
        assertTrue("contient exactement le stock", t.contient(Ressource.OR, or));
        assertFalse("ne contient pas plus que le stock", t.contient(Ressource.OR, or + 1));
    }

    @Test
    public void niveauxTaxes() {
        assertEquals("or/habitant NORMAL", 2, NiveauTaxes.NORMAL.orParHabitant());
        assertEquals("moral NORMAL", 0, NiveauTaxes.NORMAL.impactMoralParTour());
        assertEquals("or/habitant FAIBLE", 1, NiveauTaxes.FAIBLE.orParHabitant());
        assertTrue("FAIBLE remonte le moral", NiveauTaxes.FAIBLE.impactMoralParTour() > 0);
        assertEquals("or/habitant ELEVE", 3, NiveauTaxes.ELEVE.orParHabitant());
        assertTrue("ELEVE baisse le moral", NiveauTaxes.ELEVE.impactMoralParTour() < 0);
    }

    @Test
    public void taxesNormales() {
        Royaume r = new Royaume("Test");
        int orAvant = r.tresor().quantite(Ressource.OR);
        int habitants = r.population().total();
        r.appliquerTaxes();
        assertEquals("or apres taxes normales",
                orAvant + habitants * NiveauTaxes.NORMAL.orParHabitant(),
                r.tresor().quantite(Ressource.OR));
    }

    @Test
    public void taxesFaiblesMoral() {
        Royaume r = new Royaume("Test");
        r.definirNiveauTaxes(NiveauTaxes.FAIBLE);
        int moralAvant = r.moral().valeur();
        r.appliquerTaxes();
        assertEquals("moral remonte avec taxes faibles",
                moralAvant + NiveauTaxes.FAIBLE.impactMoralParTour(), r.moral().valeur());
    }

    @Test
    public void consommationNormale() {
        Royaume r = new Royaume("Test");
        int foodAvant = r.tresor().quantite(Ressource.NOURRITURE);
        int popAvant = r.population().total();
        r.appliquerConsommationCivile(new Random(1));
        assertEquals("nourriture apres consommation",
                foodAvant - popAvant * Equilibrage.CONSOMMATION_NOURRITURE_PAR_HABITANT,
                r.tresor().quantite(Ressource.NOURRITURE));
        assertEquals("pas de mort si nourriture suffisante", popAvant, r.population().total());
    }

    @Test
    public void famine() {
        Royaume r = new Royaume("Test");
        r.tresor().definirQuantite(Ressource.NOURRITURE, 0);
        int popAvant = r.population().total();
        int moralAvant = r.moral().valeur();
        r.appliquerConsommationCivile(new Random(1));
        assertTrue("des habitants meurent de faim", r.population().total() < popAvant);
        assertTrue("le moral baisse a cause de la famine", r.moral().valeur() < moralAvant);
    }
}
