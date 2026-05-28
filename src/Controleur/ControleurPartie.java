package Controleur;

import javax.swing.SwingUtilities;

import Modele.partie.Partie;
import Vue.FenetreJeu;
import Vue.VueHUD;

/**
 * Chef d'orchestre cote controleur. Detient la reference vers la {@link Partie}
 * et expose les actions de haut niveau accessibles depuis l'interface, en
 * commencant par la fin de tour.
 *
 * A l'instanciation, le controleur s'abonne aux composants Swing de la
 * {@link FenetreJeu} qui exposent les declencheurs utilisateur (bouton de fin
 * de tour notamment). Cette responsabilite de cablage suit la convention du
 * projet : aucun listener n'est branche dans le constructeur d'une vue.
 *
 * La fin de tour consiste a faire avancer la machine a etats du tour jusqu'a
 * son retour en phase de planification. Aucun dialogue bloquant n'est emis
 * pour le moment.
 */
public class ControleurPartie {

    private final Partie partie;
    private final FenetreJeu fenetre;

    /**
     * @param partie modele racine pilote par ce controleur
     * @param fenetre fenetre principale dont les composants seront cables
     */
    public ControleurPartie(Partie partie, FenetreJeu fenetre) {
        this.partie = partie;
        this.fenetre = fenetre;
        miseEnPlaceEvenements();
    }

    /**
     * Cable les ActionListener sur les composants Swing de la fenetre.
     */
    private void miseEnPlaceEvenements() {
        VueHUD hud = this.fenetre.hud();
        hud.boutonFinTour().addActionListener(e -> terminerTour());
    }

    /**
     * Declenche la resolution du tour courant : enchaine les phases de la
     * machine a etats jusqu'au retour en phase de planification, puis notifie
     * le demarrage du tour suivant.
     *
     * Pour eviter qu'une erreur de definition des transitions ne provoque
     * une boucle infinie, un garde-fou interrompt l'execution au-dela d'un
     * nombre de phases largement superieur au cycle nominal.
     */
    public void terminerTour() {
        SwingUtilities.invokeLater(() -> {
            int garde = 0;
            do {
                this.partie.passerEtape();
                garde++;
                if (garde > 50) {
                    throw new IllegalStateException(
                            "Boucle de resolution > 50 phases : transition incorrecte ?");
                }
            } while (!this.partie.enAttenteJoueur());
            this.partie.notifierTourDemarre();
        });
    }
}
