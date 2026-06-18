package Modele.infrastructure;

import Modele.royaume.Royaume;

import config.Equilibrage;

// Classe mere des 9 batiments. produire() gere le chantier puis delegue
// a appliquerProduction(), redefinie par chaque batiment.
public abstract class Batiment {

    protected int niveau;

    // True si endommage : production reduite.
    protected boolean endommage;

    // Tours restants avant la fin d'une amelioration (0 si pas de chantier).
    protected int toursRestants;

    protected Batiment() {
        this.niveau = 1;
        this.endommage = false;
        this.toursRestants = 0;
    }

    /**
     * Donne le type du batiment.
     *
     * @return le type du batiment
     */
    public abstract TypeBatiment type();

    /**
     * Donne le niveau actuel du batiment.
     *
     * @return le niveau du batiment
     */
    public int niveau() {
        return this.niveau;
    }

    /**
     * Indique si le batiment est endommage.
     *
     * @return true si le batiment est endommage
     */
    public boolean estEndommage() {
        return this.endommage;
    }

    /**
     * Marque le batiment comme endommage ou non.
     *
     * @param endommage true pour marquer le batiment endommage
     */
    public void marquerEndommage(boolean endommage) {
        this.endommage = endommage;
    }

    /**
     * Donne le nombre de tours restants avant la fin d'un chantier.
     *
     * @return le nombre de tours restants
     */
    public int toursRestants() {
        return this.toursRestants;
    }

    /**
     * Indique si une amelioration est en cours.
     *
     * @return true si un chantier est en cours
     */
    public boolean enChantier() {
        return this.toursRestants > 0;
    }

    /**
     * Indique si le batiment peut encore etre ameliore, sans etre au max ni en chantier.
     *
     * @return true si le batiment peut etre ameliore
     */
    public boolean peutEtreAmeliore() {
        return !enChantier() && this.niveau < Equilibrage.NIVEAU_MAX_BATIMENT;
    }

    /**
     * Restaure le niveau et le chantier depuis une sauvegarde.
     *
     * @param niveau le niveau a restaurer
     * @param toursRestants les tours de chantier restants a restaurer
     */
    public void restaurer(int niveau, int toursRestants) {
        this.niveau = Math.max(1, Math.min(niveau, Equilibrage.NIVEAU_MAX_BATIMENT));
        this.toursRestants = Math.max(0, toursRestants);
    }

    /**
     * Lance un chantier d'amelioration du batiment.
     *
     * @throws IllegalStateException si le batiment ne peut pas etre ameliore
     */
    public void demarrerChantier() {
        if (!peutEtreAmeliore()) {
            throw new IllegalStateException("Batiment non ameliorable.");
        }
        this.toursRestants = Equilibrage.DUREE_CHANTIER_AMELIORATION;
    }

    /**
     * Produit, ou fait avancer le chantier en cours s'il y en a un.
     *
     * @param royaume le royaume sur lequel s'applique la production
     * @throws IllegalArgumentException si le royaume est null
     */
    public final void produire(Royaume royaume) {
        if (royaume == null) {
            throw new IllegalArgumentException("Royaume requis pour la production.");
        }
        if (enChantier()) {
            this.toursRestants--;
            if (this.toursRestants == 0) {
                this.niveau++;
                // On applique le nouveau niveau tout de suite, sinon
                // l'amelioration semble sans effet le tour ou elle finit.
                appliquerProduction(royaume);
            }
            return;
        }
        appliquerProduction(royaume);
    }

    // Production propre a chaque batiment.
    protected abstract void appliquerProduction(Royaume royaume);
}
