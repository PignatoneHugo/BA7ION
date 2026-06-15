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

    // True si on peut encore ameliorer (pas au max, pas deja en chantier).
    public boolean peutEtreAmeliore() {
        return !enChantier() && this.niveau < Equilibrage.NIVEAU_MAX_BATIMENT;
    }

    // Restaure niveau et chantier depuis une sauvegarde.
    public void restaurer(int niveau, int toursRestants) {
        this.niveau = Math.max(1, Math.min(niveau, Equilibrage.NIVEAU_MAX_BATIMENT));
        this.toursRestants = Math.max(0, toursRestants);
    }

    public void demarrerChantier() {
        if (!peutEtreAmeliore()) {
            throw new IllegalStateException("Batiment non ameliorable.");
        }
        this.toursRestants = Equilibrage.DUREE_CHANTIER_AMELIORATION;
    }

    // Produit, ou fait avancer le chantier en cours.
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
