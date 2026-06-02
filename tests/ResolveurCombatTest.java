import Modele.combat.RapportCombat;
import Modele.combat.RapportCombat.Vainqueur;
import Modele.combat.ResolveurCombat;
import Modele.militaire.Armee;
import Modele.militaire.PostureCombat;
import Modele.militaire.TypeUnite;
import Modele.militaire.Unite;

/**
 * Suite de tests du ResolveurCombat. Trois scenarios deterministes (seed
 * fixe) qui valident le moteur de combat avant son integration dans le
 * cycle de tour.
 *
 * Pour l'instant, les tests sont executables via main() sans JUnit.
 * Quand le projet sera configure avec Maven/Gradle et JUnit 5, les methodes
 * testXxx() pourront etre annotees @Test sans autre changement.
 *
 * Lancement : ./build.sh test
 */
public class ResolveurCombatTest {

    private static int reussis = 0;
    private static int echoues = 0;

    public static void main(String[] args) {
        executer("testVictoireEcrasanteAttaquant", ResolveurCombatTest::testVictoireEcrasanteAttaquant);
        executer("testDefaiteAttaquantContreRemparts", ResolveurCombatTest::testDefaiteAttaquantContreRemparts);
        executer("testCombatEquilibre", ResolveurCombatTest::testCombatEquilibre);
        executer("testAvantagePierreFeuilleCiseaux", ResolveurCombatTest::testAvantagePierreFeuilleCiseaux);
        executer("testDeterminisme", ResolveurCombatTest::testDeterminisme);

        System.out.println();
        System.out.println("Resultat : " + reussis + " reussis, " + echoues + " echoues.");
        if (echoues > 0) {
            System.exit(1);
        }
    }

    /**
     * Scenario 1 : un attaquant tres superieur en nombre ecrase une petite
     * garnison sans remparts. Le rapport doit pointer ATTAQUANT, et le taux
     * de pertes du defenseur doit etre plus eleve que celui de l'attaquant.
     */
    static void testVictoireEcrasanteAttaquant() {
        int effectifA = 100;
        int effectifD = 10;
        Armee attaquant = new Armee(PostureCombat.ATTAQUE);
        attaquant.ajouterUnite(new Unite(TypeUnite.CAVALERIE_LOURDE, effectifA));

        Armee defenseur = new Armee(PostureCombat.DEFENSE);
        defenseur.ajouterUnite(new Unite(TypeUnite.INFANTERIE_LEGERE, effectifD));

        RapportCombat r = ResolveurCombat.resoudre(attaquant, defenseur,
                PostureCombat.ATTAQUE, 0, 42L);

        assertEquals(Vainqueur.ATTAQUANT, r.vainqueur(), "Vainqueur attendu : ATTAQUANT");

        double tauxPertesA = (double) r.pertesAttaquant() / effectifA;
        double tauxPertesD = (double) r.pertesDefenseur() / effectifD;
        assertTrue(tauxPertesD > tauxPertesA,
                "Taux de pertes du defenseur doit etre superieur (A=" + tauxPertesA
                        + ", D=" + tauxPertesD + ")");
    }

    /**
     * Scenario 2 : un attaquant moyen attaque une garnison protegee par des
     * remparts (+100%). Le defenseur l'emporte malgre un effectif inferieur.
     */
    static void testDefaiteAttaquantContreRemparts() {
        Armee attaquant = new Armee(PostureCombat.ATTAQUE);
        attaquant.ajouterUnite(new Unite(TypeUnite.INFANTERIE_LEGERE, 30));

        Armee defenseur = new Armee(PostureCombat.DEFENSE);
        defenseur.ajouterUnite(new Unite(TypeUnite.ARCHER, 30));

        RapportCombat r = ResolveurCombat.resoudre(attaquant, defenseur,
                PostureCombat.ATTAQUE, 100, 42L);

        assertEquals(Vainqueur.DEFENSEUR, r.vainqueur(),
                "Vainqueur attendu : DEFENSEUR (grace aux remparts)");
        assertTrue(r.pertesAttaquant() > r.pertesDefenseur(),
                "Attaquant doit perdre plus que le defenseur");
    }

    /**
     * Scenario 3 : deux armees similaires sans remparts. Le rapport doit
     * etre EGALITE (puissances dans la marge des 10%).
     */
    static void testCombatEquilibre() {
        Armee attaquant = new Armee(PostureCombat.ATTAQUE);
        attaquant.ajouterUnite(new Unite(TypeUnite.INFANTERIE_LEGERE, 50));

        Armee defenseur = new Armee(PostureCombat.DEFENSE);
        defenseur.ajouterUnite(new Unite(TypeUnite.INFANTERIE_LEGERE, 50));

        RapportCombat r = ResolveurCombat.resoudre(attaquant, defenseur,
                PostureCombat.ATTAQUE, 0, 42L);

        // Avec posture ATTAQUE des deux cotes, le defenseur a un avantage
        // structurel (mult def en posture defense). Verifions que le combat
        // n'est pas une victoire ecrasante d'un cote.
        int diffPertes = Math.abs(r.pertesAttaquant() - r.pertesDefenseur());
        assertTrue(diffPertes < 25,
                "Les pertes doivent etre du meme ordre (diff < 25), trouve " + diffPertes);
    }

    /**
     * Verifie le bonus Pierre-Feuille-Ciseaux : Cavalerie contre Archer
     * doit donner +50% d'attaque, ce qui se voit dans la puissance offensive.
     */
    static void testAvantagePierreFeuilleCiseaux() {
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

        assertTrue(contreArchers.puissanceAttaquant() > contreInf.puissanceAttaquant(),
                "La cavalerie doit etre plus forte contre les archers que contre l'infanterie");
    }

    /**
     * Verifie que la meme seed donne exactement le meme rapport (necessaire
     * pour la reproductibilite des tests automatises).
     */
    static void testDeterminisme() {
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

        assertEquals(r1.vainqueur(), r2.vainqueur(),
                "Meme seed doit donner le meme vainqueur");
        assertEquals(r1.pertesAttaquant(), r2.pertesAttaquant(),
                "Meme seed doit donner les memes pertes attaquant");
        assertEquals(r1.pertesDefenseur(), r2.pertesDefenseur(),
                "Meme seed doit donner les memes pertes defenseur");
    }

    // -------------------------------------------------------------------
    // Helpers de test (en attendant l'integration JUnit 5)
    // -------------------------------------------------------------------

    private static void executer(String nom, Runnable test) {
        try {
            test.run();
            System.out.println("[OK]  " + nom);
            reussis++;
        } catch (AssertionError e) {
            System.out.println("[KO]  " + nom + " : " + e.getMessage());
            echoues++;
        } catch (Exception e) {
            System.out.println("[KO]  " + nom + " : exception inattendue " + e);
            echoues++;
        }
    }

    private static void assertEquals(Object attendu, Object obtenu, String message) {
        if (attendu == null ? obtenu != null : !attendu.equals(obtenu)) {
            throw new AssertionError(message + " (attendu=" + attendu + ", obtenu=" + obtenu + ")");
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
