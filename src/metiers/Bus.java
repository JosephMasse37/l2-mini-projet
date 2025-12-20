package metiers;

import java.time.LocalDate;

public class Bus extends Vehicule {


    public Bus(int numVehicule, String marque, String modele, LocalDate dateFabrication, LocalDate dateMiseEnService, LocalDate dateHeureDerniereMaintenance, TypeVehicule typevehicule) {
        super(numVehicule, marque, modele, dateFabrication, dateMiseEnService, dateHeureDerniereMaintenance, typevehicule);
    }
}
