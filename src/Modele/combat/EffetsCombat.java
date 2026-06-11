package Modele.combat;

import java.util.EnumMap;
import java.util.Map;

import Modele.economie.Ressource;
import Modele.infrastructure.Batiment;
import Modele.infrastructure.Remparts;
import Modele.infrastructure.TypeBatiment;
import Modele.militaire.Armee;
import Modele.militaire.Unite;
import Modele.partie.Partie;
import Modele.royaume.Royaume;

import config.Equilibrage;

/**
 * Helper qui resout une bataille de bout en bout : appel au resolveur,
 * application des pertes militaires, des consequences sur la population
 * du defenseur si defaite, et du butin pour l'attaquant si victoire.
 *
 * Utilise par EtatCombatsSubis (bots -> joueur) et EtatCombatsOffensifs
 * (joueur -> bots) pour appliquer la meme regle aux deux camps.
 *
 * Regle defaite defensive : le defenseur perd TOUTE son armee + un % de
 * sa population civile (tirage aleatoire pondere). L'attaquant vainqueur
 * vole un % de chaque ressource du defenseur.
 */
public final class EffetsCombat {

    private EffetsCombat() {
        // utilitaire
    }

    /**
     * Applique une bataille : delegue le calcul au ResolveurCombat,
     * applique les pertes militaires de chaque cote, puis les
     * consequences cote defenseur perdant ou attaquant vainqueur.
     * Enregistre la BatailleResolue dans la Partie.
     */
    public static BatailleResolue appliquer(Bataille bataille, Partie partie) {
        int bonusRemparts = bonusRemparts(bataille.defenseur());
        long seed = partie.aleatoire().nextLong();

        // Capture les effectifs AVANT la bataille pour les afficher dans
        // le rapport (apres, on n'a plus que les effectifs restants).
        int effAvantAtt = bataille.attaquant().armee().effectifTotal();
        int effAvantDef = bataille.defenseur().armee().effectifTotal();

        RapportCombat rapport = ResolveurCombat.resoudre(
                bataille.attaquant().armee(),
                bataille.defenseur().armee(),
                bataille.posture(),
                bonusRemparts,
                seed);

        // Pertes militaires standards des deux cotes
        appliquerPertesArmee(bataille.attaquant().armee(), rapport.pertesAttaquant());
        appliquerPertesArmee(bataille.defenseur().armee(), rapport.pertesDefenseur());

        int pertesCivilesDef = 0;
        Map<Ressource, Integer> butinTransfere = new EnumMap<>(Ressource.class);

        // Defenseur perdant : armee entierement aneantie + civils + moral
        if (rapport.vainqueur() == RapportCombat.Vainqueur.ATTAQUANT) {
            aneantirArmee(bataille.defenseur().armee());
            pertesCivilesDef = appliquerPertesCiviles(
                    bataille.defenseur(), partie);
            butinTransfere = transfererButin(
                    bataille.attaquant(), bataille.defenseur());
            // Effondrement du moral : -X points qui peuvent declencher la
            // condition de defaite par moral si trop bas.
            bataille.defenseur().moral().ajuster(
                    -Equilibrage.IMPACT_MORAL_DEFAITE_DEFENSIVE);
            bataille.defenseur().notifierMoralChange();
        }

        bataille.attaquant().notifierArmeeChangee();
        bataille.defenseur().notifierArmeeChangee();
        if (pertesCivilesDef > 0) {
            bataille.defenseur().notifierPopulationChangee();
        }
        if (!butinTransfere.isEmpty()) {
            bataille.attaquant().notifierTresorChange();
            bataille.defenseur().notifierTresorChange();
        }

        BatailleResolue resolue = new BatailleResolue(
                bataille.attaquant(), bataille.defenseur(),
                rapport, effAvantAtt, effAvantDef,
                pertesCivilesDef, butinTransfere);
        partie.enregistrerBataille(resolue);
        return resolue;
    }

    /** Bonus en pourcentage donne par les Remparts du defenseur. */
    private static int bonusRemparts(Royaume defenseur) {
        Batiment b = defenseur.batiment(TypeBatiment.REMPARTS);
        if (b instanceof Remparts) {
            return ((Remparts) b).bonusDefensif();
        }
        return 0;
    }

    /** Repartit des pertes proportionnellement sur les unites de l'armee. */
    private static void appliquerPertesArmee(Armee armee, int pertesTotales) {
        if (pertesTotales <= 0 || armee.estVide()) {
            return;
        }
        int effectifTotal = armee.effectifTotal();
        int restant = Math.min(pertesTotales, effectifTotal);
        for (Unite u : armee.unites()) {
            if (restant <= 0) {
                break;
            }
            int part = (int) Math.round(
                    (double) pertesTotales * u.effectif() / effectifTotal);
            int retire = u.subirPertes(Math.min(part, restant));
            restant -= retire;
        }
    }

    /** Reduit toutes les unites a 0 (defaite defensive ecrasante). */
    private static void aneantirArmee(Armee armee) {
        for (Unite u : armee.unites()) {
            u.subirPertes(u.effectif());
        }
    }

    /**
     * Tue un pourcentage de la population civile du defenseur.
     * Retourne le nombre effectivement retire.
     */
    private static int appliquerPertesCiviles(Royaume defenseur, Partie partie) {
        int pop = defenseur.population().total();
        int aRetirer = (int) Math.round(
                pop * Equilibrage.PERTES_CIVILES_DEFAITE_PCT / 100.0);
        if (aRetirer <= 0) {
            return 0;
        }
        return defenseur.population().retirerHabitants(aRetirer, partie.aleatoire());
    }

    /**
     * L'attaquant vole un pourcentage de chaque ressource du defenseur.
     * Retourne la map des quantites effectivement transferees.
     */
    private static Map<Ressource, Integer> transfererButin(Royaume attaquant,
                                                            Royaume defenseur) {
        Map<Ressource, Integer> transfere = new EnumMap<>(Ressource.class);
        for (Ressource r : Ressource.values()) {
            int stockDef = defenseur.tresor().quantite(r);
            int aVoler = (int) Math.round(
                    stockDef * Equilibrage.BUTIN_VICTOIRE_PCT / 100.0);
            if (aVoler <= 0) {
                continue;
            }
            int reellementRetire = defenseur.tresor().retirer(r, aVoler);
            attaquant.tresor().ajouter(r, reellementRetire);
            if (reellementRetire > 0) {
                transfere.put(r, reellementRetire);
            }
        }
        return transfere;
    }
}
