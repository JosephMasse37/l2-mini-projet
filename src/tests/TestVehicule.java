package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import metiers.Tram;
import metiers.TypeVehicule;
import metiers.Vehicule;

public class TestVehicule {
    
    //Test all basic actions at the same time (constructor, getters, setters)

    @Test
    void test() {
        //Create a sample Vehicule
        Vehicule vehicule = createSampleVehicule();
        assertNotNull(vehicule, "Vehicule object should not be null after instantiation");

        //Check attributes
        assertEquals(999, vehicule.getNumVehicule(), "Vehicule numVehicule should be 999");
        assertEquals("Alstom", vehicule.getMarque(), "Vehicule marque should be 'Alstom'");
        assertEquals("Citadis", vehicule.getModele(), "Vehicule modele should be 'Citadis'");
        assertEquals(LocalDate.now(), vehicule.getDateFabrication(), "Vehicule dateFabrication should be today");
        assertEquals(LocalDate.now(), vehicule.getDateMiseEnService(), "Vehicule dateMiseEnService should be today");
        assertEquals(LocalDateTime.now().getHour(), vehicule.getDateHeureDerniereMaintenance().getHour(), "Vehicule dateHeureDerniereMaintenance hour should be current hour");

        //Check TypeVehicule
        assertNotNull(vehicule.getTypevehicule(), "Vehicule TypeVehicule should not be null");
        assertEquals(1, vehicule.getTypevehicule().getIdTypeVehicule(), "Vehicule TypeVehicule ID should be 1");
        assertEquals("Tram", vehicule.getTypevehicule().getLibelle(), "Vehicule TypeVehicule libelle should be 'Tram'");

        //Check Setters
        vehicule.setMarque("NewMarque");   
        assertEquals("NewMarque", vehicule.getMarque(), "Vehicule marque should be updated to 'NewMarque'");
        vehicule.setModele("NewModele");
        assertEquals("NewModele", vehicule.getModele(), "Vehicule modele should be updated to 'NewModele'");
        vehicule.setDateFabrication(LocalDate.of(2020, 1, 1));
        assertEquals(LocalDate.of(2020, 1, 1), vehicule.getDateFabrication(), "Vehicule dateFabrication should be updated to 2020-01-01");
        vehicule.setDateMiseEnService(LocalDate.of(2021, 1, 1));
        assertEquals(LocalDate.of(2021, 1, 1), vehicule.getDateMiseEnService(), "Vehicule dateMiseEnService should be updated to 2021-01-01");
        LocalDateTime newMaintenanceDate = LocalDateTime.of(2023, 1, 1, 10, 0);
        vehicule.setDateHeureDerniereMaintenance(newMaintenanceDate);
        assertEquals(newMaintenanceDate, vehicule.getDateHeureDerniereMaintenance(), "Vehicule dateHeureDerniereMaintenance should be updated to 2023-01-01T10:00");

        //Check ToString
        String vehiculeString = vehicule.toString();
        assertNotNull(vehiculeString, "Vehicule toString() should not return null");
        assertTrue(vehiculeString.contains("999"), "Vehicule toString() should contain numVehicule");
        assertTrue(vehiculeString.contains("NewMarque"), "Vehicule toString() should contain updated marque");
        assertTrue(vehiculeString.contains("NewModele"), "Vehicule toString() should contain updated modele");
        assertTrue(vehiculeString.contains("2020-01-01"), "Vehicule toString() should contain updated dateFabrication");
        assertTrue(vehiculeString.contains("2021-01-01"), "Vehicule toString() should contain updated dateMiseEnService");
        assertTrue(vehiculeString.contains("2023-01-01T10:00"), "Vehicule toString() should contain updated dateHeureDerniereMaintenance");
    }

    private Vehicule createSampleVehicule() {
        //Use of ID 1 for TypeVehicule (Tram) 
        TypeVehicule type = new TypeVehicule(1, "Tram"); 
        //Use of ID 999 to ensure the ID is not already in the DB
        Tram t = new Tram(999, "Alstom", "Citadis", LocalDate.now(), LocalDate.now(), LocalDateTime.now());
        t.setTypevehicule(type);
        return t;
    }
}
