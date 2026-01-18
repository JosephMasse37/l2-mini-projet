
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
import metiers.Ligne;
import metiers.TypeLigne;

//CRUD

    public class LigneDAO extends DAO<Ligne> {

        public LigneDAO(Connection connexion) {
            super(connexion);
        }

        // creer nv
        public Ligne create(Ligne uneLigne) throws DAOException {
            String query = "INSERT INTO ligne (libelle, idTypeLigne, Arret_Depart, Arret_Fin, duree) VALUES (?,?,?,?,?)";

            try (PreparedStatement ps = connexion.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                                                                              // ^^ autoincr
                ps.setString(1, uneLigne.getLibelle());
                ps.setInt(2, uneLigne.getTypeLigne().getIdTypeLigne());
                ps.setInt(3, uneLigne.getArretDepart().getIdArret());
                ps.setInt(4, uneLigne.getArretArrive().getIdArret());
                ps.setInt(5, uneLigne.getDuree());

                int lignesModifiees = ps.executeUpdate();

                if (lignesModifiees == 0) {
                    throw new DAOException("Échec de la création de la borne.");
                }
                // bloc ci dessous pas executé si if vrai
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    //si id a bien été crée
                    if (generatedKeys.next()) {
                        uneLigne.setIdLigne(generatedKeys.getInt(1)); //1 = le num de la colonne, car la seule colonne ds ce ResultSet)
                    } else {
                        throw new DAOException("Création de la ligne a échoué, aucun identifiant n'a pu être récupéré.");
                    }
                }

            } catch (SQLException e) {
                System.err.println("ERREUR BDD lors de la création d'une ligne. Détails:");
                e.printStackTrace();
                throw new DAOException("Erreur lors de la création de votre ligne", e);
            }
            return uneLigne;
        }

        public Ligne find(int IdLigne) throws DAOException {

            String query = "SELECT idLigne, libelle, idTypeLigne, Arret_Depart, Arret_Fin, duree FROM ligne WHERE idLigne = ?";

            try (PreparedStatement ps = connexion.prepareStatement(query)) {

                ps.setInt(1, IdLigne);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        //sert à transformer un simple numéro (Id) de la table ligne en un objet complet
                        ArretDAO arretDAO = new ArretDAO(connexion);
                        TypeLigneDAO typeligneDAO = new TypeLigneDAO(connexion);

                        int idLigne = rs.getInt("IdLigne");
                        String libelle = rs.getString("libelle");
                        TypeLigne typeLigne = typeligneDAO.find(rs.getInt("idTypeLigne"));
                        Arret arretDepart = arretDAO.find(rs.getInt("Arret_Depart"));
                        Arret arretArrive = arretDAO.find(rs.getInt("Arret_Fin"));
                        int duree = rs.getInt("duree");


                        return new Ligne(idLigne, libelle, typeLigne, arretDepart, arretArrive,duree);
                    } else {
                        return null; // aucune ligne trouvé
                    }
                }

            } catch (SQLException e) {
                System.err.println("ERREUR BDD lors de la recherche de la ligne ID=" + IdLigne + ". Détails:");
                e.printStackTrace();
                throw new DAOException("Erreur lors de la recherche de la ligne avec ID : " + IdLigne, e);
            }
        }

        public List<Ligne> findAll() throws DAOException {

            List<Ligne> lignes = new ArrayList<>();
            String query = "SELECT idLigne, libelle, idTypeLigne, Arret_Depart, Arret_Fin, duree FROM ligne";
            try (PreparedStatement ps = connexion.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    //sert à transformer un simple numéro (Id) de la table ligne en un objet complet
                    ArretDAO arretDAO = new ArretDAO(connexion);
                    TypeLigneDAO typeligneDAO = new TypeLigneDAO(connexion);

                    int idLigne = rs.getInt("idLigne");
                    String libelle = rs.getString("libelle");
                    TypeLigne typeLigne = typeligneDAO.find(rs.getInt("idTypeLigne"));
                    Arret arretDepart = arretDAO.find(rs.getInt("Arret_Depart"));
                    Arret arretArrive = arretDAO.find(rs.getInt("Arret_Fin"));
                    int duree = rs.getInt("duree");

                    Ligne ligne = new Ligne(idLigne, libelle, typeLigne, arretDepart, arretArrive,duree);
                    lignes.add(ligne);

                }

            } catch (SQLException e) {
                throw new DAOException("Erreur lors de la récupération de toutes les lignes", e);
            }
            return lignes;
        }

        public boolean update(Ligne uneLigne) throws DAOException {

            String query = "UPDATE ligne SET libelle=?, idTypeLigne=?, Arret_Depart=?, Arret_Fin=?, duree=? WHERE idLigne = ?";
            try (PreparedStatement ps = connexion.prepareStatement(query)) {

                ps.setString(1, uneLigne.getLibelle());
                ps.setInt(2, uneLigne.getTypeLigne().getIdTypeLigne());
                ps.setInt(3, uneLigne.getArretDepart().getIdArret());
                ps.setInt(4, uneLigne.getArretArrive().getIdArret());
                ps.setInt(5,uneLigne.getDuree());
                ps.setInt(6, uneLigne.getIdLigne());

                int lignesModifiees = ps.executeUpdate();

                return lignesModifiees > 0;

            } catch (SQLException e) {
                System.err.println("ERREUR BDD lors de la mise à jour de la ligne ID=" + uneLigne.getIdLigne() + ". Détails:");
                e.printStackTrace();
                throw new DAOException("Erreur lors de la mise à jour de la ligne avec ID : " + uneLigne.getIdLigne(), e);
            }
        }

        public boolean delete(Ligne uneLigne) throws DAOException {

            String query = "DELETE FROM ligne WHERE idLigne = ?";

            try (PreparedStatement ps = connexion.prepareStatement(query)) {

                ps.setInt(1, uneLigne.getIdLigne());

                int lignesModifiees = ps.executeUpdate();

                return lignesModifiees > 0;

            } catch (SQLException e) {
                System.err.println("ERREUR BDD lors de la suppression de la ligne ID=" + uneLigne.getIdLigne() + ". Détails:");
                e.printStackTrace();
                throw new DAOException("Erreur lors de la suppression de la ligne avec ID : " + uneLigne.getIdLigne(), e);
            }
        }

        public List<Ligne> findByType(int idTypeLigne) throws DAOException {
            List<Ligne> lignes = new ArrayList<>();
            String query = "SELECT * FROM ligne WHERE idTypeLigne = ?";

            try (PreparedStatement ps = connexion.prepareStatement(query)) {
                ps.setInt(1, idTypeLigne);
                try (ResultSet rs = ps.executeQuery()) {

                    ArretDAO arretDAO = new ArretDAO(connexion);
                    TypeLigneDAO typeDAO = new TypeLigneDAO(connexion);

                    while (rs.next()) {
                        lignes.add(new Ligne(
                                rs.getInt("idLigne"),
                                rs.getString("libelle"),
                                typeDAO.find(rs.getInt("idTypeLigne")),
                                arretDAO.find(rs.getInt("Arret_Depart")),
                                arretDAO.find(rs.getInt("Arret_Fin")),
                                rs.getInt("duree")

                                ));
                    }
                }
            } catch (SQLException e) {
                throw new DAOException("Erreur lors de la recherche par type", e);
            }
            return lignes;
        }

        public List<Ligne> getLignesParType(String typeNom) throws DAOException {
            // trad le mot en ID
            int idType = typeNom.equalsIgnoreCase("BUS") ? 1 : 2; //bus= 1
            return findByType(idType);
        }

        @Override
        public Ligne find(int id1, int id2) throws DAOException {
            throw new DAOException("Non utilisé");
        }

        @Override
        public Ligne find(int id1, int id2, int id3) throws DAOException {
            throw new DAOException("Non utilisé");
        }

        public List<Ligne> findByNomEtType(String nom, String typeNom) throws DAOException {
            List<Ligne> lignes = new ArrayList<>();

            String query = "SELECT * FROM ligne WHERE idTypeLigne = ? AND libelle LIKE ?";

            int idType = typeNom.equalsIgnoreCase("BUS") ? 1 : 2;

            try (PreparedStatement ps = connexion.prepareStatement(query)) {
                ps.setInt(1, idType);
                ps.setString(2, "%" + nom + "%");

                try (ResultSet rs = ps.executeQuery()) {
                    ArretDAO arretDAO = new ArretDAO(connexion);
                    TypeLigneDAO typeDAO = new TypeLigneDAO(connexion);
                    while (rs.next()) {
                        lignes.add(new Ligne(
                                rs.getInt("idLigne"),
                                rs.getString("libelle"),
                                typeDAO.find(rs.getInt("idTypeLigne")),
                                arretDAO.find(rs.getInt("Arret_Depart")),
                                arretDAO.find(rs.getInt("Arret_Fin")),
                                rs.getInt("duree")
                        ));
                    }
                }
            } catch (SQLException e) {
                throw new DAOException("Erreur recherche par nom", e);
            }
            return lignes;
        }
    }


