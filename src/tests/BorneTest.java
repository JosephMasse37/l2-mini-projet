package tests;

import metiers.Arret;
import metiers.Borne;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BorneTest {

    private Borne borne1; //ici pr que ts les testes puissent use it via setup
    private Arret arretVaucanson;

    @BeforeEach
    void setUp() {
        arretVaucanson = new Arret(1,"Lycée Vaucanson",47.423345013265944, 0.7060883395256786);
        borne1 = new Borne(1,104,80,arretVaucanson);

    }

     @Test
     void testConstructeurAvecID() {
         assertEquals(1, borne1.getIdBorne());
         assertEquals(104, borne1.getNbVoyageVendu());
         assertEquals(80, borne1.getNbVentesTickets());
         assertEquals(arretVaucanson, borne1.getArret());
         assertTrue(arretVaucanson.getListeBornes().contains(borne1), "L'arrêt doit contenir la borne");
    }

    @Test
    void testConstructeurSansID() {
        //pas ds setUp car peu ralentir les autres test
        Borne borne2 = new Borne(800,600,arretVaucanson);
        assertEquals(800, borne2.getNbVoyageVendu());
        assertEquals(600, borne2.getNbVentesTickets());
        assertEquals(arretVaucanson, borne2.getArret());
        assertTrue(arretVaucanson.getListeBornes().contains(borne2), "L'arrêt doit contenir la borne");
    }

    @Test
    void testSetArretRelationBidirectionnelleAjout() {
        // Arrange : creer un nouvel arrêt
        Arret nouvelArret = new Arret(2, "Gare de Tours", 47.39, 0.69);

        // Act : on change l'arrêt de la borne
        borne1.setArret(nouvelArret);

        // Assert
        assertEquals(nouvelArret, borne1.getArret(), "La borne doit pointer sur le nouvel arrêt");
        // verif que l'arrêt a aussi ajouté la borne dans sa liste de bornes
        assertTrue(nouvelArret.getListeBornes().contains(borne1));
        assertFalse(arretVaucanson.getListeBornes().contains(borne1), "Vaucanson ne doit plus avoir la borne");
    }






}