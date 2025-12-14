package passerelle;

import java.sql.Connection;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import metiers.Arret;

//CRUD

public class ArretDAO extends DAO<Arret> {

    public ArretDAO(Connection connexion) {
        super(connexion);
    }

    // creer nv arret
    public Arret create(Arret object) throws DAOException {
        String query = "INSERT INTO arret (nom,latitude,longitude) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connexion.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            // ^^ autoincr
            ps.setString(1, object.getNom());
            ps.setDouble(2, object.getLatitude());
            ps.setDouble(3, object.getLongitude());

            int lignesModifiees = ps.executeUpdate();

            if (lignesModifiees == 0) {
                throw new DAOException("Échec de la création de l'arrêt.");
            }
            // bloc ci dessous pas executé si if vrai
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                //si id a bien été crée
                if (generatedKeys.next()) {
                    object.setIdArret(generatedKeys.getInt(1)); //1 = le num de la colonne, car la seule colonne ds ce ResultSet)
                } else {
                    throw new DAOException("Création de l'arrêt a échoué, aucun identifiant n'a pu être récupéré.");
                }
            }

        } catch (SQLException e) {
            System.err.println("ERREUR BDD lors de la création d'un arrêt. Détails:");
            e.printStackTrace();
            throw new DAOException("Erreur lors de la création de votre Arret", e);
        }
        return object;
    }

    public Arret find(int idArret) throws DAOException {

        String query = "SELECT idArret, nom, latitude, longitude FROM arret WHERE idArret = ?";
        try (PreparedStatement ps = connexion.prepareStatement(query)) {

            ps.setInt(1, idArret);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    String nom = rs.getString("nom");
                    double latitude = rs.getDouble("latitude");
                    double longitude = rs.getDouble("longitude");

                    Arret arret = new Arret(idArret, nom, latitude, longitude);
                    return arret;

                } else {
                    return null; // aucun arret trouvé
                }
            }

        } catch (SQLException e) {
            System.err.println("ERREUR BDD lors de la recherche de l'arrêt ID=" + idArret + ". Détails:");
            e.printStackTrace();
            throw new DAOException("Erreur lors de la recherche de l'arrêt avec ID : " + idArret, e);
        }
    }

    public List<Arret> findAll() throws DAOException {

        List<Arret> arrets = new ArrayList<>();
        String query = "SELECT idArret, nom, latitude, longitude FROM arret";

        try (PreparedStatement ps = connexion.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                int idArret = rs.getInt("idArret");
                String nom = rs.getString("nom");
                double latitude = rs.getDouble("latitude");
                double longitude = rs.getDouble("longitude");

                // Création instance d'Arret
                Arret arret = new Arret(idArret, nom, latitude, longitude);
                arrets.add(arret);
            }

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la récupération de tous les arrêts", e);
        }
        return arrets;
    }

    public boolean update(Arret unArret) throws DAOException {

        String query = "UPDATE arret SET nom = ?, latitude = ?, longitude = ? WHERE idArret = ?";

        try (PreparedStatement ps = connexion.prepareStatement(query)) {

            ps.setString(1, unArret.getNom());
            ps.setDouble(2, unArret.getLatitude());
            ps.setDouble(3, unArret.getLongitude());
            ps.setInt(4, unArret.getIdArret());

            int lignesModifiees = ps.executeUpdate();

            return lignesModifiees > 0;

        } catch (SQLException e) {
            System.err.println("ERREUR BDD lors de la mise à jour de l'arrêt ID=" + unArret.getIdArret() + ". Détails:");
            e.printStackTrace();
            throw new DAOException("Erreur lors de la mise à jour de l'arrêt avec ID : " + unArret.getIdArret(), e);
        }
    }

    public boolean delete(Arret object) throws DAOException {

        String query = "DELETE FROM arret WHERE idArret = ?";

        try (PreparedStatement ps = connexion.prepareStatement(query)) {

            ps.setInt(1, object.getIdArret());

            int lignesModifiees = ps.executeUpdate();

            return lignesModifiees > 0;

        } catch (SQLException e) {
            System.err.println("ERREUR BDD lors de la suppression de l'arrêt ID=" + object.getIdArret() + ". Détails:");
            e.printStackTrace();
            throw new DAOException("Erreur lors de la suppression de l'arrêt avec ID : " + object.getIdArret(), e);
        }
    }

    public List<Arret> findByDebutNom(String nomRecherche) throws DAOException {

        List<Arret> arretsTrouves = new ArrayList<>();

        String query = "SELECT idArret, nom, latitude, longitude FROM arret WHERE nom LIKE ?";

        try (PreparedStatement ps = connexion.prepareStatement(query)) {

            ps.setString(1, nomRecherche + "%");

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    int idArret = rs.getInt("idArret");
                    String nom = rs.getString("nom");
                    double latitude = rs.getDouble("latitude");
                    double longitude = rs.getDouble("longitude");

                    Arret arret = new Arret(idArret, nom, latitude, longitude);
                    arretsTrouves.add(arret);
                }
            }

        } catch (SQLException e) {
            System.err.println("ERREUR BDD lors de la recherche par nom (commence par). Détails:");
            e.printStackTrace();
            throw new DAOException("Erreur lors de la recherche des arrêts par nom: " + nomRecherche, e);
        }

        return arretsTrouves;
    }
}

