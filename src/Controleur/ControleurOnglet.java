package Controleur;

import Modele.partie.Partie;
import Modele.royaume.Royaume;

/**
 * Classe parente des controleurs d'onglets metier.
 * Donne acces a la Partie et au Royaume joueur a toutes les sous-classes.
 */
public abstract class ControleurOnglet {

    protected final Partie partie;
    protected final Royaume royaumeJoueur;

    protected ControleurOnglet(Partie partie) {
        this.partie = partie;
        this.royaumeJoueur = partie.joueur();
    }
}
