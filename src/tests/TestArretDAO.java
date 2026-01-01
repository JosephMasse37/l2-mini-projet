package tests;

import metiers.Arret;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passerelle.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class TestArretDAO {
    // init des private
    private Connection connexion;
    private ArretDAO arretDAO;

    // b4 each test
    @BeforeEach
    void setUp() throws SQLException, Exception {
        this.connexion = Connexion.getConnexion(); //instancie une co
        this.connexion.setAutoCommit(false); /** on set à false car:[ par defaut MySQL = Auto-Commit càd inscrit direct. la modif ds la BDD]
         = donc on crée un espace de travail temporaire (GRACE A CA QU'ON VA FAIRE UN ROLLBACK!!)
         */
        this.arretDAO = new ArretDAO(this.connexion); // on instancie une nvlle LigneDAO connectée a la bdd
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
    @DisplayName("Creation d'un arrêt")
    void testCreate() throws DAOException {

        //je cree l'objet mtn
        Arret nouvelArret = new Arret("Paul-Louis-Courier", 47.1234, 0.5678);

        //on insere l'objet cree:
        Arret arretCree = arretDAO.create(nouvelArret);
        System.out.println(" Arret créée avec le nom : " + arretCree.getNom() + " et latitude : " + arretCree.getLatitude() + " et longitude : " + arretCree.getLongitude());

        // VALIDATION
        assertNotNull(arretCree, "L'arrêt créé ne doit pas être null");
        assertTrue(arretCree.getIdArret() > 0, "L'ID doit être généré par la BDD");
        assertEquals("Paul-Louis-Courier", arretCree.getNom());

        System.out.println("Succès | ID:" + arretCree.getIdArret() + " | Nom:" + arretCree.getNom() + " | Latitude:" + arretCree.getLatitude() + " | Longitude:" + arretCree.getLongitude());
    }


    @Test
    @DisplayName("Recherche d'un arret par ID")
    void testFindById() throws DAOException {

        //je cree l'objet et l'insere mtn
        Arret arretCree = arretDAO.create(new Arret("Leonardo", 47.0, 0.0));
        System.out.println(" Arret créée avec le nom : " + arretCree.getNom() + " et latitude : " + arretCree.getLatitude() + " et longitude : " + arretCree.getLongitude());

        //pcq Java envoie l'objet avec l'ID 0 alors :
        assertTrue(arretCree.getIdArret() > 0, "L'ID de l'arret devrait avoir été généré par la BDD");

        // on essaie de la retrouver avec son nv ID
        System.out.println("Tentative de recherche de l'ID " + arretCree.getIdArret() + " via le DAO");
        Arret trouvee = arretDAO.find(arretCree.getIdArret());

        //VALIDATION de la conformité de l'objet envoyé!! grace aux assertions
        assertEquals(arretCree.getIdArret(), trouvee.getIdArret(), "L'ID ne correspond pas");
        assertEquals(arretCree.getNom(), trouvee.getNom(), "Le nom ne correspond pas");
        assertEquals(arretCree.getLongitude(), trouvee.getLongitude(), "La Longitude ne correspond pas");
        assertEquals(arretCree.getLatitude(), trouvee.getLatitude(), "La Latitude ne correspond pas");

        System.out.println("Succès | ID:" + trouvee.getIdArret() + " | Nom:" + trouvee.getNom() + " | Latitude:" + trouvee.getLatitude() + " | Longitude:" + trouvee.getLongitude());
    }

    @Test
    @DisplayName("Mise à jour d'un arret")
    void testUpdate() throws DAOException {

        //je cree l'objet et l'insere mtn
        Arret arretCree = arretDAO.create(new Arret("Leonardo", 47.0, 0.0));
        System.out.println(" Arret créée avec le nom : " + arretCree.getNom() + " et latitude : " + arretCree.getLatitude() + " et longitude : " + arretCree.getLongitude());

        // on modif l'objet pr tester
        System.out.println("Modification locale : Libellé = '14', Durée = 25");
        arretCree.setNom("Monaco");

        // update arretCree grace a update car sinon pas modif ds la BDD
        boolean updateFait = arretDAO.update(arretCree);
        assertTrue(updateFait, "L'update aurait dû modifier 1 arret");
        System.out.println("Commande UPDATE envoyée à la base de données.");

        // on cherche pr verif
        Arret trouvee = arretDAO.find(arretCree.getIdArret());

        //VALIDATION de la conformité de l'objet envoyé!! grace aux assertions
        assertNotNull(trouvee);
        assertEquals("Monaco", trouvee.getNom());
        System.out.println("Succès : La base de données a bien été mise à jour. - Nom relu: " + trouvee.getNom() );
    }

    @Test
    @DisplayName("Suppression d'une ligne")
    void testDelete() throws DAOException {

        //je cree l'objet et l'insere mtn
        Arret arretCree = arretDAO.create(new Arret("Leonardo", 47.0, 0.0));
        System.out.println(" Arret créée avec le nom : " + arretCree.getNom() + " et latitude : " + arretCree.getLatitude() + " et longitude : " + arretCree.getLongitude());

        assertNotNull(arretDAO.find(arretCree.getIdArret()), "La ligne devrait exister avant la suppression");
        System.out.println(" Arret créée avec l'ID : " + arretCree.getIdArret() + " (" + arretCree.getNom() + ")");

        // on supp
        boolean suppressionOk = arretDAO.delete(arretCree);
        assertTrue(suppressionOk, "Confirmation de la suppression"); //verif
        System.out.println("Suppression effectuée.");

        Arret ArretSupp = arretDAO.find(arretCree.getIdArret());
        assertNull(ArretSupp, "L' Arret ne devrait plus exister après suppression");
        System.out.println("Confirmation : L' Arret est bien supprimée de la BDD.");
    }

    @Test
    @DisplayName("Récupération de tous les arrets")
    void testFindAll() throws DAOException {

        // appelle la méthode
        List<Arret> tousLesArrets = arretDAO.findAll();

        //VALIDATION de la conformité de l'objet envoyé!! grace aux assertions
        assertNotNull(tousLesArrets, "La liste ne doit pas être null");
        assertTrue(tousLesArrets.size() > 0, "La liste devrait contenir au moins un arret");
        System.out.println(" Nombre d'arrets trouvées : " + tousLesArrets.size());

        for (Arret UnArret : tousLesArrets) {
            System.out.println("Vérification Arret ID " + UnArret.getIdArret() + " : " + UnArret.getNom());

            assertNotNull(UnArret.getNom(), "Le nom de l'arrêt ne doit pas être null");
           // assertNotNull pas possible car type primitifs donc:
            assertTrue(UnArret.getLatitude() != 0.0, "La latitude est vide (0.0)");
            assertTrue(UnArret.getLongitude() != 0.0, "La longitude est vide (0.0)");
            System.out.println("Nom: " + UnArret.getNom() + " | Latitude: " + UnArret.getLatitude() + " | Longitude : " + UnArret.getLongitude()); }
    }

    @Test
    @DisplayName("Recherche par début de nom ")
    void testFindByDebutNom() throws DAOException {
        // cree un obj
        arretDAO.create(new Arret("Lycée Grandmont", 47.35, 0.70));

        // lance la recherche sur le début "Lycée"
        String recherche = "Lycée";
        List<Arret> resultats = arretDAO.findByDebutNom(recherche);

        assertNotNull(resultats, "La liste ne doit pas être null");
        assertTrue(resultats.size() >= 1, "On devrait trouver au moins les 2 arrêts créés");

        for (Arret UnArret : resultats) {

            assertNotNull(UnArret.getNom(), "L'arrêt doit avoir un nom");
            // assertNotNull pas possible car type primitifs donc:
            assertTrue(UnArret.getLatitude() != 0.0, "La latitude est vide (0.0)");
            assertTrue(UnArret.getLongitude() != 0.0, "La longitude est vide (0.0)");

            System.out.println("Succès | Trouvé : " + UnArret.getNom() + " [ID: " + UnArret.getIdArret() + "]");
        }
    }
}
