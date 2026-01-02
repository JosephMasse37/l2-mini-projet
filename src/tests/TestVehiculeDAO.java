package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metiers.Tram;
import metiers.TypeVehicule;
import metiers.Vehicule;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import passerelle.Connexion;
//import passerelle.DAO;
import passerelle.DAOException;
import passerelle.VehiculeDAO;

public class TestVehiculeDAO {
    private Connection connexion;
    private VehiculeDAO vehiculeDAO;

    @BeforeEach
    void setUp() throws SQLException, DAOException {
        //Instantiation of the DB connection using the Connexion class
        this.connexion = Connexion.getConnexion();

        //Remove auto-commit (auto save) to be able to use rollback after each query
        this.connexion.setAutoCommit(false);

        //Instantiation of a VehiculeDAO 
        this.vehiculeDAO = new VehiculeDAO(this.connexion);
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
    void testCreateVehicule() throws DAOException {
        //Create a sample Vehicule
        Vehicule vehicule = createSampleVehicule();
        Vehicule createdVehicule = vehiculeDAO.create(vehicule);
        //tests
        assertNotNull(createdVehicule, "Created Vehicule should not be null");
        assertEquals(vehicule.getNumVehicule(), createdVehicule.getNumVehicule());
        
    }

    @Test
    void testFindVehicule() throws DAOException {
        //Create a sample Vehicule
        Vehicule vehicule = createSampleVehicule();
        int numVehicule = vehicule.getNumVehicule();

        //Create it in the DB and try to find it
        vehiculeDAO.create(vehicule);
        Vehicule foundVehicule = vehiculeDAO.find(numVehicule);

        //tests
        assertNotNull(foundVehicule, "Found Vehicule should not be null");
        assertEquals(vehicule.getNumVehicule(), foundVehicule.getNumVehicule());

    }

    @Test
    void testUpdateVehicule() throws DAOException {
        //Create a sample Vehicule
        Vehicule vehicule = createSampleVehicule();
        int numVehicule = vehicule.getNumVehicule();

        vehiculeDAO.create(vehicule);
        
        //Modify some attributes of the Vehicule
        vehicule.setMarque("UpdatedMarque");
        vehicule.setModele("UpdatedModele");
        vehiculeDAO.update(vehicule);

        //Retrieve the updated Vehicule from the DB
        Vehicule DBVehicule = vehiculeDAO.find(numVehicule);

        //tests
        assertNotNull(DBVehicule, "Updated Vehicule should not be null");
        assertEquals("UpdatedMarque", DBVehicule.getMarque(), "Marque should be updated");
        assertEquals("UpdatedModele", DBVehicule.getModele(), "Modele should be updated");

    }

    @Test
    void testDeleteVehicule() throws DAOException {
        //create a sample Vehicule
        Vehicule vehicule = createSampleVehicule();
        int numVehicule = vehicule.getNumVehicule();

        //create it in the DB and check if it exists
        vehiculeDAO.create(vehicule);
        assertNotNull(vehiculeDAO.find(numVehicule));

        boolean deleted = vehiculeDAO.delete(vehicule);

        //check if it has been deleted
        assertTrue(deleted, "Vehicule should be deleted successfully");
        assertNull(vehiculeDAO.find(numVehicule), "Deleted Vehicule should not be found");
    }

    @Test
    void testFindAllVehicules() throws DAOException {
        //Create multiple sample Vehicules
        Vehicule vehicule1 = createSampleVehicule();
        Vehicule vehicule2 = new Tram(1000, "Siemens", "Avenio", LocalDate.now(), LocalDate.now(), LocalDateTime.now());
        TypeVehicule type = new TypeVehicule(1, "Tram"); 
        vehicule2.setTypevehicule(type);

        //create them in the DB
        vehiculeDAO.create(vehicule1);
        vehiculeDAO.create(vehicule2);

        //Retrieve all Vehicules from the DB
        List<Vehicule> vehicules = vehiculeDAO.findAll();

        //tests
        assertNotNull(vehicules, "List of Vehicules should not be null");
        assertTrue(vehicules.size() >= 2, "There should be at least two Vehicules in the list");
    }

    // Tests on invalid objects (always raise exceptions)

    @Test
    void testCreateInvalidVehicule() throws DAOException {
        //create a valid vehicule with id 999 and add it to the DB
        Vehicule vehicule1 = createSampleVehicule();
        vehiculeDAO.create(vehicule1);
        TypeVehicule type = new TypeVehicule(1, "Tram");

        //create an invalid vehicule with the same id and add it to the DB
        Vehicule invalidVehicule = new Tram(999, "", "", LocalDate.now(), LocalDate.now(), LocalDateTime.now());
        invalidVehicule.setTypevehicule(type);
        assertThrows(DAOException.class, () -> {
            vehiculeDAO.create(invalidVehicule);
        }, "Creating a Vehicule with an ID already in the DB should throw DAOException");

        //create an invalide Vehicule with missing attributes
        Vehicule incompleteVehicule = new Tram(1001, null, null, LocalDate.now(), LocalDate.now(), LocalDateTime.now()); 
        incompleteVehicule.setTypevehicule(type);
        assertThrows(DAOException.class, () -> {
            vehiculeDAO.create(incompleteVehicule);
        }, "Creating a Vehicule with null attributes should throw DAOException");
    }

    @Test
    void testUpdateInvalidVehicule() throws DAOException {
        //create a sample Vehicule
        Vehicule vehicule = new Tram(999, "Marque", "Modele", LocalDate.now(), LocalDate.now(), LocalDateTime.now());
        vehicule.setTypevehicule(new TypeVehicule(1, "Tram"));

        //the vehicule hasn't been added to the DB yet, so updating it should throw an exception
        assertThrows(DAOException.class, () -> {
            //Try to update a Vehicule that does not exist in the DB
            vehiculeDAO.update(vehicule);
        }, "Updating a non-existent Vehicule should throw DAOException");
    }

    @Test
    void testDeleteNonExistentVehicule() {
        //create a sample Vehicule
        Vehicule vehicule = new Tram(999, "Marque", "Modele", LocalDate.now(), LocalDate.now(), LocalDateTime.now());
        vehicule.setTypevehicule(new TypeVehicule(1, "Tram"));

        //the vehicule hasn't been added to the DB yet, so deleting it should throw an exception
        assertThrows(DAOException.class, () -> {
            //Try to delete a Vehicule that does not exist in the DB
            vehiculeDAO.delete(vehicule);
        }, "Deleting a non-existent Vehicule should throw DAOException");
    }

    /*Test all basic actions at the same time (create, find, update, delete)
      If the test passes, it means all CRUD operations work correctly*/

    @Test
    void testCRUDVehicule() throws DAOException {
        //create a sample Vehicule
        Vehicule vehicule = createSampleVehicule();
        Vehicule createdVehicule = vehiculeDAO.create(vehicule);
        int numVehicule = createdVehicule.getNumVehicule();

        assertNotNull(createdVehicule, "Created Vehicule should not be null");

        //Find the created Vehicule
        Vehicule foundVehicule = vehiculeDAO.find(numVehicule);
        assertNotNull(foundVehicule, "Found Vehicule should not be null");
        assertEquals(vehicule.getNumVehicule(), foundVehicule.getNumVehicule());

        //Update the Vehicule
        vehicule.setMarque("UpdatedMarque");
        vehiculeDAO.update(vehicule);
        Vehicule updatedVehicule = vehiculeDAO.find(numVehicule);
        assertNotNull(updatedVehicule, "Updated Vehicule should not be null");
        assertEquals("UpdatedMarque", updatedVehicule.getMarque(), "Marque should be updated");

        //Delete the Vehicule
        boolean deleted = vehiculeDAO.delete(vehicule);
        assertTrue(deleted, "Vehicule should be deleted successfully");
        assertNull(vehiculeDAO.find(numVehicule), "Deleted Vehicule should not be found");
        
    }

    //Instancing object method 

    private Vehicule createSampleVehicule() {
        //Use of ID 1 for TypeVehicule (Tram) 
        TypeVehicule type = new TypeVehicule(1, "Tram"); 
        //Use of ID 999 to ensure the ID is not already in the DB
        Tram t = new Tram(999, "Alstom", "Citadis", LocalDate.now(), LocalDate.now(), LocalDateTime.now());
        t.setTypevehicule(type);
        return t;
    }
}