package passerelle;

import metiers.Arret;
import metiers.Chauffeur;
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

public class DessertDAO {

    public static List<Arret> getDessertesUneLigne(int idLigne) {
        Connection cn = Connexion.getConnexion();

        List<Arret> listesArretsDesservis = new ArrayList<>();

        // Execution de requetes
        Statement st = null;
        ResultSet rs = null;
        try {
            st = cn.createStatement();
            String req = "SELECT idLigne, idArret FROM dessert WHERE idLigne = " + idLigne + ";";
            rs = st.executeQuery(req);

            while (rs.next()) {
                int idArret = rs.getInt("idArret");

                System.out.println("idLigne : " + idLigne + " | idArret : " + idArret);

                // Récupération des autres objets pour construire l'objet
                Arret lArret = ArretDAO.getUnArret(idArret);

                listesArretsDesservis.add(lArret);
            }

            return listesArretsDesservis;

        } catch (SQLException e) {
            System.err.println("Erreur requête SQL");
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static List<Ligne> getDessertesUnArret(int idArret) {
        Connection cn = Connexion.getConnexion();

        List<Ligne> listesLignesDesservies = new ArrayList<>();

        // Execution de requetes
        Statement st = null;
        ResultSet rs = null;
        try {
            st = cn.createStatement();
            String req = "SELECT idLigne, idArret FROM dessert WHERE idArret = " + idArret + ";";
            rs = st.executeQuery(req);

            while (rs.next()) {
                int idLigne = rs.getInt("idLigne");

                System.out.println("idLigne : " + idLigne + " | idArret : " + idArret);

                // Récupération des autres objets pour construire l'objet
                Ligne laLigne = LigneDAO.getUneLigne(idLigne);

                listesLignesDesservies.add(laLigne);
            }

            return listesLignesDesservies;

        } catch (SQLException e) {
            System.err.println("Erreur requête SQL");
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
