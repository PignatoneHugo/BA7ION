import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import Modele.economie.Ressource;
import Modele.partie.ConditionsFin;
import Modele.partie.Partie;
import Modele.partie.PartieBuilder;
import Modele.population.Role;
import Modele.royaume.Royaume;

import config.Equilibrage;

// Tests du deroulement d'une partie : tours, combats, grenouille et fin.
public class PartieTest {

    private static Partie nouvelle() {
        return new PartieBuilder().nomJoueur("Test").nombreBots(1).graineAleatoire(7).build();
    }

    /** Avance d'un tour complet et resout un eventuel evenement (1er choix). */
    private static void avancerUnTour(Partie p) {
        do {
            p.passerEtape();
        } while (!p.enAttenteJoueur());
        if (p.enAttenteEvenement()) {
            p.resoudreEvenement(p.evenementEnAttente().choix().get(0));
        }
    }

    @Test
    public void initial() {
        Partie p = nouvelle();
        assertEquals("demarre au tour 1", 1, p.numeroTour());
        assertTrue("en attente du joueur (planification)", p.enAttenteJoueur());
    }

    @Test
    public void cycleAvanceTour() {
        Partie p = nouvelle();
        avancerUnTour(p);
        assertEquals("le tour est passe a 2", 2, p.numeroTour());
        assertTrue("de retour en planification", p.enAttenteJoueur());
    }

    @Test
    public void combatsAvantTour6() {
        Partie p = nouvelle();
        p.tour().definirNumero(Equilibrage.TOUR_DEBUT_COMBATS - 1);
        assertFalse("pas de combat avant le tour de debut", p.combatsAutorises());
    }

    @Test
    public void combatsDesTour6() {
        Partie p = nouvelle();
        p.tour().definirNumero(Equilibrage.TOUR_DEBUT_COMBATS);
        assertTrue("combats autorises a partir du tour de debut", p.combatsAutorises());
    }

    @Test
    public void grenouilleTour6() {
        Partie p = nouvelle();
        assertFalse("pas encore declenchee au depart", p.grenouilleEmpoisonneeDeclenchee());
        while (p.numeroTour() <= Equilibrage.TOUR_GRENOUILLE_EMPOISONNEE) {
            avancerUnTour(p);
        }
        assertTrue("grenouille declenchee une fois le tour "
                + Equilibrage.TOUR_GRENOUILLE_EMPOISONNEE + " atteint",
                p.grenouilleEmpoisonneeDeclenchee());
    }

    @Test
    public void finEnCours() {
        Partie p = nouvelle();
        assertEquals("une partie neuve est en cours",
                ConditionsFin.Etat.EN_COURS, ConditionsFin.evaluer(p));
    }

    @Test
    public void finDefaite() {
        Partie p = nouvelle();
        Royaume j = p.joueur();
        for (Role role : Role.values()) {
            j.population().definirEffectif(role, 0);
        }
        assertEquals("population effondree => defaite",
                ConditionsFin.Etat.DEFAITE, ConditionsFin.evaluer(p));
    }

    @Test
    public void finVictoire() {
        Partie p = nouvelle();
        p.joueur().tresor().definirQuantite(Ressource.OR, Equilibrage.OR_VICTOIRE_PROSPERITE);
        assertEquals("or de prosperite atteint => victoire",
                ConditionsFin.Etat.VICTOIRE, ConditionsFin.evaluer(p));
    }
}
