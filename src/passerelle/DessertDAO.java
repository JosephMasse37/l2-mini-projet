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
        Statement st = null;
        ResultSet rs = null;
        try {
            st = connexion.createStatement();
            String req = "SELECT idLigne, idArret FROM dessert WHERE idLigne = " + idLigne + ";";
            rs = st.executeQuery(req);

            while (rs.next()) {
                int idArret = rs.getInt("idArret");

                System.out.println("idLigne : " + idLigne + " | idArret : " + idArret);

                // Récupération des autres objets pour construire l'objet
                ArretDAO arretDAO = new ArretDAO(connexion);
                Arret lArret = arretDAO.find(idArret);

                listesArretsDesservis.add(lArret);
            }

            return listesArretsDesservis;

        } catch (SQLException e) {
            System.err.println("Erreur requête SQL");
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public List<Ligne> getDessertesUnArret(int idArret) throws DAOException {
        List<Ligne> listesLignesDesservies = new ArrayList<>();

        // Execution de requetes
        Statement st = null;
        ResultSet rs = null;
        try {
            st = connexion.createStatement();
            String req = "SELECT idLigne, idArret FROM dessert WHERE idArret = " + idArret + ";";
            rs = st.executeQuery(req);

            while (rs.next()) {
                int idLigne = rs.getInt("idLigne");

                System.out.println("idLigne : " + idLigne + " | idArret : " + idArret);

                // Récupération des autres objets pour construire l'objet
                LigneDAO ligneDAO = new LigneDAO(connexion);
                Ligne laLigne = ligneDAO.find(idLigne);

                listesLignesDesservies.add(laLigne);
            }

            return listesLignesDesservies;

        } catch (SQLException e) {
            System.err.println("Erreur requête SQL");
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public Dessert find(int id1) throws DAOException {
        throw new DAOException("Non utilisé");
    }

    @Override
    public Dessert find(int idArret, int idLigne) throws DAOException {
        // Execution de requetes
        String req = "SELECT idArret, idLigne FROM dessert WHERE idArret = ? AND idLigne = ? ;";

        ResultSet rs = null;
        try {
            try(PreparedStatement ps = connexion.prepareStatement(req)) {
                ps.setInt(1, idArret);
                ps.setInt(2, idLigne);

                rs = ps.executeQuery();

                while (rs.next()) {
                    int idArret_ = rs.getInt("idArret");
                    int idLigne_ = rs.getInt("idLigne");

                    // Récupération des DAOs
                    LigneDAO ligneDAO = new LigneDAO(connexion);
                    ArretDAO arretDAO = new ArretDAO(connexion);

                    Arret lArret = arretDAO.find(idArret_);
                    Ligne laLigne = ligneDAO.find(idLigne_);
                    return new Dessert(lArret, laLigne);
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
        String req = "SELECT idArret, idLigne FROM dessert;";
        ResultSet rs = null;
        try {
            try(PreparedStatement ps = connexion.prepareStatement(req)) {
                rs = ps.executeQuery();

                while (rs.next()) {
                    int idArret = rs.getInt("idArret");
                    int idLigne = rs.getInt("idLigne");

                    // Récupération des DAOs
                    LigneDAO ligneDAO = new LigneDAO(connexion);
                    ArretDAO arretDAO = new ArretDAO(connexion);

                    Arret lArret = arretDAO.find(idArret);
                    Ligne laLigne = ligneDAO.find(idLigne);

                    lesDessertes.add(new Dessert(lArret, laLigne));
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
