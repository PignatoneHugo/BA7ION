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

    /**
     * Verifie qu'un ajout dans un stock est plafonne a la capacite.
     */
    @Test
    public void stockAjoutPlafonne() {
        Stock stock = new Stock(Ressource.OR, 50, 100);
        int ajoute = stock.ajouter(80);
        assertEquals("ajout reellement applique (plafonne)", 50, ajoute);
        assertEquals("quantite plafonnee a la capacite", 100, stock.quantite());
    }

    /**
     * Verifie qu'un retrait dans un stock ne descend pas en dessous de 0.
     */
    @Test
    public void stockRetraitPlancher() {
        Stock stock = new Stock(Ressource.BOIS, 30, 100);
        int retire = stock.retirer(200);
        assertEquals("retrait reellement applique", 30, retire);
        assertEquals("quantite plancher a 0", 0, stock.quantite());
    }

    /**
     * Verifie que definirQuantite borne la valeur entre 0 et la capacite.
     */
    @Test
    public void stockDefinirBorne() {
        Stock stock = new Stock(Ressource.PIERRE, 10, 100);
        stock.definirQuantite(999);
        assertEquals("definirQuantite plafonne a la capacite", 100, stock.quantite());
        stock.definirQuantite(-5);
        assertEquals("definirQuantite plancher a 0", 0, stock.quantite());
    }

    /**
     * Verifie que le tresor demarre avec les stocks initiaux d'equilibrage.
     */
    @Test
    public void tresorInitial() {
        Tresor tresor = new Tresor();
        assertEquals("or initial", Equilibrage.stockInitial(Ressource.OR), tresor.quantite(Ressource.OR));
        assertEquals("nourriture initiale", Equilibrage.stockInitial(Ressource.NOURRITURE),
                tresor.quantite(Ressource.NOURRITURE));
    }

    /**
     * Verifie que contient repond juste selon la quantite disponible.
     */
    @Test
    public void tresorContient() {
        Tresor tresor = new Tresor();
        int or = tresor.quantite(Ressource.OR);
        assertTrue("contient exactement le stock", tresor.contient(Ressource.OR, or));
        assertFalse("ne contient pas plus que le stock", tresor.contient(Ressource.OR, or + 1));
    }

    /**
     * Verifie l'or par habitant et l'impact moral de chaque niveau de taxes.
     */
    @Test
    public void niveauxTaxes() {
        assertEquals("or/habitant NORMAL", 2, NiveauTaxes.NORMAL.orParHabitant());
        assertEquals("moral NORMAL", 0, NiveauTaxes.NORMAL.impactMoralParTour());
        assertEquals("or/habitant FAIBLE", 1, NiveauTaxes.FAIBLE.orParHabitant());
        assertTrue("FAIBLE remonte le moral", NiveauTaxes.FAIBLE.impactMoralParTour() > 0);
        assertEquals("or/habitant ELEVE", 3, NiveauTaxes.ELEVE.orParHabitant());
        assertTrue("ELEVE baisse le moral", NiveauTaxes.ELEVE.impactMoralParTour() < 0);
    }

    /**
     * Verifie que les taxes normales rapportent l'or attendu par habitant.
     */
    @Test
    public void taxesNormales() {
        Royaume royaume = new Royaume("Test");
        int orAvant = royaume.tresor().quantite(Ressource.OR);
        int habitants = royaume.population().total();
        royaume.appliquerTaxes();
        assertEquals("or apres taxes normales",
                orAvant + habitants * NiveauTaxes.NORMAL.orParHabitant(),
                royaume.tresor().quantite(Ressource.OR));
    }

    /**
     * Verifie que les taxes faibles font remonter le moral.
     */
    @Test
    public void taxesFaiblesMoral() {
        Royaume royaume = new Royaume("Test");
        royaume.definirNiveauTaxes(NiveauTaxes.FAIBLE);
        int moralAvant = royaume.moral().valeur();
        royaume.appliquerTaxes();
        assertEquals("moral remonte avec taxes faibles",
                moralAvant + NiveauTaxes.FAIBLE.impactMoralParTour(), royaume.moral().valeur());
    }

    /**
     * Verifie la consommation de nourriture sans mort si elle suffit.
     */
    @Test
    public void consommationNormale() {
        Royaume royaume = new Royaume("Test");
        int foodAvant = royaume.tresor().quantite(Ressource.NOURRITURE);
        int popAvant = royaume.population().total();
        royaume.appliquerConsommationCivile(new Random(1));
        assertEquals("nourriture apres consommation",
                foodAvant - popAvant * Equilibrage.CONSOMMATION_NOURRITURE_PAR_HABITANT,
                royaume.tresor().quantite(Ressource.NOURRITURE));
        assertEquals("pas de mort si nourriture suffisante", popAvant, royaume.population().total());
    }

    /**
     * Verifie qu'une famine tue des habitants et fait baisser le moral.
     */
    @Test
    public void famine() {
        Royaume royaume = new Royaume("Test");
        royaume.tresor().definirQuantite(Ressource.NOURRITURE, 0);
        int popAvant = royaume.population().total();
        int moralAvant = royaume.moral().valeur();
        royaume.appliquerConsommationCivile(new Random(1));
        assertTrue("des habitants meurent de faim", royaume.population().total() < popAvant);
        assertTrue("le moral baisse a cause de la famine", royaume.moral().valeur() < moralAvant);
    }
}
