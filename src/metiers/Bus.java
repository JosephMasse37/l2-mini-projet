package metiers;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Bus extends Vehicule {


    public Bus(int numVehicule, String marque, String modele, LocalDate dateFabrication, LocalDate dateMiseEnService, LocalDateTime dateHeureDerniereMaintenance, TypeVehicule typevehicule) {
        super(numVehicule, marque, modele, dateFabrication, dateMiseEnService, dateHeureDerniereMaintenance, typevehicule);
    }
}
