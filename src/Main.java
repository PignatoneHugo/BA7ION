import javax.swing.SwingUtilities;

import Controleur.ControleurPartie;
import Modele.partie.Partie;
import Modele.partie.PartieBuilder;
import Modele.population.Role;
import Vue.FenetreJeu;

/**
 * Point d'entree de l'application BAS7ION. Cable les trois couches du MVC
 * dans l'ordre :
 * <ol>
 *   <li>construction du modele via {@link PartieBuilder} ;</li>
 *   <li>creation de la {@link FenetreJeu} (vue) sur l'Event Dispatch Thread ;</li>
 *   <li>instanciation du {@link ControleurPartie}, qui s'abonne aux
 *       composants Swing exposes par la vue.</li>
 * </ol>
 *
 * Toute la creation de l'interface graphique est confinee a l'EDT, comme
 * l'exige Swing.
 */
public class Main {

    public static void main(String[] args) {
        // Choix de la langue de l'interface.
        // Par defaut, le jeu demarre en francais (cf. Traducteur).
        // Pour tester l'interface en anglais, decommenter la ligne suivante :
        // Vue.i18n.Traducteur.definirLocale(java.util.Locale.ENGLISH);

        Partie partie = new PartieBuilder()
                .nomJoueur("Royaume du Joueur")
                .nombreBots(0)
                .build();

        // Affectation initiale qui rend la production de nourriture visible
        // des le premier tour : 6 fermiers * 2 = 12 produits, 10 habitants
        // consomment 10, soit un solde net de +2 par tour.
        partie.joueur().reaffecter(Role.INACTIF, Role.FERMIER, 6);

        SwingUtilities.invokeLater(() -> {
            FenetreJeu fenetre = new FenetreJeu(partie);
            new ControleurPartie(partie, fenetre);
            fenetre.setVisible(true);
        });
    }
}
