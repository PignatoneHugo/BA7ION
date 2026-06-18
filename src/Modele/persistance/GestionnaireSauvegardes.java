package Modele.persistance;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import Modele.partie.Partie;

/**
 * Lit et ecrit les fichiers .json d'une partie.
 * Deux usages : fichier libre choisi par le joueur, ou slots 1..5 + autosave.
 */
public final class GestionnaireSauvegardes {

    /** Dossier par defaut des sauvegardes. */
    public static final String DOSSIER = "saves";

    public static final String EXTENSION = ".json";

    private static final int NB_SLOTS = 5;

    private GestionnaireSauvegardes() {
        // Classe utilitaire.
    }

    // ----- Fichier libre (boutons + JFileChooser) -----

    /**
     * Sauvegarde la partie dans le fichier indique.
     *
     * @param partie la partie a sauvegarder
     * @param fichier le fichier de destination
     * @throws IOException en cas d'erreur d'ecriture
     */
    public static void sauvegarderDans(Partie partie, File fichier) throws IOException {
        ecrire(new Sauvegarde(partie), fichier);
    }

    /**
     * Charge une partie depuis le fichier indique.
     *
     * @param fichier le fichier a lire
     * @return la sauvegarde lue
     * @throws IOException si le fichier est introuvable ou illisible
     */
    public static Sauvegarde chargerDepuis(File fichier) throws IOException {
        if (fichier == null || !fichier.exists()) {
            throw new IOException("Fichier de sauvegarde introuvable.");
        }
        return lire(fichier);
    }

    // ----- Autosave (fin de tour), nommee d'apres le royaume -----

    /**
     * Donne le fichier d'autosave nomme d'apres le royaume du joueur.
     *
     * @param partie la partie concernee
     * @return le fichier d'autosave
     */
    public static File fichierAuto(Partie partie) {
        return new File(DOSSIER, nomFichierSur(partie.joueur().nom()) + EXTENSION);
    }

    /**
     * Sauvegarde automatiquement la partie dans son fichier d'autosave.
     *
     * @param partie la partie a sauvegarder
     * @throws IOException en cas d'erreur d'ecriture
     */
    public static void sauvegarderAuto(Partie partie) throws IOException {
        ecrire(new Sauvegarde(partie), fichierAuto(partie));
    }

    // nettoie le nom du royaume pour en faire un nom de fichier valide
    private static String nomFichierSur(String nom) {
        String sur = (nom == null ? "" : nom).replaceAll("[^\\p{L}\\p{N} _-]", "_").trim();
        return sur.isEmpty() ? "sauvegarde" : sur;
    }

    // ----- Slots numerotes -----

    /**
     * Sauvegarde la partie dans le slot indique (1..5).
     *
     * @param partie la partie a sauvegarder
     * @param slot le numero de slot (1 a 5)
     * @throws IOException en cas d'erreur d'ecriture
     * @throws IllegalArgumentException si le slot n'est pas entre 1 et 5
     */
    public static void sauvegarder(Partie partie, int slot) throws IOException {
        verifierSlot(slot);
        ecrire(new Sauvegarde(partie), new File(cheminSlot(slot)));
    }

    /**
     * Charge le slot indique, ou null s'il est vide.
     *
     * @param slot le numero de slot (1 a 5)
     * @return la sauvegarde du slot, ou null si le slot est vide
     * @throws IOException en cas d'erreur de lecture
     * @throws IllegalArgumentException si le slot n'est pas entre 1 et 5
     */
    public static Sauvegarde charger(int slot) throws IOException {
        verifierSlot(slot);
        File fichier = new File(cheminSlot(slot));
        return fichier.exists() ? lire(fichier) : null;
    }

    /**
     * Indique si le slot contient une sauvegarde.
     *
     * @param slot le numero de slot
     * @return vrai si le slot contient une sauvegarde
     */
    public static boolean slotExiste(int slot) {
        return new File(cheminSlot(slot)).exists();
    }

    /**
     * Supprime la sauvegarde du slot indique si elle existe.
     *
     * @param slot le numero de slot
     */
    public static void supprimer(int slot) {
        File fichier = new File(cheminSlot(slot));
        if (fichier.exists()) {
            fichier.delete();
        }
    }

    // -------------------------------------------------------------------

    private static void verifierSlot(int slot) {
        if (slot < 1 || slot > NB_SLOTS) {
            throw new IllegalArgumentException("Slot doit etre entre 1 et " + NB_SLOTS);
        }
    }

    private static String cheminSlot(int slot) {
        return DOSSIER + "/slot_" + slot + EXTENSION;
    }

    private static void ecrire(Sauvegarde sauvegarde, File fichier) throws IOException {
        File parent = fichier.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        Files.write(fichier.toPath(), sauvegarde.versJson().getBytes(StandardCharsets.UTF_8));
    }

    private static Sauvegarde lire(File fichier) throws IOException {
        String json = new String(Files.readAllBytes(fichier.toPath()), StandardCharsets.UTF_8);
        try {
            return Sauvegarde.depuisJson(json);
        } catch (RuntimeException exception) {
            // checksum invalide ou erreur de parsing
            throw new IOException(exception.getMessage(), exception);
        }
    }
}
