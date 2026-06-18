import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import Modele.economie.Ressource;
import Modele.militaire.PostureCombat;
import Modele.militaire.TypeUnite;
import Modele.partie.Partie;
import Modele.partie.PartieBuilder;
import Modele.persistance.Sauvegarde;

// Tests de la persistance JSON : aller-retour, checksum et ancien format.
public class PersistanceTest {

    private static Partie partieModifiee() {
        Partie partie = new PartieBuilder().nomJoueur("Royaume").nombreBots(2).graineAleatoire(7).build();
        partie.tour().definirNumero(8);
        partie.joueur().tresor().definirQuantite(Ressource.OR, 1234);
        partie.joueur().armee().recruter(TypeUnite.INFANTERIE_LEGERE, 5);
        partie.joueur().armee().definirPosture(PostureCombat.ATTAQUE);
        partie.bots().get(0).tresor().definirQuantite(Ressource.PIERRE, 321);
        return partie;
    }

    /**
     * Verifie qu'une partie sauvee puis rechargee garde son etat.
     */
    @Test
    public void allerRetour() {
        String json = new Sauvegarde(partieModifiee()).versJson();
        Partie partieRechargee = PartieBuilder.depuisSauvegarde(Sauvegarde.depuisJson(json));

        assertEquals("numero de tour restaure", 8, partieRechargee.numeroTour());
        assertEquals("or du joueur", 1234, partieRechargee.joueur().tresor().quantite(Ressource.OR));
        assertEquals("armee du joueur restauree", 5,
                partieRechargee.joueur().armee().effectifParType(TypeUnite.INFANTERIE_LEGERE));
        assertEquals("posture restauree", PostureCombat.ATTAQUE, partieRechargee.joueur().armee().posture());
        assertEquals("les deux bots sont restaures", 2, partieRechargee.bots().size());
        assertEquals("ressource d'un bot restauree", 321,
                partieRechargee.bots().get(0).tresor().quantite(Ressource.PIERRE));
        assertTrue("le bot retrouve son IA", partieRechargee.bots().get(0).estBot());
    }

    /**
     * Verifie que le JSON contient un checksum et un bloc de donnees.
     */
    @Test
    public void checksumPresent() {
        String json = new Sauvegarde(partieModifiee()).versJson();
        assertTrue("le JSON embarque un checksum SHA-256",
                json.contains("\"checksum\": \"sha256:"));
        assertTrue("le bloc de donnees est present", json.contains("\"donnees\""));
    }

    /**
     * Verifie qu'une sauvegarde falsifiee est detectee et refusee.
     */
    @Test
    public void falsificationDetectee() {
        String json = new Sauvegarde(partieModifiee()).versJson();
        String falsifie = json.replace("1234", "5678");
        assertFalse("le contenu a bien ete modifie", falsifie.equals(json));
        assertThrows("une sauvegarde falsifiee doit etre refusee", IllegalArgumentException.class,
                () -> Sauvegarde.depuisJson(falsifie));
    }

    /**
     * Verifie que l'ancien format de sauvegarde reste lisible.
     */
    @Test
    public void retroCompat() {
        String legacy = "{ \"numeroTour\": 3, \"joueur\": { \"nom\": \"Vieux\", "
                + "\"ressources\": { \"OR\": 50 } }, \"bots\": [] }";
        Partie partieRechargee = PartieBuilder.depuisSauvegarde(Sauvegarde.depuisJson(legacy));
        assertEquals("tour lu depuis l'ancien format", 3, partieRechargee.numeroTour());
        assertEquals("nom lu depuis l'ancien format", "Vieux", partieRechargee.joueur().nom());
        assertEquals("or lu depuis l'ancien format", 50, partieRechargee.joueur().tresor().quantite(Ressource.OR));
    }
}
