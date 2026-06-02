package Modele.combat;

import java.util.Random;

import Modele.militaire.Armee;
import Modele.militaire.PostureCombat;
import Modele.militaire.TableAvantages;
import Modele.militaire.Unite;

/**
 * Resolveur de combat. Methode statique pure (sans etat), deterministe via
 * la graine aleatoire fournie, ce qui rend les tests JUnit reproductibles.
 *
 * Algorithme :
 *  1. Calcule la puissance offensive de l'attaquant (effectif x attaque x
 *     bonus PFC moyen contre les unites du defenseur).
 *  2. Calcule la puissance defensive du defenseur (idem en defense).
 *  3. Applique les multiplicateurs de la posture choisie par l'attaquant.
 *  4. Applique le bonus des remparts (sauf en CONTOURNEMENT).
 *  5. Applique un alea de plus ou moins 10% sur chaque puissance.
 *  6. Vainqueur = celui dont la puissance depasse l'autre de plus de 10%.
 *  7. Pertes : 50% pour le perdant, 20% pour le gagnant, 30% chacun en cas
 *     d'egalite.
 */
public final class ResolveurCombat {

    /** Marge relative au-dela de laquelle on declare un vainqueur. */
    private static final double MARGE_VICTOIRE = 1.1;

    /** Amplitude de l'alea applique sur chaque puissance (10%). */
    private static final double AMPLITUDE_ALEA = 0.1;

    private ResolveurCombat() {
        // Classe utilitaire.
    }

    /**
     * Resout un combat entre un attaquant et un defenseur.
     *
     * @param attaquant armee attaquante
     * @param defenseur armee defenseuse
     * @param postureAttaquant posture choisie par l'attaquant
     * @param bonusRempartsPct bonus des remparts du defenseur, en pourcentage
     * @param seed graine pour l'alea (meme seed => meme resultat)
     */
    public static RapportCombat resoudre(Armee attaquant,
                                         Armee defenseur,
                                         PostureCombat postureAttaquant,
                                         int bonusRempartsPct,
                                         long seed) {
        if (attaquant == null || defenseur == null) {
            throw new IllegalArgumentException("Les armees ne peuvent pas etre null.");
        }
        if (postureAttaquant == null) {
            postureAttaquant = PostureCombat.ATTAQUE;
        }

        double puissanceA = calculerPuissanceOffensive(attaquant, defenseur);
        double puissanceD = calculerPuissanceDefensive(defenseur, attaquant);

        // Multiplicateurs de posture (cote attaquant).
        puissanceA *= postureAttaquant.multAttaque();
        puissanceD *= postureAttaquant.multDefense();

        // Bonus remparts (sauf si l'attaquant contourne).
        if (postureAttaquant.utiliseRemparts() && bonusRempartsPct > 0) {
            puissanceD *= 1.0 + bonusRempartsPct / 100.0;
        }

        // Alea borne sur chaque puissance.
        Random r = new Random(seed);
        puissanceA *= 1.0 + (r.nextDouble() * 2 - 1) * AMPLITUDE_ALEA;
        puissanceD *= 1.0 + (r.nextDouble() * 2 - 1) * AMPLITUDE_ALEA;

        // Determination du vainqueur.
        RapportCombat.Vainqueur v;
        if (puissanceA > puissanceD * MARGE_VICTOIRE) {
            v = RapportCombat.Vainqueur.ATTAQUANT;
        } else if (puissanceD > puissanceA * MARGE_VICTOIRE) {
            v = RapportCombat.Vainqueur.DEFENSEUR;
        } else {
            v = RapportCombat.Vainqueur.EGALITE;
        }

        // Calcul des pertes selon le vainqueur.
        int effectifA = attaquant.effectifTotal();
        int effectifD = defenseur.effectifTotal();
        int pertesA;
        int pertesD;
        switch (v) {
            case ATTAQUANT:
                pertesA = (int) Math.round(effectifA * 0.2);
                pertesD = (int) Math.round(effectifD * 0.5);
                break;
            case DEFENSEUR:
                pertesA = (int) Math.round(effectifA * 0.5);
                pertesD = (int) Math.round(effectifD * 0.2);
                break;
            default:
                pertesA = (int) Math.round(effectifA * 0.3);
                pertesD = (int) Math.round(effectifD * 0.3);
                break;
        }

        return new RapportCombat(v, pertesA, pertesD, puissanceA, puissanceD);
    }

    /**
     * Puissance offensive = somme sur chaque unite attaquante de
     * (effectif x attaqueBase x bonus PFC moyen contre l'armee defenseur).
     */
    private static double calculerPuissanceOffensive(Armee attaquant, Armee defenseur) {
        double puissance = 0;
        int totalDef = defenseur.effectifTotal();
        for (Unite uA : attaquant.unites()) {
            double bonus = bonusMoyenContre(uA, defenseur, totalDef);
            puissance += uA.effectif() * uA.type().attaqueBase() * bonus;
        }
        return puissance;
    }

    /**
     * Puissance defensive = somme sur chaque unite defenseuse de
     * (effectif x defenseBase x bonus PFC moyen contre l'armee attaquante).
     */
    private static double calculerPuissanceDefensive(Armee defenseur, Armee attaquant) {
        double puissance = 0;
        int totalAtt = attaquant.effectifTotal();
        for (Unite uD : defenseur.unites()) {
            double bonus = bonusMoyenContre(uD, attaquant, totalAtt);
            puissance += uD.effectif() * uD.type().defenseBase() * bonus;
        }
        return puissance;
    }

    /**
     * Calcule le bonus PFC moyen d'une unite contre une armee adverse,
     * pondere par la proportion de chaque type ennemi.
     */
    private static double bonusMoyenContre(Unite uA, Armee adversaire, int totalAdv) {
        if (totalAdv == 0) {
            return 1.0;
        }
        double bonusMoyen = 0;
        for (Unite uD : adversaire.unites()) {
            double part = (double) uD.effectif() / totalAdv;
            bonusMoyen += TableAvantages.bonusContre(uA.type(), uD.type()) * part;
        }
        return bonusMoyen;
    }
}
