import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import Modele.economie.Ressource;
import Modele.infrastructure.Batiment;
import Modele.infrastructure.Marche;
import Modele.infrastructure.TypeBatiment;
import Modele.population.Role;
import Modele.royaume.Royaume;

import config.Equilibrage;

// Tests des batiments : production, bonus de niveau, chantiers et marche.
public class InfrastructureTest {

    @Test
    public void fermeNiveau1() {
        Royaume r = new Royaume("Test");
        r.reaffecter(Role.INACTIF, Role.FERMIER, 5);
        int avant = r.tresor().quantite(Ressource.NOURRITURE);
        r.batiment(TypeBatiment.FERME).produire(r);
        assertEquals("production de la ferme niveau 1",
                avant + 5 * Equilibrage.PRODUCTION_NOURRITURE_PAR_FERMIER,
                r.tresor().quantite(Ressource.NOURRITURE));
    }

    @Test
    public void fermeBonusNiveau() {
        Royaume r = new Royaume("Test");
        r.reaffecter(Role.INACTIF, Role.FERMIER, 4);
        Batiment ferme = r.batiment(TypeBatiment.FERME);
        ferme.restaurer(3, 0);
        int avant = r.tresor().quantite(Ressource.NOURRITURE);
        ferme.produire(r);
        int base = 4 * Equilibrage.PRODUCTION_NOURRITURE_PAR_FERMIER;
        int attendu = (int) Math.round(base * (1.0 + Equilibrage.BONUS_FERME_PAR_NIVEAU * 2));
        assertEquals("production de la ferme niveau 3", avant + attendu,
                r.tresor().quantite(Ressource.NOURRITURE));
    }

    @Test
    public void mine() {
        Royaume r = new Royaume("Test");
        r.reaffecter(Role.INACTIF, Role.MINEUR, 4);
        int pierreAvant = r.tresor().quantite(Ressource.PIERRE);
        int orAvant = r.tresor().quantite(Ressource.OR);
        r.batiment(TypeBatiment.MINE).produire(r);
        assertEquals("pierre produite", pierreAvant + 4 * Equilibrage.PRODUCTION_PIERRE_PAR_MINEUR,
                r.tresor().quantite(Ressource.PIERRE));
        assertEquals("or produit", orAvant + 4 * Equilibrage.PRODUCTION_OR_PAR_MINEUR,
                r.tresor().quantite(Ressource.OR));
    }

    @Test
    public void endommage() {
        Royaume sain = new Royaume("Sain");
        sain.reaffecter(Role.INACTIF, Role.BUCHERON, 4);
        int boisAvantSain = sain.tresor().quantite(Ressource.BOIS);
        sain.batiment(TypeBatiment.SCIERIE).produire(sain);
        int prodSaine = sain.tresor().quantite(Ressource.BOIS) - boisAvantSain;

        Royaume casse = new Royaume("Casse");
        casse.reaffecter(Role.INACTIF, Role.BUCHERON, 4);
        Batiment scierie = casse.batiment(TypeBatiment.SCIERIE);
        scierie.marquerEndommage(true);
        int boisAvant = casse.tresor().quantite(Ressource.BOIS);
        scierie.produire(casse);
        int prodCassee = casse.tresor().quantite(Ressource.BOIS) - boisAvant;

        assertTrue("la scierie est marquee endommagee", scierie.estEndommage());
        assertTrue("un batiment endommage produit moins (" + prodCassee + " < " + prodSaine + ")",
                prodCassee < prodSaine);
    }

    @Test
    public void habitationsCapacite() {
        Royaume r = new Royaume("Test");
        Batiment hab = r.batiment(TypeBatiment.HABITATIONS);
        hab.restaurer(2, 0);
        hab.produire(r);
        assertEquals("capacite au niveau 2",
                Equilibrage.CAPACITE_LOGEMENT_INITIALE + Equilibrage.CAPACITE_LOGEMENT_PAR_NIVEAU_HABITATIONS,
                r.population().capaciteLogement());
    }

    @Test
    public void chantierApplicationImmediate() {
        Royaume r = new Royaume("Test");
        Batiment hab = r.batiment(TypeBatiment.HABITATIONS);
        hab.demarrerChantier();
        for (int i = 1; i < Equilibrage.DUREE_CHANTIER_AMELIORATION; i++) {
            hab.produire(r);
            assertEquals("niveau inchange pendant le chantier", 1, hab.niveau());
            assertEquals("capacite inchangee pendant le chantier",
                    Equilibrage.CAPACITE_LOGEMENT_INITIALE, r.population().capaciteLogement());
        }
        hab.produire(r);
        assertEquals("niveau augmente a la fin du chantier", 2, hab.niveau());
        assertEquals("capacite appliquee des la fin du chantier",
                Equilibrage.CAPACITE_LOGEMENT_INITIALE + Equilibrage.CAPACITE_LOGEMENT_PAR_NIVEAU_HABITATIONS,
                r.population().capaciteLogement());
    }

    @Test
    public void peutEtreAmeliore() {
        Royaume r = new Royaume("Test");
        Batiment ferme = r.batiment(TypeBatiment.FERME);
        assertTrue("ameliorable au niveau 1", ferme.peutEtreAmeliore());
        ferme.restaurer(Equilibrage.NIVEAU_MAX_BATIMENT, 0);
        assertFalse("non ameliorable au niveau max", ferme.peutEtreAmeliore());
        Batiment mine = r.batiment(TypeBatiment.MINE);
        mine.demarrerChantier();
        assertFalse("non ameliorable pendant un chantier", mine.peutEtreAmeliore());
    }

    @Test
    public void marcheTaux() {
        Royaume r = new Royaume("Test");
        Marche marche = (Marche) r.batiment(TypeBatiment.MARCHE);
        int recuNiv1 = marche.quantiteRecue(9);
        marche.restaurer(Equilibrage.NIVEAU_MAX_BATIMENT, 0);
        int recuNivMax = marche.quantiteRecue(9);
        assertTrue("le marche de haut niveau rend plus (" + recuNivMax + " > " + recuNiv1 + ")",
                recuNivMax > recuNiv1);
    }
}
