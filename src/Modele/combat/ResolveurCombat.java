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

    /**
     * Resout un combat entre l'armee attaquante et l'armee defenseuse.
     *
     * @param attaquant l'armee qui attaque
     * @param defenseur l'armee qui defend
     * @param postureAttaquant la posture de l'attaquant
     * @param bonusRempartsPct le bonus de defense des remparts en pourcentage
     * @param seed la graine de l'alea pour avoir un resultat reproductible
     * @return le rapport du combat
     * @throws IllegalArgumentException si une des deux armees est null
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

        // bonus de posture
        puissanceA *= postureAttaquant.multAttaque();
        puissanceD *= postureAttaquant.multDefense();

        // bonus remparts (sauf contournement)
        if (postureAttaquant.utiliseRemparts() && bonusRempartsPct > 0) {
            puissanceD *= 1.0 + bonusRempartsPct / 100.0;
        }

        // un peu d'alea
        Random aleatoire = new Random(seed);
        puissanceA *= 1.0 + (aleatoire.nextDouble() * 2 - 1) * AMPLITUDE_ALEA;
        puissanceD *= 1.0 + (aleatoire.nextDouble() * 2 - 1) * AMPLITUDE_ALEA;

        // qui gagne ?
        RapportCombat.Vainqueur vainqueur;
        if (puissanceA > puissanceD * MARGE_VICTOIRE) {
            vainqueur = RapportCombat.Vainqueur.ATTAQUANT;
        } else if (puissanceD > puissanceA * MARGE_VICTOIRE) {
            vainqueur = RapportCombat.Vainqueur.DEFENSEUR;
        } else {
            vainqueur = RapportCombat.Vainqueur.EGALITE;
        }

        // pertes selon le vainqueur
        int effectifA = attaquant.effectifTotal();
        int effectifD = defenseur.effectifTotal();
        int pertesA;
        int pertesD;
        switch (vainqueur) {
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

        return new RapportCombat(vainqueur, pertesA, pertesD, puissanceA, puissanceD);
    }

    // Puissance offensive de l'attaquant en tenant compte des avantages de type.
    private static double calculerPuissanceOffensive(Armee attaquant, Armee defenseur) {
        double puissance = 0;
        int totalDef = defenseur.effectifTotal();
        for (Unite uniteAttaquant : attaquant.unites()) {
            double bonus = bonusMoyenContre(uniteAttaquant, defenseur, totalDef);
            puissance += uniteAttaquant.effectif() * uniteAttaquant.type().attaqueBase() * bonus;
        }
        return puissance;
    }

    // Puissance defensive du defenseur en tenant compte des avantages de type.
    private static double calculerPuissanceDefensive(Armee defenseur, Armee attaquant) {
        double puissance = 0;
        int totalAtt = attaquant.effectifTotal();
        for (Unite uniteDefenseur : defenseur.unites()) {
            double bonus = bonusMoyenContre(uniteDefenseur, attaquant, totalAtt);
            puissance += uniteDefenseur.effectif() * uniteDefenseur.type().defenseBase() * bonus;
        }
        return puissance;
    }

    // Bonus moyen d'une unite contre une armee, pondere par les types adverses.
    private static double bonusMoyenContre(Unite unite, Armee adversaire, int totalAdv) {
        if (totalAdv == 0) {
            return 1.0;
        }
        double bonusMoyen = 0;
        for (Unite uniteAdverse : adversaire.unites()) {
            double part = (double) uniteAdverse.effectif() / totalAdv;
            bonusMoyen += TableAvantages.bonusContre(unite.type(), uniteAdverse.type()) * part;
        }
        return bonusMoyen;
    }
}
