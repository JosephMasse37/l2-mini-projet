package metiers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class ConduitSurTest {

    private Chauffeur chauffeur;
    private Ligne ligne;
    private Vehicule vehicule;
    private LocalDateTime dateHeure;

    @BeforeEach
    void setUp() {
        Utilisateur utilisateur = new Utilisateur("froland");
        chauffeur = new Chauffeur(utilisateur);
        ligne = new Ligne("Ligne 2");
        vehicule = new Vehicule("56");
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

        Ligne nouvelleLigne = new Ligne("Ligne 4");
        Vehicule nouveauVehicule = new Vehicule("67");
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

