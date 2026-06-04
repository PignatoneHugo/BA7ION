package Controleur;

import Modele.economie.NiveauTaxes;
import Modele.partie.Partie;
import Modele.population.Role;
import Vue.FenetreJeu;
import Vue.i18n.Traducteur;
import Vue.onglets.OngletEconomie;

/**
 * Controleur de l'onglet Economie. Attache les listeners sur :
 * - les boutons +/- des roles (deplace 1 habitant)
 * - les toggles de niveau de taxes
 *
 * Affiche un message dans la status bar apres chaque action pour confirmer
 * au joueur que sa demande a bien ete prise en compte.
 */
public class ControleurEconomie extends ControleurOnglet {

    private final OngletEconomie onglet;
    private final FenetreJeu fenetre;

    public ControleurEconomie(Partie partie, FenetreJeu fenetre) {
        super(partie);
        this.fenetre = fenetre;
        this.onglet = fenetre.dashboard().ongletEconomie();
        miseEnPlaceEvenements();
    }

    private void miseEnPlaceEvenements() {
        for (Role r : Role.values()) {
            if (r == Role.INACTIF) {
                continue;
            }
            final Role role = r;
            this.onglet.boutonPlus(role).addActionListener(e -> ajouter(role));
            this.onglet.boutonMoins(role).addActionListener(e -> retirer(role));
        }
        for (NiveauTaxes n : NiveauTaxes.values()) {
            final NiveauTaxes niveau = n;
            this.onglet.toggleTaxes(niveau).addActionListener(e -> changerTaxes(niveau));
        }
    }

    private void ajouter(Role role) {
        boolean ok = this.royaumeJoueur.reaffecter(Role.INACTIF, role, 1);
        if (ok) {
            this.fenetre.statusBar().setMessage(
                    Traducteur.t("status.role_ajoute") + " : "
                            + Traducteur.t(role.cleI18n()));
        }
    }

    private void retirer(Role role) {
        boolean ok = this.royaumeJoueur.reaffecter(role, Role.INACTIF, 1);
        if (ok) {
            this.fenetre.statusBar().setMessage(
                    Traducteur.t("status.role_retire") + " : "
                            + Traducteur.t(role.cleI18n()));
        }
    }

    private void changerTaxes(NiveauTaxes niveau) {
        this.royaumeJoueur.definirNiveauTaxes(niveau);
        this.fenetre.statusBar().setMessage(
                Traducteur.t("status.taxes_changees") + " : "
                        + Traducteur.t(niveau.cleI18n()));
    }
}
