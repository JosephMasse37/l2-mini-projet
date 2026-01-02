package passerelle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import metiers.Utilisateur;

import metiers.Chauffeur;

public class ChauffeurDAO extends DAO<Chauffeur> {

    public ChauffeurDAO(Connection connexion) {
        super(connexion);
    }

    @Override
    public Chauffeur create(Chauffeur unChauffeur) throws DAOException {
        String query = "INSERT INTO Chauffeur (formation_tram, username) VALUES (?, ?)";

        try (PreparedStatement ps = connexion.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setBoolean(1, unChauffeur.isFormation_tram());
            ps.setString(2, unChauffeur.getUtilisateur().getUsername());

            int lignesModifiees = ps.executeUpdate();
            if (lignesModifiees == 0) throw new DAOException("Échec création chauffeur.");

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) { //verif de l'autoincr cree
                    unChauffeur.setIdChauffeur(generatedKeys.getInt(1)); //1 = le num de la colonne, car la seule colonne ds ce ResultSet)
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la création du chauffeur", e);
        }
        return unChauffeur;
    }

    @Override
    public Chauffeur find(int idChauffeur) throws DAOException {
        String query = "SELECT idChauffeur, formation_tram, username FROM Chauffeur WHERE idChauffeur = ?";

        try (PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setInt(1, idChauffeur);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // instancie le DAO Utilisateur pour récup l'objet Utilisateur
                    UtilisateurDAO userDAO = new UtilisateurDAO(connexion);

                    int id = rs.getInt("idChauffeur");
                    boolean formation = rs.getBoolean("formation_tram");
                    Utilisateur user = userDAO.find(rs.getString("username"));

                    return new Chauffeur(id, formation, user);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur recherche chauffeur", e);
        }
        return null;
    }

    @Override
    public List<Chauffeur> findAll() throws DAOException {
        List<Chauffeur> chauffeurs = new ArrayList<>();
        String query = "SELECT * FROM Chauffeur";

        try (PreparedStatement ps = connexion.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            UtilisateurDAO userDAO = new UtilisateurDAO(connexion);

            while (rs.next()) {
                chauffeurs.add(new Chauffeur(
                        rs.getInt("idChauffeur"),
                        rs.getBoolean("formation_tram"),
                        userDAO.find(rs.getString("username"))
                ));
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur récupération de tous les chauffeurs", e);
        }
        return chauffeurs;
    }



    @Override
    public boolean update(Chauffeur unChauffeur) throws DAOException {
        String query = "UPDATE Chauffeur SET formation_tram = ?, username = ? WHERE idChauffeur = ?";

        try (PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setBoolean(1, unChauffeur.isFormation_tram());
            ps.setString(2, unChauffeur.getUtilisateur().getUsername());
            ps.setInt(3, unChauffeur.getIdChauffeur());

            int lignesModifiees = ps.executeUpdate();
            return lignesModifiees > 0;

        } catch (SQLException e) {
            throw new DAOException("Erreur update chauffeur", e);
        }
    }

    @Override
    public boolean delete(Chauffeur unChauffeur) throws DAOException {
        String query = "DELETE FROM Chauffeur WHERE idChauffeur = ?";
        try (PreparedStatement ps = connexion.prepareStatement(query)) {

            ps.setInt(1, unChauffeur.getIdChauffeur());

            int lignesModifiees = ps.executeUpdate();
            return lignesModifiees > 0;

        } catch (SQLException e) {
            throw new DAOException("Erreur suppression chauffeur", e);
        }
    }

    public List<Chauffeur> findAyantFormationTram(boolean formation) throws DAOException {
        List<Chauffeur> liste = new ArrayList<>();
        String query = "SELECT * FROM Chauffeur WHERE formation_tram = ?";

        try (PreparedStatement ps = connexion.prepareStatement(query)) {
            ps.setBoolean(1, formation);
            try (ResultSet rs = ps.executeQuery()) {
                UtilisateurDAO userDAO = new UtilisateurDAO(connexion);
                while (rs.next()) {
                    liste.add(new Chauffeur(
                            rs.getInt("idChauffeur"),
                            rs.getBoolean("formation_tram"),
                            userDAO.find(rs.getString("utilisateur"))
                    ));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la recherche par formation", e);
        }
        return liste;
    }

    @Override
    public Chauffeur find(int id1, int id2) throws DAOException {
        throw new DAOException("Non utilisé");
    }

    @Override
    public Chauffeur find(int id1, int id2, int id3) throws DAOException {
        throw new DAOException("Non utilisé");
    }
}