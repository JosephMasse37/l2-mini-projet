package passerelle;

import java.sql.Connection;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import metiers.Borne;

//CRUD

    public class BorneDAO extends DAO<Borne> {

        public BorneDAO(Connection connexion) {
            super(connexion);
        }

        // creer nv arret
        public Borne create(Borne uneBorne) throws DAOException {
            String query = "INSERT INTO borne (nbVoyageVendu,NbVentesTickets) VALUES (?, ?)";

            try (PreparedStatement ps = connexion.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                // ^^ autoincr
                ps.setInt(1, uneBorne.getNbVoyageVendu());
                ps.setInt(2, uneBorne.getNbVentesTickets());

                int lignesModifiees = ps.executeUpdate();

                if (lignesModifiees == 0) {
                    throw new DAOException("Échec de la création de la borne.");
                }
                // bloc ci dessous pas executé si if vrai
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    //si id a bien été crée
                    if (generatedKeys.next()) {
                        uneBorne.setIdBorne(generatedKeys.getInt(1)); //1 = le num de la colonne, car la seule colonne ds ce ResultSet)
                    } else {
                        throw new DAOException("Création de la borne a échoué, aucun identifiant n'a pu être récupéré.");
                    }
                }

            } catch (SQLException e) {
                System.err.println("ERREUR BDD lors de la création d'un arrêt. Détails:");
                e.printStackTrace();
                throw new DAOException("Erreur lors de la création de votre Borne", e);
            }
            return uneBorne;
        }

        public Borne find(int IdBorne) throws DAOException {

            String query = "SELECT idBorne, nbVoyageVendu, NbVentesTickets FROM borne WHERE idBorne = ?";
            try (PreparedStatement ps = connexion.prepareStatement(query)) {

                ps.setInt(1, IdBorne);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        int idBorne = rs.getInt("idBorne");
                        int nbVoyageVendu = rs.getInt("nbVoyageVendu");
                        int NbVentesTickets = rs.getInt("NbVentesTickets");

                        Borne borne = new Borne(idBorne, nbVoyageVendu, NbVentesTickets);
                        return borne;

                    } else {
                        return null; // aucune borne trouvé
                    }
                }

            } catch (SQLException e) {
                System.err.println("ERREUR BDD lors de la recherche de la borne ID=" + IdBorne + ". Détails:");
                e.printStackTrace();
                throw new DAOException("Erreur lors de la recherche de la borne avec ID : " + IdBorne, e);
            }
        }

        public List<Borne> findAll() throws DAOException {

            List<Borne> bornes = new ArrayList<>();
            String query = "SELECT idBorne, nbVoyageVendu, NbVentesTickets FROM borne";

            try (PreparedStatement ps = connexion.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    int idBorne = rs.getInt("idBorne");
                    int nbVoyageVendu = rs.getInt("nbVoyageVendu");
                    int NbVentesTickets = rs.getInt("NbVentesTickets");

                    Borne borne = new Borne(idBorne, nbVoyageVendu, NbVentesTickets);
                    bornes.add(borne);
                }

            } catch (SQLException e) {
                throw new DAOException("Erreur lors de la récupération de toutes les bornes", e);
            }
            return bornes;
        }

        public boolean update(Borne uneBorne) throws DAOException {

            String query = "UPDATE borne SET nbVoyageVendu = ?, NbVentesTickets = ? WHERE idBorne = ?";

            try (PreparedStatement ps = connexion.prepareStatement(query)) {

                ps.setInt(1, uneBorne.getNbVoyageVendu());
                ps.setInt(2, uneBorne.getNbVentesTickets());
                ps.setInt(3, uneBorne.getIdBorne());

                int lignesModifiees = ps.executeUpdate();

                return lignesModifiees > 0;

            } catch (SQLException e) {
                System.err.println("ERREUR BDD lors de la mise à jour de la borne ID=" + uneBorne.getIdBorne() + ". Détails:");
                e.printStackTrace();
                throw new DAOException("Erreur lors de la mise à jour de la borne avec ID : " + uneBorne.getIdBorne(), e);
            }
        }

        public boolean delete(Borne uneBorne) throws DAOException {

            String query = "DELETE FROM borne WHERE idBorne = ?";

            try (PreparedStatement ps = connexion.prepareStatement(query)) {

                ps.setInt(1, uneBorne.getIdBorne());

                int lignesModifiees = ps.executeUpdate();

                return lignesModifiees > 0;

            } catch (SQLException e) {
                System.err.println("ERREUR BDD lors de la suppression de la borne ID=" + uneBorne.getIdBorne() + ". Détails:");
                e.printStackTrace();
                throw new DAOException("Erreur lors de la suppression de la borne avec ID : " + uneBorne.getIdBorne(), e);
            }
        }


    }



