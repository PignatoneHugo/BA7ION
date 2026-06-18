import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import Modele.combat.Bataille;
import Modele.combat.EffetsCombat;
import Modele.militaire.PostureCombat;
import Modele.militaire.TypeUnite;
import Modele.partie.Partie;
import Modele.partie.PartieBuilder;
import Modele.royaume.Royaume;

// Tests des consequences d'une bataille (pertes civiles, aneantissement).
public class EffetsCombatTest {

    /** Un defenseur sans armee est entierement aneanti (tous ses villageois meurent). */
    @Test
    public void defenseurSansArmeeEstAneanti() {
        Partie partie = new PartieBuilder().nomJoueur("Att").nombreBots(1).graineAleatoire(1).build();
        Royaume attaquant = partie.joueur();
        Royaume defenseur = partie.bots().get(0);
        attaquant.armee().recruter(TypeUnite.INFANTERIE_LEGERE, 10);
        // le defenseur n'a aucune armee
        EffetsCombat.appliquer(new Bataille(attaquant, defenseur, PostureCombat.ATTAQUE), partie);
        assertEquals("tous les villageois du defenseur sans armee doivent mourir",
                0, defenseur.population().total());
    }

    /** Un defenseur avec une armee qui perd subit des pertes mais n'est pas aneanti. */
    @Test
    public void defenseurAvecArmeeNestPasAneanti() {
        Partie partie = new PartieBuilder().nomJoueur("Att").nombreBots(1).graineAleatoire(1).build();
        Royaume attaquant = partie.joueur();
        Royaume defenseur = partie.bots().get(0);
        attaquant.armee().recruter(TypeUnite.CAVALERIE_LOURDE, 100);
        defenseur.armee().recruter(TypeUnite.INFANTERIE_LEGERE, 5);
        int popAvant = defenseur.population().total();
        EffetsCombat.appliquer(new Bataille(attaquant, defenseur, PostureCombat.ATTAQUE), partie);
        assertTrue("le defenseur garde des villageois", defenseur.population().total() > 0);
        assertTrue("le defenseur a quand meme subi des pertes civiles",
                defenseur.population().total() < popAvant);
    }
}
