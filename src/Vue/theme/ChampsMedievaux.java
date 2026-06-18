package Vue.theme;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.border.Border;

/** Helpers pour styler les champs de saisie (texte, spinner, combo) en sombre. */
public final class ChampsMedievaux {

    private ChampsMedievaux() {
        // classe utilitaire
    }

    // bordure doree commune
    private static Border bordureChamp() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Palette.OR, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8));
    }

    /**
     * Applique le style sombre a un champ de texte.
     *
     * @param champ le champ a styler
     */
    public static void stylerChamp(JTextField champ) {
        champ.setOpaque(true);
        champ.setBackground(Palette.CHAMP_FOND);
        champ.setForeground(Palette.CHAMP_TEXTE);
        champ.setCaretColor(Palette.OR);
        champ.setBorder(bordureChamp());
    }

    /**
     * Applique le style sombre a un spinner, son champ et ses fleches.
     *
     * @param spinner le spinner a styler
     */
    public static void stylerSpinner(JSpinner spinner) {
        spinner.setOpaque(true);
        spinner.setBackground(Palette.CHAMP_FOND);
        spinner.setForeground(Palette.CHAMP_TEXTE);
        spinner.setBorder(BorderFactory.createLineBorder(Palette.OR, 1));

        if (spinner.getEditor() instanceof JSpinner.DefaultEditor) {
            JSpinner.DefaultEditor editeur = (JSpinner.DefaultEditor) spinner.getEditor();
            editeur.setOpaque(true);
            editeur.setBackground(Palette.CHAMP_FOND);
            JTextField tf = editeur.getTextField();
            tf.setOpaque(true);
            tf.setBackground(Palette.CHAMP_FOND);
            tf.setForeground(Palette.CHAMP_TEXTE);
            tf.setCaretColor(Palette.OR);
            tf.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
        }

        // les fleches haut/bas
        for (Component comp : spinner.getComponents()) {
            if (comp instanceof JButton) {
                comp.setBackground(Palette.BOUTON_FOND);
                ((JButton) comp).setBorder(
                        BorderFactory.createLineBorder(Palette.OR_FONCE, 1));
            }
        }
    }

    /**
     * Applique le style sombre a une liste deroulante.
     *
     * @param combo la liste deroulante a styler
     */
    public static void stylerCombo(JComboBox<?> combo) {
        combo.setOpaque(true);
        combo.setBackground(Palette.CHAMP_FOND);
        combo.setForeground(Palette.CHAMP_TEXTE);
        combo.setBorder(BorderFactory.createLineBorder(Palette.OR, 1));
    }
}
