package Modele.infrastructure;

import Modele.royaume.Royaume;

/**
 * Squelette commun a tous les batiments d'un royaume.
 *
 * Cette classe applique le pattern Template Method : la methode publique
 * {@link #produire(Royaume)} est finale et fixe les invariants communs
 * (controles d'arguments, hooks futurs), tandis que la logique de production
 * propre a chaque batiment est implementee par les sous-classes dans
 * {@link #appliquerProduction(Royaume)}.
 */
public abstract class Batiment {

    /** Niveau du batiment, croissant. Le niveau initial est 1. */
    protected int niveau;

    /**
     * Indique si le batiment est actuellement endommage. Un batiment
     * endommage produit a effet reduit jusqu'a sa reparation.
     */
    protected boolean endommage;

    protected Batiment() {
        this.niveau = 1;
        this.endommage = false;
    }

    /**
     * @return type concret du batiment, utilise pour le filtrage et l'i18n
     */
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
     * Incremente le niveau du batiment d'une unite, typiquement appele en
     * fin de chantier d'amelioration.
     */
    public void monterNiveau() {
        this.niveau++;
    }

    /**
     * Applique la production du tour pour ce batiment. Methode finale : les
     * sous-classes redefinissent uniquement la formule de production via
     * {@link #appliquerProduction(Royaume)}.
     *
     * @param royaume royaume beneficiaire de la production, non null
     * @throws IllegalArgumentException si {@code royaume} est null
     */
    public final void produire(Royaume royaume) {
        if (royaume == null) {
            throw new IllegalArgumentException("Royaume requis pour la production.");
        }
        appliquerProduction(royaume);
    }

    /**
     * Calcule et applique la production de ce batiment sur le tresor du
     * royaume. L'implementation doit prendre en compte le {@link #niveau} et
     * l'etat {@link #endommage}.
     *
     * @param royaume royaume beneficiaire, garanti non null
     */
    protected abstract void appliquerProduction(Royaume royaume);
}
