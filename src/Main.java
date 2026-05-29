import javax.swing.SwingUtilities;

import Controleur.ControleurPartie;
import Modele.partie.Partie;
import Modele.partie.PartieBuilder;
import Modele.population.Role;
import Vue.FenetreJeu;

/**
 * Point d'entree du jeu. Cable Modele + Vue + Controleur.
 */
public class Main {

    public static void main(String[] args) {
        // Langue de l'interface (FR par defaut).
        // Pour tester en anglais : Traducteur.definirLocale(Locale.ENGLISH);
        // Vue.i18n.Traducteur.definirLocale(java.util.Locale.ENGLISH);

        Partie partie = new PartieBuilder()
                .nomJoueur("Royaume du Joueur")
                .nombreBots(0)
                .build();

        // 6 fermiers pour avoir un bilan net de +2 nourriture/tour
        // (6*2 produits - 10 habitants consomment).
        partie.joueur().reaffecter(Role.INACTIF, Role.FERMIER, 6);

        SwingUtilities.invokeLater(() -> {
            FenetreJeu fenetre = new FenetreJeu(partie);
            new ControleurPartie(partie, fenetre);
            fenetre.setVisible(true);
        });
    }
}
