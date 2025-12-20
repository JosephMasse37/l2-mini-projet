package passerelle;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class Connexion {
    private static Connection instanceConnexion = null;

    private Connexion() {}

    public static Connection getConnexion() throws DAOException {
        try {
            if (instanceConnexion == null || instanceConnexion.isClosed()) {
                String databaseName = "bd_gestbus";
                // Parametres de connexion : url, login, mdp
                // Port mysql avec USBWebserver:3307, xampp: 3306
                String url = "jdbc:mysql://localhost:3306/" + databaseName + "?serverTimezone=UTC";
                String login = "root"; // dans l'idéal un login de connexion pour l'application, et non root...
                String password = ""; // mot de passe avec xampp

                // Creation d'une connexion avec MysqlDataSource
                MysqlDataSource mysqlDS = new MysqlDataSource();
                mysqlDS.setURL(url);
                mysqlDS.setUser(login);
                mysqlDS.setPassword(password);

                instanceConnexion = mysqlDS.getConnection();
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur d'ouverture de la connexion", e);
        }
        return instanceConnexion;
    }

    public static void close() throws DAOException {
        if (instanceConnexion != null) {
            try {
                if (!instanceConnexion.isClosed()) {
                    instanceConnexion.close();
                }
            } catch (SQLException e) {
                throw new DAOException("Erreur lors de la fermeture de la connexion", e);
            } finally {
                instanceConnexion = null;
            }
        }
    }
}
