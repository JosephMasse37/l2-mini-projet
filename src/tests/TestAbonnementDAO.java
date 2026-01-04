package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metiers.Abonnement;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import passerelle.Connexion;
//import passerelle.DAO;
import passerelle.DAOException;
import passerelle.AbonnementDAO;

public class TestAbonnementDAO {
    private Connection connexion;
    private AbonnementDAO abonnementDAO;

    @BeforeEach
    void setUp() throws SQLException, DAOException {
        //Instantiation of the DB connection using the Connexion class
        this.connexion = Connexion.getConnexion();

        //Remove auto-commit (auto save) to be able to use rollback after each query
        this.connexion.setAutoCommit(false);

        //Instantiation of a AbonnementDAO 
        this.abonnementDAO = new AbonnementDAO(this.connexion);
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
    
    //CRUD tests (should always work)

    @Test
    void testCreateAbonnement() throws DAOException {
        //Create a sample Abonnement
        Abonnement abonnement = createSampleAbonnement();
        Abonnement createdAbonnement = abonnementDAO.create(abonnement);

        //tests
        assertNotNull(createdAbonnement, "Created Abonnement should not be null");
        assertEquals(abonnement.getIdAbonnement(), createdAbonnement.getIdAbonnement());
        
    }

    @Test
    void testFindAbonnement() throws DAOException {
        //Create a sample Abonnement
        Abonnement abonnement = createSampleAbonnement();

        //Create it in the DB and try to find it
        Abonnement createdAbonnement =  abonnementDAO.create(abonnement);
        int idAbonnement = createdAbonnement.getIdAbonnement();
        Abonnement foundAbonnement = abonnementDAO.find(idAbonnement);

        //tests
        assertNotNull(foundAbonnement, "Found Abonnement should not be null");
        assertEquals(abonnement.getIdAbonnement(), foundAbonnement.getIdAbonnement());

    }

    @Test
    void testUpdateAbonnement() throws DAOException {
        //Create a sample Abonnement
        Abonnement abonnement = createSampleAbonnement();

        //Create it in the DB
        Abonnement createdAbonnement = abonnementDAO.create(abonnement);  
        int idAbonnement = createdAbonnement.getIdAbonnement();

        //Modify some fields
        abonnement.setFormule("annuel");
        abonnement.setPrix(500.0);

        //Update it in the DB
        abonnementDAO.update(abonnement);

        //Retrieve the updated Abonnement
        Abonnement updatedAbonnement = abonnementDAO.find(idAbonnement);

        //tests
        assertNotNull(updatedAbonnement, "Updated Abonnement should not be null");
        assertEquals("annuel", updatedAbonnement.getFormule(), "Formule should be updated");
        assertEquals(500.0, updatedAbonnement.getPrix(), "Prix should be updated");
    }

    @Test
    void testDeleteAbonnement() throws DAOException {
        //create a sample Abonnement
        Abonnement abonnement = createSampleAbonnement(); 
        int idAbonnement = abonnement.getIdAbonnement();

        //Create it in the DB
        abonnementDAO.create(abonnement);

        //Delete it from the DB
        abonnementDAO.delete(abonnement);

        //Try to retrieve the deleted Abonnement
        Abonnement deletedAbonnement = abonnementDAO.find(idAbonnement);

        //tests
        assertNull(deletedAbonnement, "Deleted Abonnement should be null");
    }

    @Test
    void testFindAllAbonnements() throws DAOException {
        //Create multiple sample Abonnements
        Abonnement abonnement1 = new Abonnement(1001, "mensuel", 50.0, "1");
        Abonnement abonnement2 = new Abonnement(1002, "trimestriel", 140.0, "3");
        Abonnement abonnement3 = new Abonnement(1003, "annuel", 500.0, "12");

        //Create them in the DB
        abonnementDAO.create(abonnement1);
        abonnementDAO.create(abonnement2);
        abonnementDAO.create(abonnement3);

        //Retrieve all Abonnements from the DB
        List<Abonnement> abonnements = abonnementDAO.findAll();

        //tests
        assertNotNull(abonnements, "Abonnement list should not be null");
        assertTrue(abonnements.size() >= 3, "Abonnement list should contain at least three Abonnements");

    }

    // Tests on invalid objects (always raise exceptions)

    @Test
    void testCreateInvalidAbonnement() throws DAOException {
        //create another invalid Abonnement with missing attributes
        Abonnement abo3 = new Abonnement(1004, null, 0.0, null);
        assertThrows(DAOException.class, () -> {
            abonnementDAO.create(abo3);
        }, "Creating an Abonnement with null attributes should throw DAOException");
    }

    @Test
    void testUpdateInvalidAbonnement() throws DAOException {
        //create a sample Abonnement
        Abonnement abonnement = new Abonnement(1001, "mensuel", 50.0, "1");

        //the abonnement hasn't been added to the DB yet, so updating it should throw an exception
        assertThrows(DAOException.class, () -> {
            //Try to update an Abonnement that does not exist in the DB
            abonnementDAO.update(abonnement);
        }, "Updating a non-existent Vehicule should throw DAOException");
    }

    @Test
    void testDeleteNonExistentAbonnement() {
        //create a sample Abonnement
        Abonnement abonnement = new Abonnement(1001, "mensuel", 50.0, "1");

        //the abonnement hasn't been added to the DB yet, so deleting it should throw an exception
        assertThrows(DAOException.class, () -> {
            //Try to delete an Abonnement that does not exist in the DB
            abonnementDAO.delete(abonnement);
        }, "Deleting a non-existent Abonnement should throw DAOException");
    }

    /*Test all basic actions at the same time (create, find, update, delete)
      If the test passes, it means all CRUD operations work correctly*/

    @Test
    void testCRUDAbonnement() throws DAOException {
        Abonnement abo = createSampleAbonnement(); 

        //Create the Abonnement
        abonnementDAO.create(abo);
        Abonnement created = abonnementDAO.find(abo.getIdAbonnement());
        int id = abo.getIdAbonnement();

        assertNotNull(created, "Created Abonnement should not be null");
        
        //Find the Abonnement
        Abonnement found = abonnementDAO.find(id);
        assertNotNull(found);
        assertEquals(50.0, found.getPrix());

        //Update the Abonnement
        abo.setPrix(75.0);
        abonnementDAO.update(abo);
        
        Abonnement updated = abonnementDAO.find(id);
        assertEquals(75.0, updated.getPrix());

        //Delete the Abonnement
        assertTrue(abonnementDAO.delete(abo));
        assertNull(abonnementDAO.find(id));
    }

    //Instancing object method 

    private Abonnement createSampleAbonnement() {
        Abonnement abonnement = new Abonnement(999, "mensuel", 50.0, "1");
        return abonnement;
    }
}