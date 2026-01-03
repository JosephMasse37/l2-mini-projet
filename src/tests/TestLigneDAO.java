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
import java.util.List;

import passerelle.*;


public class TestLigneDAO {
 // init des private
    private Connection connexion;
    private LigneDAO ligneDAO;
    // recup les dao needed
    private ArretDAO arretDAO;
    private TypeLigneDAO typeDAO;

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

        //je cree l'objet mtn
        Arret depart = arretDAO.find(4);
        Arret arrivee = arretDAO.find(14);
        TypeLigne type = typeDAO.find(1);
        Ligne ligne = new Ligne("14",type,depart,arrivee,8);

        //on insere la ligne cree:
        Ligne ligneCreee = ligneDAO.create(ligne);
        System.out.println(" Ligne créée avec l'ID : " + ligneCreee.getIdLigne() + " (" + ligneCreee.getLibelle() + ")");


        //VALIDATION de la conformité de l'objet envoyé!! grace aux assertions

        assertNotNull(ligneCreee, "La ligne créée ne devrait pas être null");
        assertEquals("14", ligneCreee.getLibelle(), "Le libellé ne correspond pas");
        assertEquals(type.getIdTypeLigne(), ligneCreee.getTypeLigne().getIdTypeLigne(), "Le type de ligne ne correspond pas");
        assertEquals(depart.getIdArret(), ligneCreee.getArretDepart().getIdArret(),"Le depart ne correspond pas");
        assertEquals(arrivee.getIdArret(), ligneCreee.getArretArrive().getIdArret(),"L'arrivee ne correspond pas");
        assertEquals(8, ligneCreee.getDuree(), "La durée ne correspond pas");
        //pcq Java envoie l'objet avec l'ID 0 alors :
        assertTrue(ligneCreee.getIdLigne() > 0, "L'ID de la ligne devrait avoir été généré par la BDD");
        System.out.println("Succès | ID:" + ligneCreee.getIdLigne() + " | Lib:" + ligneCreee.getLibelle() + "| Type:" + ligneCreee.getTypeLigne().getLibelle()+ " | Durée:" + ligneCreee.getDuree() + " min | Dep:" + ligneCreee.getArretDepart().getNom() + " | Arr:" + ligneCreee.getArretArrive().getNom());

    }

    @Test
    @DisplayName("Recherche d'une ligne par ID")
    void testFindById() throws DAOException {

        //je cree l'objet mtn
        Arret depart = arretDAO.find(4);
        Arret arrivee = arretDAO.find(14);
        TypeLigne type = typeDAO.find(1);
        Ligne ligne = new Ligne("14",type,depart,arrivee,8);

        //on insere la ligne cree:
        Ligne ligneCreee = ligneDAO.create(ligne);
        System.out.println(" Ligne créée avec l'ID : " + ligneCreee.getIdLigne() + " (" + ligneCreee.getLibelle() + ")");

        //pcq Java envoie l'objet avec l'ID 0 alors :
        assertTrue(ligneCreee.getIdLigne() > 0, "L'ID de la ligne devrait avoir été généré par la BDD");

        // on essaie de la retrouver avec son nv ID
        System.out.println("Tentative de recherche de l'ID " + ligneCreee.getIdLigne() + " via le DAO");
        Ligne trouvee = ligneDAO.find(ligneCreee.getIdLigne());

        //VALIDATION de la conformité de l'objet envoyé!! grace aux assertions
        assertEquals(ligneCreee.getIdLigne(), trouvee.getIdLigne(), "L'ID ne correspond pas");
        assertEquals(ligneCreee.getLibelle(), trouvee.getLibelle(), "Le libellé ne correspond pas");
        assertEquals(ligneCreee.getDuree(), trouvee.getDuree(), "La durée ne correspond pas");
        assertEquals(ligneCreee.getTypeLigne().getIdTypeLigne(), trouvee.getTypeLigne().getIdTypeLigne());
        assertEquals(ligneCreee.getArretDepart().getIdArret(), trouvee.getArretDepart().getIdArret());
        assertEquals(ligneCreee.getArretArrive().getIdArret(), trouvee.getArretArrive().getIdArret());
        System.out.println("Succès | ID:" + trouvee.getIdLigne() + " | Lib:" + trouvee.getLibelle() + "| Type:" + trouvee.getTypeLigne().getLibelle()+ " | Durée:" + trouvee.getDuree() + " min | Dep:" + trouvee.getArretDepart().getNom() + " | Arr:" + trouvee.getArretArrive().getNom());

    }

    @Test
    @DisplayName("Mise à jour d'une ligne")
    void testUpdate() throws DAOException {

        //je cree l'objet mtn
        Arret depart = arretDAO.find(4);
        Arret arrivee = arretDAO.find(14);
        TypeLigne type = typeDAO.find(1);
        Ligne ligne = new Ligne("14",type,depart,arrivee,8);

        //on insere la ligne cree:
        Ligne ligneCreee = ligneDAO.create(ligne);
        System.out.println("Ligne créée - ID: " + ligneCreee.getIdLigne() + ", Libellé: " + ligneCreee.getLibelle() + ", Durée: " + ligneCreee.getDuree());

        // on modif l'objet pr tester
        System.out.println("Modification locale : Libellé -> '14', Durée -> 25");
        ligneCreee.setLibelle("14");
        ligneCreee.setDuree(25);

        // update ligneCreee grace a update car sinon pas modif ds la BDD
        boolean updateFait = ligneDAO.update(ligneCreee);
        assertTrue(updateFait, "L'update aurait dû modifier 1 ligne"); //cr mm id
        System.out.println("Commande UPDATE envoyée à la base de données.");

        // on cherche pr verif
        Ligne trouvee = ligneDAO.find(ligneCreee.getIdLigne());

        //VALIDATION de la conformité de l'objet envoyé!! grace aux assertions
        assertNotNull(trouvee);
        assertEquals("14", trouvee.getLibelle());
        assertEquals(25, trouvee.getDuree());
        System.out.println("Succès : La base de données a bien été mise à jour. - Libellé relu: " + trouvee.getLibelle() + ", Durée relue: " + trouvee.getDuree());
    }

    @Test
    @DisplayName("Suppression d'une ligne")
    void testDelete() throws DAOException {

        //je cree l'objet mtn
        Arret depart = arretDAO.find(4);
        Arret arrivee = arretDAO.find(14);
        TypeLigne type = typeDAO.find(1);
        Ligne ligne = new Ligne("14",type,depart,arrivee,8);

        //on insere la ligne cree:
        Ligne ligneCreee = ligneDAO.create(ligne);
        assertNotNull(ligneDAO.find(ligneCreee.getIdLigne()), "La ligne devrait exister avant la suppression");
        System.out.println(" Ligne créée avec l'ID : " + ligneCreee.getIdLigne() + " (" + ligneCreee.getLibelle() + ")");

        // on supp
        boolean suppressionOk = ligneDAO.delete(ligneCreee);
        assertTrue(suppressionOk, "Confirmation de la suppression"); //verif
        System.out.println("Suppression effectuée.");

        Ligne LigneSupp = ligneDAO.find(ligneCreee.getIdLigne());
        assertNull(LigneSupp, "La ligne ne devrait plus exister après suppression");
        System.out.println("Confirmation : la ligne est bien supprimée de la BDD.");
    }

    @Test
    @DisplayName("Récupération de toutes les lignes")
    void testFindAll() throws DAOException {

        // appelle la méthode
        List<Ligne> toutesLesLignes = ligneDAO.findAll();

        //VALIDATION de la conformité de l'objet envoyé!! grace aux assertions
        assertNotNull(toutesLesLignes, "La liste ne doit pas être null");
        assertTrue(toutesLesLignes.size() > 0, "La liste devrait contenir au moins une ligne");
        System.out.println(" Nombre de lignes trouvées : " + toutesLesLignes.size());

        for (Ligne Uneligne : toutesLesLignes) {
            System.out.println("Vérification Ligne ID " + Uneligne.getIdLigne() + " : " + Uneligne.getLibelle());
            assertNotNull(Uneligne.getArretDepart(), "L'arrêt de départ est null pour la ligne ID: " + Uneligne.getIdLigne());
            assertNotNull(Uneligne.getArretArrive(), "L'arrêt d'arrivée est null pour la ligne ID: " + Uneligne.getIdLigne());
        }
    }

    @Test
    @DisplayName("Recherche des lignes par Type")
    void testFindByType() throws DAOException {

        // id 1 pour 'Bus'
        int typeATester = 1;

        // recup de la liste
        List<Ligne> ListeLignesDuType = ligneDAO.findByType(typeATester);

        assertNotNull(ListeLignesDuType, "La liste ne doit pas être null");
        assertTrue(ListeLignesDuType.size() > 0, "Il devrait y avoir au moins une ligne pour ce type en BDD");

        System.out.println("Résultat pour le Type ID " + typeATester + " :");

        for (Ligne l : ListeLignesDuType) {
            // verif que le type est bien celui demandé
            assertEquals(typeATester, l.getTypeLigne().getIdTypeLigne(), "La ligne n'appartient pas au bon type");
            // On vérifie que les arrêts sont bien chargés (pas de null) sinon crash
            assertNotNull(l.getArretDepart(), "Arrêt départ manquant pour la ligne " + l.getIdLigne());
            assertNotNull(l.getArretArrive(), "Arrêt arrivée manquant pour la ligne " + l.getIdLigne());

            System.out.println("Succès | ID:" + l.getIdLigne() + " | Lib:" + l.getLibelle() + "| Type:" + l.getTypeLigne().getLibelle()+ " | Durée:" + l.getDuree() + " min | Dep:" + l.getArretDepart().getNom() + " | Arr:" + l.getArretArrive().getNom());
        }

        System.out.println(" Succès : " + ListeLignesDuType.size() + " lignes filtrées avec succès.");
    }

}
