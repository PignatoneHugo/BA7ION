package Controleur;

import javax.swing.JOptionPane;

import Modele.partie.Partie;
import Modele.partie.PartieBuilder;
import Modele.population.Role;
import Vue.FenetreJeu;
import Vue.i18n.Traducteur;
import Vue.menu.VueMenuPrincipal;
import Vue.menu.VueNouvellePartie;

/**
 * Controleur des ecrans hors-jeu : menu principal et configuration de partie.
 * Gere les transitions entre ecrans et instancie la Partie quand le joueur
 * clique "Demarrer".
 */
public class ControleurMenu {

    private final FenetreJeu fenetre;

    public ControleurMenu(FenetreJeu fenetre) {
        this.fenetre = fenetre;
        miseEnPlaceEvenements();
    }

    private void miseEnPlaceEvenements() {
        VueMenuPrincipal menu = this.fenetre.vueMenu();
        menu.boutonNouvellePartie().addActionListener(e -> this.fenetre.afficherNouvellePartie());
        menu.boutonOptions().addActionListener(e -> ouvrirOptions());
        menu.boutonQuitter().addActionListener(e -> System.exit(0));

        VueNouvellePartie config = this.fenetre.vueNouvellePartie();
        config.boutonRetour().addActionListener(e -> this.fenetre.afficherMenu());
        config.boutonDemarrer().addActionListener(e -> demarrer());
    }

    private void ouvrirOptions() {
        // Sera implemente plus tard (ecran Options pour la langue, le theme, etc.).
        JOptionPane.showMessageDialog(this.fenetre,
                Traducteur.t("menu.options.indisponible"),
                Traducteur.t("menu.options"),
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void demarrer() {
        VueNouvellePartie config = this.fenetre.vueNouvellePartie();

        Partie partie = new PartieBuilder()
                .nomJoueur(config.nomJoueur())
                .nombreBots(config.nombreBots())
                .difficulte(config.difficulteSelectionnee())
                .build();

        // Affectation initiale qui rend la production de nourriture visible
        // des le premier tour (6 fermiers - 10 habitants = +2 nourriture/tour).
        partie.joueur().reaffecter(Role.INACTIF, Role.FERMIER, 6);

        this.fenetre.afficherJeu(partie);
        new ControleurPartie(partie, this.fenetre);
    }
}
