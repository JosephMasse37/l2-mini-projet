package tests;

import metiers.Utilisateur;
import metiers.Chauffeur;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestUtilisateur {

    @Test
    void tests() {
        // Instanciation
        Utilisateur user = new Utilisateur(
                "test.test",
                "14d5ba5b0f9a01c847a64270d50bf52d71ebf073a22643a855c55c1c9e613688",
                "Jean",
                "Dupont"
        );

        // Vérification objet
        assertNotNull(user, "Utilisateur object should not be null after instantiation");

        // Vérification des attributs
        assertEquals("test.test", user.getUsername(), "Username should be 'test.test'");
        assertEquals(
                "14d5ba5b0f9a01c847a64270d50bf52d71ebf073a22643a855c55c1c9e613688",
                user.getPassword(),
                "Password hash should match"
        );
        assertEquals("Jean", user.getPrenom(), "Prenom should be 'Jean'");
        assertEquals("Dupont", user.getNom(), "Nom should be 'Dupont'");
        assertNull(user.getChauffeur(), "Chauffeur should be null by default");

        // Test des setters
        user.setUsername("john.doe");
        assertEquals("john.doe", user.getUsername(), "Username should be updated");

        user.setPassword("newPasswordHash");
        assertEquals("newPasswordHash", user.getPassword(), "Password should be updated");

        user.setPrenom("John");
        assertEquals("John", user.getPrenom(), "Prenom should be updated");

        user.setNom("Doe");
        assertEquals("Doe", user.getNom(), "Nom should be updated");

        // Test relation avec Chauffeur
        Chauffeur chauffeur = new Chauffeur(32, true, user);
        user.setChauffeur(chauffeur);

        assertNotNull(user.getChauffeur(), "Chauffeur should not be null after setting");
        assertEquals(chauffeur, user.getChauffeur(), "Chauffeur should be correctly assigned");
        assertEquals(user, chauffeur.getUtilisateur(), "Bidirectional relation should be maintained");

        // Test toString
        String userString = user.toString();
        assertNotNull(userString, "toString() should not return null");
        assertTrue(userString.contains("john.doe"), "toString() should contain username");
        assertTrue(userString.contains("John"), "toString() should contain prenom");
        assertTrue(userString.contains("Doe"), "toString() should contain nom");
    }
}
