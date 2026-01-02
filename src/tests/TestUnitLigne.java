package metiers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class LigneTest {

    private Ligne ligne;
    private TypeLigne typeLigne;
    private Arret arret1;
    private Arret arret2;

    @BeforeEach
    void setUp() {
        typeLigne = new TypeLigne("Bus");
        arret1 = new Arret(1, "St_Pierre_Gare", 48.85, 2.35);
        arret2 = new Arret(2, "Charcot", 48.86, 2.36);

        ligne = new Ligne("Ligne 6", typeLigne, 10);
    }

    @Test
    void testConstructeurSimple() {
        assertEquals("Ligne 1", ligne.getLibelle());
        assertEquals(10, ligne.getIdLigne());
        assertEquals(typeLigne, ligne.getTypeLigne());
    }

    @Test
    void testSetLibelle() {
        ligne.setLibelle("Nouvelle Ligne");

        assertEquals("Nouvelle Ligne", ligne.getLibelle());
    }

    @Test
    void testSetTypeLigne() {
        TypeLigne nouveauType = new TypeLigne("Tram");

        ligne.setTypeLigne(nouveauType);

        assertEquals(nouveauType, ligne.getTypeLigne());
        assertTrue(nouveauType.getLignes().contains(ligne));
    }

    @Test
    void testSetArretDepart() {
        ligne.setArretDepart(arret1);

        assertEquals(arret1, ligne.getArretDepart());
        assertTrue(ligne.getArretsDesservis().contains(arret1));
        assertTrue(arret1.estDesservi(ligne));
    }

    @Test
    void testSetArretArrive() {
        ligne.setArretArrive(arret2);

        assertEquals(arret2, ligne.getArretArrive());
        assertTrue(ligne.getArretsDesservis().contains(arret2));
        assertTrue(arret2.estDesservi(ligne));
    }

    @Test
    void testAddArretDesservi() {
        ligne.addArretDesservi(arret1);

        assertEquals(6, ligne.getArretsDesservis().size());
        assertTrue(ligne.getArretsDesservis().contains(arret1));
        assertTrue(arret1.estDesservi(ligne));
    }

    @Test
    void testEstDesservi() {
        ligne.addArretDesservi(arret1);

        assertTrue(ligne.estDesservi(arret1));
        assertFalse(ligne.estDesservi(arret2));
    }

    @Test
    void testConstructeurComplet() {
        List<Arret> arrets = new ArrayList<>();
        arrets.add(arret1);
        arrets.add(arret2);

        Ligne ligneComplete = new Ligne(
                20, "Ligne 2", typeLigne, arret1, arret2, 45, arrets);

        assertEquals(20, ligneComplete.getIdLigne());
        assertEquals("Ligne 2", ligneComplete.getLibelle());
        assertEquals(45, ligneComplete.getDuree());
        assertEquals(2, ligneComplete.getArretsDesservis().size());
    }
}
