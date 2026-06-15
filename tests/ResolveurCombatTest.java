import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import Modele.combat.RapportCombat;
import Modele.combat.RapportCombat.Vainqueur;
import Modele.combat.ResolveurCombat;
import Modele.militaire.Armee;
import Modele.militaire.PostureCombat;
import Modele.militaire.TypeUnite;
import Modele.militaire.Unite;

// Tests du ResolveurCombat (seed fixe) : nombre, remparts, equilibre, PFC.
public class ResolveurCombatTest {

    // Beaucoup d'attaquants contre peu de defenseurs : l'attaquant gagne.
    @Test
    public void victoireEcrasanteAttaquant() {
        int effectifA = 100;
        int effectifD = 10;
        Armee attaquant = new Armee(PostureCombat.ATTAQUE);
        attaquant.ajouterUnite(new Unite(TypeUnite.CAVALERIE_LOURDE, effectifA));

        Armee defenseur = new Armee(PostureCombat.DEFENSE);
        defenseur.ajouterUnite(new Unite(TypeUnite.INFANTERIE_LEGERE, effectifD));

        RapportCombat r = ResolveurCombat.resoudre(attaquant, defenseur,
                PostureCombat.ATTAQUE, 0, 42L);

        assertEquals("Vainqueur attendu : ATTAQUANT", Vainqueur.ATTAQUANT, r.vainqueur());

        double tauxPertesA = (double) r.pertesAttaquant() / effectifA;
        double tauxPertesD = (double) r.pertesDefenseur() / effectifD;
        assertTrue("Taux de pertes du defenseur doit etre superieur (A=" + tauxPertesA
                + ", D=" + tauxPertesD + ")", tauxPertesD > tauxPertesA);
    }

    // A effectif egal, les remparts font gagner le defenseur.
    @Test
    public void defaiteAttaquantContreRemparts() {
        Armee attaquant = new Armee(PostureCombat.ATTAQUE);
        attaquant.ajouterUnite(new Unite(TypeUnite.INFANTERIE_LEGERE, 30));

        Armee defenseur = new Armee(PostureCombat.DEFENSE);
        defenseur.ajouterUnite(new Unite(TypeUnite.ARCHER, 30));

        RapportCombat r = ResolveurCombat.resoudre(attaquant, defenseur,
                PostureCombat.ATTAQUE, 100, 42L);

        assertEquals("Vainqueur attendu : DEFENSEUR (grace aux remparts)",
                Vainqueur.DEFENSEUR, r.vainqueur());
        assertTrue("Attaquant doit perdre plus que le defenseur",
                r.pertesAttaquant() > r.pertesDefenseur());
    }

    /** Deux armees similaires sans remparts : pas de victoire ecrasante. */
    @Test
    public void combatEquilibre() {
        Armee attaquant = new Armee(PostureCombat.ATTAQUE);
        attaquant.ajouterUnite(new Unite(TypeUnite.INFANTERIE_LEGERE, 50));

        Armee defenseur = new Armee(PostureCombat.DEFENSE);
        defenseur.ajouterUnite(new Unite(TypeUnite.INFANTERIE_LEGERE, 50));

        RapportCombat r = ResolveurCombat.resoudre(attaquant, defenseur,
                PostureCombat.ATTAQUE, 0, 42L);

        int diffPertes = Math.abs(r.pertesAttaquant() - r.pertesDefenseur());
        assertTrue("Les pertes doivent etre du meme ordre (diff < 25), trouve " + diffPertes,
                diffPertes < 25);
    }

    // PFC : la cavalerie tape plus fort sur les archers que sur l'infanterie.
    @Test
    public void avantagePierreFeuilleCiseaux() {
        Armee cavalerie = new Armee(PostureCombat.ATTAQUE);
        cavalerie.ajouterUnite(new Unite(TypeUnite.CAVALERIE_LOURDE, 10));

        Armee archers = new Armee(PostureCombat.DEFENSE);
        archers.ajouterUnite(new Unite(TypeUnite.ARCHER, 10));

        Armee infanterie = new Armee(PostureCombat.DEFENSE);
        infanterie.ajouterUnite(new Unite(TypeUnite.INFANTERIE_LEGERE, 10));

        RapportCombat contreArchers = ResolveurCombat.resoudre(cavalerie, archers,
                PostureCombat.ATTAQUE, 0, 42L);
        RapportCombat contreInf = ResolveurCombat.resoudre(cavalerie, infanterie,
                PostureCombat.ATTAQUE, 0, 42L);

        assertTrue("La cavalerie doit etre plus forte contre les archers que contre l'infanterie",
                contreArchers.puissanceAttaquant() > contreInf.puissanceAttaquant());
    }

    /** Meme seed => meme rapport (reproductibilite). */
    @Test
    public void determinisme() {
        Armee a1 = new Armee();
        a1.ajouterUnite(new Unite(TypeUnite.INFANTERIE_LEGERE, 25));
        Armee d1 = new Armee();
        d1.ajouterUnite(new Unite(TypeUnite.LANCIER, 25));

        Armee a2 = new Armee();
        a2.ajouterUnite(new Unite(TypeUnite.INFANTERIE_LEGERE, 25));
        Armee d2 = new Armee();
        d2.ajouterUnite(new Unite(TypeUnite.LANCIER, 25));

        RapportCombat r1 = ResolveurCombat.resoudre(a1, d1, PostureCombat.ATTAQUE, 10, 123L);
        RapportCombat r2 = ResolveurCombat.resoudre(a2, d2, PostureCombat.ATTAQUE, 10, 123L);

        assertEquals("Meme seed doit donner le meme vainqueur", r1.vainqueur(), r2.vainqueur());
        assertEquals("Meme seed doit donner les memes pertes attaquant",
                r1.pertesAttaquant(), r2.pertesAttaquant());
        assertEquals("Meme seed doit donner les memes pertes defenseur",
                r1.pertesDefenseur(), r2.pertesDefenseur());
    }
}
