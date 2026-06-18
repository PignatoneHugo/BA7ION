package Modele.ia;

import java.util.Random;

import Modele.action.ActionAmeliorer;
import Modele.action.ActionAttaquer;
import Modele.action.ActionMobiliser;
import Modele.economie.Ressource;
import Modele.infrastructure.Batiment;
import Modele.infrastructure.TypeBatiment;
import Modele.militaire.TypeUnite;
import Modele.partie.Partie;
import Modele.population.Population;
import Modele.population.Role;
import Modele.royaume.Royaume;

import config.Equilibrage;

// IA equilibree : repartit sa population, recrute, ameliore ses batiments
// et attaque le joueur quand son armee est assez grosse.
public class StrategieEquilibree implements StrategieIA {

    /**
     * Joue le tour du bot : recrute, equilibre la population, ameliore et decide d'attaquer.
     *
     * @param bot le royaume controle par l'IA
     * @param partie la partie en cours
     */
    @Override
    public void jouerTour(Royaume bot, Partie partie) {
        // recruter avant d'equilibrer, sinon il n'y a plus d'inactifs dispo
        recruterSoldats(bot, partie.aleatoire());
        equilibrerPopulation(bot);
        ameliorerBatiment(bot, partie.aleatoire());
        decisionAttaque(bot, partie);

        // le bot joue sa file d'actions tout de suite
        bot.fileActions().executerToutes(bot);
        bot.notifierFileActionsChangee();
    }

    // Repartit la population : moitie fermiers, 1/5 mineurs, 1/5 bucherons, 1/10 erudits.
    private void equilibrerPopulation(Royaume bot) {
        Population pop = bot.population();
        int total = pop.total();
        int inactifs = pop.effectif(Role.INACTIF);

        int fermiersCibles = Math.max(1, total / 2);
        int mineursCibles = total / 5;
        int bucheronsCibles = total / 5;
        int eruditsCibles = total / 10;

        affecter(bot, Role.FERMIER, fermiersCibles - pop.effectif(Role.FERMIER), inactifs);
        inactifs = pop.effectif(Role.INACTIF);
        affecter(bot, Role.MINEUR, mineursCibles - pop.effectif(Role.MINEUR), inactifs);
        inactifs = pop.effectif(Role.INACTIF);
        affecter(bot, Role.BUCHERON, bucheronsCibles - pop.effectif(Role.BUCHERON), inactifs);
        inactifs = pop.effectif(Role.INACTIF);
        affecter(bot, Role.ERUDIT, eruditsCibles - pop.effectif(Role.ERUDIT), inactifs);
    }

    private void affecter(Royaume bot, Role cible, int besoin, int inactifsDispo) {
        if (besoin <= 0 || inactifsDispo <= 0) {
            return;
        }
        bot.reaffecter(Role.INACTIF, cible, Math.min(besoin, inactifsDispo));
    }

    // Recrute des soldats si le bot a assez d'or et peu d'armee.
    private void recruterSoldats(Royaume bot, Random aleatoire) {
        int or = bot.tresor().quantite(Ressource.OR);
        int effectifArmee = bot.armee().effectifTotal();
        int popCivile = bot.population().total();

        if (or < Equilibrage.SEUIL_OR_RECRUTEMENT_IA
                || effectifArmee >= popCivile / 2) {
            return;
        }
        // entre 3 et 8 recrues par tour
        int voulus = 3 + aleatoire.nextInt(6);
        int inactifs = bot.population().effectif(Role.INACTIF);
        int aRecruter = Math.min(voulus, inactifs);
        // limite par l'or disponible
        int abordables = or / Equilibrage.COUT_OR_PAR_SOLDAT;
        aRecruter = Math.min(aRecruter, abordables);
        if (aRecruter <= 0) {
            return;
        }
        // on passe les inactifs en soldats puis on planifie la mobilisation
        bot.reaffecter(Role.INACTIF, Role.SOLDAT, aRecruter);
        bot.fileActions().ajouter(
                new ActionMobiliser(TypeUnite.INFANTERIE_LEGERE, aRecruter));
    }

    // Ameliore un batiment au hasard parmi ceux ameliorables.
    private void ameliorerBatiment(Royaume bot, Random aleatoire) {
        TypeBatiment[] types = TypeBatiment.values();
        for (int essai = 0; essai < 3; essai++) {
            TypeBatiment type = types[aleatoire.nextInt(types.length)];
            Batiment batiment = bot.batiment(type);
            if (batiment == null || !batiment.peutEtreAmeliore()) {
                continue;
            }
            ActionAmeliorer action = new ActionAmeliorer(type);
            if (action.estExecutable(bot)) {
                // le bot doit pouvoir payer
                if (peutPayer(bot, type, batiment.niveau() + 1)) {
                    bot.fileActions().ajouter(action);
                    payer(bot, type, batiment.niveau() + 1);
                    return;
                }
            }
        }
    }

    private boolean peutPayer(Royaume bot, TypeBatiment type, int niveauCible) {
        for (var cout : Equilibrage.coutAmelioration(type, niveauCible).entrySet()) {
            if (!bot.tresor().contient(cout.getKey(), cout.getValue())) {
                return false;
            }
        }
        return true;
    }

    private void payer(Royaume bot, TypeBatiment type, int niveauCible) {
        for (var cout : Equilibrage.coutAmelioration(type, niveauCible).entrySet()) {
            bot.tresor().retirer(cout.getKey(), cout.getValue());
        }
    }

    // Decide d'attaquer le joueur ou non. Plus l'armee est grosse, plus la chance est haute.
    private void decisionAttaque(Royaume bot, Partie partie) {
        // pas de combat avant le tour d'ouverture
        if (!partie.combatsAutorises()) {
            return;
        }
        int effectif = bot.armee().effectifTotal();
        if (effectif < Equilibrage.EFFECTIF_MIN_POUR_ATTAQUE_IA) {
            return;
        }
        // proba de base + bonus selon la taille de l'armee
        double chance = Equilibrage.PROBA_ATTAQUE_IA_BASE
                + Math.min(0.40, effectif / 100.0);
        if (partie.aleatoire().nextDouble() > chance) {
            return;
        }
        bot.fileActions().ajouter(new ActionAttaquer(partie.joueur()));
    }
}
