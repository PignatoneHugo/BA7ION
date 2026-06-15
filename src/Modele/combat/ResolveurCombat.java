package Modele.combat;

import java.util.Random;

import Modele.militaire.Armee;
import Modele.militaire.PostureCombat;
import Modele.militaire.TableAvantages;
import Modele.militaire.Unite;

// Calcule le resultat d'un combat. La meme seed donne toujours le meme
// resultat, pratique pour les tests.
public final class ResolveurCombat {

    // Ecart de puissance a partir duquel il y a un vainqueur.
    private static final double MARGE_VICTOIRE = 1.1;

    // Amplitude de l'alea sur chaque puissance.
    private static final double AMPLITUDE_ALEA = 0.1;

    private ResolveurCombat() {
    }

    // Resout un combat entre l'armee attaquante et l'armee defenseuse.
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

        // bonus de posture
        puissanceA *= postureAttaquant.multAttaque();
        puissanceD *= postureAttaquant.multDefense();

        // bonus remparts (sauf contournement)
        if (postureAttaquant.utiliseRemparts() && bonusRempartsPct > 0) {
            puissanceD *= 1.0 + bonusRempartsPct / 100.0;
        }

        // un peu d'alea
        Random r = new Random(seed);
        puissanceA *= 1.0 + (r.nextDouble() * 2 - 1) * AMPLITUDE_ALEA;
        puissanceD *= 1.0 + (r.nextDouble() * 2 - 1) * AMPLITUDE_ALEA;

        // qui gagne ?
        RapportCombat.Vainqueur v;
        if (puissanceA > puissanceD * MARGE_VICTOIRE) {
            v = RapportCombat.Vainqueur.ATTAQUANT;
        } else if (puissanceD > puissanceA * MARGE_VICTOIRE) {
            v = RapportCombat.Vainqueur.DEFENSEUR;
        } else {
            v = RapportCombat.Vainqueur.EGALITE;
        }

        // pertes selon le vainqueur
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

    // Puissance offensive de l'attaquant en tenant compte des avantages de type.
    private static double calculerPuissanceOffensive(Armee attaquant, Armee defenseur) {
        double puissance = 0;
        int totalDef = defenseur.effectifTotal();
        for (Unite uA : attaquant.unites()) {
            double bonus = bonusMoyenContre(uA, defenseur, totalDef);
            puissance += uA.effectif() * uA.type().attaqueBase() * bonus;
        }
        return puissance;
    }

    // Puissance defensive du defenseur en tenant compte des avantages de type.
    private static double calculerPuissanceDefensive(Armee defenseur, Armee attaquant) {
        double puissance = 0;
        int totalAtt = attaquant.effectifTotal();
        for (Unite uD : defenseur.unites()) {
            double bonus = bonusMoyenContre(uD, attaquant, totalAtt);
            puissance += uD.effectif() * uD.type().defenseBase() * bonus;
        }
        return puissance;
    }

    // Bonus moyen d'une unite contre une armee, pondere par les types adverses.
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
