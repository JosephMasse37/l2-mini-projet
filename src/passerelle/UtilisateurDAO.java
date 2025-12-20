package passerelle;

import metiers.Hashing;
import metiers.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO extends DAO<Utilisateur> {

    public UtilisateurDAO(Connection connexion) {
        super(connexion);
    }

    public Utilisateur getUnUtilisateur(String username, String password, boolean hashed) throws DAOException {
        String hashedPassword = "";

        if (!hashed) {
            // Hachage du mot de passe avec SHA-256
            hashedPassword = Hashing.digest(password);
        }

        // Execution de requetes
        String req = "SELECT username, prenom, nom, username, password FROM utilisateur WHERE username = ? AND password = ? ;";

        ResultSet rs = null;
        try {
            try(PreparedStatement ps = connexion.prepareStatement(req)) {
                ps.setString(1, username);
                if (!hashed) {
                    ps.setString(2, hashedPassword);
                } else {
                    ps.setString(2, password);
                }

                rs = ps.executeQuery();

                while (rs.next()) {
                    String nom = rs.getString("nom");
                    String prenom = rs.getString("prenom");
                    String password_ = rs.getString("password");
                    String username_ = rs.getString("username");

                    return new Utilisateur(username_, password_, prenom, nom);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur requête SQL");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Utilisateur find(int id1) throws DAOException {
        throw new DAOException("Non utilisé");
    }

    @Override
    public Utilisateur find(int id1, int id2) throws DAOException {
        throw new DAOException("Non utilisé");
    }

    @Override
    public Utilisateur find(int id1, int id2, int id3) throws DAOException {
        throw new DAOException("Non utilisé");
    }

    @Override
    public List<Utilisateur> findAll() throws DAOException {
        List<Utilisateur> lesUsers = new ArrayList<>();

        // Execution de requetes
        String req = "SELECT username, prenom, nom, username, password FROM utilisateur;";
        ResultSet rs = null;
        try {
            try(PreparedStatement ps = connexion.prepareStatement(req)) {
                rs = ps.executeQuery();

                while (rs.next()) {
                    String nom = rs.getString("nom");
                    String prenom = rs.getString("prenom");
                    String password_ = rs.getString("password");
                    String username_ = rs.getString("username");

                    lesUsers.add(new Utilisateur(username_, password_, prenom, nom));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur requête SQL");
            e.printStackTrace();
        }
        return lesUsers;
    }

    @Override
    public Utilisateur create(Utilisateur utilisateur) throws DAOException {

        String query = "INSERT INTO utilisateur (username, password, prenom, nom) VALUES (?, ?, ?, ?)";

        try(PreparedStatement ps = connexion.prepareStatement(query)) {

            ps.setString(1, utilisateur.getUsername());
            ps.setString(2, utilisateur.getPassword());
            ps.setString(3, utilisateur.getPrenom());
            ps.setString(4, utilisateur.getNom());

            int lignesModifiees = ps.executeUpdate();

            if(lignesModifiees == 0) {
                throw new DAOException("Fail to create the Utilisateur no addition to the database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Error while creating Utilisateur", e);
        }
        return utilisateur;
    }

    @Override
    public boolean update(Utilisateur utilisateur) throws DAOException {
        String query = "UPDATE utilisateur SET password = ?, prenom = ?, nom = ? WHERE username = ?";

        try (PreparedStatement ps = connexion.prepareStatement(query)) {

            ps.setString(1, utilisateur.getPassword());
            ps.setString(2, utilisateur.getPrenom());
            ps.setString(3, utilisateur.getNom());
            ps.setString(4, utilisateur.getUsername());

            int lignesModifiees = ps.executeUpdate();

            return lignesModifiees > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Erreur lors de la mise à jour de l'utilisateur avec ID : " + utilisateur.getUsername(), e);
        }
    }

    @Override
    public boolean delete(Utilisateur utilisateur) throws DAOException {
        String query = "DELETE FROM utilisateur WHERE username = ?";

        try (PreparedStatement ps = connexion.prepareStatement(query)) {

            ps.setString(1, utilisateur.getUsername());

            int lignesModifiees = ps.executeUpdate();

            return lignesModifiees > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Erreur lors de la suppression de l'utilisateur avec ID : " + utilisateur.getUsername(), e);
        }
    }
}
