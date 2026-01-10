package metiers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class TestTypeVehicule {

    private TypeVehicule typeVehicule;
    private Vehicule vehicule1;
    private Vehicule vehicule2;

    @BeforeEach
    void setUp() {
        typeVehicule = new TypeVehicule(1, "Bus");
        vehicule1 = new Bus(999, "Alstom", "Citadis", LocalDate.now(), LocalDate.now(), LocalDateTime.now());
        vehicule1.setTypevehicule(typeVehicule);

        vehicule2 = new Bus(999, "Alstom", "Citadis", LocalDate.now(), LocalDate.now(), LocalDateTime.now());
        vehicule2.setTypevehicule(typeVehicule);
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
