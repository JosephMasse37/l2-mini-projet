package metiers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class TypeVehiculeTest {

    private TypeVehicule typeVehicule;
    private Vehicule vehicule1;
    private Vehicule vehicule2;

    @BeforeEach
    void setUp() {
        typeVehicule = new TypeVehicule(1, "Bus");
        vehicule1 = new Vehicule("78");
        vehicule2 = new Vehicule("79");
    }

    @Test
    void testConstructeurAvecIdEtLibelle() {
        assertEquals(1, typeVehicule.getIdTypeVehicule());
        assertEquals("Bus", typeVehicule.getLibelle());
        assertNotNull(typeVehicule.getListeVehicules());
        assertTrue(typeVehicule.getListeVehicules().isEmpty());
    }

    @Test
    void testConstructeurAvecListeVehicules() {
        List<Vehicule> vehicules = new ArrayList<>();
        vehicules.add(vehicule1);

        TypeVehicule tv = new TypeVehicule("Tram", vehicules);

        assertEquals("Tram", tv.getLibelle());
        assertEquals(1, tv.getListeVehicules().size());
        assertEquals(vehicule1, tv.getListeVehicules().get(0));
    }

    @Test
    void testSettersEtGetters() {
        typeVehicule.setIdTypeVehicule(2);
        typeVehicule.setLibelle("Métro");

        assertEquals(2, typeVehicule.getIdTypeVehicule());
        assertEquals("Métro", typeVehicule.getLibelle());
    }

    @Test
    void testAddVehicule() {
        typeVehicule.addVehicule(vehicule1);

        assertEquals(1, typeVehicule.getListeVehicules().size());
        assertTrue(typeVehicule.getListeVehicules().contains(vehicule1));
        assertEquals(typeVehicule, vehicule1.getTypevehicule());
    }

    @Test
    void testAddVehiculeSansDoublon() {
        typeVehicule.addVehicule(vehicule1);
        typeVehicule.addVehicule(vehicule1); // ajout doublon

        assertEquals(1, typeVehicule.getListeVehicules().size());
    }

    @Test
    void testSetListeVehicules() {
        List<Vehicule> vehicules = new ArrayList<>();
        vehicules.add(vehicule2);

        typeVehicule.setListeVehicules(vehicules);

        assertEquals(1, typeVehicule.getListeVehicules().size());
        assertEquals(vehicule2, typeVehicule.getListeVehicules().get(0));
    }
}
