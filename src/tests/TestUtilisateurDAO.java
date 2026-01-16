package tests;

import metiers.Arret;
import metiers.Dessert;
import metiers.Ligne;
import metiers.Utilisateur;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passerelle.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestUtilisateurDAO {
    private Connection connexion;
    private UtilisateurDAO utilisateurDAO;

    @BeforeEach
    void setUp() throws SQLException, DAOException {
        //Instantiation of the DB connection using the Connexion class
        this.connexion = Connexion.getConnexion();

        //Remove auto-commit (auto save) to be able to use rollback after each query
        this.connexion.setAutoCommit(false);

        //Instantiation of DAOs
        this.utilisateurDAO = new UtilisateurDAO(this.connexion);
    }

    @AfterEach
    void tearDown() throws SQLException, DAOException {
        //If the connection exists, use rollback to undo any changes made during the tests
        if (connexion != null) {
            //Undo the changes from the tests
            connexion.rollback();

            //Put back auto-commit
            connexion.setAutoCommit(true);

            //Close the connection
            Connexion.close();
        }
    }

    @Test
    @DisplayName("Creation d'un Utilisateur")
    void testCreate() throws DAOException {
        // Insertion
        Utilisateur utilisateurCree = utilisateurDAO.create(new Utilisateur("test.test", "14d5ba5b0f9a01c847a64270d50bf52d71ebf073a22643a855c55c1c9e613688", "PrenomTest", "NomTest"));
        System.out.println("Utilisateur créée avec " + utilisateurCree);

        // Vérification de la conformité de l'objet envoyé
        assertNotNull(utilisateurCree, "La desserte créée ne devrait pas être null");

        System.out.println("Succès Utilisateur : " + utilisateurCree);
    }

    @Test
    @DisplayName("Recherche d'un utilisateur par ID")
    void testFindById() throws DAOException {
        System.out.println("Tentative de recherche via le DAO.");
        Utilisateur lUtilisateur = utilisateurDAO.find("joseph.masse");

        // Vérification de la conformité de l'objet envoyé
        assertNotNull(lUtilisateur, "L'utilisateur ne devrait pas être null");

        System.out.println("Succès Utilisateur : " + lUtilisateur);
    }

    @Test
    @DisplayName("Récupération d'un utilisateur en vérifiant son mot de passe")
    void testGetUnUtilisateur() throws DAOException {
        System.out.println("Tentative de recherche via le DAO [GET].");
        Utilisateur lUtilisateur = utilisateurDAO.getUnUtilisateur("joseph.masse", "etudiant007", false);

        // Vérification de la conformité de l'objet envoyé
        assertNotNull(lUtilisateur, "L'utilisateur ne devrait pas être null");

        System.out.println("Succès Utilisateur : " + lUtilisateur);
    }

    @Test
    @DisplayName("Récupération de toutes les Dessertes")
    void testGetUtilisateursNonChauffeur() throws DAOException {
        // Appel méthode
        List<Utilisateur> tousLesUtilisateurs = utilisateurDAO.getUtilisateursNonChauffeur();

        // Vérification de la conformité de la liste
        assertNotNull(tousLesUtilisateurs, "La liste ne doit pas être null");
        assertFalse(tousLesUtilisateurs.isEmpty(), "La liste devrait contenir au moins un utilisateur");
        System.out.println(" Nombre de Dessertes trouvés : " + tousLesUtilisateurs.size());

        for (Utilisateur unUtilisateur : tousLesUtilisateurs) {
            System.out.println("Vérification Utilisateur : " + unUtilisateur);
        }
    }

    @Test
    @DisplayName("Suppression d'une Desserte")
    void testDelete() throws DAOException {
        // Insertion
        Utilisateur utilisateurCree = utilisateurDAO.create(new Utilisateur("test.test", "14d5ba5b0f9a01c847a64270d50bf52d71ebf073a22643a855c55c1c9e613688", "PrenomTest", "NomTest"));
        System.out.println("Utilisateur crée avec " + utilisateurCree);

        // Vérification de la conformité de l'objet envoyé
        assertNotNull(utilisateurCree, "L'utilisateur créée ne devrait pas être null");

        // Suppression BDD
        boolean delete = utilisateurDAO.delete(utilisateurCree);

        assertTrue(delete, "La suppression n'a pas pu se faire");

        System.out.println("Succès Utilisateur : " + utilisateurCree);
    }

    @Test
    @DisplayName("Récupération de toutes les Dessertes")
    void testFindAll() throws DAOException {
        // Appel méthode
        List<Utilisateur> tousLesUtilisateurs = utilisateurDAO.findAll();

        // Vérification de la conformité de la liste
        assertNotNull(tousLesUtilisateurs, "La liste ne doit pas être null");
        assertFalse(tousLesUtilisateurs.isEmpty(), "La liste devrait contenir au moins un utilisateur");
        System.out.println(" Nombre de Dessertes trouvés : " + tousLesUtilisateurs.size());

        for (Utilisateur unUtilisateur : tousLesUtilisateurs) {
            System.out.println("Vérification Utilisateur : " + unUtilisateur);
        }
    }
}
