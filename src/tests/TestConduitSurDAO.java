package tests;

import metiers.Chauffeur;
import metiers.ConduitSur;
import metiers.Ligne;
import metiers.Vehicule;
import org.junit.jupiter.api.*;
import passerelle.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestConduitSurDAO {
    private Connection connexion;
    private VehiculeDAO vehiculeDAO;
    private ChauffeurDAO chauffeurDAO;
    private LigneDAO ligneDAO;
    private ConduitSurDAO conduitSurDAO;

    @BeforeEach
    void setUp() throws SQLException, DAOException {
        //Instantiation of the DB connection using the Connexion class
        this.connexion = Connexion.getConnexion();

        //Remove auto-commit (auto save) to be able to use rollback after each query
        this.connexion.setAutoCommit(false);

        //Instantiation of DAOs
        this.vehiculeDAO = new VehiculeDAO(this.connexion);
        this.chauffeurDAO = new ChauffeurDAO(this.connexion);
        this.ligneDAO = new LigneDAO(this.connexion);
        this.conduitSurDAO = new ConduitSurDAO(this.connexion);
    }

    @AfterEach
    void tearDown() throws SQLException, DAOException {
        //If the connection exists, use rollback to undo any changes made during the tests
        if (connexion != null) {
            //Undo the changes from the tests
            connexion.rollback();

            //Put back auto-commit
            connexion.setAutoCommit(true);

            //Close the connection
            Connexion.close();
        }
    }

    @Test
    @DisplayName("Creation d'une Conduite")
    void testCreate() throws DAOException {
        // Création des objets nécessaires à la création d'une Conduite
        Chauffeur leChauffeur = chauffeurDAO.find(2);
        Ligne laLigne = ligneDAO.find(6);
        Vehicule leVehicule = vehiculeDAO.find(701);
        String dateHeure = "2026-01-09 22:07:00";

        // Insertion
        ConduitSur conduiteCreee = conduitSurDAO.create(new ConduitSur(leChauffeur, laLigne, leVehicule, LocalDateTime.parse(dateHeure, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        System.out.println("Conduite crée avec " + conduiteCreee);

        // Vérification de la conformité de l'objet envoyé
        assertNotNull(conduiteCreee, "La conduite créée ne devrait pas être null");

        // Vérification des clés étrangères
        assertNotNull(conduiteCreee.getLeChauffeur(), "Le chauffeur lié ne devrait pas être null");
        assertNotNull(conduiteCreee.getUneLigne(), "La ligne liée ne devrait pas être null");
        assertNotNull(conduiteCreee.getUnVehicule(), "Le véhicule lié ne devrait pas être null");

        assertEquals(leChauffeur.getIdChauffeur(), conduiteCreee.getLeChauffeur().getIdChauffeur(), "Le chauffeur ne correspond pas");
        assertEquals(laLigne.getIdLigne(), conduiteCreee.getUneLigne().getIdLigne(), "La ligne ne correspond pas");
        assertEquals(leVehicule.getNumVehicule(), conduiteCreee.getUnVehicule().getNumVehicule(), "Le véhicule ne correspond pas");

        System.out.println("Succès Conduite : " + conduiteCreee);
    }

    @Test
    @DisplayName("Recherche d'une conduite par IDs")
    void testFindById() throws DAOException {

        // Création des objets nécessaires à la création d'une Conduite
        int chauffeurID = 1;
        int ligneID = 1;
        int numVehicule = 53;
        String dateHeure = "2026-01-09 22:07:00";

        System.out.println("Tentative de recherche via le DAO.");
        ConduitSur laConduite = conduitSurDAO.find(1, 1, 53, LocalDateTime.parse("2025-12-02 15:18:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        // Vérification de la conformité de l'objet envoyé
        assertNotNull(laConduite, "La conduite ne devrait pas être null");

        // Vérification des clés étrangères
        assertNotNull(laConduite.getLeChauffeur(), "Le chauffeur lié ne devrait pas être null");
        assertNotNull(laConduite.getUneLigne(), "La ligne liée ne devrait pas être null");
        assertNotNull(laConduite.getUnVehicule(), "Le véhicule lié ne devrait pas être null");

        assertEquals(chauffeurID, laConduite.getLeChauffeur().getIdChauffeur(), "Le chauffeur ne correspond pas");
        assertEquals(ligneID, laConduite.getUneLigne().getIdLigne(), "La ligne ne correspond pas");
        assertEquals(numVehicule, laConduite.getUnVehicule().getNumVehicule(), "Le véhicule ne correspond pas");

        System.out.println("Succès Conduite : " + laConduite);
    }

    @Test
    @DisplayName("Mise à jour d'une Conduite")
    void testUpdate() throws DAOException {
        // Création des objets nécessaires à la création d'une Conduite
        Chauffeur leChauffeur = chauffeurDAO.find(2);
        Ligne laLigne = ligneDAO.find(6);
        Vehicule leVehicule = vehiculeDAO.find(701);
        String dateHeure = "2026-01-02 22:07:00";

        // Insertion
        ConduitSur conduiteCreee = conduitSurDAO.create(new ConduitSur(leChauffeur, laLigne, leVehicule, LocalDateTime.parse(dateHeure, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        System.out.println("Conduite crée avec " + conduiteCreee);

        // Vérification de la conformité de l'objet envoyé
        assertNotNull(conduiteCreee, "La conduite créée ne devrait pas être null");

        // Modification locale puis dans la BDD
        conduiteCreee.setNbValidation(12);
        boolean update = conduitSurDAO.update(conduiteCreee);

        assertTrue(update, "La modification n'a pas pu se faire");

        System.out.println("Succès Conduite : " + conduiteCreee);
    }

    @Test
    @DisplayName("Suppression d'une Conduite")
    void testDelete() throws DAOException {
        // Création des objets nécessaires à la création d'une Conduite
        Chauffeur leChauffeur = chauffeurDAO.find(2);
        Ligne laLigne = ligneDAO.find(6);
        Vehicule leVehicule = vehiculeDAO.find(701);
        String dateHeure = "2026-01-02 22:07:00";

        // Insertion
        ConduitSur conduiteCreee = conduitSurDAO.create(new ConduitSur(leChauffeur, laLigne, leVehicule, LocalDateTime.parse(dateHeure, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        System.out.println("Conduite crée avec " + conduiteCreee);

        // Vérification de la conformité de l'objet envoyé
        assertNotNull(conduiteCreee, "La conduite créée ne devrait pas être null");

        // Suppression BDD
        boolean delete = conduitSurDAO.delete(conduiteCreee);

        assertTrue(delete, "La suppression n'a pas pu se faire");

        System.out.println("Succès Conduite : " + conduiteCreee);
    }

    @Test
    @DisplayName("Récupération de toutes les Conduites d'un chauffeur")
    void testGetConduitesUnChauffeur() throws DAOException {
        // Appel méthode
        List<ConduitSur> toutesLesConduites = conduitSurDAO.getConduiteUnChauffeur(1);

        // Vérification de la conformité de la liste
        assertNotNull(toutesLesConduites, "La liste ne doit pas être null");
        assertFalse(toutesLesConduites.isEmpty(), "La liste devrait contenir au moins une conduite");
        System.out.println(" Nombre de Chauffeurs trouvés : " + toutesLesConduites.size());

        for (ConduitSur uneConduite : toutesLesConduites) {
            System.out.println("Vérification Conduite : " + uneConduite);
            // Vérification des clés étrangères
            assertNotNull(uneConduite.getLeChauffeur(), "Le chauffeur lié ne devrait pas être null");
            assertNotNull(uneConduite.getUneLigne(), "La ligne liée ne devrait pas être null");
            assertNotNull(uneConduite.getUnVehicule(), "Le véhicule lié ne devrait pas être null");
        }
    }

    @Test
    @DisplayName("Récupération de toutes les Conduites d'une ligne")
    void testGetConduitesUneLigne() throws DAOException {
        // Appel méthode
        List<ConduitSur> toutesLesConduites = conduitSurDAO.getConduiteUneLigne(1);

        // Vérification de la conformité de la liste
        assertNotNull(toutesLesConduites, "La liste ne doit pas être null");
        assertFalse(toutesLesConduites.isEmpty(), "La liste devrait contenir au moins une conduite");
        System.out.println(" Nombre de Chauffeurs trouvés : " + toutesLesConduites.size());

        for (ConduitSur uneConduite : toutesLesConduites) {
            System.out.println("Vérification Conduite : " + uneConduite);
            // Vérification des clés étrangères
            assertNotNull(uneConduite.getLeChauffeur(), "Le chauffeur lié ne devrait pas être null");
            assertNotNull(uneConduite.getUneLigne(), "La ligne liée ne devrait pas être null");
            assertNotNull(uneConduite.getUnVehicule(), "Le véhicule lié ne devrait pas être null");
        }
    }

    @Test
    @DisplayName("Récupération de toutes les Conduites d'un véhicule")
    void testGetConduiteUnVehicule() throws DAOException {
        // Appel méthode
        List<ConduitSur> toutesLesConduites = conduitSurDAO.getConduiteUnVehicule(54);

        // Vérification de la conformité de la liste
        assertNotNull(toutesLesConduites, "La liste ne doit pas être null");
        assertFalse(toutesLesConduites.isEmpty(), "La liste devrait contenir au moins une conduite");
        System.out.println(" Nombre de Chauffeurs trouvés : " + toutesLesConduites.size());

        for (ConduitSur uneConduite : toutesLesConduites) {
            System.out.println("Vérification Conduite : " + uneConduite);
            // Vérification des clés étrangères
            assertNotNull(uneConduite.getLeChauffeur(), "Le chauffeur lié ne devrait pas être null");
            assertNotNull(uneConduite.getUneLigne(), "La ligne liée ne devrait pas être null");
            assertNotNull(uneConduite.getUnVehicule(), "Le véhicule lié ne devrait pas être null");
        }
    }

    @Test
    @DisplayName("Récupération de toutes les Conduites d'un chauffeur, d'un véhicule et d'une ligne")
    void testGetConduitesUnChauffeurUneLigneUnVehicule() throws DAOException {
        // Appel méthode
        List<ConduitSur> toutesLesConduites = conduitSurDAO.getConduitesUnChauffeurUneLigneUnVehicule(1,1,53);

        // Vérification de la conformité de la liste
        assertNotNull(toutesLesConduites, "La liste ne doit pas être null");
        assertFalse(toutesLesConduites.isEmpty(), "La liste devrait contenir au moins une conduite");
        System.out.println(" Nombre de Chauffeurs trouvés : " + toutesLesConduites.size());

        for (ConduitSur uneConduite : toutesLesConduites) {
            System.out.println("Vérification Conduite : " + uneConduite);
            // Vérification des clés étrangères
            assertNotNull(uneConduite.getLeChauffeur(), "Le chauffeur lié ne devrait pas être null");
            assertNotNull(uneConduite.getUneLigne(), "La ligne liée ne devrait pas être null");
            assertNotNull(uneConduite.getUnVehicule(), "Le véhicule lié ne devrait pas être null");
        }
    }

    @Test
    @DisplayName("Récupération de toutes les Conduites")
    void testFindAll() throws DAOException {
        // Appel méthode
        List<ConduitSur> toutesLesConduites = conduitSurDAO.findAll();

        // Vérification de la conformité de la liste
        assertNotNull(toutesLesConduites, "La liste ne doit pas être null");
        assertFalse(toutesLesConduites.isEmpty(), "La liste devrait contenir au moins une conduite");
        System.out.println(" Nombre de Chauffeurs trouvés : " + toutesLesConduites.size());

        for (ConduitSur uneConduite : toutesLesConduites) {
            System.out.println("Vérification Conduite : " + uneConduite);
            // Vérification des clés étrangères
            assertNotNull(uneConduite.getLeChauffeur(), "Le chauffeur lié ne devrait pas être null");
            assertNotNull(uneConduite.getUneLigne(), "La ligne liée ne devrait pas être null");
            assertNotNull(uneConduite.getUnVehicule(), "Le véhicule lié ne devrait pas être null");
        }
    }
}
