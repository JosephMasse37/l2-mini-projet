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

public class ConduitSurDAO {
    public static List<ConduitSur> getConduiteUnVehicule(int numVehicule) {
        Connection cn = Connexion.getConnexion();

        List<ConduitSur> listeConduites = new ArrayList<>();

        // Execution de requetes
        Statement st = null;
        ResultSet rs = null;
        try {
            st = cn.createStatement();
            String req = "SELECT idLigne, idChauffeur, numVehicule, dateHeureConduite, nbValidation FROM conduitsur WHERE numVehicule = " + numVehicule + ";";
            rs = st.executeQuery(req);

            while (rs.next()) {
                int idLigne = rs.getInt("idLigne");
                int idChauffeur = rs.getInt("idChauffeur");
                String dateHeureConduiteString = rs.getString("dateHeureConduite");
                LocalDateTime dateHeureConduite = LocalDateTime.parse(dateHeureConduiteString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                System.out.println("numVehicule : " + numVehicule + " | IdLigne : " + idLigne + " | idChauffeur : " + idChauffeur +
                        " | dateHeureConduite : " + dateHeureConduite);

                // Récupération des autres objets pour construire l'objet
                Ligne laLigne = LigneDAO.getUneLigne(idLigne);
                Chauffeur leChauffeur = ChauffeurDAO.getUnChauffeur(idChauffeur);
                Vehicule unVehicule = VehiculeDAO.getUnVehicule(numVehicule);

                listeConduites.add(new ConduitSur(leChauffeur, laLigne, unVehicule, dateHeureConduite));
            }
        } catch (SQLException e) {
            System.err.println("Erreur requête SQL");
            e.printStackTrace();
        }

        return listeConduites;
    }

    public static List<ConduitSur> getConduiteUnChauffeur(int idChauffeur) {
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
                LocalDateTime dateHeureConduite = LocalDateTime.parse(dateHeureConduiteString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                System.out.println("numVehicule : " + numVehicule + " | IdLigne : " + idLigne + " | idChauffeur : " + idChauffeur +
                        " | dateHeureConduite : " + dateHeureConduite);

                // Récupération des autres objets pour construire l'objet
                Ligne laLigne = LigneDAO.getUneLigne(idLigne);
                Chauffeur leChauffeur = ChauffeurDAO.getUnChauffeur(idChauffeur);
                Vehicule unVehicule = VehiculeDAO.getUnVehicule(numVehicule);

                listeConduites.add(new ConduitSur(leChauffeur, laLigne, unVehicule, dateHeureConduite));
            }
        } catch (SQLException e) {
            System.err.println("Erreur requête SQL");
            e.printStackTrace();
        }

        return listeConduites;
    }

    public static List<ConduitSur> getConduiteUneLigne(int idLigne) {
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
                String dateHeureConduiteString = rs.getString("dateHeureConduite");
                LocalDateTime dateHeureConduite = LocalDateTime.parse(dateHeureConduiteString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                System.out.println("numVehicule : " + numVehicule + " | IdLigne : " + idLigne + " | idChauffeur : " + idChauffeur +
                        " | dateHeureConduite : " + dateHeureConduite);

                // Récupération des autres objets pour construire l'objet
                Ligne laLigne = LigneDAO.getUneLigne(idLigne);
                Chauffeur leChauffeur = ChauffeurDAO.getUnChauffeur(idChauffeur);
                Vehicule unVehicule = VehiculeDAO.getUnVehicule(numVehicule);

                listeConduites.add(new ConduitSur(leChauffeur, laLigne, unVehicule, dateHeureConduite));
            }
        } catch (SQLException e) {
            System.err.println("Erreur requête SQL");
            e.printStackTrace();
        }

        return listeConduites;
    }

    public static List<ConduitSur> getConduitesUnChauffeurUneLigneUnVehicule(int idChauffeur, int idLigne, int numVehicule) {
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
                String dateHeureConduiteString = rs.getString("dateHeureConduite");
                LocalDateTime dateHeureConduite = LocalDateTime.parse(dateHeureConduiteString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                System.out.println("numVehicule : " + numVehicule + " | IdLigne : " + idLigne + " | idChauffeur : " + idChauffeur +
                        " | dateHeureConduite : " + dateHeureConduite);

                // Récupération des autres objets pour construire l'objet
                Ligne laLigne = LigneDAO.getUneLigne(idLigne);
                Chauffeur leChauffeur = ChauffeurDAO.getUnChauffeur(idChauffeur);
                Vehicule unVehicule = VehiculeDAO.getUnVehicule(numVehicule);

                listeConduites.add(new ConduitSur(leChauffeur, laLigne, unVehicule, dateHeureConduite));
            }
        } catch (SQLException e) {
            System.err.println("Erreur requête SQL");
            e.printStackTrace();
        }

        return listeConduites;
    }
}
