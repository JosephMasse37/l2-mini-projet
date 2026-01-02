package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metiers.Client;
import metiers.Abonnement;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import passerelle.Connexion;
//import passerelle.DAO;
import passerelle.DAOException;
import passerelle.ClientDAO;
import passerelle.AbonnementDAO;

public class TestClientDAO {
    private Connection connexion;
    private ClientDAO clientDAO;
    private AbonnementDAO abonnementDAO;

    @BeforeEach
    void setUp() throws SQLException, DAOException {
        //Instantiation of the DB connection using the Connexion class
        this.connexion = Connexion.getConnexion();

        //Remove auto-commit (auto save) to be able to use rollback after each query
        this.connexion.setAutoCommit(false);

        //Instantiation of a ClientDAO 
        this.clientDAO = new ClientDAO(this.connexion);
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
    void testCreateClient() throws DAOException {
        //Create a sample Client
        Client client = createSampleClient();
        Client createdClient = clientDAO.create(client);
        //tests
        assertNotNull(createdClient, "Created Client should not be null");
        assertEquals(client.getIdClient(), createdClient.getIdClient());
        
    }

    @Test
    void testFindClient() throws DAOException {
        //Create a sample Client
        Client client = createSampleClient();

        //Create it in the DB and try to find it
        Client createdClient = clientDAO.create(client);
        int IDClient = createdClient.getIdClient();
        
        Client foundClient = clientDAO.find(IDClient);
        
        //tests
        assertNotNull(foundClient, "Found Client should not be null");
        assertEquals(IDClient, foundClient.getIdClient());
    }

    @Test
    void testUpdateClient() throws DAOException {
        //Create a sample Client
        Client client = createSampleClient();
        
        Client createdClient = clientDAO.create(client);
        int IDClient = createdClient.getIdClient();

        //Modify some attributes of the Client
        createdClient.setNom("Dupont");
        createdClient.setPrenom("Jean");
        clientDAO.update(createdClient);

        //Retrieve the updated Client from the DB
        Client DBClient = clientDAO.find(IDClient);

        //tests
        assertNotNull(DBClient, "Updated Client should not be null");
        assertEquals("Dupont", DBClient.getNom(), "Nom should be updated");
        assertEquals("Jean", DBClient.getPrenom(), "Prenom should be updated");

    }

    @Test
    void testDeleteClient() throws DAOException {
        //create a sample Client
        Client client = createSampleClient();
        

        //create it in the DB and check if it exists
        Client createdClient = clientDAO.create(client);
        int IDClient = client.getIdClient();
        assertNotNull(clientDAO.find(IDClient));
        boolean deleted = clientDAO.delete(createdClient);

        //check if it has been deleted
        assertTrue(deleted, "Client should be deleted successfully");
        assertNull(clientDAO.find(IDClient), "Deleted Client should not be found");
    }

    @Test
    void testFindAllClients() throws DAOException {
        //Create multiple sample Clients
        Client client1 = createSampleClient();
        Client client2 = new Client(800, "Dupont", "Jean", 30);
        Abonnement abo = new Abonnement(1, "Mensuel", 29.99, "2024-01-01");
        abo.setIdAbonnement(1); 
        //Linking the abonnement to the client
        client2.setUnAbonnement(abo);

        //create them in the DB
        clientDAO.create(client1);
        clientDAO.create(client2);

        //Retrieve all Clients from the DB
        List<Client> clients = clientDAO.findAll();
        //tests
        assertNotNull(clients, "List of Clients should not be null");
        assertTrue(clients.size() >= 2, "There should be at least two Clients in the list");
    }

    // Tests on invalid objects (always raise exceptions)

    @Test
    void testCreateInvalidClient() throws DAOException {
        // Create a client with ID 999
        Client c1 = new Client(1000, null, null, 0);
        Abonnement abo = new Abonnement(1, "Mensuel", 29.99, "2024-01-01");
        abo.setIdAbonnement(1); 
        //Linking the abonnement to the client
        c1.setUnAbonnement(abo);

        //Trying to create a client with null attributes should raise an exception
        assertThrows(DAOException.class, () -> {
            clientDAO.create(c1);
        }, "Creating a client with null attributes should throw DAOException");
    }

    @Test
    void testUpdateInvalidClient() {
        //create a non-existent Client
        Client c = new Client(8888, "Fantome", "Casper", 955);
        
        //the client hasn't been added to the DB yet, so updating it should throw an exception
        assertThrows(DAOException.class, () -> {
            //Try to update a Client that does not exist in the DB
            clientDAO.update(c);
        }, "Updating a non-existent client should throw DAOException");
    }

    @Test
    void testDeleteNonExistentClient() {
        //create a non-existent Client
        Client c = new Client(7777, "Inconnu", "Inconnu", 0);

        //the client hasn't been added to the DB yet, so deleting it should throw an exception
        assertThrows(DAOException.class, () -> {
            //Try to delete a Client that does not exist in the DB
            clientDAO.delete(c);
        }, "Deleting a non-existent client should throw DAOException");
    }

    //Test all basic actions at the same time (create, find, update, delete)

    @Test
    void testCRUDClient() throws DAOException {
        //Create a sample Client
        Client client = createSampleClient();
        Client createdClient = clientDAO.create(client);
        assertNotNull(createdClient);
        int id = createdClient.getIdClient();

        //Find the created Client
        Client foundClient = clientDAO.find(id);
        assertNotNull(foundClient);
        assertEquals("Martin", foundClient.getNom());

        //Update the Client
        client.setNom("NomModifie");
        clientDAO.update(client);
        
        Client updatedClient = clientDAO.find(id);
        assertEquals("NomModifie", updatedClient.getNom(), "Le nom devrait être mis à jour");

        //Delete the Client
        boolean deleted = clientDAO.delete(client);
        assertTrue(deleted);
        assertNull(clientDAO.find(id), "Le client ne devrait plus exister après suppression");
    }

    //Instancing object method 

    private Client createSampleClient() {
        Client client = new Client(999, "Martin", "Alice", 25);
        Abonnement abo = new Abonnement(1, "Mensuel", 29.99, "2024-01-01");
        abo.setIdAbonnement(1); 
    
        // On lie l'abonnement au client
        client.setUnAbonnement(abo); 
    
        return client;
    }
}