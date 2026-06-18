package Controleur;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import Modele.partie.Partie;
import Modele.partie.PartieBuilder;
import Modele.persistance.GestionnaireSauvegardes;
import Modele.persistance.Sauvegarde;
import Modele.population.Role;
import Vue.FenetreJeu;
import Vue.menu.VueMenuPrincipal;
import Vue.menu.VueNouvellePartie;

// Controleur des ecrans hors-jeu : menu principal et config de partie.
public class ControleurMenu {

    private final FenetreJeu fenetre;

    /**
     * Construit le controleur des ecrans hors-jeu et branche leurs boutons.
     *
     * @param fenetre la fenetre de jeu
     */
    public ControleurMenu(FenetreJeu fenetre) {
        this.fenetre = fenetre;
        miseEnPlaceEvenements();
    }

    private void miseEnPlaceEvenements() {
        VueMenuPrincipal menu = this.fenetre.vueMenu();
        menu.boutonNouvellePartie().addActionListener(evenement -> this.fenetre.afficherNouvellePartie());
        menu.boutonCharger().addActionListener(evenement -> charger());
        menu.boutonQuitter().addActionListener(evenement -> System.exit(0));

        VueNouvellePartie config = this.fenetre.vueNouvellePartie();
        config.boutonRetour().addActionListener(evenement -> this.fenetre.afficherMenu());
        config.boutonDemarrer().addActionListener(evenement -> demarrer());
    }

    // Choisit un fichier, charge la sauvegarde et lance la partie.
    private void charger() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Charger une sauvegarde");
        File dossier = new File(GestionnaireSauvegardes.DOSSIER);
        if (dossier.isDirectory()) {
            chooser.setCurrentDirectory(dossier);
        }
        chooser.setFileFilter(new FileNameExtensionFilter(
                "Sauvegardes BA7ION (*.json)", "json"));
        if (chooser.showOpenDialog(this.fenetre) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            Sauvegarde sauvegarde = GestionnaireSauvegardes.chargerDepuis(chooser.getSelectedFile());
            Partie partie = PartieBuilder.depuisSauvegarde(sauvegarde);
            this.fenetre.afficherJeu(partie);
            new ControleurPartie(partie, this.fenetre);
        } catch (IOException | RuntimeException ex) {
            JOptionPane.showMessageDialog(this.fenetre,
                    "Impossible de charger la sauvegarde :\n" + ex.getMessage(),
                    "Erreur de chargement", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void demarrer() {
        VueNouvellePartie config = this.fenetre.vueNouvellePartie();

        Partie partie = new PartieBuilder()
                .nomJoueur(config.nomJoueur())
                .nombreBots(config.nombreBots())
                .difficulte(config.difficulteSelectionnee())
                .build();

        // On met 6 fermiers au depart pour avoir de la nourriture des le tour 1.
        partie.joueur().reaffecter(Role.INACTIF, Role.FERMIER, 6);

        this.fenetre.afficherJeu(partie);
        new ControleurPartie(partie, this.fenetre);
    }
}
