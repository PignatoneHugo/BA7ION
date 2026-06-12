import javax.swing.SwingUtilities;

import Controleur.ControleurMenu;
import Vue.FenetreJeu;

/**
 * Point d'entree du jeu. Ouvre la fenetre sur le menu principal.
 * La Partie est creee plus tard, quand le joueur clique "Demarrer"
 * (cf. ControleurMenu).
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FenetreJeu fenetre = new FenetreJeu();
            new ControleurMenu(fenetre);
            fenetre.afficherMenu();
            fenetre.setVisible(true);
        });
    }
}
