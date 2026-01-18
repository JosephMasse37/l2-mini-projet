package tests;

import metiers.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passerelle.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestDessertDAO {
    private Connection connexion;
    private ArretDAO arretDAO;
    private LigneDAO ligneDAO;
    private DessertDAO dessertDAO;

    @BeforeEach
    void setUp() throws SQLException, DAOException {
        //Instantiation of the DB connection using the Connexion class
        this.connexion = Connexion.getConnexion();

        //Remove auto-commit (auto save) to be able to use rollback after each query
        this.connexion.setAutoCommit(false);

        //Instantiation of DAOs
        this.arretDAO = new ArretDAO(this.connexion);
        this.ligneDAO = new LigneDAO(this.connexion);
        this.dessertDAO = new DessertDAO(this.connexion);
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
    @DisplayName("Creation d'une Desserte")
    void testCreate() throws DAOException {
        // Création des objets nécessaires à la création d'une Conduite
        Arret lArret = arretDAO.find(2);
        Ligne laLigne = ligneDAO.find(6);

        // Insertion
        Dessert dessertecreee = dessertDAO.create(new Dessert(lArret, laLigne, 1));
        System.out.println("Desserte créée avec " + dessertecreee);

        // Vérification de la conformité de l'objet envoyé
        assertNotNull(dessertecreee, "La desserte créée ne devrait pas être null");

        // Vérification des clés étrangères
        assertNotNull(dessertecreee.getUnArret(), "L'arrêt lié ne devrait pas être null");
        assertNotNull(dessertecreee.getUneLigne(), "La ligne liée ne devrait pas être null");

        assertEquals(lArret.getIdArret(), dessertecreee.getUnArret().getIdArret(), "L'arrêt ne correspond pas");
        assertEquals(laLigne.getIdLigne(), dessertecreee.getUneLigne().getIdLigne(), "La ligne ne correspond pas");

        System.out.println("Succès Desserte : " + dessertecreee);
    }

    @Test
    @DisplayName("Recherche d'une desserte par IDs")
    void testFindById() throws DAOException {

        // Création des objets nécessaires à la création d'une Conduite
        int arretID = 1;
        int ligneID = 1;

        System.out.println("Tentative de recherche via le DAO.");
        Dessert laDesserte = dessertDAO.find(1, 1);

        // Vérification de la conformité de l'objet envoyé
        assertNotNull(laDesserte, "La desserte ne devrait pas être null");

        // Vérification des clés étrangères
        assertNotNull(laDesserte.getUnArret(), "L'arrêt lié ne devrait pas être null");
        assertNotNull(laDesserte.getUneLigne(), "La ligne liée ne devrait pas être null");

        assertEquals(arretID, laDesserte.getUnArret().getIdArret(), "L'arrêt ne correspond pas");
        assertEquals(ligneID, laDesserte.getUneLigne().getIdLigne(), "La ligne ne correspond pas");

        System.out.println("Succès Desserte : " + laDesserte);
    }

    @Test
    @DisplayName("Récupération de toutes les Dessertes d'un Arrêt")
    void testGetDessertesUnArret() throws DAOException {
        // Appel méthode
        List<Ligne> toutesLesLignes = dessertDAO.getDessertesUnArret(8);

        // Vérification de la conformité de la liste
        assertNotNull(toutesLesLignes, "La liste ne doit pas être null");
        assertFalse(toutesLesLignes.isEmpty(), "La liste devrait contenir au moins une conduite");
        System.out.println(" Nombre de Dessertes trouvés : " + toutesLesLignes.size());

        for (Ligne uneLigne : toutesLesLignes) {
            System.out.println("Vérification Desserte : Ligne(s) " + uneLigne.getLibelle());
            assertNotNull(uneLigne, "La ligne liée ne devrait pas être null");
        }
    }

    @Test
    @DisplayName("Récupération de toutes les Dessertes d'une ligne")
    void testGetDessertesUneLigne() throws DAOException {
        // Appel méthode
        List<Arret> toutesLesArrets = dessertDAO.getDessertesUneLigne(6);

        // Vérification de la conformité de la liste
        assertNotNull(toutesLesArrets, "La liste ne doit pas être null");
        assertFalse(toutesLesArrets.isEmpty(), "La liste devrait contenir au moins une conduite");
        System.out.println(" Nombre de Dessertes trouvés : " + toutesLesArrets.size());

        for (Arret unArret : toutesLesArrets) {
            System.out.println("Vérification Desserte : " + unArret.getNom());
            // Vérification des clés étrangères
            assertNotNull(unArret, "L'arrêt lié ne devrait pas être null");
        }
    }

    @Test
    @DisplayName("Suppression d'une Desserte")
    void testDelete() throws DAOException {
        // Création des objets nécessaires à la création d'une Conduite
        Arret lArret = arretDAO.find(9);
        Ligne laLigne = ligneDAO.find(1);

        // Insertion
        Dessert desserteCreee = dessertDAO.create(new Dessert(lArret, laLigne, 1));
        System.out.println("Desserte crée avec " + desserteCreee);

        // Vérification de la conformité de l'objet envoyé
        assertNotNull(desserteCreee, "La conduite créée ne devrait pas être null");

        // Suppression BDD
        boolean delete = dessertDAO.delete(desserteCreee);

        assertTrue(delete, "La suppression n'a pas pu se faire");

        System.out.println("Succès Desserte : " + desserteCreee);
    }

    @Test
    @DisplayName("Récupération de toutes les Dessertes")
    void testFindAll() throws DAOException {
        // Appel méthode
        List<Dessert> toutesLesDessertes = dessertDAO.findAll();

        // Vérification de la conformité de la liste
        assertNotNull(toutesLesDessertes, "La liste ne doit pas être null");
        assertFalse(toutesLesDessertes.isEmpty(), "La liste devrait contenir au moins une conduite");
        System.out.println(" Nombre de Dessertes trouvés : " + toutesLesDessertes.size());

        for (Dessert uneDesserte : toutesLesDessertes) {
            System.out.println("Vérification Desserte : " + uneDesserte);
            // Vérification des clés étrangères
            assertNotNull(uneDesserte.getUnArret(), "L'arrêt lié ne devrait pas être null");
            assertNotNull(uneDesserte.getUneLigne(), "La ligne liée ne devrait pas être null");
        }
    }
}