package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import metiers.Bus;

public class TestBus {
    
    @Test
    void test() {
        Bus bus = new Bus(2, "Mercedes", "Citaro", LocalDate.of(2017, 4, 10), LocalDate.of(2018, 2, 5), LocalDateTime.of(2024, 5, 1, 9, 0));
        assertNotNull(bus, "Bus object should not be null after instantiation");

        // Check attributes
        assertEquals(2, bus.getNumVehicule(), "Bus numVehicule should be 2");
        assertEquals("Mercedes", bus.getMarque(), "Bus marque should be 'Mercedes'");
        assertEquals("Citaro", bus.getModele(), "Bus modele should be 'Citaro'");
        assertEquals(LocalDate.of(2017, 4, 10), bus.getDateFabrication(), "Bus dateFabrication should be 2017-04-10");
        assertEquals(LocalDate.of(2018, 2, 5), bus.getDateMiseEnService(), "Bus dateMiseEnService should be 2018-02-05");
        assertEquals(LocalDateTime.of(2024, 5, 1, 9, 0), bus.getDateHeureDerniereMaintenance(), "Bus dateHeureDerniereMaintenance should be 2024-05-01T09:00");  
        
        // Check Setters
        bus.setMarque("Volvo");
        assertEquals("Volvo", bus.getMarque(), "Bus marque should be updated to 'Volvo'");
        bus.setModele("7900 Electric");
        assertEquals("7900 Electric", bus.getModele(), "Bus modele should be updated to '7900 Electric'");
        bus.setDateFabrication(LocalDate.of(2016, 3, 15));
        assertEquals(LocalDate.of(2016, 3, 15), bus.getDateFabrication(), "Bus dateFabrication should be updated to 2016-03-15");
        bus.setDateMiseEnService(LocalDate.of(2017, 1, 20));
        assertEquals(LocalDate.of(2017, 1, 20), bus.getDateMiseEnService(), "Bus dateMiseEnService should be updated to 2017-01-20");
        LocalDateTime newMaintenanceDate = LocalDateTime.of(2025, 6, 1, 11, 30);
        bus.setDateHeureDerniereMaintenance(newMaintenanceDate);   
        assertEquals(newMaintenanceDate, bus.getDateHeureDerniereMaintenance(), "Bus dateHeureDerniereMaintenance should be updated to 2025-06-01T11:30");  
        
        // Check ToString
        String busString = bus.toString();
        assertNotNull(busString, "Bus toString() should not return null");
        assertTrue(busString.contains("2"), "Bus toString() should contain numVehicule");
        assertTrue(busString.contains("Volvo"), "Bus toString() should contain updated marque");
        assertTrue(busString.contains("7900 Electric"), "Bus toString() should contain updated modele");
        assertTrue(busString.contains("2016-03-15"), "Bus toString() should contain updated dateFabrication");
        assertTrue(busString.contains("2017-01-20"), "Bus toString() should contain updated dateMiseEnService");
        assertTrue(busString.contains("2025-06-01T11:30"), "Bus toString() should contain updated dateHeureDerniereMaintenance");
    }    
}
