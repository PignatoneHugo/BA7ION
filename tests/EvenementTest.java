import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

import Modele.economie.Ressource;
import Modele.evenement.CatalogueEvenements;
import Modele.evenement.Choix;
import Modele.evenement.EffetSimple;
import Modele.evenement.Evenement;
import Modele.evenement.GrenouilleEmpoisonnee;
import Modele.royaume.Royaume;

// Tests des evenements : effets, choix, catalogue et la grenouille.
public class EvenementTest {

    /**
     * Verifie qu'un effet simple change l'or, la population et le moral.
     */
    @Test
    public void effetApplique() {
        Royaume royaume = new Royaume("Test");
        int orAvant = royaume.tresor().quantite(Ressource.OR);
        int popAvant = royaume.population().total();
        int moralAvant = royaume.moral().valeur();

        new EffetSimple(-50, 2, -5).appliquer(royaume, new Random(1));

        assertEquals("or apres effet", orAvant - 50, royaume.tresor().quantite(Ressource.OR));
        assertEquals("pertes humaines", popAvant - 2, royaume.population().total());
        assertEquals("impact moral", moralAvant - 5, royaume.moral().valeur());
    }

    /**
     * Verifie qu'un effet est applicable seulement si l'or suffit.
     */
    @Test
    public void effetCoutVerifie() {
        Royaume royaume = new Royaume("Test");
        assertTrue("applicable si l'or suffit", new EffetSimple(-500, 0, 0).peutEtreApplique(royaume));
        assertFalse("non applicable si l'or manque", new EffetSimple(-501, 0, 0).peutEtreApplique(royaume));
    }

    /**
     * Verifie qu'un choix est possible seulement s'il est abordable.
     */
    @Test
    public void choixPossible() {
        Royaume royaume = new Royaume("Test");
        assertTrue("choix abordable",
                new Choix("ok", new EffetSimple(-100, 0, 0)).peutEtreChoisi(royaume));
        assertFalse("choix inabordable",
                new Choix("trop cher", new EffetSimple(-9999, 0, 0)).peutEtreChoisi(royaume));
    }

    /**
     * Verifie qu'un choix avec libelle ou effet null est refuse.
     */
    @Test
    public void choixNull() {
        assertThrows("libelle null refuse", IllegalArgumentException.class,
                () -> new Choix(null, new EffetSimple(0, 0, 0)));
        assertThrows("effet null refuse", IllegalArgumentException.class,
                () -> new Choix("x", null));
    }

    /**
     * Verifie que chaque poids est positif et que leur somme egale POIDS_TOTAL.
     */
    @Test
    public void cataloguePoids() {
        int somme = 0;
        for (CatalogueEvenements.Entree entree : CatalogueEvenements.ENTREES) {
            assertTrue("chaque poids est strictement positif", entree.poids > 0);
            somme += entree.poids;
        }
        assertEquals("POIDS_TOTAL = somme des poids", somme, CatalogueEvenements.POIDS_TOTAL);
    }

    /**
     * Verifie que chaque fabrique produit un evenement avec titre et choix.
     */
    @Test
    public void catalogueFabriques() {
        for (CatalogueEvenements.Entree entree : CatalogueEvenements.ENTREES) {
            Evenement evenement = entree.fabrique.get();
            assertNotNull("la fabrique produit un evenement", evenement);
            assertNotNull("l'evenement a un titre", evenement.titre());
            assertFalse("l'evenement propose au moins un choix", evenement.choix().isEmpty());
        }
    }

    /**
     * Verifie que la grenouille empoisonnee propose 3 choix et a un titre.
     */
    @Test
    public void grenouilleChoix() {
        Evenement grenouille = new GrenouilleEmpoisonnee();
        assertEquals("la grenouille propose 3 choix", 3, grenouille.choix().size());
        assertNotNull("titre present", grenouille.titre());
    }
}
