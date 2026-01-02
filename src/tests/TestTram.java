package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import metiers.Tram;

public class TestTram {
    
    @Test
    void test() {
        Tram tram = new Tram(1, "Siemens", "Avenio", LocalDate.of(2018, 5, 20), LocalDate.of(2019, 3, 15), LocalDateTime.of(2024, 6, 1, 10, 0));
        assertNotNull(tram, "Tram object should not be null after instantiation");

        // Check attributes
        assertEquals(1, tram.getNumVehicule(), "Tram numVehicule should be 1");
        assertEquals("Siemens", tram.getMarque(), "Tram marque should be 'Siemens'");
        assertEquals("Avenio", tram.getModele(), "Tram modele should be 'Avenio'");
        assertEquals(LocalDate.of(2018, 5, 20), tram.getDateFabrication(), "Tram dateFabrication should be 2018-05-20");
        assertEquals(LocalDate.of(2019, 3, 15), tram.getDateMiseEnService(), "Tram dateMiseEnService should be 2019-03-15");
        assertEquals(LocalDateTime.of(2024, 6, 1, 10, 0), tram.getDateHeureDerniereMaintenance(), "Tram dateHeureDerniereMaintenance should be 2024-06-01T10:00");  
        
        // Check Setters
        tram.setMarque("Bombardier");
        assertEquals("Bombardier", tram.getMarque(), "Tram marque should be updated to 'Bombardier'");
        tram.setModele("Flexity");
        assertEquals("Flexity", tram.getModele(), "Tram modele should be updated to 'Flexity'");
        tram.setDateFabrication(LocalDate.of(2017, 4, 10));
        assertEquals(LocalDate.of(2017, 4, 10), tram.getDateFabrication(), "Tram dateFabrication should be updated to 2017-04-10");
        tram.setDateMiseEnService(LocalDate.of(2018, 2, 5));
        assertEquals(LocalDate.of(2018, 2, 5), tram.getDateMiseEnService(), "Tram dateMiseEnService should be updated to 2018-02-05");
        LocalDateTime newMaintenanceDate = LocalDateTime.of(2025, 7, 1, 14, 30);
        tram.setDateHeureDerniereMaintenance(newMaintenanceDate);
        assertEquals(newMaintenanceDate, tram.getDateHeureDerniereMaintenance(), "Tram dateHeureDerniereMaintenance should be updated to 2025-07-01T14:30");    

        // Check ToString
        String tramString = tram.toString();
        assertNotNull(tramString, "Tram toString() should not return null");
        assertTrue(tramString.contains("1"), "Tram toString() should contain numVehicule");
        assertTrue(tramString.contains("Bombardier"), "Tram toString() should contain updated marque");
        assertTrue(tramString.contains("Flexity"), "Tram toString() should contain updated modele");
        assertTrue(tramString.contains("2017-04-10"), "Tram toString() should contain updated dateFabrication");
        assertTrue(tramString.contains("2018-02-05"), "Tram toString() should contain updated dateMiseEnService");
        assertTrue(tramString.contains("2025-07-01T14:30"), "Tram toString() should contain updated dateHeureDerniereMaintenance");
        
    }
}
