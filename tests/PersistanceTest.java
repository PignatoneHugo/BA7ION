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
        Partie p = new PartieBuilder().nomJoueur("Royaume").nombreBots(2).graineAleatoire(7).build();
        p.tour().definirNumero(8);
        p.joueur().tresor().definirQuantite(Ressource.OR, 1234);
        p.joueur().armee().recruter(TypeUnite.INFANTERIE_LEGERE, 5);
        p.joueur().armee().definirPosture(PostureCombat.ATTAQUE);
        p.bots().get(0).tresor().definirQuantite(Ressource.PIERRE, 321);
        return p;
    }

    @Test
    public void allerRetour() {
        String json = new Sauvegarde(partieModifiee()).versJson();
        Partie r = PartieBuilder.depuisSauvegarde(Sauvegarde.depuisJson(json));

        assertEquals("numero de tour restaure", 8, r.numeroTour());
        assertEquals("or du joueur", 1234, r.joueur().tresor().quantite(Ressource.OR));
        assertEquals("armee du joueur restauree", 5,
                r.joueur().armee().effectifParType(TypeUnite.INFANTERIE_LEGERE));
        assertEquals("posture restauree", PostureCombat.ATTAQUE, r.joueur().armee().posture());
        assertEquals("les deux bots sont restaures", 2, r.bots().size());
        assertEquals("ressource d'un bot restauree", 321,
                r.bots().get(0).tresor().quantite(Ressource.PIERRE));
        assertTrue("le bot retrouve son IA", r.bots().get(0).estBot());
    }

    @Test
    public void checksumPresent() {
        String json = new Sauvegarde(partieModifiee()).versJson();
        assertTrue("le JSON embarque un checksum SHA-256",
                json.contains("\"checksum\": \"sha256:"));
        assertTrue("le bloc de donnees est present", json.contains("\"donnees\""));
    }

    @Test
    public void falsificationDetectee() {
        String json = new Sauvegarde(partieModifiee()).versJson();
        String falsifie = json.replace("1234", "5678");
        assertFalse("le contenu a bien ete modifie", falsifie.equals(json));
        assertThrows("une sauvegarde falsifiee doit etre refusee", IllegalArgumentException.class,
                () -> Sauvegarde.depuisJson(falsifie));
    }

    @Test
    public void retroCompat() {
        String legacy = "{ \"numeroTour\": 3, \"joueur\": { \"nom\": \"Vieux\", "
                + "\"ressources\": { \"OR\": 50 } }, \"bots\": [] }";
        Partie r = PartieBuilder.depuisSauvegarde(Sauvegarde.depuisJson(legacy));
        assertEquals("tour lu depuis l'ancien format", 3, r.numeroTour());
        assertEquals("nom lu depuis l'ancien format", "Vieux", r.joueur().nom());
        assertEquals("or lu depuis l'ancien format", 50, r.joueur().tresor().quantite(Ressource.OR));
    }
}
