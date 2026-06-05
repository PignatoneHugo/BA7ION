package Vue;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Vue.i18n.Traducteur;

/**
 * Barre d'etat permanente en bas de la fenetre. Affiche un message court
 * qui informe le joueur de la derniere action effectuee (clic, fin de tour,
 * evenement, etc.).
 *
 * Mise a jour par les controleurs via setMessage().
 */
public class VueStatusBar extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JLabel labelMessage;

    public VueStatusBar() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 0, 0, java.awt.Color.DARK_GRAY),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)));

        this.labelMessage = new JLabel(Traducteur.t("status.pret"));
        add(this.labelMessage, BorderLayout.WEST);
    }

    /** Definit le message affiche dans la barre d'etat. */
    public void setMessage(String texte) {
        this.labelMessage.setText(texte != null ? texte : "");
    }
}
