import javax.swing.SwingUtilities;

import Controleur.ControleurMenu;
import Vue.FenetreJeu;

// Point d'entree du jeu : ouvre le menu principal.
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
