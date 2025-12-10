package passerelle;

import metiers.Hashing;
import metiers.Utilisateur;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UtilisateurDAO {
    public static Utilisateur getUnUtilisateur(String username, String password) {
        Connection cn = Connexion.getConnexion();

        // Hachage du mot de passe avec SHA-256
        String hashedPassword = Hashing.digest(password);

        // Execution de requetes
        Statement st = null;
        ResultSet rs = null;
        try {
            st = cn.createStatement();
            String req = "SELECT username, prenom, nom, username, password FROM utilisateur WHERE username = '" + username + "' AND password = '" + hashedPassword + "';";
            rs = st.executeQuery(req);

            while (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String password_ = rs.getString("password");
                String username_ = rs.getString("username");
                System.out.println("Client : " + nom + " " + prenom + " MDP : " + password_ + " USERNAME : " + username_);

                return new Utilisateur(username_, password_, prenom, nom);
            }

        } catch (SQLException e) {
            System.err.println("Erreur requête SQL");
            e.printStackTrace();
        }

        return null;
    }
}
