package metiers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArretTest {

    private Arret arret;
    private Ligne ligne1;
    private Ligne ligne2;

    @BeforeEach
    void setUp() {
        arret = new Arret(1, "Gare_de_Tours", 47.389011100000, 0.692014600000);
        ligne1 = new Ligne(100, "Ligne 2");
        ligne2 = new Ligne(200, "Ligne 4");
    }

    @Test
    void testConstructeurAvecId() {
        assertEquals(1, arret.getIdArret());
        assertEquals("Gare Centrale", arret.getNom());
        assertEquals(48.8566, arret.getLatitude());
        assertEquals(2.3522, arret.getLongitude());
    }

    @Test
    void testSettersEtGetters() {
        arret.setIdArret(2);
        arret.setNom("Nationale");
        arret.setLatitude(48.867);
        arret.setLongitude(2.363);

        assertEquals(2, arret.getIdArret());
        assertEquals("Nationale", arret.getNom());
        assertEquals(48.867, arret.getLatitude());
        assertEquals(2.363, arret.getLongitude());
    }

    @Test
    void testAddBorne() {
        Borne borne = new Borne(1);

        arret.addBorne(borne);

        assertEquals(1, arret.getListeBornes().size());
        assertTrue(arret.getListeBornes().contains(borne));
    }

    @Test
    void testAddLigneDesservie() {
        arret.addLigneDesservie(ligne1);

        assertEquals(1, arret.getListeLignesDesservies().size());
        assertTrue(arret.getListeLignesDesservies().contains(ligne1));
        assertTrue(ligne1.estDesservi(arret));
    }

    @Test
    void testEstDesservi() {
        arret.addLigneDesservie(ligne1);

        assertTrue(arret.estDesservi(ligne1));
        assertFalse(arret.estDesservi(ligne2));
    }

    @Test
    void testConstructeurSansId() {
        Arret nouvelArret = new Arret("Lac", 48.853, 2.369);

        assertEquals("Lac", nouvelArret.getNom());
        assertEquals(48.853, nouvelArret.getLatitude());
        assertEquals(2.369, nouvelArret.getLongitude());
    }
}
