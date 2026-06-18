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

    /**
     * Verifie la population de depart, ses inactifs et sa capacite de logement.
     */
    @Test
    public void initial() {
        Population population = new Population();
        assertEquals("total initial", Equilibrage.POPULATION_INITIALE, population.total());
        assertEquals("tous inactifs au depart", Equilibrage.POPULATION_INITIALE,
                population.effectif(Role.INACTIF));
        assertEquals("capacite de logement initiale", Equilibrage.CAPACITE_LOGEMENT_INITIALE,
                population.capaciteLogement());
    }

    /**
     * Verifie qu'une reaffectation change les roles sans changer le total.
     */
    @Test
    public void reaffecter() {
        Population population = new Population();
        boolean reussite = population.reaffecter(Role.INACTIF, Role.FERMIER, 6);
        assertTrue("reaffectation acceptee", reussite);
        assertEquals("fermiers", 6, population.effectif(Role.FERMIER));
        assertEquals("inactifs restants", 4, population.effectif(Role.INACTIF));
        assertEquals("total inchange par une reaffectation", 10, population.total());
    }

    /**
     * Verifie qu'une reaffectation est refusee s'il manque des inactifs.
     */
    @Test
    public void reaffecterRefus() {
        Population population = new Population();
        boolean reussite = population.reaffecter(Role.INACTIF, Role.MINEUR, 999);
        assertFalse("reaffectation refusee si pas assez d'inactifs", reussite);
        assertEquals("aucun mineur ajoute", 0, population.effectif(Role.MINEUR));
    }

    /**
     * Verifie qu'on ne peut pas ajouter d'inactifs au-dela de la capacite.
     */
    @Test
    public void ajoutPlafonne() {
        Population population = new Population();
        int ajoute = population.ajouterInactifs(100);
        assertEquals("on ne depasse pas la capacite",
                Equilibrage.CAPACITE_LOGEMENT_INITIALE - Equilibrage.POPULATION_INITIALE, ajoute);
        assertEquals("logement plein", Equilibrage.CAPACITE_LOGEMENT_INITIALE, population.total());
    }

    /**
     * Verifie que retirerInactifs ne touche que les inactifs disponibles.
     */
    @Test
    public void retirerInactifs() {
        Population population = new Population();
        population.reaffecter(Role.INACTIF, Role.FERMIER, 8);
        int retire = population.retirerInactifs(5);
        assertEquals("ne retire que les inactifs disponibles", 2, retire);
        assertEquals("les fermiers ne sont pas touches", 8, population.effectif(Role.FERMIER));
    }

    /**
     * Verifie que retirerHabitants enleve bien le nombre demande.
     */
    @Test
    public void retirerHabitants() {
        Population population = new Population();
        int retire = population.retirerHabitants(3, new Random(42));
        assertEquals("3 habitants retires", 3, retire);
        assertEquals("total reduit de 3", 7, population.total());
    }

    /**
     * Verifie que definirEffectif fixe directement l'effectif d'un role.
     */
    @Test
    public void definirEffectif() {
        Population population = new Population();
        population.definirEffectif(Role.SOLDAT, 4);
        assertEquals("effectif fixe directement", 4, population.effectif(Role.SOLDAT));
    }

    /**
     * Verifie qu'un effectif negatif est refuse.
     */
    @Test
    public void definirEffectifNegatif() {
        Population population = new Population();
        assertThrows("un effectif negatif doit etre refuse", IllegalArgumentException.class,
                () -> population.definirEffectif(Role.FERMIER, -1));
    }
}
