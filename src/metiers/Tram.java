package metiers;

import java.time.LocalDate;

public class Tram extends Vehicule {

    public Tram(int numVehicule, String marque, String modele, LocalDate dateFabrication, LocalDate dateMiseEnService, LocalDate dateHeureDerniereMaintenance, TypeVehicule typevehicule) {
        super(numVehicule, marque, modele, dateFabrication, dateMiseEnService, dateHeureDerniereMaintenance, typevehicule);
    }
}
