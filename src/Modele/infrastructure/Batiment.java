package Modele.infrastructure;

import Modele.royaume.Royaume;

/**
 * Classe abstraite parente des 9 batiments. Pattern Template Method :
 * la methode produire() est finale et appelle appliquerProduction()
 * que chaque sous-classe redefinit avec sa propre formule.
 */
public abstract class Batiment {

    /** Niveau du batiment (1 par defaut). */
    protected int niveau;

    /** True si le batiment est endommage : production reduite. */
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

    public void monterNiveau() {
        this.niveau++;
    }

    /** Applique la production du batiment. Appele pendant la phase de production. */
    public final void produire(Royaume royaume) {
        if (royaume == null) {
            throw new IllegalArgumentException("Royaume requis pour la production.");
        }
        appliquerProduction(royaume);
    }

    /** Formule de production specifique a chaque type de batiment. */
    protected abstract void appliquerProduction(Royaume royaume);
}
