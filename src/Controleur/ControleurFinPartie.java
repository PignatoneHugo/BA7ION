package Controleur;

import Vue.FenetreJeu;

/**
 * Controleur de l'ecran de fin de partie. Cable les boutons Rejouer
 * (retour a l'ecran de configuration de partie) et Menu principal
 * (retour a l'accueil).
 */
public class ControleurFinPartie {

    private final FenetreJeu fenetre;

    public ControleurFinPartie(FenetreJeu fenetre) {
        this.fenetre = fenetre;
        miseEnPlaceEvenements();
    }

    private void miseEnPlaceEvenements() {
        this.fenetre.vueFinPartie().boutonRejouer().addActionListener(
                e -> this.fenetre.afficherNouvellePartie());
        this.fenetre.vueFinPartie().boutonMenuPrincipal().addActionListener(
                e -> this.fenetre.afficherMenu());
    }
}
