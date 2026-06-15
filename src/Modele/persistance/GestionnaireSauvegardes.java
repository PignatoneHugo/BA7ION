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

    /** Sauvegarde la partie dans le fichier indique. */
    public static void sauvegarderDans(Partie partie, File fichier) throws IOException {
        ecrire(new Sauvegarde(partie), fichier);
    }

    /** Charge une partie depuis le fichier indique. */
    public static Sauvegarde chargerDepuis(File fichier) throws IOException {
        if (fichier == null || !fichier.exists()) {
            throw new IOException("Fichier de sauvegarde introuvable.");
        }
        return lire(fichier);
    }

    // ----- Autosave (fin de tour), nommee d'apres le royaume -----

    /** Fichier d'autosave : "saves/&lt;nom du royaume&gt;.json". */
    public static File fichierAuto(Partie partie) {
        return new File(DOSSIER, nomFichierSur(partie.joueur().nom()) + EXTENSION);
    }

    /** Autosave de la partie. */
    public static void sauvegarderAuto(Partie partie) throws IOException {
        ecrire(new Sauvegarde(partie), fichierAuto(partie));
    }

    // nettoie le nom du royaume pour en faire un nom de fichier valide
    private static String nomFichierSur(String nom) {
        String sur = (nom == null ? "" : nom).replaceAll("[^\\p{L}\\p{N} _-]", "_").trim();
        return sur.isEmpty() ? "sauvegarde" : sur;
    }

    // ----- Slots numerotes -----

    /** Sauvegarde la partie dans le slot indique (1..5). */
    public static void sauvegarder(Partie partie, int slot) throws IOException {
        verifierSlot(slot);
        ecrire(new Sauvegarde(partie), new File(cheminSlot(slot)));
    }

    /** Charge le slot, ou null s'il est vide. */
    public static Sauvegarde charger(int slot) throws IOException {
        verifierSlot(slot);
        File f = new File(cheminSlot(slot));
        return f.exists() ? lire(f) : null;
    }

    // vrai si le slot contient une sauvegarde
    public static boolean slotExiste(int slot) {
        return new File(cheminSlot(slot)).exists();
    }

    public static void supprimer(int slot) {
        File f = new File(cheminSlot(slot));
        if (f.exists()) {
            f.delete();
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

    private static void ecrire(Sauvegarde s, File fichier) throws IOException {
        File parent = fichier.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        Files.write(fichier.toPath(), s.versJson().getBytes(StandardCharsets.UTF_8));
    }

    private static Sauvegarde lire(File fichier) throws IOException {
        String json = new String(Files.readAllBytes(fichier.toPath()), StandardCharsets.UTF_8);
        try {
            return Sauvegarde.depuisJson(json);
        } catch (RuntimeException e) {
            // checksum invalide ou erreur de parsing
            throw new IOException(e.getMessage(), e);
        }
    }
}
