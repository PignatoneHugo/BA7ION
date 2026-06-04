package Vue.dialogue;

import java.awt.Component;
import java.util.List;

import javax.swing.JOptionPane;

import Modele.evenement.Choix;
import Modele.evenement.Evenement;
import Vue.i18n.Traducteur;

/**
 * Popup modal qui affiche un evenement et propose ses choix au joueur.
 * Bloque l'EDT jusqu'a ce que le joueur ait clique sur l'un des boutons.
 *
 * Vue passive : retourne juste le Choix selectionne, c'est au controleur
 * d'appeler partie.resoudreEvenement(choix).
 */
public final class DialogueEvenement {

    private DialogueEvenement() {
        // Classe utilitaire.
    }

    /**
     * Affiche le dialogue modal de l'evenement et attend la reponse du joueur.
     *
     * @param parent composant parent pour le positionnement
     * @param evenement evenement a presenter
     * @return le choix selectionne, ou le premier choix par defaut si le
     *         joueur a ferme la fenetre sans choisir
     */
    public static Choix afficher(Component parent, Evenement evenement) {
        List<Choix> choix = evenement.choix();
        String[] options = new String[choix.size()];
        for (int i = 0; i < choix.size(); i++) {
            options[i] = Traducteur.t(choix.get(i).cleI18n());
        }

        int indice = JOptionPane.showOptionDialog(
                parent,
                Traducteur.t(evenement.cleDescription()),
                Traducteur.t(evenement.cleTitre()),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options[0]);

        // Si le joueur ferme la fenetre, on prend le premier choix par defaut.
        if (indice == JOptionPane.CLOSED_OPTION || indice < 0) {
            return choix.get(0);
        }
        return choix.get(indice);
    }
}
