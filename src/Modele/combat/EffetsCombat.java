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

// Resout une bataille en entier : pertes des deux camps, et si le defenseur
// perd, pertes civiles + butin pour l'attaquant.
public final class EffetsCombat {

    private EffetsCombat() {
    }

    /**
     * Resout la bataille, applique tous ses effets et l'enregistre dans la partie.
     *
     * @param bataille la bataille a resoudre
     * @param partie la partie en cours
     * @return le bilan de la bataille resolue
     */
    public static BatailleResolue appliquer(Bataille bataille, Partie partie) {
        int bonusRemparts = bonusRemparts(bataille.defenseur());
        long seed = partie.aleatoire().nextLong();

        // on garde les effectifs avant la bataille pour le rapport
        int effAvantAtt = bataille.attaquant().armee().effectifTotal();
        int effAvantDef = bataille.defenseur().armee().effectifTotal();

        // un defenseur attaque alors qu'il n'a plus d'armee est aneanti
        boolean defenseurSansArmee = effAvantDef == 0;

        RapportCombat rapport = ResolveurCombat.resoudre(
                bataille.attaquant().armee(),
                bataille.defenseur().armee(),
                bataille.posture(),
                bonusRemparts,
                seed);

        // pertes militaires des deux cotes
        appliquerPertesArmee(bataille.attaquant().armee(), rapport.pertesAttaquant());
        appliquerPertesArmee(bataille.defenseur().armee(), rapport.pertesDefenseur());

        int pertesCivilesDef = 0;
        Map<Ressource, Integer> butinTransfere = new EnumMap<>(Ressource.class);

        // si le defenseur perd : armee detruite, civils tues, butin et moral en baisse
        if (rapport.vainqueur() == RapportCombat.Vainqueur.ATTAQUANT) {
            aneantirArmee(bataille.defenseur().armee());
            pertesCivilesDef = appliquerPertesCiviles(
                    bataille.defenseur(), partie, defenseurSansArmee);
            butinTransfere = transfererButin(
                    bataille.attaquant(), bataille.defenseur());
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

    // Bonus de defense (en %) donne par les remparts du defenseur.
    private static int bonusRemparts(Royaume defenseur) {
        Batiment batiment = defenseur.batiment(TypeBatiment.REMPARTS);
        if (batiment instanceof Remparts) {
            return ((Remparts) batiment).bonusDefensif();
        }
        return 0;
    }

    // Repartit les pertes proportionnellement sur les unites.
    private static void appliquerPertesArmee(Armee armee, int pertesTotales) {
        if (pertesTotales <= 0 || armee.estVide()) {
            return;
        }
        int effectifTotal = armee.effectifTotal();
        int restant = Math.min(pertesTotales, effectifTotal);
        for (Unite unite : armee.unites()) {
            if (restant <= 0) {
                break;
            }
            int part = (int) Math.round(
                    (double) pertesTotales * unite.effectif() / effectifTotal);
            int retire = unite.subirPertes(Math.min(part, restant));
            restant -= retire;
        }
    }

    // Met toutes les unites a 0.
    private static void aneantirArmee(Armee armee) {
        for (Unite unite : armee.unites()) {
            unite.subirPertes(unite.effectif());
        }
    }

    // Tue les civils du defenseur : tous s'il n'avait plus d'armee (aneantissement
    // total), sinon un pourcentage. Renvoie combien sont morts.
    private static int appliquerPertesCiviles(Royaume defenseur, Partie partie,
                                              boolean sansArmee) {
        int pop = defenseur.population().total();
        int aRetirer = sansArmee
                ? pop
                : (int) Math.round(pop * Equilibrage.PERTES_CIVILES_DEFAITE_PCT / 100.0);
        if (aRetirer <= 0) {
            return 0;
        }
        return defenseur.population().retirerHabitants(aRetirer, partie.aleatoire());
    }

    // L'attaquant vole un % de chaque ressource du defenseur. Renvoie ce qui a ete pris.
    private static Map<Ressource, Integer> transfererButin(Royaume attaquant,
                                                            Royaume defenseur) {
        Map<Ressource, Integer> transfere = new EnumMap<>(Ressource.class);
        for (Ressource ressource : Ressource.values()) {
            int stockDef = defenseur.tresor().quantite(ressource);
            int aVoler = (int) Math.round(
                    stockDef * Equilibrage.BUTIN_VICTOIRE_PCT / 100.0);
            if (aVoler <= 0) {
                continue;
            }
            int reellementRetire = defenseur.tresor().retirer(ressource, aVoler);
            attaquant.tresor().ajouter(ressource, reellementRetire);
            if (reellementRetire > 0) {
                transfere.put(ressource, reellementRetire);
            }
        }
        return transfere;
    }
}
