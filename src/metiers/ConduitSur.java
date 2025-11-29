package metiers;

import java.time.LocalDateTime;

public class ConduitSur {
    private Chauffeur leChauffeur;
    private Ligne uneLigne;
    private Vehicule unVehicule;

    private LocalDateTime dateHeureConduite;
    private int nbValidation;

    public Chauffeur getLeChauffeur() {
        return leChauffeur;
    }

    public void setLeChauffeur(Chauffeur leChauffeur) {
        this.leChauffeur = leChauffeur;
    }

    public Ligne getUneLigne() {
        return uneLigne;
    }

    public void setUneLigne(Ligne uneLigne) {
        this.uneLigne = uneLigne;
    }

    public Vehicule getUnVehicule() {
        return unVehicule;
    }

    public void setUnVehicule(Vehicule unVehicule) {
        this.unVehicule = unVehicule;
    }

    public LocalDateTime getDateHeureConduite() {
        return dateHeureConduite;
    }

    public void setDateHeureConduite(LocalDateTime dateHeureConduite) {
        this.dateHeureConduite = dateHeureConduite;
    }

    public int getNbValidation() {
        return nbValidation;
    }

    public void setNbValidation(int nbValidation) {
        this.nbValidation = nbValidation;
    }
}
