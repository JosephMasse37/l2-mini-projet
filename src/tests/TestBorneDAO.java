
package tests;

import metiers.Arret;
import metiers.Borne;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passerelle.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class TestBorneDAO {
    // init des private
    private Connection connexion;
    private BorneDAO borneDAO;

    // b4 each test
    @BeforeEach
    void setUp() throws SQLException, Exception {
        this.connexion = Connexion.getConnexion(); //instancie une co
        this.connexion.setAutoCommit(false); /** on set à false car:[ par defaut MySQL = Auto-Commit càd inscrit direct. la modif ds la BDD]
         = donc on crée un espace de travail temporaire (GRACE A CA QU'ON VA FAIRE UN ROLLBACK!!)
         */
        this.borneDAO = new BorneDAO(this.connexion); // on instancie une nvlle LigneDAO connectée a la bdd
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
    @DisplayName("Creation d'une borne")
    void testCreate() throws DAOException {

        // recup les dao needed
        ArretDAO arretDAO = new ArretDAO(connexion);

        //je cree l'objet mtn
        Arret arret = arretDAO.find(4);
        Borne nouvelleBorne = new Borne(8, 8, arret);

        //on insere l'objet cree:
        Borne borneCree = borneDAO.create(nouvelleBorne);
        System.out.println("Borne créée avec id : " + borneCree.getIdBorne() + " et arrêt : " + borneCree.getArret().getNom() + " et voyages vendus : " + borneCree.getNbVoyageVendu() + " et tickets vendus : " + borneCree.getNbVentesTickets());

        // VALIDATION
        assertNotNull(borneCree, "La borne créé ne doit pas être null");
        assertTrue(borneCree.getIdBorne() > 0, "L'ID doit être généré par la BDD");
        assertNotNull(borneCree.getArret(), "L'objet Arret doit être présent dans la borne");

        System.out.println("Succès ! Borne trouvée : ID " + borneCree.getIdBorne() +
                " située à l'arrêt : " + borneCree.getArret().getNom() +
                " avec " + borneCree.getNbVoyageVendu() + " voyages vendus."+
                " et tickets vendus : " + borneCree.getNbVentesTickets());    }

    @Test
    @DisplayName("Recherche d'une borne par ID")
    void testFindById() throws DAOException {

        int idBorneExistant = 1;

        System.out.println("Tentative de recherche de la borne ID " + idBorneExistant + " via le DAO");

        Borne trouvee = borneDAO.find(idBorneExistant);

        assertNotNull(trouvee);
        assertEquals(1, trouvee.getIdBorne());
        assertEquals(15, trouvee.getNbVoyageVendu());
        assertEquals(56, trouvee.getNbVentesTickets());
        assertNotNull(trouvee.getArret(), "L'arrêt lié ne doit pas être null");
        assertEquals("Gare_de_Tours", trouvee.getArret().getNom());

        System.out.println("Succès ! Borne trouvée : ID " + trouvee.getIdBorne() +
                " située à l'arrêt : " + trouvee.getArret().getNom() +
                " avec " + trouvee.getNbVoyageVendu() + " voyages vendus."+
                " et tickets vendus : " + trouvee.getNbVentesTickets());
    }

    @Test
    @DisplayName("Mise à jour d'une borne")
    void testUpdate() throws DAOException {

        int idBorneExistant = 1;

        Borne borne = borneDAO.find(idBorneExistant);

        // on modif l'objet pr tester
        System.out.println("Modification locale : NbVoyageVendu = '55'");
        borne.setNbVoyageVendu(55);

        // update borne grace a update car sinon pas modif ds la BDD

        boolean updateFait = borneDAO.update(borne);
        assertTrue(updateFait, "L'update aurait dû modifier 1 borne");
        System.out.println("Commande UPDATE envoyée à la base de données.");

        // on cherche pr verif
        Borne trouvee = borneDAO.find(idBorneExistant);

        //VALIDATION de la conformité de l'objet envoyé!! grace aux assertions
        assertNotNull(trouvee);
        assertEquals(55, trouvee.getNbVoyageVendu());
        System.out.println("Mise à jour réussie pour la borne ID " + trouvee.getIdBorne());
    }

    @Test
    @DisplayName("Suppression d'une borne")
    void testDelete() throws DAOException {

        // recup les dao needed
        ArretDAO arretDAO = new ArretDAO(connexion);

        //je cree l'objet mtn
        Arret arret = arretDAO.find(4);
        Borne nouvelleBorne = new Borne(8, 8, arret);

        //on insere l'objet cree:
        Borne borneCree = borneDAO.create(nouvelleBorne);
        System.out.println("Borne créée avec id : " + borneCree.getIdBorne() + " et arrêt : " + borneCree.getArret().getNom() + " et voyages vendus : " + borneCree.getNbVoyageVendu() + " et tickets vendus : " + borneCree.getNbVentesTickets());

        int idBorneTemp = borneCree.getIdBorne();

        // supp
        boolean suppressionOk = borneDAO.delete(borneCree);
        assertTrue(suppressionOk);

        // vérif
        assertNull(borneDAO.find(idBorneTemp), "La borne ne devrait plus exister");
        System.out.println("Borne temporaire ID " + idBorneTemp + " supprimée avec succès.");
    }

    @Test
    @DisplayName("Récupération de toutes les bornes")
    void testFindAll() throws DAOException {

        List<Borne> toutesLesBornes = borneDAO.findAll();

        assertNotNull(toutesLesBornes, "La liste ne doit pas être null");
        assertTrue(toutesLesBornes.size() > 0, "La liste devrait contenir au moins une borne");

        System.out.println("Nombre de bornes trouvées : " + toutesLesBornes.size());

        for (Borne b : toutesLesBornes) {

            assertNotNull(b.getArret(), "Chaque borne doit avoir un arrêt associé");

            System.out.println("Borne ID " + b.getIdBorne() +
                    " Arret " + b.getArret().getNom() +
                    " Voyages " + b.getNbVoyageVendu() +
                    " Tickets " + b.getNbVentesTickets());
        }
    }

}
