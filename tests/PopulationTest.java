import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

import Modele.population.Population;
import Modele.population.Role;

import config.Equilibrage;

// Tests de la population : roles, croissance, retraits et garde-fous.
public class PopulationTest {

    @Test
    public void initial() {
        Population p = new Population();
        assertEquals("total initial", Equilibrage.POPULATION_INITIALE, p.total());
        assertEquals("tous inactifs au depart", Equilibrage.POPULATION_INITIALE,
                p.effectif(Role.INACTIF));
        assertEquals("capacite de logement initiale", Equilibrage.CAPACITE_LOGEMENT_INITIALE,
                p.capaciteLogement());
    }

    @Test
    public void reaffecter() {
        Population p = new Population();
        boolean ok = p.reaffecter(Role.INACTIF, Role.FERMIER, 6);
        assertTrue("reaffectation acceptee", ok);
        assertEquals("fermiers", 6, p.effectif(Role.FERMIER));
        assertEquals("inactifs restants", 4, p.effectif(Role.INACTIF));
        assertEquals("total inchange par une reaffectation", 10, p.total());
    }

    @Test
    public void reaffecterRefus() {
        Population p = new Population();
        boolean ok = p.reaffecter(Role.INACTIF, Role.MINEUR, 999);
        assertFalse("reaffectation refusee si pas assez d'inactifs", ok);
        assertEquals("aucun mineur ajoute", 0, p.effectif(Role.MINEUR));
    }

    @Test
    public void ajoutPlafonne() {
        Population p = new Population();
        int ajoute = p.ajouterInactifs(100);
        assertEquals("on ne depasse pas la capacite",
                Equilibrage.CAPACITE_LOGEMENT_INITIALE - Equilibrage.POPULATION_INITIALE, ajoute);
        assertEquals("logement plein", Equilibrage.CAPACITE_LOGEMENT_INITIALE, p.total());
    }

    @Test
    public void retirerInactifs() {
        Population p = new Population();
        p.reaffecter(Role.INACTIF, Role.FERMIER, 8);
        int retire = p.retirerInactifs(5);
        assertEquals("ne retire que les inactifs disponibles", 2, retire);
        assertEquals("les fermiers ne sont pas touches", 8, p.effectif(Role.FERMIER));
    }

    @Test
    public void retirerHabitants() {
        Population p = new Population();
        int retire = p.retirerHabitants(3, new Random(42));
        assertEquals("3 habitants retires", 3, retire);
        assertEquals("total reduit de 3", 7, p.total());
    }

    @Test
    public void definirEffectif() {
        Population p = new Population();
        p.definirEffectif(Role.SOLDAT, 4);
        assertEquals("effectif fixe directement", 4, p.effectif(Role.SOLDAT));
    }

    @Test
    public void definirEffectifNegatif() {
        Population p = new Population();
        assertThrows("un effectif negatif doit etre refuse", IllegalArgumentException.class,
                () -> p.definirEffectif(Role.FERMIER, -1));
    }
}
