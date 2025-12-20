package passerelle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import metiers.TypeVehicule;

public class TypeVehiculeDAO extends DAO<TypeVehicule> {
    public TypeVehiculeDAO(Connection connexion) {
        super(connexion);
    }

    @Override
    public TypeVehicule create(TypeVehicule typeVehicule) throws DAOException {
        String query = "INSERT INTO typeVehicule (libelle) VALUES (?)";

        try (PreparedStatement ps = connexion.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, typeVehicule.getLibelle());

            int lignesModifiees = ps.executeUpdate();

            if (lignesModifiees == 0) {
                throw new DAOException("Fail to create the typeVehicule no addition to the database");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    typeVehicule.setIdTypeVehicule(generatedKeys.getInt(1));
                } else {
                    throw new DAOException("Creating typeVehicule failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error while creating typeVehicule. Details:");
            e.printStackTrace();
            throw new DAOException("Error while creating the typeVehicule", e);
        }
        return typeVehicule;
    }

    @Override
    public TypeVehicule find(int idTypeVehicule) throws DAOException {
        String query = "SELECT idTypeVehicule, libelle FROM typeVehicule WHERE idTypeVehicule = ?";
        
        try (PreparedStatement ps = connexion.prepareStatement(query)) {

            ps.setInt(1, idTypeVehicule);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    String libelle = rs.getString("libelle");
                    return new TypeVehicule(idTypeVehicule, libelle);
                
                } else {
                    return null; // No typeVehicule found with the given ID
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while searching for the typeVehicule with ID = " + idTypeVehicule + "Details:");
            e.printStackTrace();
            throw new DAOException("Error while searching for the typeVehicule with ID : " + idTypeVehicule, e);
        }
    }

    @Override
    public boolean update(TypeVehicule typeVehicule) throws DAOException {
        String query = "UPDATE typeVehicule SET libelle = ? WHERE idTypeVehicule = ?";

        try (PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setString(1, typeVehicule.getLibelle());
            ps.setInt(2, typeVehicule.getIdTypeVehicule());

            int lignesModifiees = ps.executeUpdate();

            if (lignesModifiees == 0) {
                throw new DAOException("Fail to update the typeVehicule, no modification in the database");
            }

            return true;

        } catch (SQLException e) {
            System.err.println("Error while updating typeVehicule with ID = " + typeVehicule.getIdTypeVehicule() + " Details:");
            e.printStackTrace();
            throw new DAOException("Error while updating the typeVehicule with ID : " + typeVehicule.getIdTypeVehicule(), e);
        }
    }

    @Override
    public boolean delete(TypeVehicule typeVehicule) throws DAOException {
        String query = "DELETE FROM typeVehicule WHERE idTypeVehicule = ?";

        try (PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setInt(1, typeVehicule.getIdTypeVehicule());

            int lignesSupprimees = ps.executeUpdate();

            if (lignesSupprimees == 0) {
                throw new DAOException("Fail to delete the typeVehicule, no modification in the database");
            }

            return true;
        } catch (SQLException e) {
            System.err.println("Error while deleting client with ID = " + typeVehicule.getIdTypeVehicule() + " Details:");
            e.printStackTrace();
            throw new DAOException("Error while deleting the client with ID : " + typeVehicule.getIdTypeVehicule(), e);
        }
    }

    @Override
    public java.util.List<TypeVehicule> findAll() throws DAOException {
        List<TypeVehicule> typeVehicules = new ArrayList<>();
        String query = "SELECT * FROM typeVehicule";

        // try-with-ressources to auto-close resources (ps and rs)
        try (PreparedStatement ps = connexion.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int idTypeVehicule = rs.getInt("idTypeVehicule");
                String libelle = rs.getString("libelle");

                // Create an instance of TypeVehicule to add it to the list
                TypeVehicule typeVehicule = new TypeVehicule(idTypeVehicule, libelle);
                typeVehicules.add(typeVehicule);
            }
        } catch (SQLException e) {
            throw new DAOException("Error while fetching all typeVehicules", e);
        }
        return typeVehicules;
    }

    @Override
    public TypeVehicule find(int id1, int id2) throws DAOException {
        throw new DAOException("Not used");
    }

    @Override
    public TypeVehicule find(int id1, int id2, int id3) throws DAOException {
        throw new DAOException("Not used");
    }
}
