package Modele.combat;

import java.util.EnumMap;
import java.util.Map;

import Modele.economie.Ressource;
import Modele.royaume.Royaume;

/**
 * Bataille resolue : la Bataille initiale + le rapport du resolveur +
 * les effets de bord effectivement appliques (pertes civiles, butin
 * transfere). Stockee dans Partie.batraillesDuTour pour pouvoir etre
 * affichee dans le recap de fin de tour.
 */
public class BatailleResolue {

    private final Royaume attaquant;
    private final Royaume defenseur;
    private final RapportCombat rapport;
    private final int effectifAvantAttaquant;
    private final int effectifAvantDefenseur;
    private final int pertesCivilesDefenseur;
    private final Map<Ressource, Integer> butin;

    public BatailleResolue(Royaume attaquant,
                           Royaume defenseur,
                           RapportCombat rapport,
                           int effectifAvantAttaquant,
                           int effectifAvantDefenseur,
                           int pertesCivilesDefenseur,
                           Map<Ressource, Integer> butin) {
        this.attaquant = attaquant;
        this.defenseur = defenseur;
        this.rapport = rapport;
        this.effectifAvantAttaquant = Math.max(0, effectifAvantAttaquant);
        this.effectifAvantDefenseur = Math.max(0, effectifAvantDefenseur);
        this.pertesCivilesDefenseur = Math.max(0, pertesCivilesDefenseur);
        this.butin = new EnumMap<>(Ressource.class);
        if (butin != null) {
            for (Map.Entry<Ressource, Integer> e : butin.entrySet()) {
                if (e.getValue() != null && e.getValue() > 0) {
                    this.butin.put(e.getKey(), e.getValue());
                }
            }
        }
    }

    public Royaume attaquant() {
        return this.attaquant;
    }

    public Royaume defenseur() {
        return this.defenseur;
    }

    public RapportCombat rapport() {
        return this.rapport;
    }

    /** Effectif d'armee de l'attaquant juste avant la bataille. */
    public int effectifAvantAttaquant() {
        return this.effectifAvantAttaquant;
    }

    /** Effectif d'armee du defenseur juste avant la bataille. */
    public int effectifAvantDefenseur() {
        return this.effectifAvantDefenseur;
    }

    public int pertesCivilesDefenseur() {
        return this.pertesCivilesDefenseur;
    }

    public Map<Ressource, Integer> butin() {
        return this.butin;
    }
}
