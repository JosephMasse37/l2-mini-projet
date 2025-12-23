package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metiers.Vehicule;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

import passerelle.Connexion;
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
    void testCreateVehicule() {
        
    }

    @Test
    void testFindVehicule() {
        //Code to test finding a Vehicule by ID
    }

    @Test
    void testUpdateVehicule() {
        //Code to test updating a Vehicule
    }

    @Test
    void testDeleteVehicule() {
        //Code to test deleting a Vehicule
    }

    @Test
    void testFindAllVehicules() {
        //Code to test finding all Vehicules
    }

    // Tests on invalid objects (always raise exceptions)

    @Test
    void testCreateInvalidVehicule() {
        //Code to test creating an invalid Vehicule
    }

    @Test
    void testUpdateInvalidVehicule() {
        //Code to test updating an invalid Vehicule
    }

    @Test
    void testDeleteNonExistentVehicule() {
        //Code to test deleting a non-existent Vehicule
    }

    //Test all basic actions at the same time (create, find, update, delete)

    @Test
    void testCRUDVehicule() {
        //Code to test the full CRUD cycle for a Vehicule
    }

    //Instancing object method 

    private Vehicule createSampleVehicule() {
        //Code to create and return a Vehicule object
        return null;
    }
}
