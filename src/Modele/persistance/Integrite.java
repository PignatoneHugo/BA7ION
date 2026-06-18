package Modele.persistance;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** Checksum SHA-256 d'une sauvegarde, pour detecter les fichiers modifies. */
final class Integrite {

    private static final String PREFIXE = "sha256:";

    private Integrite() {
        // Classe utilitaire.
    }

    /** Checksum prefixe "sha256:..." du JSON donne. */
    static String checksum(String jsonDonnees) {
        return PREFIXE + sha256(jsonDonnees);
    }

    private static String sha256(String contenu) {
        try {
            MessageDigest digesteur = MessageDigest.getInstance("SHA-256");
            byte[] hash = digesteur.digest(contenu.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexa = new StringBuilder(hash.length * 2);
            for (byte octet : hash) {
                hexa.append(Character.forDigit((octet >> 4) & 0xF, 16));
                hexa.append(Character.forDigit(octet & 0xF, 16));
            }
            return hexa.toString();
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("Algorithme SHA-256 indisponible.", exception);
        }
    }
}
