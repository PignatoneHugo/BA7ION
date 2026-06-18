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

    /**
     * Verifie la production de nourriture d'une ferme de niveau 1.
     */
    @Test
    public void fermeNiveau1() {
        Royaume royaume = new Royaume("Test");
        royaume.reaffecter(Role.INACTIF, Role.FERMIER, 5);
        int avant = royaume.tresor().quantite(Ressource.NOURRITURE);
        royaume.batiment(TypeBatiment.FERME).produire(royaume);
        assertEquals("production de la ferme niveau 1",
                avant + 5 * Equilibrage.PRODUCTION_NOURRITURE_PAR_FERMIER,
                royaume.tresor().quantite(Ressource.NOURRITURE));
    }

    /**
     * Verifie que le niveau d'une ferme augmente sa production.
     */
    @Test
    public void fermeBonusNiveau() {
        Royaume royaume = new Royaume("Test");
        royaume.reaffecter(Role.INACTIF, Role.FERMIER, 4);
        Batiment ferme = royaume.batiment(TypeBatiment.FERME);
        ferme.restaurer(3, 0);
        int avant = royaume.tresor().quantite(Ressource.NOURRITURE);
        ferme.produire(royaume);
        int base = 4 * Equilibrage.PRODUCTION_NOURRITURE_PAR_FERMIER;
        int attendu = (int) Math.round(base * (1.0 + Equilibrage.BONUS_FERME_PAR_NIVEAU * 2));
        assertEquals("production de la ferme niveau 3", avant + attendu,
                royaume.tresor().quantite(Ressource.NOURRITURE));
    }

    /**
     * Verifie que la mine produit de la pierre et de l'or.
     */
    @Test
    public void mine() {
        Royaume royaume = new Royaume("Test");
        royaume.reaffecter(Role.INACTIF, Role.MINEUR, 4);
        int pierreAvant = royaume.tresor().quantite(Ressource.PIERRE);
        int orAvant = royaume.tresor().quantite(Ressource.OR);
        royaume.batiment(TypeBatiment.MINE).produire(royaume);
        assertEquals("pierre produite", pierreAvant + 4 * Equilibrage.PRODUCTION_PIERRE_PAR_MINEUR,
                royaume.tresor().quantite(Ressource.PIERRE));
        assertEquals("or produit", orAvant + 4 * Equilibrage.PRODUCTION_OR_PAR_MINEUR,
                royaume.tresor().quantite(Ressource.OR));
    }

    /**
     * Verifie qu'un batiment endommage produit moins qu'un batiment sain.
     */
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

    /**
     * Verifie la capacite de logement des habitations au niveau 2.
     */
    @Test
    public void habitationsCapacite() {
        Royaume royaume = new Royaume("Test");
        Batiment habitations = royaume.batiment(TypeBatiment.HABITATIONS);
        habitations.restaurer(2, 0);
        habitations.produire(royaume);
        assertEquals("capacite au niveau 2",
                Equilibrage.CAPACITE_LOGEMENT_INITIALE + Equilibrage.CAPACITE_LOGEMENT_PAR_NIVEAU_HABITATIONS,
                royaume.population().capaciteLogement());
    }

    /**
     * Verifie que le niveau et la capacite changent seulement a la fin du chantier.
     */
    @Test
    public void chantierApplicationImmediate() {
        Royaume royaume = new Royaume("Test");
        Batiment habitations = royaume.batiment(TypeBatiment.HABITATIONS);
        habitations.demarrerChantier();
        for (int indice = 1; indice < Equilibrage.DUREE_CHANTIER_AMELIORATION; indice++) {
            habitations.produire(royaume);
            assertEquals("niveau inchange pendant le chantier", 1, habitations.niveau());
            assertEquals("capacite inchangee pendant le chantier",
                    Equilibrage.CAPACITE_LOGEMENT_INITIALE, royaume.population().capaciteLogement());
        }
        habitations.produire(royaume);
        assertEquals("niveau augmente a la fin du chantier", 2, habitations.niveau());
        assertEquals("capacite appliquee des la fin du chantier",
                Equilibrage.CAPACITE_LOGEMENT_INITIALE + Equilibrage.CAPACITE_LOGEMENT_PAR_NIVEAU_HABITATIONS,
                royaume.population().capaciteLogement());
    }

    /**
     * Verifie qu'un batiment au niveau max ou en chantier n'est pas ameliorable.
     */
    @Test
    public void peutEtreAmeliore() {
        Royaume royaume = new Royaume("Test");
        Batiment ferme = royaume.batiment(TypeBatiment.FERME);
        assertTrue("ameliorable au niveau 1", ferme.peutEtreAmeliore());
        ferme.restaurer(Equilibrage.NIVEAU_MAX_BATIMENT, 0);
        assertFalse("non ameliorable au niveau max", ferme.peutEtreAmeliore());
        Batiment mine = royaume.batiment(TypeBatiment.MINE);
        mine.demarrerChantier();
        assertFalse("non ameliorable pendant un chantier", mine.peutEtreAmeliore());
    }

    /**
     * Verifie qu'un marche de haut niveau rend plus lors d'un echange.
     */
    @Test
    public void marcheTaux() {
        Royaume royaume = new Royaume("Test");
        Marche marche = (Marche) royaume.batiment(TypeBatiment.MARCHE);
        int recuNiv1 = marche.quantiteRecue(9);
        marche.restaurer(Equilibrage.NIVEAU_MAX_BATIMENT, 0);
        int recuNivMax = marche.quantiteRecue(9);
        assertTrue("le marche de haut niveau rend plus (" + recuNivMax + " > " + recuNiv1 + ")",
                recuNivMax > recuNiv1);
    }
}
