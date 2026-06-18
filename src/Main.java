import javax.swing.SwingUtilities;

import Controleur.ControleurMenu;
import Vue.FenetreJeu;

// Point d'entree du jeu : ouvre le menu principal.
public class Main {

    /**
     * Point d'entree du programme : ouvre le menu principal du jeu.
     *
     * @param args arguments de la ligne de commande (non utilises)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FenetreJeu fenetre = new FenetreJeu();
            new ControleurMenu(fenetre);
            fenetre.afficherMenu();
            fenetre.setVisible(true);
        });
    }
}
