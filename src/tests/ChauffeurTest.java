package tests;

import metiers.Arret;
import metiers.Borne;
import metiers.Chauffeur;
import metiers.Utilisateur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChauffeurTest {

    private Chauffeur chauffeur1; //ici pr que ts les testes puissent use it via setup
    private Utilisateur user1;


    @BeforeEach
    void setUp() {
        user1 = new Utilisateur("meline.asefatulu","123","meline","asefa");
        chauffeur1 = new Chauffeur(1,true,user1);
    }

    @Test
    void testConstructeurAvecID() {
        assertEquals(1, chauffeur1.getIdChauffeur(), "L'ID doit être 1");
        assertTrue(chauffeur1.isFormation_tram(), "La formation tram doit être à true");
        assertEquals(user1, chauffeur1.getUtilisateur(), "Le chauffeur doit être lié à user1");
        // Vérification de la bidirectionnalité (si ton code le gère)
        assertEquals(chauffeur1, user1.getChauffeur(), "L'utilisateur doit pointer vers son chauffeur");
    }

    @Test
    void testConstructeurSansID() {

        Utilisateur user2 = new Utilisateur("valentine.azerty","123","valentine","azerty");
        Chauffeur chauffeur2 = new Chauffeur(false,user2);

        assertFalse(chauffeur2.isFormation_tram(), "La formation tram doit être à false");
        assertEquals(user2, chauffeur2.getUtilisateur(), "Le chauffeur doit être lié à user2");
        assertEquals(chauffeur2, user2.getChauffeur(), "L'utilisateur doit pointer vers son chauffeur");
    }


}