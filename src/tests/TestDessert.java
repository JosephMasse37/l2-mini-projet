package tests;

import metiers.Arret;
import metiers.Ligne;
import metiers.TypeLigne;
import metiers.Dessert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestDessert {

    @Test
    void testDessert() {
        // Création des objets nécessaires
        Arret arret = new Arret(1, "Gare de Tours", 48.8566, 2.3522);
        TypeLigne typeLigne = new TypeLigne(1, "Bus");
        Ligne ligne = new Ligne("Ligne A", typeLigne, 10);

        // Vérification préconditions
        assertTrue(arret.getListeLignesDesservies().isEmpty(), "Arret should have no lignes initially");
        assertTrue(ligne.getArretsDesservis().isEmpty(), "Ligne should have no arrets initially");

        // Instanciation
        Dessert dessert = new Dessert(arret, ligne);

        // Vérification objet
        assertNotNull(dessert, "Dessert object should not be null after instantiation");

        // Vérification getters
        assertEquals(arret, dessert.getUnArret(), "Arret should be correctly assigned");
        assertEquals(ligne, dessert.getUneLigne(), "Ligne should be correctly assigned");

        // Vérification relations bidirectionnelles
        assertTrue(
                arret.getListeLignesDesservies().contains(ligne),
                "Arret should contain the ligne in listeLignesDesservies"
        );

        assertTrue(
                ligne.getArretsDesservis().contains(arret),
                "Ligne should contain the arret in arretsDesservis"
        );

        // Test des setters
        Arret nouvelArret = new Arret(2, "Place de la Mairie", 48.8570, 2.3500);
        TypeLigne nouveauType = new TypeLigne(2, "Tram");
        Ligne nouvelleLigne = new Ligne("Ligne B", nouveauType, 20);

        dessert.setUnArret(nouvelArret);
        dessert.setUneLigne(nouvelleLigne);

        assertEquals(nouvelArret, dessert.getUnArret(), "Arret should be updated");
        assertEquals(nouvelleLigne, dessert.getUneLigne(), "Ligne should be updated");

        // Test toString
        String dessertString = dessert.toString();
        assertNotNull(dessertString, "toString() should not return null");
        assertTrue(dessertString.contains("Place de la Mairie"), "toString() should contain arret name");
        assertTrue(dessertString.contains("Ligne B"), "toString() should contain ligne libelle");
    }
}
