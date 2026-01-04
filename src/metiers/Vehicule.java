package metiers;

import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class Vehicule {
    private int numVehicule;
    private String marque;
    private String modele;
    private LocalDate dateFabrication;
    private LocalDate dateMiseEnService;
    private LocalDateTime dateHeureDerniereMaintenance;
// pour co (FK genre)
    private TypeVehicule typevehicule;

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public int getNumVehicule() {
        return numVehicule;
    }

    public void setNumVehicule(int numVehicule) {
        this.numVehicule = numVehicule;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public LocalDate getDateFabrication() {
        return dateFabrication;
    }

    public void setDateFabrication(LocalDate dateFabrication) {
        this.dateFabrication = dateFabrication;
    }

    public LocalDate getDateMiseEnService() {
        return dateMiseEnService;
    }

    public void setDateMiseEnService(LocalDate dateMiseEnService) {
        this.dateMiseEnService = dateMiseEnService;
    }

    public LocalDateTime getDateHeureDerniereMaintenance() {
        return dateHeureDerniereMaintenance;
    }

    public void setDateHeureDerniereMaintenance(LocalDateTime dateHeureDerniereMaintenance) {
        this.dateHeureDerniereMaintenance = dateHeureDerniereMaintenance;
    }

    public TypeVehicule getTypevehicule() {
        return typevehicule;
    }

    public void setTypevehicule(TypeVehicule typevehicule) {
        this.typevehicule = typevehicule;
        // PAS OUBLIER
        typevehicule.addVehicule(this); // add un vehic dans le type aussi
    }

    public Vehicule( int numVehicule,String marque,String modele,LocalDate dateFabrication,LocalDate dateMiseEnService,LocalDateTime dateHeureDerniereMaintenance){
        this.numVehicule=numVehicule;
        this.marque=marque;
        this.modele=modele;
        this.dateFabrication=dateFabrication;
        this.dateMiseEnService=dateMiseEnService;
        this.dateHeureDerniereMaintenance=dateHeureDerniereMaintenance;
    }

    public Vehicule( int numVehicule,String marque,String modele,LocalDate dateFabrication,LocalDate dateMiseEnService,LocalDateTime dateHeureDerniereMaintenance,TypeVehicule typevehicule){
        this.numVehicule=numVehicule;
        this.marque=marque;
        this.modele=modele;
        this.dateFabrication=dateFabrication;
        this.dateMiseEnService=dateMiseEnService;
        this.dateHeureDerniereMaintenance=dateHeureDerniereMaintenance;
        setTypevehicule(typevehicule); // pour que Vehicule et Typevehic soit synchro
    }

    @Override
    public String toString() {
        return "Vehicule [numVehicule=" + numVehicule + ", marque=" + marque + ", modele=" + modele
                + ", dateFabrication=" + dateFabrication + ", dateMiseEnService=" + dateMiseEnService
                + ", dateHeureDerniereMaintenance=" + dateHeureDerniereMaintenance + ", typevehicule=" + typevehicule
                + "]";
    }
}
