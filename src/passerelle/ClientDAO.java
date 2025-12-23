package passerelle;
import java.sql.Connection;
import java.sql.SQLException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

import metiers.Abonnement;
import metiers.Client;


public class ClientDAO extends DAO<Client>  {

    public ClientDAO(Connection connexion) {
        super(connexion);
    }

    @Override
    public Client create(Client client) throws DAOException {
        String query = "INSERT INTO client (nom, prenom, age, idAbonnement) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connexion.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setInt(3, client.getAge());
            ps.setInt(4, client.getUnAbonnement().getIdAbonnement());

            int lignesModifiees = ps.executeUpdate();

            if (lignesModifiees == 0) {
                throw new DAOException("Fail to create the client no addition to the database");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    client.setIdClient(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating client failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error while creating client. Details:");
            e.printStackTrace();
            throw new DAOException("Error while creating the client", e);
        }
        return client;
    }

    @Override
    public Client find(int idClient) throws DAOException {
        String query = "SELECT idClient, nom, prenom, age FROM client WHERE idClient = ?";
        
        try (PreparedStatement ps = connexion.prepareStatement(query)) {
            
            ps.setInt(1, idClient);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    AbonnementDAO abonnementDAO = new AbonnementDAO(connexion);
                   
                    String nom = rs.getString("nom");
                    String prenom = rs.getString("prenom");
                    int age = rs.getInt("age");
                    Abonnement abonnement = abonnementDAO.find(rs.getInt("idAbonnement"));

                    return new Client(idClient, nom, prenom, age, abonnement);
                } else {
                    return null; // No abonnement found with the given ID
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while searching for the abonnement with ID = " + idClient + "Details:");
            e.printStackTrace();
            throw new DAOException("Error while searching for the abonnement with ID : " + idClient, e);
        }
    }

    @Override
    public boolean update(Client client) throws DAOException {
        String query = "UPDATE client SET nom = ?, prenom = ?, age = ?, idAbonnement = ? WHERE idClient = ?";

        try (PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setInt(3, client.getAge());
            ps.setInt(4, client.getUnAbonnement().getIdAbonnement());

            int lignesModifiees = ps.executeUpdate();

            if (lignesModifiees == 0) {
                throw new DAOException("Fail to update the client, no modification in the database");
            }

            return true;

        } catch (SQLException e) {
            System.err.println("Error while updating client with ID = " + client.getIdClient() + " Details:");
            e.printStackTrace();
            throw new DAOException("Error while updating the client with ID : " + client.getIdClient(), e);
        }
    }

    @Override
    public boolean delete(Client client) throws DAOException {
        String query = "DELETE FROM client WHERE idClient = ?";

        try (PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setInt(1, client.getIdClient());

            int lignesSupprimees = ps.executeUpdate();

            if (lignesSupprimees == 0) {
                throw new DAOException("Fail to delete the client, no modification in the database");
            }

            return true;
        } catch (SQLException e) {
            System.err.println("Error while deleting client with ID = " + client.getIdClient() + " Details:");
            e.printStackTrace();
            throw new DAOException("Error while deleting the client with ID : " + client.getIdClient(), e);
        }
    }

    @Override
    public List<Client> findAll() throws DAOException {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM client";

        // try-with-ressources to auto-close resources (ps and rs)
        try (PreparedStatement ps = connexion.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                AbonnementDAO abonnementDAO = new AbonnementDAO(connexion);
                int idClient = rs.getInt("idClient");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                int age = rs.getInt("age");
                Abonnement abonnement = abonnementDAO.find(rs.getInt("idAbonnement"));

                // Create an instance of Abonnement to add it to the list
                Client client = new Client(idClient, nom, prenom, age, abonnement);
                clients.add(client);
            }
        } catch (SQLException e) {
            throw new DAOException("Error while fetching all clients", e);
        }
        return clients;
    }

    @Override
    public Client find(int id1, int id2) throws DAOException {
        throw new DAOException("Not used");
    }

    @Override
    public Client find(int id1, int id2, int id3) throws DAOException {
        throw new DAOException("Not used");
    }

    
}
