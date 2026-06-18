package Controleur;

import Vue.FenetreJeu;

// Controleur de l'ecran de fin de partie : boutons Rejouer et Menu.
public class ControleurFinPartie {

    private final FenetreJeu fenetre;

    /**
     * Construit le controleur de l'ecran de fin de partie et branche ses boutons.
     *
     * @param fenetre la fenetre de jeu
     */
    public ControleurFinPartie(FenetreJeu fenetre) {
        this.fenetre = fenetre;
        miseEnPlaceEvenements();
    }

    private void miseEnPlaceEvenements() {
        this.fenetre.vueFinPartie().boutonRejouer().addActionListener(
                evenement -> this.fenetre.afficherNouvellePartie());
        this.fenetre.vueFinPartie().boutonMenuPrincipal().addActionListener(
                evenement -> this.fenetre.afficherMenu());
    }
}
