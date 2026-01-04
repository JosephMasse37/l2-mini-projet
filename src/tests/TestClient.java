package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import metiers.Abonnement;
import metiers.Client;

public class TestClient {

    @Test
    void test() {
        //Check Constructor
        Client client = new Client(1, "Legent", "Valentine", 25);
        assertNotNull(client, "L'objet Client ne devrait pas être nul après instanciation");

        //Check Getters
        assertEquals(1, client.getIdClient(), "L'idClient devrait être 1");
        assertEquals("Legent", client.getNom(), "Le nom devrait être 'Legent'");
        assertEquals("Valentine", client.getPrenom(), "Le prénom devrait être 'Valentine'");
        assertEquals(25, client.getAge(), "L'âge devrait être 25");
        assertNull(client.getUnAbonnement(), "L'abonnement devrait être nul initialement");

        //Check Setters
        client.setNom("Martin");
        assertEquals("Martin", client.getNom(), "Le nom devrait être mis à jour à 'Martin'");
        client.setPrenom("Jean");
        assertEquals("Jean", client.getPrenom(), "Le prénom devrait être mis à jour à 'Jean'");
        client.setAge(30);
        assertEquals(30, client.getAge(), "L'âge devrait être mis à jour à 30");

        Abonnement abo = new Abonnement(10, "annuel", 200, "2025-2026");
        client.setUnAbonnement(abo);
        assertEquals(abo, client.getUnAbonnement(), "L'abonnement devrait être mis à jour");

        //Check ToString
        String clientString = client.toString();
        assertNotNull(clientString);
        assertTrue(clientString.contains("Martin"), "toString devrait contenir le nouveau nom");
    }
}