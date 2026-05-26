package Modele.infrastructure;

import Modele.royaume.Royaume;

/**
 * Classe abstraite parente des 9 batiments.
 *
 * Utilise le pattern Template Method pour le calcul de production :
 *   - {@link #produire(Royaume)} (final) decrit le squelette,
 *   - les sous-classes implementent {@link #appliquerProduction(Royaume)} pour
 *     definir la ressource produite et la formule de calcul.
 *
 * Au Sprint 1 on ne gere ni les niveaux d'amelioration ni les degats ; ils
 * seront ajoutes dans un Sprint suivant.
 */
public abstract class Batiment {

    /** Niveau actuel du batiment (1 a 5, max selon US-INFRA-11). */
    protected int niveau;

    /** True si le batiment est endommage (production reduite, cf. US-INFRA-23). */
    protected boolean endommage;

    protected Batiment() {
        this.niveau = 1;
        this.endommage = false;
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

    /**
     * Augmente le niveau de 1. A appeler en fin de chantier d'amelioration.
     */
    public void monterNiveau() {
        this.niveau++;
    }

    /**
     * Template Method : applique la production du batiment au tresor du royaume.
     * A appeler durant la phase EtatProduction du cycle de tour.
     *
     * Les sous-classes implementent {@link #appliquerProduction(Royaume)} pour
     * la formule specifique. L'attribut {@code endommage} reduit l'effet (US-INFRA-23).
     */
    public final void produire(Royaume royaume) {
        if (royaume == null) {
            throw new IllegalArgumentException("Royaume requis pour la production.");
        }
        appliquerProduction(royaume);
    }

    /**
     * A implementer par chaque sous-classe : ajoute au tresor du royaume la
     * production calculee a partir du nombre d'habitants concernes et des
     * modificateurs (niveau, decrets, saison, endommagement).
     */
    protected abstract void appliquerProduction(Royaume royaume);
}
