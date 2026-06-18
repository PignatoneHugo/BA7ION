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
    private static void avancerUnTour(Partie partie) {
        do {
            partie.passerEtape();
        } while (!partie.enAttenteJoueur());
        if (partie.enAttenteEvenement()) {
            partie.resoudreEvenement(partie.evenementEnAttente().choix().get(0));
        }
    }

    /**
     * Verifie qu'une partie neuve demarre au tour 1 en attente du joueur.
     */
    @Test
    public void initial() {
        Partie partie = nouvelle();
        assertEquals("demarre au tour 1", 1, partie.numeroTour());
        assertTrue("en attente du joueur (planification)", partie.enAttenteJoueur());
    }

    /**
     * Verifie qu'avancer un tour passe au tour suivant en planification.
     */
    @Test
    public void cycleAvanceTour() {
        Partie partie = nouvelle();
        avancerUnTour(partie);
        assertEquals("le tour est passe a 2", 2, partie.numeroTour());
        assertTrue("de retour en planification", partie.enAttenteJoueur());
    }

    /**
     * Verifie que les combats sont interdits avant le tour de debut.
     */
    @Test
    public void combatsAvantTour6() {
        Partie partie = nouvelle();
        partie.tour().definirNumero(Equilibrage.TOUR_DEBUT_COMBATS - 1);
        assertFalse("pas de combat avant le tour de debut", partie.combatsAutorises());
    }

    /**
     * Verifie que les combats sont autorises a partir du tour de debut.
     */
    @Test
    public void combatsDesTour6() {
        Partie partie = nouvelle();
        partie.tour().definirNumero(Equilibrage.TOUR_DEBUT_COMBATS);
        assertTrue("combats autorises a partir du tour de debut", partie.combatsAutorises());
    }

    /**
     * Verifie que la grenouille empoisonnee se declenche au tour prevu.
     */
    @Test
    public void grenouilleTour6() {
        Partie partie = nouvelle();
        assertFalse("pas encore declenchee au depart", partie.grenouilleEmpoisonneeDeclenchee());
        while (partie.numeroTour() <= Equilibrage.TOUR_GRENOUILLE_EMPOISONNEE) {
            avancerUnTour(partie);
        }
        assertTrue("grenouille declenchee une fois le tour "
                + Equilibrage.TOUR_GRENOUILLE_EMPOISONNEE + " atteint",
                partie.grenouilleEmpoisonneeDeclenchee());
    }

    /**
     * Verifie qu'une partie neuve est consideree en cours.
     */
    @Test
    public void finEnCours() {
        Partie partie = nouvelle();
        assertEquals("une partie neuve est en cours",
                ConditionsFin.Etat.EN_COURS, ConditionsFin.evaluer(partie));
    }

    /**
     * Verifie qu'une population effondree entraine la defaite.
     */
    @Test
    public void finDefaite() {
        Partie partie = nouvelle();
        Royaume joueur = partie.joueur();
        for (Role role : Role.values()) {
            joueur.population().definirEffectif(role, 0);
        }
        assertEquals("population effondree => defaite",
                ConditionsFin.Etat.DEFAITE, ConditionsFin.evaluer(partie));
    }

    /**
     * Verifie qu'atteindre l'or de prosperite entraine la victoire.
     */
    @Test
    public void finVictoire() {
        Partie partie = nouvelle();
        partie.joueur().tresor().definirQuantite(Ressource.OR, Equilibrage.OR_VICTOIRE_PROSPERITE);
        assertEquals("or de prosperite atteint => victoire",
                ConditionsFin.Etat.VICTOIRE, ConditionsFin.evaluer(partie));
    }
}
