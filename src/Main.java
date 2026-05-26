import javax.swing.SwingUtilities;

import Controleur.ControleurPartie;
import Modele.partie.Partie;
import Modele.partie.PartieBuilder;
import Modele.population.Role;
import Vue.FenetreJeu;

/**
 * Point d'entree du jeu BAS7ION (Sprint 1).
 *
 * Cable Modele + Vue + Controleur selon le pattern MVC du cours (cf. TP Chat).
 * Apres le Sprint 1, l'application passera d'abord par un MenuPrincipal qui
 * appellera ce cablage uniquement apres "Nouvelle partie".
 */
public class Main {

    public static void main(String[] args) {
        // --- Construction du modele ---
        Partie partie = new PartieBuilder()
                .nomJoueur("Royaume du Joueur")
                .nombreBots(0)
                .build();

        // Pour valider visuellement la production au Sprint 1, on affecte
        // 6 inactifs en fermiers des le tour 1.
        //   Production : 6 fermiers x 2 = 12 nourriture / tour
        //   Consommation : 10 habitants x 1 = 10 nourriture / tour
        //   Bilan net : +2 / tour --> la nourriture monte visiblement.
        // Cette affectation sera plus tard a la charge du joueur via l'UI.
        partie.joueur().reaffecter(Role.INACTIF, Role.FERMIER, 6);

        // --- Construction de la vue + controleur, sur l'EDT Swing ---
        SwingUtilities.invokeLater(() -> {
            FenetreJeu fenetre = new FenetreJeu(partie);
            new ControleurPartie(partie, fenetre);
            fenetre.setVisible(true);
        });
    }
}
