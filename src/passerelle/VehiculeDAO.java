package passerelle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import metiers.TypeVehicule;
import metiers.Vehicule;

public class VehiculeDAO extends DAO<Vehicule> {

    public VehiculeDAO(Connection connexion) {
        super(connexion);
    }

    @Override
    public Vehicule create(Vehicule vehicule) throws DAOException {
        String query = "INSERT INTO vehicule (numVehicule, marque, modele, dateFabrication, dateMiseEnService, dateHeureDerniereMaintenance, idTypeVehicule) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connexion.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, vehicule.getNumVehicule());
            ps.setString(2, vehicule.getMarque());
            ps.setString(3, vehicule.getModele());
            ps.setDate(4, java.sql.Date.valueOf(vehicule.getDateFabrication()));
            ps.setDate(5, java.sql.Date.valueOf(vehicule.getDateMiseEnService()));
            ps.setDate(6, java.sql.Date.valueOf(vehicule.getDateHeureDerniereMaintenance()));
            ps.setInt(7, vehicule.getTypevehicule().getIdTypeVehicule());

            int lignesModifiees = ps.executeUpdate();

            if (lignesModifiees == 0) {
                throw new DAOException("Fail to create the vehicule no addition to the database");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    vehicule.setNumVehicule(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating vehicule failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error while creating vehicule. Details:");
            e.printStackTrace();
            throw new DAOException("Error while creating the vehicule", e);
        }
        return vehicule;
    }

    @Override
    public Vehicule find(int idVehicule) throws DAOException {
        String query = "SELECT numVehicule, marque, modele, dateFabrication, dateMiseEnService, dateHeureDerniereMaintenance, idTypeVehicule FROM vehicule WHERE numVehicule = ?";
        
        try (PreparedStatement ps = connexion.prepareStatement(query)) {
            
            ps.setInt(1, idVehicule);

            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    TypeVehiculeDAO typeVehiculeDAO = new TypeVehiculeDAO(connexion);
                   
                    int numVehicule = rs.getInt("numVehicule");
                    String marque = rs.getString("marque");
                    String modele = rs.getString("modele");
                    LocalDate dateFabrication = rs.getDate("dateFabrication").toLocalDate();
                    LocalDate dateMiseEnService = rs.getDate("dateMiseEnService").toLocalDate();
                    LocalDate dateHeureDerniereMaintenance = rs.getDate("dateHeureDerniereMaintenance").toLocalDate();
                    TypeVehicule typeVehicule = typeVehiculeDAO.find(rs.getInt("idTypeVehicule"));

                    return new Vehicule(numVehicule, marque, modele, dateFabrication, dateMiseEnService, dateHeureDerniereMaintenance, typeVehicule);
                } else {
                    return null; // No vehicule found with the given ID
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while searching for the vehicule with ID = " + idVehicule + "Details:");
            e.printStackTrace();
            throw new DAOException("Error while searching for the vehicule with ID : " + idVehicule, e);
        }
    }

    @Override
    public boolean update(Vehicule vehicule) throws DAOException {
        String query = "UPDATE vehicule SET marque = ?, modele = ?, dateFabrication = ?, dateMiseEnService = ?, dateHeureDerniereMaintenance = ?, idTypeVehicule = ? WHERE numVehicule = ?";

        try (PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setString(1, vehicule.getMarque());
            ps.setString(2, vehicule.getModele());
            ps.setDate(3, java.sql.Date.valueOf(vehicule.getDateFabrication()));
            ps.setDate(4, java.sql.Date.valueOf(vehicule.getDateMiseEnService()));
            ps.setDate(5, java.sql.Date.valueOf(vehicule.getDateHeureDerniereMaintenance()));
            ps.setInt(6, vehicule.getTypevehicule().getIdTypeVehicule());
            ps.setInt(7, vehicule.getNumVehicule());

            int lignesModifiees = ps.executeUpdate();

            if (lignesModifiees == 0) {
                throw new DAOException("Fail to update the vehicule, no modification in the database");
            }

            return true;
        } catch (SQLException e) {
            System.err.println("Error while updating vehicule with ID = " + vehicule.getNumVehicule() + " Details:");
            e.printStackTrace();
            throw new DAOException("Error while updating the vehicule with ID : " + vehicule.getNumVehicule(), e);
        }
    }

    @Override
    public boolean delete(Vehicule vehicule) throws DAOException {
        String query = "DELETE FROM vehicule WHERE numVehicule = ?";

        try (PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setInt(1, vehicule.getNumVehicule());

            int lignesSupprimees = ps.executeUpdate();

            if (lignesSupprimees == 0) {
                throw new DAOException("Fail to delete the vehicule, no modification in the database");
            }

            return true;
        } catch (SQLException e) {
            System.err.println("Error while deleting vehicule with ID = " + vehicule.getNumVehicule() + " Details:");
            e.printStackTrace();
            throw new DAOException("Error while deleting the vehicule with ID : " + vehicule.getNumVehicule(), e);
        }
    }

    @Override
    public List<Vehicule> findAll() throws DAOException {
        List<Vehicule> vehicules = new ArrayList<>();
        String query = "SELECT * FROM vehicule";

        // try-with-ressources to auto-close resources (ps and rs)
        try (PreparedStatement ps = connexion.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TypeVehiculeDAO typeVehiculeDAO = new TypeVehiculeDAO(connexion);

                int numVehicule = rs.getInt("numVehicule");
                String marque = rs.getString("marque");
                String modele = rs.getString("modele");
                LocalDate dateFabrication = rs.getDate("dateFabrication").toLocalDate();
                LocalDate dateMiseEnService = rs.getDate("dateMiseEnService").toLocalDate();
                LocalDate dateHeureDerniereMaintenance = rs.getDate("dateHeureDerniereMaintenance").toLocalDate();
                TypeVehicule idTypeVehicule = typeVehiculeDAO.find(rs.getInt("idTypeVehicule"));

                
                // Create an instance of Vehicule to add it to the list
                Vehicule vehicule = new Vehicule(numVehicule, marque, modele, dateFabrication, dateMiseEnService, dateHeureDerniereMaintenance, idTypeVehicule);
                vehicules.add(vehicule);
            }
        } catch (SQLException e) {
            throw new DAOException("Error while fetching all vehicules", e);
        }
        return vehicules;
    }
    
    @Override
    public Vehicule find(int id1, int id2) throws DAOException {
        throw new DAOException("Not used");
    }

    @Override
    public Vehicule find(int id1, int id2, int id3) throws DAOException {
        throw new DAOException("Not used");
    }
}
