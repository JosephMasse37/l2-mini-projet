package metiers;

import java.util.ArrayList;
import java.util.List;

public class TypeVehicule {

    private int idTypeVehicule;
    private String libelle;
//car n
    private List<Vehicule> listeVehicules = new ArrayList<>();

    public int getIdTypeVehicule() {
        return idTypeVehicule;
    }

    public void setIdTypeVehicule(int idTypeVehicule) {
        this.idTypeVehicule = idTypeVehicule;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public List<Vehicule> getListeVehicules() {
        return listeVehicules;
    }

    public void setListeVehicules(List<Vehicule> listeVehicules) {
        this.listeVehicules = listeVehicules;

    }

    public void addVehicule(Vehicule vehicule){
        listeVehicules.add(vehicule);
    }




}



