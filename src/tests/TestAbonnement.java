package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import metiers.Abonnement;
import metiers.Client;
import java.util.ArrayList;
import java.util.List;

public class TestAbonnement {

    @Test
    void test() {
        Client client = new Client(1, "Legent", "Valentine", 25);
        List<Client> listeClients = new ArrayList<>();
        listeClients.add(client);
        
        //Check Constructor
        Abonnement abo = new Abonnement(10, "annuel", 200, "2025-2026", listeClients);
        assertNotNull(abo);

        //Check getters
        assertEquals(10, abo.getIdAbonnement());
        assertEquals("annuel", abo.getFormule());
        assertEquals(200, abo.getPrix());
        assertEquals("2025-2026", abo.getDuree());
        assertEquals(1, abo.getListeClients().size());

        //Check setters
        abo.setIdAbonnement(20);
        assertEquals(20, abo.getIdAbonnement());
        abo.setFormule("mensuel");
        assertEquals("mensuel", abo.getFormule());
        abo.setPrix(35.00);
        assertEquals(35.00, abo.getPrix());
        abo.setDuree("2025-2026");
        assertEquals("2025-2026", abo.getDuree());
        List<Client> newListeClients = new ArrayList<>();
        Client client2 = new Client(2, "Doe", "John", 30);
        newListeClients.add(client2);
        abo.setListeClients(newListeClients);
        assertEquals(1, abo.getListeClients().size());
        abo.addClient(client);
        assertEquals(2, abo.getListeClients().size());

        //Check ToString
        assertTrue(abo.toString().contains("35.0"));
    }
}