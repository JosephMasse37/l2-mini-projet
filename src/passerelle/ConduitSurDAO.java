package passerelle;

import metiers.Chauffeur;
import metiers.ConduitSur;
import metiers.Ligne;
import metiers.Vehicule;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ConduitSurDAO extends DAO<ConduitSur> {

    public ConduitSurDAO(Connection connexion) {
        super(connexion);
    }

    public List<ConduitSur> getConduiteUnVehicule(int numVehicule) throws DAOException {

        List<ConduitSur> listeConduites = new ArrayList<>();

        // Execution de requetes
        Statement st = null;
        ResultSet rs = null;
        try {
            st = connexion.createStatement();
            String req = "SELECT idLigne, idChauffeur, numVehicule, dateHeureConduite, nbValidation FROM conduitsur WHERE numVehicule = " + numVehicule + ";";
            rs = st.executeQuery(req);

            while (rs.next()) {
                int idLigne = rs.getInt("idLigne");
                int idChauffeur = rs.getInt("idChauffeur");
                String dateHeureConduiteString = rs.getString("dateHeureConduite");
                LocalDateTime dateHeureConduite = LocalDateTime.parse(dateHeureConduiteString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                // Récupération des DAOs
                LigneDAO ligneDAO = new LigneDAO(connexion);
                ChauffeurDAO chauffeurDAO = new ChauffeurDAO(connexion);
                VehiculeDAO vehiculeDAO = new VehiculeDAO(connexion);

                // Récupération des autres objets pour construire l'objet
                Ligne laLigne = ligneDAO.find(idLigne);
                Chauffeur leChauffeur = chauffeurDAO.find(idChauffeur);
                Vehicule unVehicule = vehiculeDAO.find(numVehicule);

                listeConduites.add(new ConduitSur(leChauffeur, laLigne, unVehicule, dateHeureConduite));
            }
        } catch (SQLException e) {
            System.err.println("Erreur requête SQL");
            e.printStackTrace();
        }

        return listeConduites;
    }

    public List<ConduitSur> getConduiteUnChauffeur(int idChauffeur) throws DAOException {
        List<ConduitSur> listeConduites = new ArrayList<>();

        // Execution de requetes
        Statement st = null;
        ResultSet rs = null;
        try {
            st = connexion.createStatement();
            String req = "SELECT idLigne, idChauffeur, numVehicule, dateHeureConduite, nbValidation FROM conduitsur WHERE idChauffeur = " + idChauffeur + ";";
            rs = st.executeQuery(req);

            while (rs.next()) {
                int idLigne = rs.getInt("idLigne");
                int numVehicule = rs.getInt("numVehicule");
                String dateHeureConduiteString = rs.getString("dateHeureConduite");
                int nbValidation = rs.getInt("nbValidation");
                LocalDateTime dateHeureConduite = LocalDateTime.parse(dateHeureConduiteString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                System.out.println("numVehicule : " + numVehicule + " | IdLigne : " + idLigne + " | idChauffeur : " + idChauffeur +
                        " | dateHeureConduite : " + dateHeureConduite);

                // Récupération des DAOs
                LigneDAO ligneDAO = new LigneDAO(connexion);
                ChauffeurDAO chauffeurDAO = new ChauffeurDAO(connexion);
                VehiculeDAO vehiculeDAO = new VehiculeDAO(connexion);

                // Récupération des autres objets pour construire l'objet
                Ligne laLigne = ligneDAO.find(idLigne);
                Chauffeur leChauffeur = chauffeurDAO.find(idChauffeur);
                Vehicule unVehicule = vehiculeDAO.find(numVehicule);

                listeConduites.add(new ConduitSur(leChauffeur, laLigne, unVehicule, dateHeureConduite, nbValidation));
            }
        } catch (SQLException e) {
            System.err.println("Erreur requête SQL");
            e.printStackTrace();
        }

        return listeConduites;
    }

    public List<ConduitSur> getConduiteUneLigne(int idLigne) throws DAOException {
        List<ConduitSur> listeConduites = new ArrayList<>();

        // Execution de requetes
        Statement st = null;
        ResultSet rs = null;
        try {
            st = connexion.createStatement();
            String req = "SELECT idLigne, idChauffeur, numVehicule, dateHeureConduite, nbValidation FROM conduitsur WHERE idLigne = " + idLigne + ";";
            rs = st.executeQuery(req);

            while (rs.next()) {
                int numVehicule = rs.getInt("numVehicule");
                int idChauffeur = rs.getInt("idChauffeur");
                int nbValidation = rs.getInt("nbValidation");
                String dateHeureConduiteString = rs.getString("dateHeureConduite");
                LocalDateTime dateHeureConduite = LocalDateTime.parse(dateHeureConduiteString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                System.out.println("numVehicule : " + numVehicule + " | IdLigne : " + idLigne + " | idChauffeur : " + idChauffeur +
                        " | dateHeureConduite : " + dateHeureConduite);

                // Récupération des DAOs
                LigneDAO ligneDAO = new LigneDAO(connexion);
                ChauffeurDAO chauffeurDAO = new ChauffeurDAO(connexion);
                VehiculeDAO vehiculeDAO = new VehiculeDAO(connexion);

                // Récupération des autres objets pour construire l'objet
                Ligne laLigne = ligneDAO.find(idLigne);
                Chauffeur leChauffeur = chauffeurDAO.find(idChauffeur);
                Vehicule unVehicule = vehiculeDAO.find(numVehicule);

                listeConduites.add(new ConduitSur(leChauffeur, laLigne, unVehicule, dateHeureConduite, nbValidation));
            }
        } catch (SQLException e) {
            System.err.println("Erreur requête SQL");
            e.printStackTrace();
        }

        return listeConduites;
    }

    public List<ConduitSur> getConduitesUnChauffeurUneLigneUnVehicule(int idChauffeur, int idLigne, int numVehicule) throws DAOException {
        List<ConduitSur> listeConduites = new ArrayList<>();

        // Execution de requetes
        Statement st = null;
        ResultSet rs = null;
        try {
            st = connexion.createStatement();
            String req = "SELECT idLigne, idChauffeur, numVehicule, dateHeureConduite, nbValidation FROM conduitsur " +
                    "WHERE idChauffeur = ?" + idChauffeur + " AND idLigne = " + idLigne + " AND numVehicule = " + numVehicule + ";";
            rs = st.executeQuery(req);

            while (rs.next()) {
                int nbValidation = rs.getInt("nbValidation");
                String dateHeureConduiteString = rs.getString("dateHeureConduite");
                LocalDateTime dateHeureConduite = LocalDateTime.parse(dateHeureConduiteString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                System.out.println("numVehicule : " + numVehicule + " | IdLigne : " + idLigne + " | idChauffeur : " + idChauffeur +
                        " | dateHeureConduite : " + dateHeureConduite);

                // Récupération des DAOs
                LigneDAO ligneDAO = new LigneDAO(connexion);
                ChauffeurDAO chauffeurDAO = new ChauffeurDAO(connexion);
                VehiculeDAO vehiculeDAO = new VehiculeDAO(connexion);

                // Récupération des autres objets pour construire l'objet
                Ligne laLigne = ligneDAO.find(idLigne);
                Chauffeur leChauffeur = chauffeurDAO.find(idChauffeur);
                Vehicule unVehicule = vehiculeDAO.find(numVehicule);

                listeConduites.add(new ConduitSur(leChauffeur, laLigne, unVehicule, dateHeureConduite, nbValidation));
            }
        } catch (SQLException e) {
            System.err.println("Erreur requête SQL");
            e.printStackTrace();
        }

        return listeConduites;
    }

    @Override
    public ConduitSur find(int id) throws DAOException {
        throw new DAOException("Non utilisé");
    }

    @Override
    public ConduitSur find(int id1, int id2) throws DAOException {
        throw new DAOException("Non utilisé");
    }

    @Override
    public ConduitSur find(int id1, int id2, int id3) throws DAOException {
        throw new DAOException("Non utilisé");
    }

    public ConduitSur find(int idChauffeur, int idLigne, int numVehicule, LocalDateTime dateHeureConduite) throws DAOException {

        // Execution de requetes
        Statement st = null;
        ResultSet rs = null;
        try {
            String dateHeureConduiteString = dateHeureConduite.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            st = connexion.createStatement();
            String req = "SELECT idLigne, idChauffeur, numVehicule, dateHeureConduite, nbValidation FROM conduitsur " +
                    "WHERE idChauffeur = " + idChauffeur + " AND idLigne = " + idLigne + " AND numVehicule = " + numVehicule +
                    " AND dateHeureConduite = '" + dateHeureConduiteString + "';";
            rs = st.executeQuery(req);

            while (rs.next()) {
                int nbValidation = rs.getInt("nbValidation");

                // Récupération des DAOs
                LigneDAO ligneDAO = new LigneDAO(connexion);
                ChauffeurDAO chauffeurDAO = new ChauffeurDAO(connexion);
                VehiculeDAO vehiculeDAO = new VehiculeDAO(connexion);

                // Récupération des autres objets pour construire l'objet
                Ligne laLigne = ligneDAO.find(idLigne);
                Chauffeur leChauffeur = chauffeurDAO.find(idChauffeur);
                Vehicule unVehicule = vehiculeDAO.find(numVehicule);

                return new ConduitSur(leChauffeur, laLigne, unVehicule, dateHeureConduite, nbValidation);
            }
        } catch (SQLException e) {
            System.err.println("Erreur requête SQL");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ConduitSur create(ConduitSur uneConduite) throws DAOException {
        String query = "INSERT INTO conduitsur (idLigne, idChauffeur, numVehicule, dateHeureConduite, nbValidation) VALUES (?, ?, ?, ?, ?)";

        try(PreparedStatement ps = connexion.prepareStatement(query)) {

            ps.setInt(1, uneConduite.getUneLigne().getIdLigne());
            ps.setInt(2, uneConduite.getLeChauffeur().getIdChauffeur());
            ps.setInt(3, uneConduite.getUnVehicule().getNumVehicule());
            ps.setString(4, uneConduite.getDateHeureConduite().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            ps.setInt(5, uneConduite.getNbValidation());

            int lignesModifiees = ps.executeUpdate();

            if(lignesModifiees == 0) {
                throw new DAOException("Fail to create the ConduitSur no addition to the database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Error while creating ConduitSur", e);
        }
        return uneConduite;
    }

    @Override
    public boolean update(ConduitSur uneConduite) throws DAOException {
        String query = "UPDATE conduitsur SET nbValidation = ? WHERE idLigne = ? AND idChauffeur = ? AND numVehicule = ? AND dateHeureConduite = ?";

        try (PreparedStatement ps = connexion.prepareStatement(query)) {

            ps.setInt(2, uneConduite.getUneLigne().getIdLigne());
            ps.setInt(3, uneConduite.getLeChauffeur().getIdChauffeur());
            ps.setInt(4, uneConduite.getUnVehicule().getNumVehicule());
            ps.setString(5, uneConduite.getDateHeureConduite().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            ps.setInt(1, uneConduite.getNbValidation());

            int lignesModifiees = ps.executeUpdate();

            return lignesModifiees > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Erreur lors de la mise à jour d'une conduite : " + uneConduite.toString(), e);
        }
    }

    @Override
    public boolean delete(ConduitSur uneConduite) throws DAOException {
        String query = "DELETE FROM conduitsur WHERE idLigne = ? AND idChauffeur = ? AND numVehicule = ? AND dateHeureConduite = ?";

        try (PreparedStatement ps = connexion.prepareStatement(query)) {

            ps.setInt(1, uneConduite.getUneLigne().getIdLigne());
            ps.setInt(2, uneConduite.getLeChauffeur().getIdChauffeur());
            ps.setInt(3, uneConduite.getUnVehicule().getNumVehicule());
            ps.setString(4, uneConduite.getDateHeureConduite().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            int lignesModifiees = ps.executeUpdate();

            return lignesModifiees > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Erreur lors de la suppression d'une conduite : " + uneConduite.toString(), e);
        }
    }

    @Override
    public List<ConduitSur> findAll() throws DAOException {
        Connection cn = Connexion.getConnexion();

        List<ConduitSur> listeConduites = new ArrayList<>();

        // Execution de requetes
        Statement st = null;
        ResultSet rs = null;
        try {
            st = cn.createStatement();
            String req = "SELECT idLigne, idChauffeur, numVehicule, dateHeureConduite, nbValidation FROM conduitsur";
            rs = st.executeQuery(req);

            while (rs.next()) {
                int idLigne = rs.getInt("IdLigne");
                int idChauffeur = rs.getInt("IdChauffeur");
                int numVehicule = rs.getInt("numVehicule");
                String dateHeureConduiteString = rs.getString("dateHeureConduite");
                int nbValidation = rs.getInt("nbValidation");

                LocalDateTime dateHeureConduite = LocalDateTime.parse(dateHeureConduiteString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                // Récupération des DAOs
                LigneDAO ligneDAO = new LigneDAO(connexion);
                ChauffeurDAO chauffeurDAO = new ChauffeurDAO(connexion);
                VehiculeDAO vehiculeDAO = new VehiculeDAO(connexion);

                // Récupération des autres objets pour construire l'objet
                Ligne laLigne = ligneDAO.find(idLigne);
                Chauffeur leChauffeur = chauffeurDAO.find(idChauffeur);
                Vehicule unVehicule = vehiculeDAO.find(numVehicule);

                listeConduites.add(new ConduitSur(leChauffeur, laLigne, unVehicule, dateHeureConduite, nbValidation));
            }
        } catch (SQLException e) {
            System.err.println("Erreur requête SQL");
            e.printStackTrace();
        }

        return null;
    }
}
