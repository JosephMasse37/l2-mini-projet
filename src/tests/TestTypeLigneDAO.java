
package tests;

import metiers.TypeLigne;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passerelle.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class TestTypeLigneDAO {
    // init des private
    private Connection connexion;
    private TypeLigneDAO typeLigneDAO;

    // b4 each test
    @BeforeEach
    void setUp() throws SQLException, Exception {
        this.connexion = Connexion.getConnexion(); //instancie une co
        this.connexion.setAutoCommit(false); /** on set à false car:[ par defaut MySQL = Auto-Commit càd inscrit direct. la modif ds la BDD]
         = donc on crée un espace de travail temporaire (GRACE A CA QU'ON VA FAIRE UN ROLLBACK!!)
         */
        this.typeLigneDAO = new TypeLigneDAO(this.connexion); // on instancie une nvlle LigneDAO connectée a la bdd
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
    @DisplayName("Creation d'un type de ligne")
    void testCreate() throws DAOException {

        //je cree l'objet mtn
        TypeLigne nouveauType = new TypeLigne("metro");

        //on insere l'objet cree:
        TypeLigne typeCree = typeLigneDAO.create(nouveauType);
        System.out.println("TypeLigne créé avec id : " + typeCree.getIdTypeLigne() +
                " et libelle : " + typeCree.getLibelle());

        // VALIDATION
        assertNotNull(typeCree, "Le type créé ne doit pas être null");
        assertTrue(typeCree.getIdTypeLigne() > 0, "L'ID doit être généré par la BDD");
        assertEquals("metro", typeCree.getLibelle());

        System.out.println("Succès ! Type créé : ID " + typeCree.getIdTypeLigne() + " Libellé : " + typeCree.getLibelle());
    }

    @Test
    @DisplayName("Recherche d'un type de ligne par ID")
    void testFindById() throws DAOException {

        //je cree l'objet mtn
        TypeLigne nouveauType = new TypeLigne("metro");

        //on insere l'objet cree:
        TypeLigne typeCree = typeLigneDAO.create(nouveauType);
        System.out.println("TypeLigne créé avec id : " + typeCree.getIdTypeLigne() +
                " et libelle : " + typeCree.getLibelle());

        System.out.println("Tentative de recherche du typedeligne ID " + typeCree.getIdTypeLigne() + " via le DAO");
        TypeLigne trouvee = typeLigneDAO.find(typeCree.getIdTypeLigne());

        // VALIDATION
        assertNotNull(trouvee, "Le type de ligne devrait être retrouvé en BDD");
        assertEquals(typeCree.getIdTypeLigne(), trouvee.getIdTypeLigne(), "L'ID ne correspond pas");
        assertEquals("metro", trouvee.getLibelle(), "Le libellé ne correspond pas");

        System.out.println("Succès ! Type trouvé : ID " + typeCree.getIdTypeLigne() + " Libellé : " + typeCree.getLibelle());
    }

    @Test
    @DisplayName("Mise à jour d'un type de ligne")
    void testUpdate() throws DAOException {

        //je cree l'objet mtn
        TypeLigne nouveauType = new TypeLigne("metro");

        //on insere l'objet cree:
        TypeLigne typeCree = typeLigneDAO.create(nouveauType);
        System.out.println("TypeLigne créé avec id : " + typeCree.getIdTypeLigne() +
                " et libelle : " + typeCree.getLibelle());

        // on modif l'objet pr tester
        System.out.println("Modification locale : libelle = 'avion'");
        typeCree.setLibelle("avion");

        // update type de ligne grace a update car sinon pas modif ds la BDD
        boolean updateFait = typeLigneDAO.update(typeCree);
        assertTrue(updateFait, "L'update aurait dû modifier 1 type de ligne");
        System.out.println("Commande UPDATE envoyée à la base de données :"+ typeCree.getLibelle());

        // on cherche pr verif
        TypeLigne trouvee = typeLigneDAO.find(typeCree.getIdTypeLigne());

        //VALIDATION de la conformité de l'objet envoyé!! grace aux assertions
        assertNotNull(trouvee);
        assertEquals("avion", trouvee.getLibelle());
        System.out.println("Mise à jour réussie pour la borne ID " + trouvee.getIdTypeLigne());
    }

    @Test
    @DisplayName("Suppression d'un type de ligne")
    void testDelete() throws DAOException {

        //je cree l'objet mtn
        TypeLigne nouveauType = new TypeLigne("metro");

        //on insere l'objet cree:
        TypeLigne typeCree = typeLigneDAO.create(nouveauType);
        System.out.println("TypeLigne créé avec id : " + typeCree.getIdTypeLigne() +
                " et libelle : " + typeCree.getLibelle());

        int idTypeLigneTemp = typeCree.getIdTypeLigne();

        // supp
        boolean suppressionOk = typeLigneDAO.delete(typeCree);
        assertTrue(suppressionOk);

        // vérif
        assertNull(typeLigneDAO.find(idTypeLigneTemp), "Le type de ligne ne devrait plus exister");
        System.out.println("TypeLigne temporaire ID " + idTypeLigneTemp + " supprimé avec succès.");
    }

    @Test
    @DisplayName("Récupération de toutes les bornes")
    void testFindAll() throws DAOException {

        List<TypeLigne> toutesLesTypesLigne = typeLigneDAO.findAll();

        assertNotNull(toutesLesTypesLigne, "La liste ne doit pas être null");
        assertTrue(toutesLesTypesLigne.size() > 0, "La liste devrait contenir au moins un type de ligne");

        System.out.println("Nombre de type de ligne trouvées : " + toutesLesTypesLigne.size());

        for (TypeLigne UnType : toutesLesTypesLigne) {

            System.out.println("Type de Ligne ID " + UnType.getIdTypeLigne() +
                    " Libelle " + UnType.getLibelle() );
        }
    }

}
