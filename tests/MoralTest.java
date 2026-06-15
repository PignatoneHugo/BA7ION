import static org.junit.Assert.assertEquals;

import org.junit.Test;

import Modele.population.Moral;

import config.Equilibrage;

// Tests du moral : valeur initiale, bornage et variation renvoyee par ajuster.
public class MoralTest {

    @Test
    public void initial() {
        assertEquals("moral initial", Equilibrage.MORAL_INITIAL, new Moral().valeur());
    }

    @Test
    public void ajusterPositif() {
        Moral m = new Moral();
        m.ajuster(10);
        assertEquals("moral apres +10", Equilibrage.MORAL_INITIAL + 10, m.valeur());
    }

    @Test
    public void plafond() {
        Moral m = new Moral();
        m.ajuster(1000);
        assertEquals("moral plafonne", Equilibrage.MORAL_MAX, m.valeur());
    }

    @Test
    public void plancher() {
        Moral m = new Moral();
        m.ajuster(-1000);
        assertEquals("moral plancher", Equilibrage.MORAL_MIN, m.valeur());
    }

    @Test
    public void variationReelle() {
        Moral m = new Moral();
        int delta = m.ajuster(1000);
        assertEquals("variation reellement appliquee",
                Equilibrage.MORAL_MAX - Equilibrage.MORAL_INITIAL, delta);
    }

    @Test
    public void definir() {
        Moral m = new Moral();
        m.definir(75);
        assertEquals("definir fixe la valeur", 75, m.valeur());
        m.definir(999);
        assertEquals("definir plafonne", Equilibrage.MORAL_MAX, m.valeur());
    }

    @Test
    public void constructeurBorne() {
        assertEquals("ctor plafonne", Equilibrage.MORAL_MAX, new Moral(150).valeur());
        assertEquals("ctor plancher", Equilibrage.MORAL_MIN, new Moral(-10).valeur());
    }
}
