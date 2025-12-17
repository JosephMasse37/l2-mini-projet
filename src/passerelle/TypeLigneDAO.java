package passerelle;

import java.sql.Connection;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import metiers.TypeLigne;


//CRUD

public class TypeLigneDAO extends DAO<TypeLigne> {

    public TypeLigneDAO(Connection connexion) {
        super(connexion);
    }

    // creer nv 
    public TypeLigne create(TypeLigne uneTypeLigne) throws DAOException {
        String query = "INSERT INTO TypeLigne (libelle) VALUES (?)";

        try (PreparedStatement ps = connexion.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            // ^^ autoincr
            ps.setString(1, uneTypeLigne.getLibelle());

            int lignesModifiees = ps.executeUpdate();

            if (lignesModifiees == 0) {
                throw new DAOException("Échec de la création de la typeligne.");
            }
            // bloc ci dessous pas executé si if vrai
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                //si id a bien été crée
                if (generatedKeys.next()) {
                    uneTypeLigne.setIdTypeLigne(generatedKeys.getInt(1)); //1 = le num de la colonne, car la seule colonne ds ce ResultSet)
                } else {
                    throw new DAOException("Création du typeligne a échoué, aucun identifiant n'a pu être récupéré.");
                }
            }

        } catch (SQLException e) {
            System.err.println("ERREUR BDD lors de la création d'une typeligne. Détails:");
            e.printStackTrace();
            throw new DAOException("Erreur lors de la création de votre typeligne", e);
        }
        return uneTypeLigne;
    }

    public TypeLigne find(int idTypeLigne) throws DAOException {

        String query = "SELECT idTypeLigne, libelle FROM TypeLigne WHERE idTypeLigne = ?";            try (PreparedStatement ps = connexion.prepareStatement(query)) {

            ps.setInt(1, idTypeLigne);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    int idtypeligne = rs.getInt("idTypeLigne");
                    String libelle = rs.getString("libelle");

                    return new TypeLigne(idtypeligne, libelle);
                } else {
                    return null; // aucune idtypeligne trouvé
                }
            }

        } catch (SQLException e) {
            System.err.println("ERREUR BDD lors de la recherche du TypeLigne ID=" + idTypeLigne + ". Détails:");
            e.printStackTrace();
            throw new DAOException("Erreur lors de la recherche du TypeLigne avec ID : " + idTypeLigne, e);
        }
    }

    public List<TypeLigne> findAll() throws DAOException {

        List<TypeLigne> TypeLignes = new ArrayList<>();
        String query = "SELECT idTypeLigne, libelle FROM TypeLigne";

        try (PreparedStatement ps = connexion.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                int idtypeligne = rs.getInt("idTypeLigne");
                String libelle = rs.getString("libelle");

                TypeLigne uneTypeLigne = new TypeLigne(idtypeligne, libelle);
                TypeLignes.add(uneTypeLigne);
            }

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la récupération de toutes les TypeLigne", e);
        }
        return TypeLignes;
    }

    public boolean update(TypeLigne uneTypeLigne) throws DAOException {

        String query = "UPDATE typeligne SET libelle=? WHERE IdTypeLigne = ?";

        try (PreparedStatement ps = connexion.prepareStatement(query)) {

            ps.setString(1, uneTypeLigne.getLibelle());
            ps.setInt(2, uneTypeLigne.getIdTypeLigne());

            int lignesModifiees = ps.executeUpdate();

            return lignesModifiees > 0;

        } catch (SQLException e) {
            System.err.println("ERREUR BDD lors de la mise à jour de la TypeLigne ID=" + uneTypeLigne.getIdTypeLigne() + ". Détails:");
            e.printStackTrace();
            throw new DAOException("Erreur lors de la mise à jour de la TypeLigne avec ID : " + uneTypeLigne.getIdTypeLigne(), e);
        }
    }

    public boolean delete(TypeLigne uneTypeLigne) throws DAOException {

        String query = "DELETE FROM ligne WHERE idLigne = ?";

        try (PreparedStatement ps = connexion.prepareStatement(query)) {

            ps.setInt(1, uneTypeLigne.getIdTypeLigne());

            int lignesModifiees = ps.executeUpdate();

            return lignesModifiees > 0;

        } catch (SQLException e) {
            System.err.println("ERREUR BDD lors de la suppression de la TypeLigne ID=" + uneTypeLigne.getIdTypeLigne() + ". Détails:");
            e.printStackTrace();
            throw new DAOException("Erreur lors de la suppression de la TypeLigne avec ID : " + uneTypeLigne.getIdTypeLigne(), e);
        }
    }


}

