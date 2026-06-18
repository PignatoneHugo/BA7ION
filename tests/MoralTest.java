import static org.junit.Assert.assertEquals;

import org.junit.Test;

import Modele.population.Moral;

import config.Equilibrage;

// Tests du moral : valeur initiale, bornage et variation renvoyee par ajuster.
public class MoralTest {

    /**
     * Verifie que le moral demarre a sa valeur initiale.
     */
    @Test
    public void initial() {
        assertEquals("moral initial", Equilibrage.MORAL_INITIAL, new Moral().valeur());
    }

    /**
     * Verifie qu'un ajustement positif augmente le moral.
     */
    @Test
    public void ajusterPositif() {
        Moral moral = new Moral();
        moral.ajuster(10);
        assertEquals("moral apres +10", Equilibrage.MORAL_INITIAL + 10, moral.valeur());
    }

    /**
     * Verifie que le moral est plafonne a sa valeur maximale.
     */
    @Test
    public void plafond() {
        Moral moral = new Moral();
        moral.ajuster(1000);
        assertEquals("moral plafonne", Equilibrage.MORAL_MAX, moral.valeur());
    }

    /**
     * Verifie que le moral est plancher a sa valeur minimale.
     */
    @Test
    public void plancher() {
        Moral moral = new Moral();
        moral.ajuster(-1000);
        assertEquals("moral plancher", Equilibrage.MORAL_MIN, moral.valeur());
    }

    /**
     * Verifie que ajuster renvoie la variation reellement appliquee.
     */
    @Test
    public void variationReelle() {
        Moral moral = new Moral();
        int delta = moral.ajuster(1000);
        assertEquals("variation reellement appliquee",
                Equilibrage.MORAL_MAX - Equilibrage.MORAL_INITIAL, delta);
    }

    /**
     * Verifie que definir fixe le moral et le plafonne si besoin.
     */
    @Test
    public void definir() {
        Moral moral = new Moral();
        moral.definir(75);
        assertEquals("definir fixe la valeur", 75, moral.valeur());
        moral.definir(999);
        assertEquals("definir plafonne", Equilibrage.MORAL_MAX, moral.valeur());
    }

    /**
     * Verifie que le constructeur borne le moral entre min et max.
     */
    @Test
    public void constructeurBorne() {
        assertEquals("ctor plafonne", Equilibrage.MORAL_MAX, new Moral(150).valeur());
        assertEquals("ctor plancher", Equilibrage.MORAL_MIN, new Moral(-10).valeur());
    }
}
