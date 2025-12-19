package passerelle;

import metiers.Chauffeur;
import metiers.ConduitSur;
import metiers.Ligne;
import metiers.Vehicule;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

                System.out.println("numVehicule : " + numVehicule + " | IdLigne : " + idLigne + " | idChauffeur : " + idChauffeur +
                        " | dateHeureConduite : " + dateHeureConduite);

                // Récupération des DAOs
                LigneDAO ligneDAO = new LigneDAO(connexion);
                ChauffeurDAO chauffeurDAO = ChauffeurDAO(connexion);
                VehiculeDAO vehiculeDAO = VehiculeDAO(connexion);

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
        Connection cn = Connexion.getConnexion();

        List<ConduitSur> listeConduites = new ArrayList<>();

        // Execution de requetes
        Statement st = null;
        ResultSet rs = null;
        try {
            st = cn.createStatement();
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
                ChauffeurDAO chauffeurDAO = ChauffeurDAO(connexion);
                VehiculeDAO vehiculeDAO = VehiculeDAO(connexion);

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
        Connection cn = Connexion.getConnexion();

        List<ConduitSur> listeConduites = new ArrayList<>();

        // Execution de requetes
        Statement st = null;
        ResultSet rs = null;
        try {
            st = cn.createStatement();
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
                ChauffeurDAO chauffeurDAO = ChauffeurDAO(connexion);
                VehiculeDAO vehiculeDAO = VehiculeDAO(connexion);

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
        Connection cn = Connexion.getConnexion();

        List<ConduitSur> listeConduites = new ArrayList<>();

        // Execution de requetes
        Statement st = null;
        ResultSet rs = null;
        try {
            st = cn.createStatement();
            String req = "SELECT idLigne, idChauffeur, numVehicule, dateHeureConduite, nbValidation FROM conduitsur " +
                    "WHERE idChauffeur = " + idChauffeur + " AND idLigne = " + idLigne + " AND numVehicule = " + numVehicule + ";";
            rs = st.executeQuery(req);

            while (rs.next()) {
                int nbValidation = rs.getInt("nbValidation");
                String dateHeureConduiteString = rs.getString("dateHeureConduite");
                LocalDateTime dateHeureConduite = LocalDateTime.parse(dateHeureConduiteString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                System.out.println("numVehicule : " + numVehicule + " | IdLigne : " + idLigne + " | idChauffeur : " + idChauffeur +
                        " | dateHeureConduite : " + dateHeureConduite);

                // Récupération des DAOs
                LigneDAO ligneDAO = new LigneDAO(connexion);
                ChauffeurDAO chauffeurDAO = ChauffeurDAO(connexion);
                VehiculeDAO vehiculeDAO = VehiculeDAO(connexion);

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
        Connection cn = Connexion.getConnexion();

        List<ConduitSur> listeConduites = new ArrayList<>();

        // Execution de requetes
        Statement st = null;
        ResultSet rs = null;
        try {
            String dateHeureConduiteString = dateHeureConduite.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            st = cn.createStatement();
            String req = "SELECT idLigne, idChauffeur, numVehicule, dateHeureConduite, nbValidation FROM conduitsur " +
                    "WHERE idChauffeur = " + idChauffeur + " AND idLigne = " + idLigne + " AND numVehicule = " + numVehicule +
                    " AND dateHeureConduite = '" + dateHeureConduiteString + "';";
            rs = st.executeQuery(req);

            while (rs.next()) {
                int nbValidation = rs.getInt("nbValidation");

                System.out.println("numVehicule : " + numVehicule + " | IdLigne : " + idLigne + " | idChauffeur : " + idChauffeur +
                        " | dateHeureConduite : " + dateHeureConduite);

                // Récupération des DAOs
                LigneDAO ligneDAO = new LigneDAO(connexion);
                ChauffeurDAO chauffeurDAO = ChauffeurDAO(connexion);
                VehiculeDAO vehiculeDAO = VehiculeDAO(connexion);

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
    public ConduitSur create(ConduitSur object) throws DAOException {
        return null;
    }

    @Override
    public boolean update(ConduitSur object) throws DAOException {
        return false;
    }

    @Override
    public boolean delete(ConduitSur object) throws DAOException {
        return false;
    }

    @Override
    public List<ConduitSur> findAll() throws DAOException {
        return List.of();
    }
}
