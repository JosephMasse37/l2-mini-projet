
package passerelle;
import java.sql.Connection;
import java.util.List;

public abstract class DAO<T> {
    protected Connection connexion;

    public DAO(Connection connexion) {
        this.connexion = connexion;
    }

    //Create a new object in the database and return it
    public abstract T create(T object) throws DAOException;

    //Find an object by its ID in the database and return it
    public abstract T find(int id) throws DAOException;

    //Find utilisé par DessertDAO
    public abstract T find(int id1, int id2) throws DAOException;

    //Find utilisé par ConduitSurDAO
    public abstract T find(int id1, int id2, int id3) throws DAOException;

    //Update an existing object in the database
    public abstract boolean update(T object) throws DAOException;

    //Delete an object from the database
    public abstract boolean delete(T object) throws DAOException;

    //Find all objects in the database
    public abstract List<T> findAll() throws DAOException;

}
