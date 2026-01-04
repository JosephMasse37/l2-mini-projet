package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metiers.TypeVehicule;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import passerelle.Connexion;
//import passerelle.DAO;
import passerelle.DAOException;
import passerelle.TypeVehiculeDAO;

public class TestTypeVehiculeDAO {
    private Connection connexion;
    private TypeVehiculeDAO typeVehiculeDAO;

    @BeforeEach
    void setUp() throws SQLException, DAOException {
        //Instantiation of the DB connection using the Connexion class
        this.connexion = Connexion.getConnexion();

        //Remove auto-commit (auto save) to be able to use rollback after each query
        this.connexion.setAutoCommit(false);

        //Instantiation of a TypeVehiculeDAO 
        this.typeVehiculeDAO = new TypeVehiculeDAO(this.connexion);
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
    void testCreateTypeVehicule() throws DAOException {
        //Create a sample TypeVehicule
        TypeVehicule type = createSampleTypeVehicule();
        TypeVehicule createdType = typeVehiculeDAO.create(type);
        //tests
        assertNotNull(createdType, "Created TypeVehicule should not be null");
        assertEquals(type.getLibelle(), createdType.getLibelle());

    }

    @Test
    void testFindTypeVehicule() throws DAOException {
        //Create a sample TypeVehicule
        TypeVehicule type = createSampleTypeVehicule();

        //Create it in the DB and try to find it
        typeVehiculeDAO.create(type);
        TypeVehicule foundType = typeVehiculeDAO.find(type.getIdTypeVehicule());

        //tests
        assertNotNull(foundType, "Found TypeVehicule should not be null");
        assertEquals(type.getLibelle(), foundType.getLibelle());

    }

    @Test
    void testUpdateTypeVehicule() throws DAOException {
        //Create a sample TypeVehicule
        TypeVehicule type = createSampleTypeVehicule();

        typeVehiculeDAO.create(type);

        //Modify some attributes of the TypeVehicule
        type.setLibelle("Bus");
        typeVehiculeDAO.update(type);
        //Retrieve the updated TypeVehicule from the DB
        TypeVehicule DBType = typeVehiculeDAO.find(type.getIdTypeVehicule());

        //tests
        assertNotNull(DBType, "Updated TypeVehicule should not be null");
        assertEquals("Bus", DBType.getLibelle(), "Libelle should be updated");

    }

    @Test
    void testDeleteTypeVehicule() throws DAOException {
        //create a sample TypeVehicule
        TypeVehicule type = createSampleTypeVehicule();

        //create it in the DB and check if it exists
        typeVehiculeDAO.create(type);
        assertNotNull(typeVehiculeDAO.find(type.getIdTypeVehicule()));

        boolean deleted = typeVehiculeDAO.delete(type);

        //check if it has been deleted
        assertTrue(deleted, "TypeVehicule should be deleted successfully");
        assertNull(typeVehiculeDAO.find(type.getIdTypeVehicule()), "Deleted TypeVehicule should not be found");
    }

    @Test
    void testFindAllTypeVehicules() throws DAOException {
        //create multiple sample TypeVehicules
        TypeVehicule type1 = createSampleTypeVehicule(); // ID 999
        TypeVehicule type2 = new TypeVehicule(1000, "Trotinnette");

        //add them to the DB
        typeVehiculeDAO.create(type1);
        typeVehiculeDAO.create(type2);

        //retrieve all TypeVehicules from the DB
        List<TypeVehicule> types = typeVehiculeDAO.findAll();

        //tests
        assertNotNull(types);
        assertTrue(types.size() >= 2);
    }

    // Tests on invalid objects (always raise exceptions)

    @Test
    void testCreateInvalidTypeVehicule() throws DAOException {
        //Try to create a TypeVehicule with null attributes
        TypeVehicule t3 = new TypeVehicule(null, null);
        assertThrows(DAOException.class, () -> {
            typeVehiculeDAO.create(t3);
        }, "Creating a TypeVehicule with null attributes should throw DAOException");
    }

    @Test
    void testUpdateInvalidTypeVehicule() {
        //Create a sample TypeVehicule
        TypeVehicule t = new TypeVehicule(8888, "Fantome");

        //the TypeVehicule hasn't been added to the DB yet, so updating it should throw an exception
        assertThrows(DAOException.class, () -> {
            //Try to update a TypeVehicule that does not exist in the DB
            typeVehiculeDAO.update(t);
        });
    }

    @Test
    void testDeleteNonExistentTypeVehicule() {
        //Create a sample TypeVehicule
        TypeVehicule t = new TypeVehicule(7777, "Inconnu");

        //the TypeVehicule hasn't been added to the DB yet, so deleting it should throw an exception
        assertThrows(DAOException.class, () -> {
            //Try to delete a TypeVehicule that does not exist in the DB
            typeVehiculeDAO.delete(t);
        });
    }

    //Test all basic actions at the same time (create, find, update, delete)

    @Test
    void testCRUDTypeVehicule() throws DAOException {
        //Create a sample TypeVehicule
        TypeVehicule type = createSampleTypeVehicule();
        typeVehiculeDAO.create(type);
        int id = type.getIdTypeVehicule();

        //Find the created TypeVehicule
        TypeVehicule found = typeVehiculeDAO.find(id);
        assertNotNull(found);
        assertEquals("Velo", found.getLibelle());

        //Update the TypeVehicule
        type.setLibelle("Hyperloop");
        typeVehiculeDAO.update(type);
        
        TypeVehicule updated = typeVehiculeDAO.find(id);
        assertEquals("Hyperloop", updated.getLibelle());

        //Delete the TypeVehicule
        assertTrue(typeVehiculeDAO.delete(type));
        assertNull(typeVehiculeDAO.find(id));
    }

    //Instancing object method 

    private TypeVehicule createSampleTypeVehicule() {
        return new TypeVehicule(999, "Velo");
    }
}
