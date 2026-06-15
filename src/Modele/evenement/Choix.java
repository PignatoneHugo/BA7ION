package Modele.evenement;

import Modele.royaume.Royaume;

/** Un choix propose au joueur : un libelle et un effet sur le royaume. */
public class Choix {

    private final String libelle;
    private final EffetEvenement effet;

    public Choix(String libelle, EffetEvenement effet) {
        if (libelle == null || effet == null) {
            throw new IllegalArgumentException("libelle et effet ne peuvent pas etre null.");
        }
        this.libelle = libelle;
        this.effet = effet;
    }

    public String libelle() {
        return this.libelle;
    }

    public EffetEvenement effet() {
        return this.effet;
    }

    // vrai si le royaume a les ressources pour ce choix
    public boolean peutEtreChoisi(Royaume royaume) {
        return this.effet.peutEtreApplique(royaume);
    }
}
