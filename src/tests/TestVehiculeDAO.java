package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    
    @Test
    void testCreateAndDeleteVehicule() {
        // Code to test creation and deletion of Vehicule
    }

    @Test
    void testFindVehicule() {
        // Code to test finding a Vehicule by ID
    }
}
