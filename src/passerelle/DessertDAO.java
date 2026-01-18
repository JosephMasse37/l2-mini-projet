package passerelle;

import metiers.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DessertDAO extends DAO<Dessert> {

    public DessertDAO(Connection connexion) {
        super(connexion);
    }

    public List<Arret> getDessertesUneLigne(int idLigne) throws DAOException {
        List<Arret> listesArretsDesservis = new ArrayList<>();

        // Execution de requetes
        String req = "SELECT idLigne, idArret, ordre FROM dessert WHERE idLigne = ? ORDER BY ordre";

        ResultSet rs = null;
        try {
            try(PreparedStatement ps = connexion.prepareStatement(req)) {
                ps.setInt(1, idLigne);

                rs = ps.executeQuery();

                while (rs.next()) {
                    int idArret = rs.getInt("idArret");

                    // Récupération des DAOs
                    ArretDAO arretDAO = new ArretDAO(connexion);

                    listesArretsDesservis.add(arretDAO.find(idArret));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur requête SQL");
            e.printStackTrace();
        }

        return listesArretsDesservis;
    }

    public List<Ligne> getDessertesUnArret(int idArret) throws DAOException {
        List<Ligne> listesLignesDesservies = new ArrayList<>();

        // Execution de requetes
        String req = "SELECT idLigne, idArret FROM dessert WHERE idArret = ?";

        ResultSet rs = null;
        try {
            try(PreparedStatement ps = connexion.prepareStatement(req)) {
                ps.setInt(1, idArret);

                rs = ps.executeQuery();

                while (rs.next()) {
                    int idLigne = rs.getInt("idLigne");

                    // Récupération des DAOs
                    LigneDAO ligneDAO = new LigneDAO(connexion);

                    listesLignesDesservies.add(ligneDAO.find(idLigne));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur requête SQL");
            e.printStackTrace();
        }

        return listesLignesDesservies;
    }

    @Override
    public Dessert find(int id1) throws DAOException {
        throw new DAOException("Non utilisé");
    }

    @Override
    public Dessert find(int idArret, int idLigne) throws DAOException {
        // Execution de requetes
        String req = "SELECT idArret, idLigne, ordre FROM dessert WHERE idArret = ? AND idLigne = ?";

        ResultSet rs = null;
        try {
            try(PreparedStatement ps = connexion.prepareStatement(req)) {
                ps.setInt(1, idArret);
                ps.setInt(2, idLigne);

                rs = ps.executeQuery();

                while (rs.next()) {
                    int ordre = rs.getInt("ordre");

                    // Récupération des DAOs
                    LigneDAO ligneDAO = new LigneDAO(connexion);
                    ArretDAO arretDAO = new ArretDAO(connexion);

                    Arret lArret = arretDAO.find(idArret);
                    Ligne laLigne = ligneDAO.find(idLigne);

                    return new Dessert(lArret, laLigne, ordre);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur requête SQL");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Dessert find(int id1, int id2, int id3) throws DAOException {
        throw new DAOException("Non utilisé");
    }

    @Override
    public List<Dessert> findAll() throws DAOException {
        List<Dessert> lesDessertes = new ArrayList<>();

        // Execution de requetes
        String req = "SELECT idArret, idLigne, ordre FROM dessert ORDER BY idLigne, ordre;";
        ResultSet rs = null;
        try {
            try(PreparedStatement ps = connexion.prepareStatement(req)) {
                rs = ps.executeQuery();

                while (rs.next()) {
                    int idArret = rs.getInt("idArret");
                    int idLigne = rs.getInt("idLigne");
                    int ordre = rs.getInt("ordre");

                    // Récupération des DAOs
                    LigneDAO ligneDAO = new LigneDAO(connexion);
                    ArretDAO arretDAO = new ArretDAO(connexion);

                    Arret lArret = arretDAO.find(idArret);
                    Ligne laLigne = ligneDAO.find(idLigne);

                    lesDessertes.add(new Dessert(lArret, laLigne, ordre));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur requête SQL");
            e.printStackTrace();
        }
        return lesDessertes;
    }

    @Override
    public Dessert create(Dessert dessert) throws DAOException {
        String query = "INSERT INTO dessert (idArret, idLigne) VALUES (?, ?)";

        try(PreparedStatement ps = connexion.prepareStatement(query)) {

            ps.setInt(1, dessert.getUnArret().getIdArret());
            ps.setInt(2, dessert.getUneLigne().getIdLigne());

            int lignesModifiees = ps.executeUpdate();

            if(lignesModifiees == 0) {
                throw new DAOException("Fail to create the Dessert no addition to the database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Error while creating Dessert", e);
        }
        return dessert;
    }

    @Override
    public boolean update(Dessert dessert) throws DAOException {
        // Non utilisable => Pas de UPDATE sur une clé primaire
        throw new DAOException("Non utilisé");
    }

    @Override
    public boolean delete(Dessert dessert) throws DAOException {
        String query = "DELETE FROM dessert WHERE idArret = ? AND idLigne = ?";

        try (PreparedStatement ps = connexion.prepareStatement(query)) {

            ps.setInt(1, dessert.getUnArret().getIdArret());
            ps.setInt(2, dessert.getUneLigne().getIdLigne());

            int lignesModifiees = ps.executeUpdate();

            return lignesModifiees > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Erreur lors de la suppression de la desserte avec l'ID Arrêt : " + dessert.getUnArret().getIdArret() +
                    "\net l'ID Ligne : " + dessert.getUneLigne().getIdLigne(), e);
        }
    }
}
