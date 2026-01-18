package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import metiers.*;

class TestTypeLigne {

    private TypeLigne typeLigne;
    private Ligne ligne1;
    private Ligne ligne2;

    @BeforeEach
    void setUp() {
        typeLigne = new TypeLigne(1, "Bus");
        ligne1 = new Ligne(10, "Ligne A", typeLigne);
        ligne2 = new Ligne(20, "Ligne B", typeLigne);
    }

    @Test
    void testConstructeurAvecId() {
        assertEquals(1, typeLigne.getIdTypeLigne());
        assertEquals("Bus", typeLigne.getLibelle());
        assertNotNull(typeLigne.getListeLignes());
        assertTrue(typeLigne.getListeLignes().isEmpty());
    }

    @Test
    void testConstructeurSansId() {
        TypeLigne tl = new TypeLigne("Tram");

        assertEquals("Tram", tl.getLibelle());
        assertNotNull(tl.getListeLignes());
        assertTrue(tl.getListeLignes().isEmpty());
    }

    @Test
    void testSettersEtGetters() {
        typeLigne.setIdTypeLigne(2);
        typeLigne.setLibelle("Métro");

        assertEquals(2, typeLigne.getIdTypeLigne());
        assertEquals("Métro", typeLigne.getLibelle());
    }

    @Test
    void testAddLigne() {
        typeLigne.addLigne(ligne1);
        typeLigne.addLigne(ligne2);

        assertEquals(2, typeLigne.getListeLignes().size());
        assertTrue(typeLigne.getListeLignes().contains(ligne1));
        assertTrue(typeLigne.getListeLignes().contains(ligne2));
    }

    @Test
    void testSetListeLignes() {
        List<Ligne> nouvellesLignes = new ArrayList<>();
        nouvellesLignes.add(ligne1);

        typeLigne.setListeLignes(nouvellesLignes);

        assertEquals(1, typeLigne.getListeLignes().size());
        assertEquals(ligne1, typeLigne.getListeLignes().get(0));
    }
}
