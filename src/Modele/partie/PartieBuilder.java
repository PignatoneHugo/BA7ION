package Modele.partie;

import java.util.ArrayList;
import java.util.List;

import Modele.royaume.Royaume;

/**
 * Constructeur fluide pour {@link Partie}. Permet de configurer les
 * parametres optionnels (nom du joueur, nombre d'adversaires, graine
 * aleatoire) puis de produire une instance prete a l'emploi via {@link #build()}.
 *
 * <p>Exemple :</p>
 * <pre>
 *     Partie p = new PartieBuilder()
 *             .nomJoueur("Mon Royaume")
 *             .nombreBots(2)
 *             .graineAleatoire(42L)
 *             .build();
 * </pre>
 */
public class PartieBuilder {

    private String nomJoueur = "Royaume du Joueur";
    private int nombreBots = 0;
    private long graineAleatoire = System.nanoTime();

    /**
     * Definit le nom du royaume du joueur. Une valeur null ou blanche est
     * silencieusement ignoree et la valeur par defaut est conservee.
     *
     * @return cette instance, pour chainage
     */
    public PartieBuilder nomJoueur(String nom) {
        if (nom != null && !nom.isBlank()) {
            this.nomJoueur = nom;
        }
        return this;
    }

    /**
     * @param n nombre de bots adverses, entre 0 et 4 inclus
     * @return cette instance, pour chainage
     * @throws IllegalArgumentException si {@code n} est hors de l'intervalle accepte
     */
    public PartieBuilder nombreBots(int n) {
        if (n < 0 || n > 4) {
            throw new IllegalArgumentException("Le nombre de bots doit etre entre 0 et 4.");
        }
        this.nombreBots = n;
        return this;
    }

    /**
     * Fixe la graine du generateur aleatoire de la partie. Indispensable
     * pour rendre reproductibles les tests automatises sur les composants
     * aleatoires (combats, evenements, IA).
     *
     * @return cette instance, pour chainage
     */
    public PartieBuilder graineAleatoire(long graine) {
        this.graineAleatoire = graine;
        return this;
    }

    public long graineAleatoire() {
        return this.graineAleatoire;
    }

    /**
     * Produit la partie configuree. Le joueur et les bots sont instancies
     * comme des royaumes vierges ; les bots ne sont pas encore relies a une
     * strategie d'IA.
     *
     * @return une nouvelle partie prete a etre demarree
     */
    public Partie build() {
        Royaume joueur = new Royaume(this.nomJoueur);
        List<Royaume> bots = new ArrayList<>();
        for (int i = 0; i < this.nombreBots; i++) {
            bots.add(new Royaume("Bot " + (i + 1)));
        }
        return new Partie(joueur, bots);
    }
}
