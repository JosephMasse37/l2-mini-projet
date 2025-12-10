package passerelle;

import java.sql.Connection;
import java.util.List;

/*Generic abstract DAO class for managing data access.

JDBC Resource Management
For every JDBC operation, always use the try-with-resources pattern to
ensure the automatic closing of PreparedStatement and ResultSet resources:

try (PreparedStatement ps = connexion.prepareStatement(sql)) {
    ps.setInt(1, id);
    try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
            // traiter le résultat
        }
    }
  } catch (SQLException e) {
      throw new DAOException("Message d'erreur", e);
  }
}*/

public abstract class DAO<T> {
    protected Connection connexion;

    public DAO(Connection connexion) {
        this.connexion = connexion;
    }

    //Create a new object in the database and return it
    public abstract T create(T object) throws DAOException;

    //Find an object by its ID in the database and return it
    public abstract T find(int id) throws DAOException;

    //Update an existing object in the database
    public abstract boolean update(T object) throws DAOException;

    //Delete an object from the database
    public abstract boolean delete(T object) throws DAOException;

    //Find all objects in the database
    public abstract List<T> findAll() throws DAOException;

}
