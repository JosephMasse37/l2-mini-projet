package tests;

import metiers.Arret;
import metiers.Ligne;
import metiers.TypeLigne;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

import passerelle.*;


public class TestLigneDAO {
 // init des private
    private Connection connexion;
    private LigneDAO ligneDAO;

    // b4 each test
    @BeforeEach
    void setUp() throws SQLException, Exception {
        this.connexion = Connexion.getConnexion(); //instancie une co
        this.connexion.setAutoCommit(false); /** on set à false car:[ par defaut MySQL = Auto-Commit càd inscrit direct. la modif ds la BDD]
                                               = donc on crée un espace de travail temporaire (GRACE A CA QU'ON VA FAIRE UN ROLLBACK!!)
                                                */
        this.ligneDAO = new LigneDAO(this.connexion); // on instancie une nvlle LigneDAO connectée a la bdd
    }

    //after each test
    @AfterEach
    void tearDown() throws SQLException, DAOException {
        if (connexion != null) { /** On vérifie que la connexion existe bien pour éviter de faire planter
                                 le prog si la BDD était injoignable au départ. */

            connexion.rollback(); // vide l'espace de travail temp
            connexion.setAutoCommit(true);
            Connexion.close(); // on ferme pr éviter de saturer le serveur w bcp de connexions ouvertes.
        }
    }


    @Test
    @DisplayName("Creation d'une ligne")
    void testCreate() throws DAOException {

        // recup les dao needed
        ArretDAO arretDAO = new ArretDAO(connexion);
        TypeLigneDAO typeDAO = new TypeLigneDAO(connexion);

        Arret depart = arretDAO.find(4);
        Arret arrivee = arretDAO.find(14);
        TypeLigne type = typeDAO.find(1);

        Ligne ligne = new Ligne("14",type,depart,arrivee,8);
        Ligne creee = ligneDAO.create(ligne);

       //pas fini obvs
    }

}
