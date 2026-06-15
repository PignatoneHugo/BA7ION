package Controleur;

import Modele.partie.Partie;
import Modele.royaume.Royaume;

// Classe mere des controleurs d'onglets : donne acces a la Partie et au joueur.
public abstract class ControleurOnglet {

    protected final Partie partie;
    protected final Royaume royaumeJoueur;

    protected ControleurOnglet(Partie partie) {
        this.partie = partie;
        this.royaumeJoueur = partie.joueur();
    }
}
