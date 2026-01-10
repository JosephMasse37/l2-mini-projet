package metiers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

class TestConduitSur {

    private Chauffeur chauffeur;
    private Ligne ligne;
    private Vehicule vehicule;
    private LocalDateTime dateHeure;
    private TypeLigne typeLigne;

    @BeforeEach
    void setUp() {
        Utilisateur utilisateur = new Utilisateur("froland.test", "14d5ba5b0f9a01c847a64270d50bf52d71ebf073a22643a855c55c1c9e613688", "Froland", "Test");
        chauffeur = new Chauffeur(500, true, utilisateur);
        typeLigne = new TypeLigne(5,"Test");
        ligne = new Ligne(100, "Ligne 2", typeLigne);
        vehicule = new Tram(67, "Alstom", "Citadis", LocalDate.now(), LocalDate.now(), LocalDateTime.now());
        dateHeure = LocalDateTime.of(2025, 1, 1, 8, 30);
    }

    @Test
    void testConstructeurSansNbValidation() {
        ConduitSur conduitSur = new ConduitSur(chauffeur, ligne, vehicule, dateHeure);

        assertEquals(chauffeur, conduitSur.getLeChauffeur());
        assertEquals(ligne, conduitSur.getUneLigne());
        assertEquals(vehicule, conduitSur.getUnVehicule());
        assertEquals(dateHeure, conduitSur.getDateHeureConduite());
        assertEquals(0, conduitSur.getNbValidation());
    }

    @Test
    void testConstructeurAvecNbValidation() {
        ConduitSur conduitSur = new ConduitSur(
                chauffeur, ligne, vehicule, dateHeure, 42);

        assertEquals(42, conduitSur.getNbValidation());
    }

    @Test
    void testSettersEtGetters() {
        ConduitSur conduitSur = new ConduitSur(chauffeur, ligne, vehicule, dateHeure);

        Ligne nouvelleLigne = new Ligne(101, "Ligne 12", typeLigne);
        Vehicule nouveauVehicule = new Bus(998, "Alstom", "Citadis", LocalDate.now(), LocalDate.now(), LocalDateTime.now());
        LocalDateTime nouvelleDate = LocalDateTime.now();

        conduitSur.setUneLigne(nouvelleLigne);
        conduitSur.setUnVehicule(nouveauVehicule);
        conduitSur.setDateHeureConduite(nouvelleDate);
        conduitSur.setNbValidation(10);

        assertEquals(nouvelleLigne, conduitSur.getUneLigne());
        assertEquals(nouveauVehicule, conduitSur.getUnVehicule());
        assertEquals(nouvelleDate, conduitSur.getDateHeureConduite());
        assertEquals(10, conduitSur.getNbValidation());
    }

    @Test
    void testToString() {
        ConduitSur conduitSur = new ConduitSur(chauffeur, ligne, vehicule, dateHeure);

        String resultat = conduitSur.toString();

        assertTrue(resultat.contains("Ligne = Ligne 2"));
        assertTrue(resultat.contains("Chauffeur = froland"));
        assertTrue(resultat.contains("Véhicule : 67"));
        assertTrue(resultat.contains(dateHeure.toString()));
    }
}

