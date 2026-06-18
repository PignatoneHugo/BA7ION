package Controleur;

import Modele.action.ActionRecruterVillageois;
import Modele.economie.NiveauTaxes;
import Modele.economie.Ressource;
import Modele.partie.Partie;
import Modele.population.Role;
import Vue.FenetreJeu;
import Vue.onglets.OngletEconomie;

import config.Equilibrage;

// Controleur de l'onglet Economie : boutons +/-, taxes et recrutement.
public class ControleurEconomie extends ControleurOnglet {

    private final OngletEconomie onglet;
    private final FenetreJeu fenetre;

    /**
     * Construit le controleur de l'onglet Economie et branche ses boutons.
     *
     * @param partie la partie en cours
     * @param fenetre la fenetre de jeu
     */
    public ControleurEconomie(Partie partie, FenetreJeu fenetre) {
        super(partie);
        this.fenetre = fenetre;
        this.onglet = fenetre.dashboard().ongletEconomie();
        miseEnPlaceEvenements();
    }

    private void miseEnPlaceEvenements() {
        for (Role roleCourant : Role.values()) {
            if (roleCourant == Role.INACTIF) {
                continue;
            }
            final Role role = roleCourant;
            this.onglet.boutonPlus(role).addActionListener(evenement -> ajouter(role));
            this.onglet.boutonMoins(role).addActionListener(evenement -> retirer(role));
        }
        for (NiveauTaxes niveauCourant : NiveauTaxes.values()) {
            final NiveauTaxes niveau = niveauCourant;
            this.onglet.toggleTaxes(niveau).addActionListener(evenement -> changerTaxes(niveau));
        }
        this.onglet.boutonRecruterVillageois().addActionListener(evenement -> recruterVillageois());
    }

    private void ajouter(Role role) {
        boolean reussite = this.royaumeJoueur.reaffecter(Role.INACTIF, role, 1);
        if (reussite) {
            this.fenetre.statusBar().setMessage(
                    "+1 habitant affecte" + " : "
                            + role.libelle());
        }
    }

    private void retirer(Role role) {
        boolean reussite = this.royaumeJoueur.reaffecter(role, Role.INACTIF, 1);
        if (reussite) {
            this.fenetre.statusBar().setMessage(
                    "-1 habitant retire" + " : "
                            + role.libelle());
        }
    }

    private void changerTaxes(NiveauTaxes niveau) {
        this.royaumeJoueur.definirNiveauTaxes(niveau);
        this.fenetre.statusBar().setMessage(
                "Taxes ajustees" + " : "
                        + niveau.libelle());
    }

    private void recruterVillageois() {
        ActionRecruterVillageois action = new ActionRecruterVillageois();
        if (!action.estExecutable(this.royaumeJoueur)) {
            return;
        }
        // Action immediate, pas besoin de la file.
        this.royaumeJoueur.tresor().retirer(Ressource.NOURRITURE,
                Equilibrage.COUT_NOURRITURE_PAR_VILLAGEOIS);
        this.royaumeJoueur.population().ajouterInactifs(1);
        this.royaumeJoueur.notifierTresorChange();
        this.royaumeJoueur.notifierPopulationChangee();
        this.fenetre.statusBar().setMessage("Nouveau villageois accueilli au royaume");
    }
}
