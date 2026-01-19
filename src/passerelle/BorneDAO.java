package passerelle;

import java.sql.Connection;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import metiers.Arret;
import metiers.Borne;

//CRUD

    public class BorneDAO extends DAO<Borne> {

        public BorneDAO(Connection connexion) {
            super(connexion);
        }

        // creer nv arret
        public Borne create(Borne uneBorne) throws DAOException {
            String query = "INSERT INTO borne (nbVoyageVendu, nbVentesTickets, idArret) VALUES (?, ?, ?)";

            try (PreparedStatement ps = connexion.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                // ^^ autoincr
                ps.setInt(1, uneBorne.getNbVoyageVendu());
                ps.setInt(2, uneBorne.getNbVentesTickets());

                // pr recup idArret:
                if (uneBorne.getArret() != null) {
                    ps.setInt(3, uneBorne.getArret().getIdArret());
                } else {
                    throw new DAOException("Impossible de créer une borne sans arrêt associé.");
                }

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

            String query = "SELECT idBorne, nbVoyageVendu, NbVentesTickets,idArret FROM borne WHERE idBorne = ?";

            ArretDAO arretDAO = new ArretDAO(this.connexion);

            try (PreparedStatement ps = connexion.prepareStatement(query)) {

                ps.setInt(1, IdBorne);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        int idBorne = rs.getInt("idBorne");
                        int nbVoyageVendu = rs.getInt("nbVoyageVendu");
                        int NbVentesTickets = rs.getInt("NbVentesTickets");
                        Arret idArret = arretDAO.find(rs.getInt("idArret"));

                        Borne borne = new Borne(idBorne, nbVoyageVendu, NbVentesTickets,idArret);
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
            String query = "SELECT idBorne, nbVoyageVendu, NbVentesTickets, idArret FROM borne";

            ArretDAO arretDAO = new ArretDAO(this.connexion);

            try (PreparedStatement ps = connexion.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    int idBorne = rs.getInt("idBorne");
                    int nbVoyageVendu = rs.getInt("nbVoyageVendu");
                    int NbVentesTickets = rs.getInt("NbVentesTickets");
                    Arret idArret = arretDAO.find(rs.getInt("idArret"));


                    Borne borne = new Borne(idBorne, nbVoyageVendu, NbVentesTickets,idArret);
                    bornes.add(borne);
                }

            } catch (SQLException e) {
                throw new DAOException("Erreur lors de la récupération de toutes les bornes", e);
            }
            return bornes;
        }

        public boolean update(Borne uneBorne) throws DAOException {

            String query = "UPDATE borne SET nbVoyageVendu = ?, NbVentesTickets = ?, idArret = ? WHERE idBorne = ?";

            try (PreparedStatement ps = connexion.prepareStatement(query)) {

                ps.setInt(1, uneBorne.getNbVoyageVendu());
                ps.setInt(2, uneBorne.getNbVentesTickets());

                if (uneBorne.getArret() != null) {
                    ps.setInt(3, uneBorne.getArret().getIdArret());
                } else {
                    throw new DAOException("Impossible de mettre à jour : l'arrêt est manquant.");
                }

                ps.setInt(4, uneBorne.getIdBorne());

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

        @Override
        public Borne find(int id1, int id2) throws DAOException {
            throw new DAOException("Non utilisé");
        }

        @Override
        public Borne find(int id1, int id2, int id3) throws DAOException {
            throw new DAOException("Non utilisé");
        }
        public Map<String, String> getStatsExtremes() throws DAOException {
            Map<String, String> stats = new HashMap<>();

            //  plus rentable (Celle qui a le plus gros nbVentesTickets)
            String sqlBest = "SELECT idBorne, nbVentesTickets, idArret FROM borne ORDER BY nbVentesTickets DESC LIMIT 1";

            //  pire (Celle qui a le moins de nbVentesTickets)
            String sqlPire = "SELECT idBorne, nbVentesTickets, idArret FROM borne ORDER BY nbVentesTickets ASC LIMIT 1";

            try (java.sql.Statement st = connexion.createStatement()) {

                ArretDAO arretDAO = new ArretDAO(connexion);

                //  pour la meilleure
                try (ResultSet rs1 = st.executeQuery(sqlBest)) {
                    if (rs1.next()) {           // convert en string
                        stats.put("best_id", String.valueOf(rs1.getInt("idBorne") + " à l'arrêt " + arretDAO.find(rs1.getInt("idArret")).getNom()));
                        stats.put("best_ventes", String.valueOf(rs1.getInt("nbVentesTickets")));
                    }
                }

                //  pour la pire
                try (ResultSet rs2 = st.executeQuery(sqlPire)) {
                    if (rs2.next()) {
                        stats.put("pire_id", String.valueOf(rs2.getInt("idBorne") + " à l'arrêt " + arretDAO.find(rs2.getInt("idArret")).getNom()));
                        stats.put("pire_ventes", String.valueOf(rs2.getInt("nbVentesTickets")));
                    }
                }

            } catch (SQLException e) {
                throw new DAOException("Erreur lors du calcul des stats de rentabilité", e);
            }
            return stats;
        }
    }



