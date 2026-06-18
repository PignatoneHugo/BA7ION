package Modele.persistance;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Modele.partie.Partie;
import Modele.royaume.Royaume;

/**
 * Etat complet d'une partie : le joueur et les bots. Serialise en JSON avec
 * Gson, dans une enveloppe { version, checksum, donnees }.
 */
public class Sauvegarde {

    /** Version du format de sauvegarde. */
    public static final int VERSION = 3;

    // GSON lisible pour le fichier, GSON_COMPACT pour le checksum
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting().disableHtmlEscaping().create();
    private static final Gson GSON_COMPACT = new GsonBuilder()
            .disableHtmlEscaping().create();

    public final int numeroTour;
    public final long graineAleatoire;
    public final boolean grenouilleEmpoisonneeDeclenchee;
    public final EtatRoyaume joueur;
    public final List<EtatRoyaume> bots;

    /**
     * Capture l'etat courant d'une partie.
     *
     * @param partie la partie a sauvegarder
     */
    public Sauvegarde(Partie partie) {
        this.numeroTour = partie.numeroTour();
        this.graineAleatoire = partie.aleatoire().nextLong();
        this.grenouilleEmpoisonneeDeclenchee = partie.grenouilleEmpoisonneeDeclenchee();
        this.joueur = new EtatRoyaume(partie.joueur());
        this.bots = new ArrayList<>();
        for (Royaume bot : partie.bots()) {
            this.bots.add(new EtatRoyaume(bot));
        }
    }

    // ----- Ecriture JSON -----

    /**
     * Serialise la sauvegarde en JSON avec l'enveloppe et le checksum.
     *
     * @return le JSON de la sauvegarde
     */
    public String versJson() {
        String corps = GSON_COMPACT.toJson(this);
        Enveloppe enveloppe = new Enveloppe(VERSION, Integrite.checksum(corps), this);
        return GSON.toJson(enveloppe);
    }

    // ----- Relecture JSON -----

    /**
     * Reconstruit une sauvegarde depuis son JSON et verifie le checksum.
     *
     * @param json le texte JSON a relire
     * @return la sauvegarde reconstruite
     * @throws IllegalArgumentException si le JSON est invalide ou le checksum faux
     */
    public static Sauvegarde depuisJson(String json) {
        JsonElement racineEl = JsonParser.parseString(json);
        if (racineEl == null || !racineEl.isJsonObject()) {
            throw new IllegalArgumentException("Racine JSON : objet attendu.");
        }
        JsonObject racine = racineEl.getAsJsonObject();

        Sauvegarde donnees;
        JsonElement donneesEl = racine.get("donnees");
        if (donneesEl != null && donneesEl.isJsonObject()) {
            // avec enveloppe : on lit le bloc puis on verifie
            donnees = GSON.fromJson(donneesEl, Sauvegarde.class);
            JsonElement checksum = racine.get("checksum");
            int version = (racine.has("version") && racine.get("version").isJsonPrimitive())
                    ? racine.get("version").getAsInt() : 0;
            if (checksum != null && checksum.isJsonPrimitive() && version >= VERSION) {
                String calcule = Integrite.checksum(GSON_COMPACT.toJson(donnees));
                if (!checksum.getAsString().equals(calcule)) {
                    throw new IllegalArgumentException(
                            "Integrite compromise : la sauvegarde a ete modifiee ou corrompue.");
                }
            }
        } else {
            // ancien format : donnees directement a la racine
            donnees = GSON.fromJson(racine, Sauvegarde.class);
        }

        if (donnees == null || donnees.joueur == null) {
            throw new IllegalArgumentException("Format de sauvegarde non reconnu.");
        }
        return donnees;
    }

    /** Enveloppe : metadonnees + donnees. */
    private static final class Enveloppe {
        final int version;
        final String checksum;
        final Sauvegarde donnees;

        Enveloppe(int version, String checksum, Sauvegarde donnees) {
            this.version = version;
            this.checksum = checksum;
            this.donnees = donnees;
        }
    }
}
