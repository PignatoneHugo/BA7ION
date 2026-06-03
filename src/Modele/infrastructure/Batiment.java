package Modele.infrastructure;

import Modele.royaume.Royaume;

import config.Equilibrage;

/**
 * Classe abstraite parente des 9 batiments. Pattern Template Method :
 * la methode produire() est finale et appelle appliquerProduction()
 * que chaque sous-classe redefinit avec sa propre formule.
 *
 * Gere aussi le chantier d'amelioration : tant que toursRestants > 0,
 * le batiment ne produit pas, et le compteur descend a chaque tour.
 * Quand il arrive a 0, le niveau monte.
 */
public abstract class Batiment {

    /** Niveau du batiment (1 par defaut). */
    protected int niveau;

    /** True si le batiment est endommage : production reduite. */
    protected boolean endommage;

    /** Tours restants avant la fin du chantier d'amelioration (0 si pas de chantier). */
    protected int toursRestants;

    protected Batiment() {
        this.niveau = 1;
        this.endommage = false;
        this.toursRestants = 0;
    }

    public abstract TypeBatiment type();

    public int niveau() {
        return this.niveau;
    }

    public boolean estEndommage() {
        return this.endommage;
    }

    public void marquerEndommage(boolean endommage) {
        this.endommage = endommage;
    }

    public int toursRestants() {
        return this.toursRestants;
    }

    public boolean enChantier() {
        return this.toursRestants > 0;
    }

    /** True si on peut encore ameliorer ce batiment (pas au max, pas deja en chantier). */
    public boolean peutEtreAmeliore() {
        return !enChantier() && this.niveau < Equilibrage.NIVEAU_MAX_BATIMENT;
    }

    /** Demarre un chantier d'amelioration. */
    public void demarrerChantier() {
        if (!peutEtreAmeliore()) {
            throw new IllegalStateException("Batiment non ameliorable.");
        }
        this.toursRestants = Equilibrage.DUREE_CHANTIER_AMELIORATION;
    }

    /**
     * Applique la production du batiment (ou avance le chantier en cours).
     * Appele pendant la phase de production.
     */
    public final void produire(Royaume royaume) {
        if (royaume == null) {
            throw new IllegalArgumentException("Royaume requis pour la production.");
        }
        if (enChantier()) {
            this.toursRestants--;
            if (this.toursRestants == 0) {
                this.niveau++;
            }
            return;
        }
        appliquerProduction(royaume);
    }

    /** Formule de production specifique a chaque type de batiment. */
    protected abstract void appliquerProduction(Royaume royaume);
}
