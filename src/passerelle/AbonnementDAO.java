package passerelle;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import metiers.Abonnement;

public class AbonnementDAO extends DAO<Abonnement> {

    public AbonnementDAO(Connection connexion) {
        super(connexion);
    }

    // If we add a new type of abonnement
    public Abonnement create(Abonnement abonnement) throws DAOException {
        String query = "INSERT INTO abonnement (formule, prix, duree) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connexion.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, abonnement.getFormule());
            ps.setDouble(2, abonnement.getPrix());
            ps.setString(3, abonnement.getDuree());

            int lignesModifiees = ps.executeUpdate();

            if (lignesModifiees == 0) {
                throw new DAOException("Fail to create the abonnement no addition to the database");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    abonnement.setIdAbonnement(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating abonnement failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error while creating abonnement. Details:");
            e.printStackTrace();
            throw new DAOException("Error while creating the abonnement", e);
        }
        return abonnement;
    }

    public Abonnement find(int idAbonnement) throws DAOException {
        String query = "SELECT idAbonnement, formule, prix, duree FROM abonnement WHERE idAbonnement = ?";
        
        try (PreparedStatement ps = connexion.prepareStatement(query)) {
            
            ps.setInt(1, idAbonnement);

            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                   
                    String formule = rs.getString("formule");
                    double prix = rs.getDouble("prix");
                    String duree = rs.getString("duree");

                    return new Abonnement(idAbonnement, formule, prix, duree);
                } else {
                    return null; // No abonnement found with the given ID
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while searching for the abonnement with ID = " + idAbonnement + "Details:");
            e.printStackTrace();
            throw new DAOException("Error while searching for the abonnement with ID : " + idAbonnement, e);
        }
    }

    public List<Abonnement> findAll() throws DAOException {
        List<Abonnement> abonnements = new ArrayList<>();
        String query = "SELECT * FROM abonnement";

        // try-with-ressources to auto-close resources (ps and rs)
        try (PreparedStatement ps = connexion.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int idAbonnement = rs.getInt("idAbonnement");
                String formule = rs.getString("formule");
                double prix = rs.getDouble("prix");
                String duree = rs.getString("duree");

                // Create an instance of Abonnement to add it to the list
                Abonnement abonnement = new Abonnement(idAbonnement, formule, prix, duree);
                abonnements.add(abonnement);
            }
        } catch (SQLException e) {
            throw new DAOException("Error while fetching all abonnements", e);
        }
        return abonnements;
    }

    // If we change the price for example
    public boolean update(Abonnement unAbonnement) throws DAOException {
        String query = "UPDATE abonnement SET formule = ?, prix = ?, duree = ? WHERE idAbonnement = ?";

        try (PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setString(1, unAbonnement.getFormule());
            ps.setDouble(2, unAbonnement.getPrix());
            ps.setString(3, unAbonnement.getDuree());
            ps.setInt(4, unAbonnement.getIdAbonnement());

            int lignesModifiees = ps.executeUpdate();

            if (lignesModifiees == 0) {
                throw new DAOException("Fail to update the abonnement, no modification in the database");
            }

            return true;

        } catch (SQLException e) {
            System.err.println("Error while updating abonnement with ID = " + unAbonnement.getIdAbonnement() + " Details:");
            e.printStackTrace();
            throw new DAOException("Error while updating the abonnement with ID : " + unAbonnement.getIdAbonnement(), e);
        }
    }

    // Delete a type of abonnement
    public boolean delete(Abonnement abonnement) throws DAOException {
        String query = "DELETE FROM abonnement WHERE idAbonnement = ?";

        try (PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setInt(1, abonnement.getIdAbonnement());

            int lignesSupprimees = ps.executeUpdate();

            if (lignesSupprimees == 0) {
                throw new DAOException("Fail to delete the abonnement no modification in the database");
            }

            return true;
        } catch (SQLException e) {
            System.err.println("Error while deleting abonnement with ID = " + abonnement.getIdAbonnement() + " Details:");
            e.printStackTrace();
            throw new DAOException("Error while deleting the abonnement with ID : " + abonnement.getIdAbonnement(), e);
        }
    }

    @Override
    public Abonnement find(int id1, int id2) throws DAOException {
        throw new DAOException("Not used");
    }

    @Override
    public Abonnement find(int id1, int id2, int id3) throws DAOException {
        throw new DAOException("Not used");
    }
}
