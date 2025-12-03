package passerelle;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class Connexion {
    private Connection instanceConnexion = null;

    public Connection getConnexion() {
        if (instanceConnexion == null) {
            String databaseName = "bd_gestbus";
            // Parametres de connexion : url, login, mdp
            // Port mysql avec USBWebserver:3307, xampp: 3306
            String url = "jdbc:mysql://localhost:3306/" + databaseName + "?serverTimezone=UTC";
            String login = "root"; // dans l'idal un login de connexion pour l'application, et non root...
            String password = ""; // mot de passe avec xampp

            // Creation d'une connexion avec MysqlDataSource
            MysqlDataSource mysqlDS = new MysqlDataSource();
            mysqlDS.setURL(url);
            mysqlDS.setUser(login);
            mysqlDS.setPassword(password);

            try {
                instanceConnexion = mysqlDS.getConnection();
            } catch (
                    SQLException e1) {
                System.err.println("Erreur de parcours de connexion");
                e1.printStackTrace();
            }
        }

        return instanceConnexion;
    }
}
