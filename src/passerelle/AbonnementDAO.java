package passerelle;
import java.sql.Connection;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import metiers.Abonnement;

public class AbonnementDAO extends DAO<Abonnement>{
    
    public AbonnementDAO(Connection connexion) {
        super(connexion);
    }

    //If we add a new type of abonnement (not necessary)
    public Abonnement create(Abonnement object) throws DAOException {
        String query = "INSERT INTO abonnement (formule, dateDabut, prix) VALUES (?, ?, ?)";

        try(PreparedStatement ps = connexion.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, object.getFormule());
            ps.setDouble(2, object.getPrix());
            ps.setString(3, object.getDuree());

            int lignesModifiees = ps.executeUpdate();

            if(lignesModifiees == 0) {
                throw new DAOException("Fail to create the abonnement no addition to the database");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    object.setIdAbonnement(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating abonnement failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Error while creating abonnements", e);
        }
        return object;
    }

    public Abonnement find(int idAbonnement) throws DAOException {
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    public List<Abonnement> findAll() throws DAOException {
        List<Abonnement> abonnements = new ArrayList<>();
        String query = "SELECT * FROM abonnement";
        
        //try-with-ressources to auto-close resources (ps and rs)
        try (PreparedStatement ps = connexion.prepareStatement(query);  
            ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                int idAbonnement = rs.getInt("idAbonnement");
                String formule = rs.getString("formule");
                double prix = rs.getDouble("prix");
                String duree = rs.getString("duree");

                //Create an instance of Abonnement to add it to the list
                Abonnement abonnement = new Abonnement(idAbonnement, formule, prix, duree);
                abonnements.add(abonnement);
            }
        } catch(SQLException e) {
            throw new DAOException("Error while fetching all abonnements", e);
        }
        return abonnements;
    }
    
    //If we change the price for example (not necessary)
    public boolean update(Abonnement unAbonnement) throws DAOException{
        return false;
    }

    //Delete a type of abonnement (not useful in our case)
    public boolean delete(Abonnement object) throws DAOException {
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
}
