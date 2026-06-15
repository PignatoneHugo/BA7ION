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
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(contenu.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                sb.append(Character.forDigit((b >> 4) & 0xF, 16));
                sb.append(Character.forDigit(b & 0xF, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algorithme SHA-256 indisponible.", e);
        }
    }
}
