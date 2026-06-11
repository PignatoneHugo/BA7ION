package Modele.persistance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Modele.partie.Partie;

/**
 * Gestionnaire de sauvegardes : ecrit et lit des fichiers .sav contenant
 * un objet Sauvegarde serialise (binaire Java natif).
 *
 * Sprint 3 : 5 slots numerotes (1 a 5) + 1 slot "autosave".
 * Les fichiers sont stockes dans le dossier "saves/" a la racine du projet.
 */
public final class GestionnaireSauvegardes {

    private static final String DOSSIER = "saves";
    private static final int NB_SLOTS = 5;

    private GestionnaireSauvegardes() {
        // Classe utilitaire.
    }

    /** Sauvegarde la partie dans le slot indique (1..5). */
    public static void sauvegarder(Partie partie, int slot) throws IOException {
        if (slot < 1 || slot > NB_SLOTS) {
            throw new IllegalArgumentException("Slot doit etre entre 1 et " + NB_SLOTS);
        }
        ecrire(new Sauvegarde(partie), cheminSlot(slot));
    }

    /** Sauvegarde automatique dans un slot dedie (typiquement appelee en fin de tour). */
    public static void autosauvegarder(Partie partie) throws IOException {
        ecrire(new Sauvegarde(partie), cheminAutosave());
    }

    /** Charge la sauvegarde du slot indique, ou null si le slot est vide. */
    public static Sauvegarde charger(int slot) throws IOException {
        if (slot < 1 || slot > NB_SLOTS) {
            throw new IllegalArgumentException("Slot doit etre entre 1 et " + NB_SLOTS);
        }
        return lire(cheminSlot(slot));
    }

    /** Charge l'autosave, ou null si elle n'existe pas. */
    public static Sauvegarde chargerAutosave() throws IOException {
        return lire(cheminAutosave());
    }

    /** True si le slot contient une sauvegarde. */
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

    private static String cheminSlot(int slot) {
        return DOSSIER + "/slot_" + slot + ".sav";
    }

    private static String cheminAutosave() {
        return DOSSIER + "/autosave.sav";
    }

    private static void ecrire(Sauvegarde s, String chemin) throws IOException {
        File f = new File(chemin);
        f.getParentFile().mkdirs();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f))) {
            out.writeObject(s);
        }
    }

    private static Sauvegarde lire(String chemin) throws IOException {
        File f = new File(chemin);
        if (!f.exists()) {
            return null;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            return (Sauvegarde) in.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Format de sauvegarde non reconnu", e);
        }
    }
}
