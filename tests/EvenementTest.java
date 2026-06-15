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

    @Test
    public void effetApplique() {
        Royaume r = new Royaume("Test");
        int orAvant = r.tresor().quantite(Ressource.OR);
        int popAvant = r.population().total();
        int moralAvant = r.moral().valeur();

        new EffetSimple(-50, 2, -5).appliquer(r, new Random(1));

        assertEquals("or apres effet", orAvant - 50, r.tresor().quantite(Ressource.OR));
        assertEquals("pertes humaines", popAvant - 2, r.population().total());
        assertEquals("impact moral", moralAvant - 5, r.moral().valeur());
    }

    @Test
    public void effetCoutVerifie() {
        Royaume r = new Royaume("Test");
        assertTrue("applicable si l'or suffit", new EffetSimple(-500, 0, 0).peutEtreApplique(r));
        assertFalse("non applicable si l'or manque", new EffetSimple(-501, 0, 0).peutEtreApplique(r));
    }

    @Test
    public void choixPossible() {
        Royaume r = new Royaume("Test");
        assertTrue("choix abordable",
                new Choix("ok", new EffetSimple(-100, 0, 0)).peutEtreChoisi(r));
        assertFalse("choix inabordable",
                new Choix("trop cher", new EffetSimple(-9999, 0, 0)).peutEtreChoisi(r));
    }

    @Test
    public void choixNull() {
        assertThrows("libelle null refuse", IllegalArgumentException.class,
                () -> new Choix(null, new EffetSimple(0, 0, 0)));
        assertThrows("effet null refuse", IllegalArgumentException.class,
                () -> new Choix("x", null));
    }

    @Test
    public void cataloguePoids() {
        int somme = 0;
        for (CatalogueEvenements.Entree e : CatalogueEvenements.ENTREES) {
            assertTrue("chaque poids est strictement positif", e.poids > 0);
            somme += e.poids;
        }
        assertEquals("POIDS_TOTAL = somme des poids", somme, CatalogueEvenements.POIDS_TOTAL);
    }

    @Test
    public void catalogueFabriques() {
        for (CatalogueEvenements.Entree e : CatalogueEvenements.ENTREES) {
            Evenement ev = e.fabrique.get();
            assertNotNull("la fabrique produit un evenement", ev);
            assertNotNull("l'evenement a un titre", ev.titre());
            assertFalse("l'evenement propose au moins un choix", ev.choix().isEmpty());
        }
    }

    @Test
    public void grenouilleChoix() {
        Evenement g = new GrenouilleEmpoisonnee();
        assertEquals("la grenouille propose 3 choix", 3, g.choix().size());
        assertNotNull("titre present", g.titre());
    }
}
