package Modele.evenement;

import Modele.royaume.Royaume;

/** Un choix propose au joueur : un libelle et un effet sur le royaume. */
public class Choix {

    private final String libelle;
    private final EffetEvenement effet;

    /**
     * Cree un choix avec son libelle et son effet.
     *
     * @param libelle le texte du choix
     * @param effet l'effet applique au royaume si le choix est pris
     * @throws IllegalArgumentException si libelle ou effet est null
     */
    public Choix(String libelle, EffetEvenement effet) {
        if (libelle == null || effet == null) {
            throw new IllegalArgumentException("libelle et effet ne peuvent pas etre null.");
        }
        this.libelle = libelle;
        this.effet = effet;
    }

    /**
     * Donne le libelle du choix.
     *
     * @return le libelle
     */
    public String libelle() {
        return this.libelle;
    }

    /**
     * Donne l'effet du choix.
     *
     * @return l'effet
     */
    public EffetEvenement effet() {
        return this.effet;
    }

    /**
     * Indique si le royaume peut prendre ce choix.
     *
     * @param royaume le royaume concerne
     * @return vrai si le royaume a les ressources pour ce choix
     */
    public boolean peutEtreChoisi(Royaume royaume) {
        return this.effet.peutEtreApplique(royaume);
    }
}
