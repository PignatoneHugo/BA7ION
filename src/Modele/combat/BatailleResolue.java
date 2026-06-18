package Modele.combat;

import java.util.EnumMap;
import java.util.Map;

import Modele.economie.Ressource;
import Modele.royaume.Royaume;

// Une bataille terminee : le rapport plus ce qui a vraiment ete applique
// (pertes civiles, butin). Sert au recap de fin de tour.
public class BatailleResolue {

    private final Royaume attaquant;
    private final Royaume defenseur;
    private final RapportCombat rapport;
    private final int effectifAvantAttaquant;
    private final int effectifAvantDefenseur;
    private final int pertesCivilesDefenseur;
    private final Map<Ressource, Integer> butin;

    /**
     * Cree le bilan d'une bataille terminee avec ce qui a vraiment ete applique.
     *
     * @param attaquant le royaume attaquant
     * @param defenseur le royaume defenseur
     * @param rapport le rapport du combat
     * @param effectifAvantAttaquant l'effectif de l'attaquant avant la bataille
     * @param effectifAvantDefenseur l'effectif du defenseur avant la bataille
     * @param pertesCivilesDefenseur le nombre de civils tues chez le defenseur
     * @param butin les ressources prises au defenseur
     */
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
            for (Map.Entry<Ressource, Integer> entree : butin.entrySet()) {
                if (entree.getValue() != null && entree.getValue() > 0) {
                    this.butin.put(entree.getKey(), entree.getValue());
                }
            }
        }
    }

    /**
     * Renvoie le royaume attaquant.
     *
     * @return le royaume attaquant
     */
    public Royaume attaquant() {
        return this.attaquant;
    }

    /**
     * Renvoie le royaume defenseur.
     *
     * @return le royaume defenseur
     */
    public Royaume defenseur() {
        return this.defenseur;
    }

    /**
     * Renvoie le rapport du combat.
     *
     * @return le rapport de combat
     */
    public RapportCombat rapport() {
        return this.rapport;
    }

    /**
     * Renvoie l'effectif de l'attaquant avant la bataille.
     *
     * @return l'effectif avant bataille de l'attaquant
     */
    public int effectifAvantAttaquant() {
        return this.effectifAvantAttaquant;
    }

    /**
     * Renvoie l'effectif du defenseur avant la bataille.
     *
     * @return l'effectif avant bataille du defenseur
     */
    public int effectifAvantDefenseur() {
        return this.effectifAvantDefenseur;
    }

    /**
     * Renvoie le nombre de civils tues chez le defenseur.
     *
     * @return le nombre de pertes civiles du defenseur
     */
    public int pertesCivilesDefenseur() {
        return this.pertesCivilesDefenseur;
    }

    /**
     * Renvoie les ressources prises au defenseur.
     *
     * @return le butin par ressource
     */
    public Map<Ressource, Integer> butin() {
        return this.butin;
    }
}
