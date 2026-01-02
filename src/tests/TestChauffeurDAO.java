
package tests;

import metiers.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

        import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import passerelle.*;


public class TestChauffeurDAO {
    // init des private
    private Connection connexion;
    private ChauffeurDAO chauffeurDAO;
    // recup les dao needed
    UtilisateurDAO utilisateurDAO = new UtilisateurDAO(connexion);


    // b4 each test
    @BeforeEach
    void setUp() throws SQLException, Exception {
        this.connexion = Connexion.getConnexion(); //instancie une co
        this.connexion.setAutoCommit(false); /** on set à false car:[ par defaut MySQL = Auto-Commit càd inscrit direct. la modif ds la BDD]
         = donc on crée un espace de travail temporaire (GRACE A CA QU'ON VA FAIRE UN ROLLBACK!!)
         */
        this.chauffeurDAO = new ChauffeurDAO(this.connexion); // on instancie une nvlle LigneDAO connectée a la bdd
        this.utilisateurDAO = new UtilisateurDAO(this.connexion);
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
    @DisplayName("Creation d'un Chauffeur")
    void testCreate() throws DAOException {

        //je cree l'objet mtn car le Chauffeur qui ne peut pas vivre sans l'Utilisateur
        Utilisateur meline = new Utilisateur("meline.azerty", "mdp123", "Meline", "azerty");
        utilisateurDAO.create(meline); //crea
        System.out.println("Utilisateur meline.azerty créé pour le test.");

        Chauffeur UnChauffeur = new Chauffeur(true,meline);

        //on insere la ligne cree:
        Chauffeur ChauffeurCreee = chauffeurDAO.create(UnChauffeur);
        System.out.println(" Chauffeur crée avec : ID " + ChauffeurCreee.getIdChauffeur() + " créé pour " + meline.getUsername());

        //VALIDATION de la conformité de l'objet envoyé!! grace aux assertions
        assertNotNull(ChauffeurCreee, "Le Chauffeur créée ne devrait pas être null");

        //  vérifie que le lien w l'utilisateur est ok
        assertNotNull(ChauffeurCreee.getUtilisateur(), "L'utilisateur lié ne devrait pas être null");
        assertEquals(meline.getUsername(), ChauffeurCreee.getUtilisateur().getUsername(), "Le username ne correspond pas");
        //pcq Java envoie l'objet avec l'ID 0 alors :
        assertTrue(ChauffeurCreee.getIdChauffeur() > 0, "L'ID du Chauffeur devrait avoir été généré par la BDD");
        System.out.println("Succès : Chauffeur ID " + ChauffeurCreee.getIdChauffeur() + " créé pour " + meline.getUsername());
    }

    @Test
    @DisplayName("Recherche d'un Chauffeur par ID")
    void testFindById() throws DAOException {

        //je cree l'objet mtn car le Chauffeur qui ne peut pas vivre sans l'Utilisateur
        Utilisateur meline = new Utilisateur("meline.azerty", "mdp123", "Meline", "azerty");
        utilisateurDAO.create(meline); //crea
        System.out.println("Utilisateur meline.azerty créé pour le test.");

        Chauffeur UnChauffeur = new Chauffeur(true,meline);

        //on insere la ligne cree:
        Chauffeur ChauffeurCreee = chauffeurDAO.create(UnChauffeur);
        System.out.println(" Chauffeur crée avec : ID " + ChauffeurCreee.getIdChauffeur() + " créé pour " + meline.getUsername());

        //pcq Java envoie l'objet avec l'ID 0 alors :
        assertTrue(ChauffeurCreee.getIdChauffeur() > 0, "L'ID du Chauffeur devrait avoir été généré par la BDD");

        // on essaie de la retrouver avec son ID
        System.out.println("Tentative de recherche de l'ID " + ChauffeurCreee.getIdChauffeur() + " via le DAO");
        Chauffeur trouve = chauffeurDAO.find(ChauffeurCreee.getIdChauffeur());

        //VALIDATION de la conformité de l'objet envoyé!! grace aux assertions
        assertEquals(ChauffeurCreee.getIdChauffeur(), trouve.getIdChauffeur(), "L'ID ne correspond pas");
        //  vérifie que le lien w l'utilisateur est ok
        assertNotNull(trouve.getUtilisateur(), "L'utilisateur lié ne devrait pas être null");
        assertEquals("meline.azerty", trouve.getUtilisateur().getUsername(), "Le username ne correspond pas");
        assertEquals(ChauffeurCreee.isFormation_tram(), trouve.isFormation_tram(), "La formation tram ne correspond pas");

        System.out.println("Succès : Chauffeur ID trouve " + ChauffeurCreee.getIdChauffeur() + " créé pour " + meline.getUsername());

    }

    @Test
    @DisplayName("Mise à jour d'un Chauffeur")
    void testUpdate() throws DAOException {

        //je cree l'objet mtn car le Chauffeur qui ne peut pas vivre sans l'Utilisateur
        Utilisateur meline = new Utilisateur("meline.azerty", "mdp123", "Meline", "azerty");
        utilisateurDAO.create(meline); //crea
        System.out.println("Utilisateur meline.azerty créé pour le test.");

        Chauffeur UnChauffeur = new Chauffeur(false,meline);

        //on insere la ligne cree:
        Chauffeur ChauffeurCreee = chauffeurDAO.create(UnChauffeur);
        System.out.println(" Chauffeur crée avec : ID " + ChauffeurCreee.getIdChauffeur() + " créé pour " + meline.getUsername());

        // on modif l'objet pr tester
        System.out.println("Modification locale : formation tram -> 'vrai'");
        ChauffeurCreee.setFormation_tram(true);

        // update ChauffeurCreee grace a update car sinon pas modif ds la BDD
        boolean updateFait = chauffeurDAO.update(ChauffeurCreee);
        assertTrue(updateFait, "L'update aurait dû modifier 1 ligne");
        System.out.println("Commande UPDATE envoyée à la base de données.");

        // on cherche pr verif
        Chauffeur trouvee = chauffeurDAO.find(ChauffeurCreee.getIdChauffeur());

        //VALIDATION de la conformité de l'objet envoyé!! grace aux assertions
        assertNotNull(trouvee);
        assertEquals(true, trouvee.isFormation_tram());
        System.out.println("Succès : Chauffeur modifié ID " + ChauffeurCreee.getIdChauffeur() + " créé pour " + meline.getUsername());
    }

    @Test
    @DisplayName("Suppression d'un Chauffeur")
    void testDelete() throws DAOException {

        //je cree l'objet mtn car le Chauffeur qui ne peut pas vivre sans l'Utilisateur
        Utilisateur meline = new Utilisateur("meline.azerty", "mdp123", "Meline", "azerty");
        utilisateurDAO.create(meline); //crea
        System.out.println("Utilisateur meline.azerty créé pour le test.");

        Chauffeur UnChauffeur = new Chauffeur(false,meline);

        //on insere la ligne cree:
        Chauffeur ChauffeurCreee = chauffeurDAO.create(UnChauffeur);
        System.out.println(" Chauffeur crée avec : ID " + ChauffeurCreee.getIdChauffeur() + " créé pour " + meline.getUsername());

        // on supp
        boolean suppressionOk = chauffeurDAO.delete(ChauffeurCreee);
        assertTrue(suppressionOk, "Confirmation de la suppression"); //verif
        System.out.println("Suppression effectuée.");

        Chauffeur ChauffeurSupp = chauffeurDAO.find(ChauffeurCreee.getIdChauffeur());
        assertNull(ChauffeurSupp, "Le Chauffeur ne devrait plus exister après suppression");
        System.out.println("Confirmation : le Chauffeur est bien supprimée de la BDD.");
    }


    @Test
    @DisplayName("Récupération de tous les Chauffeurs")
    void testFindAll() throws DAOException {

        // appelle la méthode
        List<Chauffeur> tousLesChauffeurs = chauffeurDAO.findAll();

        //VALIDATION de la conformité de l'objet envoyé!! grace aux assertions
        assertNotNull(tousLesChauffeurs, "La liste ne doit pas être null");
        assertTrue(tousLesChauffeurs.size() > 0, "La liste devrait contenir au moins un Chauffeur");
        System.out.println(" Nombre de Chauffeurs trouvés : " + tousLesChauffeurs.size());

        for (Chauffeur UnChauffeurs : tousLesChauffeurs) {
            System.out.println("Vérification Chauffeur ID " + UnChauffeurs.getIdChauffeur() + " : " + UnChauffeurs.getUtilisateur().getUsername());
            assertNotNull(UnChauffeurs.getUtilisateur(), "L'utilisateur lié ne devrait pas etre null pour le chauffeur ID: " + UnChauffeurs.getIdChauffeur());
            assertNotNull(UnChauffeurs.getUtilisateur().getUsername(), "Le username ne devrait pas etre null pour le chauffeur ID: " + UnChauffeurs.getIdChauffeur()); }
    }


}
