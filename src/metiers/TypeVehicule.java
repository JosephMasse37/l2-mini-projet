package metiers;

import java.util.ArrayList;
import java.util.List;

public class TypeVehicule {

    private int idTypeVehicule;
    private String libelle;
    private List<Vehicule> listeVehicules = new ArrayList<>(); //car n

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
        if (!listeVehicules.contains(vehicule)) {
            listeVehicules.add(vehicule);
            vehicule.setTypevehicule(this); // pour que ca soit bidirectionnelle
        }
    }


    public TypeVehicule(int idTypeVehicule,String libelle, List<Vehicule> listeVehicules ){
        this.idTypeVehicule=idTypeVehicule;
        this.libelle=libelle;
        this.listeVehicules=listeVehicules;
    }

    public TypeVehicule(int idTypeVehicule, String libelle ){
        this.idTypeVehicule=idTypeVehicule;
        this.libelle=libelle;
    }

    public TypeVehicule(String libelle, List<Vehicule> listeVehicules ){
        this.libelle=libelle;
        this.listeVehicules=listeVehicules;
    }



}



